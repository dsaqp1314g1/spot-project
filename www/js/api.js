var BEETER_API_HOME="http://localhost:8080/spot-api";
var spotID;

$('#CerrarSpot').click(function(e){
	 e.preventDefault();
	 $('#spot-detail').fadeOut('slow');
	return false;
});

$('#buscar-amigo').click(function(e) {

	e.preventDefault();	
	console.log("Click");
	if($('#buscar_campo').val()===''){
		$("#buscar-error").show();
		$("#buscar-error").fadeOut(4000);

}
	else{
		$('#error-perfil').show();

	$('#comment-form').hide();
	getUserParam($("#buscar_campo").val());
	//$("#buscar_campo").val('');
	}
});
//FALTA SI YA HAS DADO HA MEGUSTA QUE YA NO PUEDAS HACERLO ************************************
$('#NOmegusta').click(function(e){
	 e.preventDefault();
	 $('#NOmegusta').hide();
	 $('#megusta').show();
	 NOMegustaSpot(spotID);
	return false;
});
$('#megusta').click(function(e){
	 e.preventDefault();
	 $('#megusta').hide();
	 $('#NOmegusta').show();
	 MegustaSpot(spotID);
	return false;
});

function Link(rel, linkheader){
	this.rel = rel;
	this.href = decodeURIComponent(linkheader.find(rel).template().template);
	this.type = linkheader.find(rel).attr('type');
	this.title = linkheader.find(rel).attr('title');
}

function buildLinks(linkheaders){
	var links = {};
	$.each(linkheaders, function(i,linkheader){
		var linkhdr = $.linkheaders(linkheader);
		var rels = linkhdr.find().attr('rel').split(' ');
		$.each(rels, function(key,rel){
			var link = new Link(rel, linkhdr);
			links[rel] = link;
		});
	});

	return links;
}

function Comentario(comentario){
	this.idcomentario = comentario.idcomentario;
	this.idspot = comentario.idspot;
	this.usuario = comentario.usuario;
	this.comentario = comentario.comentario;
	this.fechacreacion = comentario.fechacreacion;
	this.links = buildLinks(comentario.links);
	var instance = this;
	this.getLinks = function(rel){
		return this.links[rel];
	}
}

function Spot(spot){
	this.botonmegusta = spot.botonmegusta;
	this.comentario = spot.comentario;
	this.fechasubida = spot.fechasubida;
	this.imageURL = spot.imageURL;
	this.usuario = spot.usuario;
	this.idspot = spot.idspot;
	this.title = spot.title;
	this.megusta = spot.megusta;
	this.deporte = spot.deporte;
	this.latitud = spot.latitud;
	this.longitud = spot.longitud;
	this.ciudad = spot.ciudad;
	this.links = buildLinks(spot.links);
	var instance = this;
	this.getLinks = function(rel){
		return this.links[rel];
	}
}

function User(user){
	this.username = user.username;
	this.name = user.name;
	this.userpass = user.userpass;
	this.conectado = user.conectado;
	this.email = user.email;
	this.actualizacionescollection = user.actualizacionescollection;
	this.actumegustacollection = user.actumegustacollection;
	this.mensajesCollection = user.mensajesCollection;
	this.links = buildLinks(user.links);
	this.getLinks = function(rel){
		return this.links[rel];
	}
}

function Actualizacionescollection(actualizacionescollection){
	this.actualizacion = actualizacionescollection.actualizacion;
}

function Actualizaciones(actualizaciones){
	this.fechacreacion = actualizaciones.fechacreacion;
	this.idcomentario = actualizaciones.idcomentario;
	this.idspot = actualizaciones.idspot;
	this.nombrecomentario = actualizaciones.nombrecomentario;
	this.usercomentario = actualizaciones.usercomentario;
	this.links = buildLinks(actualizaciones.links);
	this.getLinks = function(rel){
		return this.links[rel];
	}
}

function MensajesCollection(mensajesCollection){
	this.mensajes = mensajesCollection.mensajes;
}

