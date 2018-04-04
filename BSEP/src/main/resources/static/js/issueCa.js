import { bsep } from './bsep.js';

bsep.getCaCertificates().then(certificates => {
    const dropdown = document.getElementById('signerSerialNumber');

    for (let cert of certificates) {
        dropdown.innerHTML += `<option value="${cert.serialNumber}">${cert.commonName}</option>`
    }
});

document.getElementById('certForm').addEventListener('submit', e => {
    e.preventDefault();
    
    const serialNumber =  document.getElementById('signerSerialNumber').value; 

    const certInfo = {
        commonName : document.getElementById('cn').value,
        givenname : document.getElementById('surname').value,
        surname : document.getElementById('givenName').value,
        organization : document.getElementById('o').value,
        organizationalUnitName : document.getElementById('ou').value,
        countryCode : document.getElementById('c').value,
        email : document.getElementById('e').value,
        expirationDate : document.getElementById('date').value
    }

    bsep.issueCa(serialNumber, certInfo).then(certificate => {
        alert('Certificate issued');
        console.log(certificate);
    }).catch(err => {
        alert('This certificate cannot sign other certificates!');
        console.log(err);
    });
});
