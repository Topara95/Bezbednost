import { bsep } from './bsep.js';

document.getElementById('certForm').addEventListener('submit', e => {
    e.preventDefault();

    const certInfo = {
        commonName: document.getElementById('cn').value,
        givenname: document.getElementById('surname').value,
        surname: document.getElementById('givenName').value,
        organization: document.getElementById('o').value,
        organizationalUnitName: document.getElementById('ou').value,
        countryCode: document.getElementById('c').value,
        email: document.getElementById('e').value,
        expirationDate: document.getElementById('date').value
    };

    bsep.addCertificate(certInfo).then(res => {
        alert('Sertifikat je kreiran')
        console.log(res);
    }).catch(error => {
        alert('Sertifikat nije kreiran');
        console.log(error);
    });
});

