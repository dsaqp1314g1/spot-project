var API_BASE_URL = "http://localhost:8181/spot-api";//cambiar spot-api por spot-auth
var API_BASE = "http://localhost:8181";
var password;

$("#button-auth").click(function(e) {
	e.preventDefault();
	console.log("Comienza el log");
	var user = new Object();
	user.username = $('#nombre-auth').val();
	user.userpass = $('#password-auth').val();
	password= hex_md5($('#password-auth').val());
	login(JSON.stringify(user));	
});
$("#error-auth").click(function(e) {
$("#error-auth").hide();
});

function login(user){
	var url = API_BASE_URL + '/user';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		contentType: 'application/vnd.spot.api.user+json',
		data: user
		}).done(function (data, status, jqxhr) {
			console.log("Reciviendo respuesta");
			var usuario = new User(data);
			console.log("Usuario registrado: " + usuario.username);
			if(password == usuario.userpass){
			  setCookie("username", usuario.username);//guardamos el valor del nombre del usuario en la cookie
			  console.log(getCookie("username"));
		      location.href = "C:/Users/Developer/git/spot-project/www/index.html";
		     
			}
			else{
				    $("#error-auth").show();
			}
	})
    .fail(function (jqXHR, textStatus) {
    	console.log("Fallo: ");
		console.log(textStatus);
	});
}
