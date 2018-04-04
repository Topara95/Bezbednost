function handleErrors(response) {
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}

function json(res) {
    return res.json();
}

export const bsep = {
    getCertificates: function() {
        return fetch('/api/certificates', {
            credentials: 'include'
        })
            .then(handleErrors)
            .then(json);
    },
    getCaCertificates: function() {
        return fetch('/api/certificates?type=ca', {
            credentials: 'include'
        })
            .then(handleErrors)
            .then(json); 
    },
    addCertificate: function(certificate) {
        return fetch('/api/certificates', {
            method: 'post',
            body: JSON.stringify(certificate),
            headers: {
                'content-type' : 'application/json'
            },
            credentials: 'include'
        })
        .then(handleErrors)
        .then(json);
    },
    issueCa: function(serialNumber, certificate) {
        return fetch(`/api/certificates/${serialNumber}/signable`, {
            method: 'post',
            headers: {
                'content-type' : 'application/json'
            },
            body: JSON.stringify(certificate),
            credentials: 'include'
        })
        .then(handleErrors)
        .then(json);
    },
    issueCert: function(serialNumber, certificate) {
        return fetch(`/api/certificates/${serialNumber}/end`, {
            method: 'post',
            headers: {
                'content-type' : 'application/json'
            },
            body: JSON.stringify(certificate),
            credentials: 'include'
        })
        .then(handleErrors)
        .then(json);
    },
    register: function(userInfo){
    	return fetch(`/api/account/register`, {
    		method: 'post',
            headers: {
                'content-type' : 'application/json'
            },
            body: JSON.stringify(userInfo),
            credentials: 'include'
    	})
    	.then(handleErrors)	
    },
    login: function(userInfo){
    	return fetch(`/api/account/login`, {
    		method: 'post',
            headers: {
                'content-type' : 'application/json'
            },
            body: JSON.stringify(userInfo),
            credentials: 'include'
    	})
    	.then(handleErrors)	
    },
    logout: function(){
    	return fetch(`/api/account/logout`, {
            method: 'post',
            credentials: 'include'
    	})
    	.then(handleErrors)
    },
    isLogged: function(){
    	return fetch(`/api/account/isLogged`, {
            method: 'get',
            credentials: 'include'
    	})
    	.then(handleErrors)
    }
}