function Mensajes(mensajes){
	this.fechacreacion = mensajes.fechacreacion;
	this.idmensaje = mensajes.idmensaje;
	this.userTx = mensajes.userTx;
	this.userRx = mensajes.userRx;
	this.mensaje = mensajes.mensaje;
	this.links = buildLinks(mensajes.links);
	this.getLinks = function(rel){
		return this.links[rel];
	}
}

function Actumegustacollection(actumegustacollection){
	this.actualizacion = actumegustacollection.actualizacion;
}

function StingCollection(stingCollection){
	this.oldestTimestamp = stingCollection.oldestTimestamp;
	this.newestTimestamp = stingCollection.newestTimestamp;
	this.stings = stingCollection.stings;

	this.links = buildLinks(stingCollection.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}
}

function RootAPI(rootAPI){
	this.links = buildLinks(rootAPI.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}
}


function loadRootAPI(success){
	$.ajax({
		url : BEETER_API_HOME,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		var rootAPI = new RootAPI(response);
    	success(rootAPI);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
	
}

function getStings(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		var stingCollection = new StingCollection(response);
		//success(response.stings);
		success(stingCollection);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}
//zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
function getUsuario(url, success){
	  console.log(""+url);

	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	}).done(function (data, status, jqxhr) {
		var user = $.parseJSON(jqxhr.responseText);
		success(user);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}
function getSpot(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	}).done(function (data, status, jqxhr) {
		var spot = $.parseJSON(jqxhr.responseText);
		success(spot);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function updateSting(url, type, sting, success){
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		contentType: type, 
		data: sting
	})
	.done(function (data, status, jqxhr) {
		var sting = $.parseJSON(jqxhr.responseText);
		success(sting);
		//console.log("SUCCESS ON PUT!!");
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}
function deleteComment(idspot,idcoment) {
	var url = API_BASE_URL +"/spots/"+idspot+"/comentario/"+idcoment;
	
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {			
		getSpotId(idspot);
	}).fail(function() {
		alert("ERROR");
	});
}

function deleteActualizacion(idspot, idcomentario) {
	var url = API_BASE_URL +"/spots/"+idspot+"/actualizacion/"+idcomentario;
	
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {	
		getUser();
	}).fail(function() {
		alert("ERROR");
	});
}

function deleteActuMegusta(idspot, usermegusta) {
	var url = API_BASE_URL +"/spots/"+idspot+"/actumegusta/"+usermegusta;
	
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {	
		getUser();
	}).fail(function() {
		alert("ERROR");
	});
}

