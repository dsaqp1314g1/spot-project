var API_BASE_URL = "http://localhost:8181/spot-api";
var stingsURL;

$('#mensajes').click(function(e) {
	e.preventDefault();	
	 $(this).toggleClass('active');
	 $(this).children('a').toggleClass('active');
	 $(this).siblings('li').children('a').removeClass('active');
	 $('#actualizaciones').removeClass('active');
	 $('#perfil-scroll-able-mensajes').show();
	 $('#perfil-scroll-able').hide();

});
$('#actualizaciones').click(function(e) {
	e.preventDefault();	
	 $(this).toggleClass('active');
	 $(this).children('a').toggleClass('active');
	 $(this).siblings('li').children('a').removeClass('active');
	 $('#mensajes').removeClass('active');
	 $('#perfil-scroll-able').show();
	 $('#perfil-scroll-able-mensajes').hide();
});

$('#CerrarPerfil').click(function(e){
	 e.preventDefault();
	 $('#spots-perfil').fadeOut('slow');
	return false;
});

$("#closing").click(function() {
	$("#buscar-error").hide();
});
$('#button-delete-comment').click(function(e) {
	e.preventDefault();					
	deleteComment($("#buscar_ciud").val());
});
$('#comment-cancel').click(function(e) {
	e.preventDefault();					
	$("#edit-comment").val('');
});
$("#closing").click(function() {
	$("#exam-error").hide();
});

$(document).ready(function(){
		getUser();
	});

