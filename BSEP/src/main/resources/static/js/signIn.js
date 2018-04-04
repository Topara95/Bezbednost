import { bsep } from './bsep.js';


document.getElementById('signInForm').addEventListener('submit', e => {
    e.preventDefault();
    const userInfo = {
        username : document.getElementById('username').value,
        password : document.getElementById('password').value
    }

    bsep.login(userInfo).then(user=>{
        alert('User logged in');
        sessionStorage.setItem("loggedUser", user);
        location.reload();
    }).catch(err => {
        alert('Error during signing in!');
        console.log(err);
    });
    
});