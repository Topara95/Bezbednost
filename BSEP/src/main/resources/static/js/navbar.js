window.onload = function generateNavbar(){
	const navbar = document.getElementById('regnav');
	
	$.ajax({
    	url: "../api/account/isLogged",
    	method : "GET",
    	success: function(user){
    		if(user.username != null){
	    		navbar.innerHTML = '';
	    		navbar.innerHTML += `<ul class="navbar-nav ml-auto" id="regnav">
	                	<li class="nav-item">
	                    	<a class="nav-link" href="#">User `+user.username+` signed in</a>
	                    </li>
	                    <li class="nav-item">
	                    	<a class="nav-link" href="csrrequest.html">Make certificate signing request</a>
	                    </li>
	                    <li class="nav-item">
	                    	<a class="nav-link" id="signOut" href="#">Sign out</a>
	                    </li>
	                </ul>`
	           if(user.userType == "ADMIN"){
	        	   $("#regnav").append(`<li class="nav-item">
	        			   <a class="nav-link" href="pendingcsr.html">View pending CSR</a>
	        	   </li>`)
	           }   	
    		}
    	},
    	error: function(){
    		alert("error");
    	}
    });
	
};

$(document).on('click','#signOut',function(){
    $.ajax({
    	url: "../api/account/logut",
    	method : "POST",
    	success: function(){
    		location.reload();
    		sessionStorage.removeItem('loggedUser');
    	},
    	error: function(){
    		alert("error");
    	}
    });
});