function getUser() {
	var url = API_BASE_URL + '/user/' + $.cookie('username');	
	$('progress').toggle();
	$("#perfil_result").text("");
	$("#perfil-scroll-able").text("");
	$("#perfil-titulo-spot").text($.cookie('username'));
	$("#perfil-scroll-able").show();
	$("#perfil-scroll-able-mensajes").hide();
	$("#mensaje-comment").hide();
	$("#enviar-mensaje").hide();
	$("#mensaje-cancel").hide();
	$("#navperfil").show();

	$("#titulo-mensaje").hide();

	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
				var user = new User(data);
				$('progress').toggle();
				
					$('<strong> Name : </strong> ' + user.name + '<br>').appendTo($('#perfil_result'));
					$('<strong> Email : </strong> ' + user.email + '<br>').appendTo($('#perfil_result'));
					$.each(user.mensajesCollection.mensajes, function(i, v) {
						var mensajes = v;
						$('<strong> El usuario : </strong>' + mensajes.userTx + '<br>').appendTo($('#perfil-scroll-able-mensajes'));
						$('<strong> te ha enviado este mensaje: </strong> ' +  mensajes.mensaje + '<br>').appendTo($('#perfil-scroll-able-mensajes'));
						$('<strong> El dia : </strong> ' + mensajes.fechacreacion + '<br><br>').appendTo($('#perfil-scroll-able-mensajes'));
						$('<button id="responder'+ user.name+mensajes.idmensaje+'">'+"Responder"+'</button><br><br>').appendTo($('#perfil-scroll-able-mensajes'));												
						$('<style type="text/css">  #responder'+ user.name+mensajes.idmensaje+'{ background: linear-gradient(to bottom, rgba(69, 72, 77, 1) 0%, rgba(0, 0, 0, 1) 100%);border: 2px solid #FFBF00; color: white; border-radius: 5px; padding: 5px 15px;} </style>').appendTo($('#perfil-scroll-able-mensajes'));
						$('#responder'+user.name+mensajes.idmensaje).click(function(e){
							e.preventDefault();
							//deleteActualizacion(actualizacion.idspot, actualizacion.idcomentario);
							$("#responder"+ user.name+mensajes.idmensaje).hide();
							getUserParam(mensajes.userTx);
							return false;
						});
						$('<hr>').appendTo($('#perfil-scroll-able-mensajes'));

					});
					$.each(user.actualizacionescollection.actualizacion, function(i, v) {
						var actualizacion = v;
						$('<strong> El usuario : </strong>' + actualizacion.usercomentario + '<br>').appendTo($('#perfil-scroll-able'));
						$('<strong> A hecho un comentario en el spot : </strong> ' + actualizacion.nombrecomentario + '<br>').appendTo($('#perfil-scroll-able'));
						$('<strong> El dia : </strong> ' + actualizacion.fechacreacion + '<br><br>').appendTo($('#perfil-scroll-able'));
						$('<button id="ver-actu'+ actualizacion.idspot+actualizacion.idcomentario+'">'+"Ver Spot"+'</button><br><br>').appendTo($('#perfil-scroll-able'));												
						$('<style type="text/css">  #ver-actu'+ actualizacion.idspot+actualizacion.idcomentario+'{ background: linear-gradient(to bottom, rgba(69, 72, 77, 1) 0%, rgba(0, 0, 0, 1) 100%);border: 2px solid #FFBF00; color: white; border-radius: 5px; padding: 5px 15px;} </style>').appendTo($('#perfil-scroll-able'));
						$('#ver-actu'+actualizacion.idspot+actualizacion.idcomentario).click(function(e){
							e.preventDefault();
							deleteActualizacion(actualizacion.idspot, actualizacion.idcomentario);
							getSpotId(actualizacion.idspot);
							return false;
						});
						$('<hr>').appendTo($('#perfil-scroll-able'));

					});
					$.each(user.actumegustacollection.actualizacion, function(i, v) {
						var actualizacion = v;
						$('<strong> El usuario : </strong>' + actualizacion.usermegusta + '<br>').appendTo($('#perfil-scroll-able'));
						$('<strong> Dio a ' + actualizacion.estado + ' sobre el espot spot </strong> ' + actualizacion.nombrespot + '<br>').appendTo($('#perfil-scroll-able'));
						$('<strong> El dia : </strong> ' + actualizacion.fechacreacion + '<br><br>').appendTo($('#perfil-scroll-able'));
						$('<button id="ver-actus'+ actualizacion.idspot+actualizacion.usermegusta+'">'+"Ver Spot"+'</button><br><br>').appendTo($('#perfil-scroll-able'));
						$('<style type="text/css">  #ver-actus'+ actualizacion.idspot+actualizacion.usermegusta+'{ background: linear-gradient(to bottom, rgba(69, 72, 77, 1) 0%, rgba(0, 0, 0, 1) 100%);border: 2px solid #FFBF00; color: white; border-radius: 5px; padding: 5px 15px;} </style>').appendTo($('#perfil-scroll-able'));
						$('#ver-actus'+actualizacion.idspot+actualizacion.usermegusta).click(function(e){
							e.preventDefault();
							console.log("Click y preparado para eliminar actu: " + actualizacion.idspot + "  "+actualizacion.usermegusta);
							deleteActuMegusta(actualizacion.idspot, actualizacion.usermegusta);
							getSpotId(actualizacion.idspot);
							return false;
						});
						$('<hr>').appendTo($('#perfil-scroll-able'));

					});
					getSpotByUser(user.username);
//					var link = $('<a id="user-link" href="'+ user.getLinks("abrir-spots-user").href+'">'+ "Spots of: "+ user.name +'</a>');
//					link.click(function(e){
//						e.preventDefault();
//						loadSpots($(e.target).attr('href'));
//						return false;
//					});
//					var div = $('<div></div>')
//					div.append(link);
//					$('#perfil_result').append(div);
														

	}).fail(function() {
		$('progress').toggle();

		$("#perfil_result").text("NO RESULT");
	});
}

function loadSpots(url){	
	getSpot(url, function(user){ 
		getSpotByUser(user.username);
	});
}

function getSpotByUser(username) {
	var url = API_BASE_URL + '/user/'+ username +'/spots';
	$("#spots-perfil-container").text("");
	$('#comment-form').hide();
	deleteMarkers();
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		var repos = data;
		$.each(repos.spots, function(i, v) {
			var spot = new Spot(v);
			var idmarker = spot.idspot;
			var contentString ='<h4> Titulo: ' + spot.title + '</h4>'+ 
			'<strong> Usuario: </strong> ' + spot.usuario + '<br>'+
			'<strong> Ciudad: </strong> ' + spot.ciudad + '<br>'+
			'<strong> Deporte: </strong> ' + spot.deporte + '<br>';
			var myLatlng = new google.maps.LatLng(spot.latitud, spot.longitud);
			initialize(myLatlng, contentString, idmarker, icnMarker(spot.deporte));
		});
	}).fail(function() {
		$("#spots-perfil-container").text("NO RESULT");
	});
}

function loadSpott(url){
	getSpot(url, function(spot){
		getSpotId(spot.idspot);
	});
}