function getSpotId(id) {
	var url = API_BASE_URL + '/spots/'+id;
	$("#spot-detail").fadeIn('slow');
	$("#spot_result").text("");	
	$("#spot-scroll-able").text("");
	$('#estado-Megustas').text("Me gusta");
	$('#megusta').show();
	$('#NOmegusta').hide();
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
				var spot = new Spot(data);
				spotID=spot.idspot;
				$('#uploadedImage').attr('src', spot.imageURL);
				$('#titulo-spot').text(spot.title);
				$('#contador-Megustas').text(spot.megusta);	
				$('<strong> Usuario: </strong>').appendTo($('#spot_result'));
				$('<a id="spot-link-user'+spot.usuario+spot.idspot+'" href="'+spot.getLinks("abrir-user").href+'">'+spot.usuario+'</a><br>').appendTo($('#spot_result'));;
				$('<style type="text/css">  #spot-link-user'+spot.usuario+spot.idspot+'{color: #FFBF00;} </style>').appendTo($('#spot-scroll-able'));
				$("#spot-link-user"+spot.usuario+spot.idspot).click(function(e){
					e.preventDefault();
					loadUser($(e.target).attr('href'));
					$("#spot-detail").fadeOut('slow');
					$("#spots-perfil").fadeIn('slow');
					return false;
				});
				$('<strong> Ciudad: </strong> ' + spot.ciudad + '<br>').appendTo($('#spot_result'));
				$('<strong> Deporte: </strong> ' + spot.deporte + '<br>').appendTo($('#spot_result'));
					$.each(spot.comentario, function(i, v) {
						var comentario = new Comentario(v);
						$('<a id="spot-link-user'+comentario.idcomentario+'" href="'+comentario.getLinks("abrir-user").href+'">'+comentario.usuario+'</a>').appendTo($('#spot-scroll-able'));
						$('<style type="text/css">  #spot-link-user'+comentario.idcomentario+'{color: #FFBF00;} </style>').appendTo($('#spot-scroll-able'));
						$('<section id="section'+comentario.idcomentario+'"><h8>'+'Fecha: '+comentario.fechacreacion+'</h8><h4><p>Comentario: '+ comentario.comentario + '</p><h4></section>').appendTo($('#spot-scroll-able'));
						$('<style type="text/css">  #section'+comentario.idcomentario+'{ border: 2px solid #FFBF00; color: white; border-radius: 5px; padding: 5px 15px;} </style>').appendTo($('#spot-scroll-able'));

						$("#spot-link-user"+comentario.idcomentario).click(function(e){
							e.preventDefault();
							loadUser($(e.target).attr('href'));
							$("#spot-detail").fadeOut('slow');
							$("#spots-perfil").fadeIn('slow');
							return false;
						});
						$('<br>').appendTo($('#spot-scroll-able'));
						if (comentario.usuario === $.cookie('username'))
							{
						$('<button id="delete-coms'+ comentario.idcomentario+'">'+"Delete"+'</button><br><br>').appendTo($('#spot-scroll-able'));
						$('<style type="text/css">  #delete-coms'+ comentario.idcomentario+'{ background: linear-gradient(to bottom, rgba(69, 72, 77, 1) 0%, rgba(0, 0, 0, 1) 100%);border: 2px solid #FFBF00; color: white; border-radius: 5px; padding: 5px 15px;} </style>').appendTo($('#spot-scroll-able'));
						$('#delete-coms'+comentario.idcomentario).click(function(e){
							e.preventDefault();
							deleteComment(spot.idspot,comentario.idcomentario);
							return false;
						});
							}
						$('<hr>').appendTo($('#spot-scroll-able'));

					});
				$('<hr>').appendTo($('#spot_result'));
				$.each(spot.botonmegusta, function(i, v) {
					var botonmegusta = v;
					console.log("entro en spot.botonmegusta");
					if (botonmegusta.usermegusta === $.cookie('username'))
						{
							console.log("ya le he hecho megusta ha "+spotID);
							$('#estado-Megustas').text("Ya no me gusta");
							$('#NOmegusta').show();
							$('#megusta').hide();
							
						}
					else {
							console.log("no le he hecho megusta ha "+spotID);
							$('#estado-Megustas').text("Me gusta");
							$('#megusta').show();
							$('#NOmegusta').hide();

						}
					
				});
	}).fail(function() {
		$("#spot_result").text("NO RESULT");
	});
}

$('#enviar-mensaje').click(function(e) {
	if ($('#mensaje-comment').val() === '') {
		$('#alerta-nohaymensaje').show();
	} else {
		e.preventDefault();
		$('#alerta-nohaymensaje').hide();
		$('#alerta-mensajeenviado').show();
		$('#alerta-mensajeenviado').fadeOut(4000);
		var mensaje = new Object();	
		mensaje.userTx = $.cookie('username');
		mensaje.userRx = $('#perfil-titulo-spot').text();
		mensaje.mensaje = $('#mensaje-comment').val();

		$("#mensaje-comment").val('');
		createMensaje(JSON.stringify(mensaje));
	}
});

$('#comment-ok').click(function(e) {
	if ($('#edit-comment').val() === '') {
		$('#alerta-nohaycoment').show();
	} else {
		$('#alerta-nohaycoment').hide();
		$('#alerta-comentenviado').show();
		$('#alerta-comentenviado').fadeOut(4000);
		e.preventDefault();
		var comment = new Object();	
		comment.usuario = $.cookie('username');
		comment.comentario = $('#edit-comment').val();
		$("#edit-comment").val('');
		createComentario(spotID, JSON.stringify(comment));
	}
});

function NOMegustaSpot(id) {
	var url = API_BASE_URL + '/spots/'+id+ '/NOmegustas/'+$.cookie('username');
	
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		getSpotId(id);
	}).fail(function() {
		$("#spot_result").text("NO RESULT");
	});
}

