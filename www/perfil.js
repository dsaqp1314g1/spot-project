var API_BASE_URL = "http://localhost:8181/spot-api";
var stingsURL;


$('#buscar-amigo').click(function(e) {
	e.preventDefault();	
	 $("#error-perfil-div").hide();
	 $('#buscar-error').hide();
	if($('#buscar_campo').val()===''){
		$('#buscar-error').show();
}
	else{
	$('#comment-form').hide();
	getUserParam($("#buscar_campo").val());}
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
	$("#perfil-titulo-spot").text($.cookie('username'));

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
					$('<strong> Actualizaciones: </strong><br>').appendTo($('#perfil_result'));
					$.each(user.actualizacionescollection.actualizacion, function(i, v) {
						var actualizacion = v;
						$('<strong> El usuario : </strong>' + actualizacion.usercomentario + '<br>').appendTo($('#perfil-scroll-able'));
						$('<strong> A hecho un comentario en el spot : </strong> ' + actualizacion.nombrecomentario + '<br>').appendTo($('#perfil-scroll-able'));
						$('<strong> El dia : </strong> ' + actualizacion.fechacreacion + '<br><br>').appendTo($('#perfil-scroll-able'));
						
						$('<button id="ver-actu'+ actualizacion.idspot+'" class="btn btn-default">'+"¡Ver Spot!"+'</button><br><br>').appendTo($('#perfil-scroll-able'));												
						$('#ver-actu'+actualizacion.idspot).click(function(e){
							e.preventDefault();
							deleteActualizacion(actualizacion.idspot, actualizacion.idcomentario);
							getSpotId(actualizacion.idspot);
							return false;
						});
						$('<hr>').appendTo($('#perfil-scroll-able'));

					});
					$.each(user.actumegustacollection.actualizacion, function(i, v) {
						var actualizacion = v;
						$('<strong> El usuario : </strong>' + actualizacion.usermegusta + '<br>').appendTo($('#pperfil-scroll-able'));
						$('<strong> Dio a ' + actualizacion.estado + ' sobre el espot spot </strong> ' + actualizacion.nombrespot + '<br>').appendTo($('#perfil-scroll-able'));
						$('<strong> El dia : </strong> ' + actualizacion.fechacreacion + '<br><br>').appendTo($('#perfil_result'));
						$('<button id="ver-actu'+ actualizacion.idspot+'" class="btn btn-default">'+"¡Ver Spot!"+'</button><br><br>').appendTo($('#perfil-scroll-able'));												
						$('#ver-actu'+actualizacion.idspot).click(function(e){
							e.preventDefault();
							deleteActuMegusta(actualizacion.idspot, actualizacion.userspot);
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
