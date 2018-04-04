import { bsep } from './bsep.js';

function certTableRow(cert) {
    return `
        <tr>
            <td>${cert.commonName}</td>
            <td>${cert.givenname}</td>
            <td>${cert.surname}</td>
            <td>${cert.organization}</td>
            <td>${cert.organizationalUnitName}</td>
            <td>${cert.countryCode}</td>
            <td>${cert.email}</td>
            <td>
            <button class="btn btn-danger btn-sm" data-serial-number="${cert.serialNumber}">Revoke</button>
                <button class="btn btn-warning btn-sm" data-serial-number="${cert.serialNumber}">Check validity</button>
                <a href="/api/certificates/download/${cert.serialNumber}" class="btn btn-success btn-sm">Download</a>
            </td>
        </tr>
    `;
};

// Check certificate
document.getElementById('certTable').addEventListener('click', e => {
    if (e.target.classList.contains('btn-warning')) {
        fetch('api/certificates/check/' + e.target.dataset.serialNumber, {
        	credentials: 'include'
        })
            .then(res => {
                if (res.ok) {
                    alert('Sertifikat je validan');
                } else {
                    alert('Nije validan');
                }
            });
    }
});

// Revoke certificate
document.getElementById('certTable').addEventListener('click', e => {
    if (e.target.classList.contains('btn-danger')) {
        fetch('api/certificates/revoke/' + e.target.dataset.serialNumber, {
            method: 'put',
            credentials: 'include'
        }).then(res => {
        	if(res.ok){
        		
        	}else{
        		alert("Samo administrator moze povuci sertifikat!")
        	}
        });
    }
});

bsep.getCertificates().then(certificates => {
    const table = document.getElementById('certTable');

    for (let cert of certificates) {
        table.innerHTML += certTableRow(cert);
    }
});