function MegustaSpot(id) {
	var url = API_BASE_URL + '/spots/'+id+ '/megustas/'+$.cookie('username');
	
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		getSpotId(id);
	}).fail(function() {
		$("#spot_result").text("NO RESULT");
	});
}

function deleteSting(url, success){
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true
	})
	.done(function (data, status, jqxhr) {
		//var sting = $.parseJSON(jqxhr.responseText);
		success();
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function createComentario(id,coment){
	console.log("entro en createcomentario idspot :" +id);
	var url = API_BASE_URL + "/spots/"+ id +"/comentario";	
	$.ajax({
		url : url,
		type : 'POST', 
		crossDomain : true,
		contentType: 'application/vnd.catalogo.spot.comentario+json',
		data: coment
	})
	.done(function (data, status, jqxhr) {
		var coment = $.parseJSON(jqxhr.responseText);
		getSpotId(id);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function createMensaje(mensaje){
	
	var url = API_BASE_URL + "/user/"+ mensaje.userRx+"/mensaje";	
	$.ajax({
		url : url,
		type : 'POST', 
		crossDomain : true,
		contentType: 'application/vnd.spot.api.user.mensajes+json',
		data: mensaje
	})
	.done(function (data, status, jqxhr) {
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function getUserParam(user) {
	var url = API_BASE_URL + '/user/'+user;
	$('progress').toggle();
	console.log("Get User by param");
	$("#perfil_result").text("");
	$("#spot_result").text("");
	$("#spots-perfil-container").text("");
	$("#perfil-scroll-able").hide();
	$("#perfil-scroll-able-mensajes").hide();
	$("#navperfil").hide();
	$("#titulo-mensaje").show();



	$("#scroll").text("");
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
				var user = new User(data);
				$('#spots-perfil').show();

				$('progress').toggle();
														
				if (user.username == $.cookie('username')){
					$("#perfil-scroll-able").show();
					$("#navperfil").show();
					$("#mensaje-comment").hide();
					$("#enviar-mensaje").hide();
					$("#mensaje-cancel").hide();
					$("#titulo-mensaje").hide();
					$("#online").hide();
					$('#offline').hide();

					if (user.name==null)
					{
						$('#spots-perfil').hide();

					 $('#error-perfil').show();
						$('#error-perfil').fadeOut(4000);
					}
				else{
					$("#perfil-titulo-spot").text(user.username);
					$('<strong> Name : </strong> ' + user.name + '<br>').appendTo($('#perfil_result'));
					$('<strong> Email : </strong> ' + user.email + '<br>').appendTo($('#perfil_result'));
					$("#scroll").text("Actualizaciones");
					getSpotByUser(user.username);
				}
				}
				else{if (user.name==null)
					{
					$('#spots-perfil').hide();

					 $('#error-perfil').show();
						$('#error-perfil').fadeOut(4000);

					}
				else{
					$("#mensaje-comment").show();
					$("#enviar-mensaje").show();
					$("#mensaje-cancel").show();
					$("#perfil-titulo-spot").text(user.username);
					$('<strong> Name : </strong> ' + user.name + '<br>').appendTo($('#perfil_result'));
					$('<strong> Email : </strong> ' + user.email + '<br>').appendTo($('#perfil_result'));
					if (user.conectado=="si")
						{
						  $("#online").show();
						  $('#offline').hide();
						}
					else
						{
						  $("#offline").show();
						  $('#online').hide();
						}
					$("#scroll").text("");
					getSpotByUser(user.username);
				}}
	}).fail(function() {
		 $('#error-perfil').show();

	});
}




function desconected(){
	var url = API_BASE_URL + "/user/"+ $.cookie('username');
	console.log("Desconectando usuario: " +  url);
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true
	})
	.done(function (data, status, jqxhr) {
		$.removeCookie ('username');
		window.location.replace("/auth.html");
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
		alert(textStatus);
	});
}

$('#salir').click(function(e) {
	e.preventDefault();
	desconected();
});
