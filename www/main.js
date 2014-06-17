var API_BASE_URL="http://localhost:8181/spot-api";
var stingsURL;

$("#button-list-spots").click(function(e) {
	e.preventDefault();
	$('#exam-error').hide();
	if ($('#buscar_ciud').val() === '')
	{
	$('#exam-error').show();	
	//getSpotsParam($("#buscar_ciud").val(),$("#buscar_mod").val());
	}
else
{
	//eliminar los markers paramostrar los nuevos
	deleteMarkers();
	//centra el mapa en la ciudad buscada como parametro
	codeAddress();
	getSpotsParam($("#buscar_ciud").val(),$("#buscar_mod").val());
}
});

$('#buscar-amigo').click(function(e) {
	e.preventDefault();	
	 $("#error-perfil-div").hide();
	 $('#buscar-error').hide();
	if($('#buscar_campo').val()===''){
		$('#buscar-error').show();
}
	else{
	$('#comment-form').hide();
	window.location.replace("/perfil.html");
	getUserParam($("#buscar_campo").val());}
});

$("#button-list-one").click(function(e) {
	e.preventDefault();
	if ($('#buscar_id').val() === '')
	{
	$('#exam-error').show();	
	//getSpotsParam($("#buscar_ciud").val(),$("#buscar_mod").val());
	}
else
{
	getSpotId($("#buscar_id").val());
}
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

function getSpots() {
	var url = API_BASE_URL + '/spots';
	$('progress').toggle();

	$("#repos_result").text("");
	
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
				var repos = data;
				$('progress').toggle();

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
		$('progress').toggle();

		$("#repos_result").text("NO RESULT");
	});
}

function loadSpot(url){
	getSpot(url, function(spot){
		showSting(spot);
	});
}

function getSpotsParam(ciudad, modal) {
	var url = API_BASE_URL + '/spots/search?ciudad='+ciudad+'&deporte='+modal;
	$('progress').toggle();

	if (modal === "Todas")
		var url = API_BASE_URL + '/spots/search?ciudad='+ciudad;
	
	$("#repos_result").text("");	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
				var repos = data;
				$('progress').toggle();
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
		$('progress').toggle();
		$("#repos_result").text("NO RESULT");
	});
}

function loadSpots(url){
	$('#spots-container').show();
	var stings = getStings(url, function (stingCollection){
		$.each(spotCollection.spots, function(index,item){
			var spot = new Spot(item);
			var link = $('<a id="sting-link" href="'+spot.getLink("self").href+'">'+spot.id +' ('+spot.usuario+')'+'</a>');
			link.click(function(e){
				e.preventDefault();
				loadSpot($(e.target).attr('href'));
				return false;
			});
			var div = $('<div></div>')
			div.append(link);
			$('#spots-collection').append(div);
		});
	});
}
function loadSpot(url){
	getSpot(url, function(spot){
		getSpotId(spot.idspot);
	});
}

function hideEditForm() {
	$('#edit-sting-form').hide();
}

var stingsURL;
$(document).ready(function(){	
// loadRootAPI(function(rootAPI){
// // stingsURL = rootAPI.getLink('spots').href;
// // loadStings(rootAPI.getLink('spots').href);
// getTasks();
// }); var mapOptions = {
	// this works! (lat, lng are global variables read from localStorage
	
	//getSpots();
	
	//comprovacion de la cookie
		  if($.cookie('username')) {
			  console.log("Usuario autentificado : " + $.cookie('username'));
			  getSpots();
		  }
		  else{
			  console.log("Usuario no autentificado");
			  window.location.replace("/register.html");
		  }
	
	
});
