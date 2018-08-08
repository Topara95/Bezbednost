import { bsep } from './bsep.js';


document.getElementById('registerform').addEventListener('submit', e => {
    e.preventDefault();
    
    const p =  document.getElementById('password').value;
    const cp = document.getElementById('password-confirm').value;

    if(p == cp){
	    const userInfo = {
	        username : document.getElementById('username').value,
	        password : document.getElementById('password').value
	    }
	
	    bsep.register(userInfo).then(user=>{
	        alert('User registered');
	        console.log(user);
	    }).catch(err => {
	        alert('Error during register!');
	        console.log(err);
	    });
    }else{
    	alert("Passwords must match!");
    }
});