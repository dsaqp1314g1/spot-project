var API_BASE_URL = "http://localhost:8181/spot-api";//cambiar spot-api por spot-auth
var API_BASE = "http://localhost:8181";
var password;
var username;

$("#button-auth").click(function(e) {
	e.preventDefault();
	
	if ($('#nombre-auth').val()=="" || $('#password-auth').val()==""  )
		{
		   $("#alerta-auth-div").show();
		}
	else 
		{
	console.log("Comienza el log");
	var user = new Object();
	user.username = $('#nombre-auth').val();
	user.userpass = $('#password-auth').val();
	password= hex_md5($('#password-auth').val());
	login(JSON.stringify(user));
}
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
			   //guardamos el valor del nombre del usuario en la cookie
				var nombreusuario = usuario.username;
			  $.cookie('username', nombreusuario);
		      var currentusr = $.cookie('username');
		      console.log("Usuario guardad en la cookie : " + currentusr);
		      console.log("Usuario guardad en la cookie2 : " + $.cookie('username'));
		      window.location.replace("/index.html");
			}
			else{
				    $("#error-auth-div").show();
				    $("#alerta-auth-div").hide();
			}
	})
    .fail(function (jqXHR, textStatus) {
    	console.log("Fallo: ");
		console.log(textStatus);
	});
}



$("#button-regist").click(function(e) {
	e.preventDefault();
	if ($('#nombre-auth').val()=="" || $('#password-auth').val()=="" || $('#name-regist').val()=="" || $('#mail-regist').val()==""  )
	{
	   $("#alerta-auth-div").show();
	}
	else {
	console.log("Comienza el register");
	username=$('#user-regist').val();
	var user = new Object();
	user.username = $('#user-regist').val();
	user.userpass = $('#password-regist').val();
	user.name = $('#name-regist').val();
	user.email = $('#mail-regist').val();
	console.log("Usuario que solicita registrarse : " + username);
	register(JSON.stringify(user));	
	}
});

function register(user){
	var url = API_BASE_URL + '/user/' + username ;
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		contentType: 'application/vnd.spot.api.user+json',
		data: user
		}).done(function (data, status, jqxhr) {
			console.log("Reciviendo respuesta");
			var usuario = new User(data);
			console.log("Usuario registrado correctamente");
			 window.location.replace("/auth.html");
	})
    .fail(function (jqXHR, textStatus) {
    	console.log("Fallo: ");
		console.log(textStatus);
	});
}
