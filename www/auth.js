var API_BASE_URL = "http://localhost:8181/spot-api";//cambiar spot-api por spot-auth
var API_BASE = "http://localhost:8181";

$("#button-auth").click(function(e) {
	e.preventDefault();
	console.log("Comienza el log");
	var user = new Object();
	user.username = $('#nombre-auth').val();
	user.userpass = $('#password-auth').val();
	login(JSON.stringify(user));	
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
		var user = $.parseJSON(jqxhr.responseText);
		open(API_BASE + "/www/index.html");
	})
    .fail(function (jqXHR, textStatus) {
    	console.log("Fallo: ");
		console.log(textStatus);
	});
}
