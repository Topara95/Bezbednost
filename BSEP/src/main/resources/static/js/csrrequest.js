import { bsep } from './bsep.js';


document.getElementById('csrForm').addEventListener('submit', e => {
    e.preventDefault();
    
    const csrInfo = {
        commonName : document.getElementById('cn').value,
        givenname : document.getElementById('surname').value,
        surname : document.getElementById('givenName').value,
        organization : document.getElementById('o').value,
        organizationalUnitName : document.getElementById('ou').value,
        countryCode : document.getElementById('c').value,
        email : document.getElementById('e').value
    };

    bsep.makeCsrRequest(csrInfo).then(csr => {
        alert('CSR made');
        console.log(csr);
    }).catch(err => {
        alert('Error submiting CSR');
        console.log(err);
    });
});