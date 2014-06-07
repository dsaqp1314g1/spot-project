var API_BASE_URL = "http://localhost:8181/spot-api";//cambiar spot-api por spot-auth

$("#button-auth").click(function(e) {
	e.preventDefault();
	console.log("Comienza el log");
	if ($('#nombre-auth').val() === '' || $('#password-auth').val() === '')
	{
	$('#exam-error').show();	
	}
else
{
	var user = new Object();
	user.username = $('#nombre-auth').val();
	user.userpass = $('#password-auth').val();
	login(JSON.stringify(user));	
}
});

function login(user){
	var url = API_BASE_URL + '/user';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'application/vnd.spot.api.user+json',
		data: user
		}).done(function (data, status, jqxhr) {
		console.log("Reciviendo respuesta");
		var user = $.parseJSON(jqxhr.responseText);
	})
    .fail(function (jqXHR, textStatus) {
    	console.log("Fallo: ");
		console.log(textStatus);
	});
}