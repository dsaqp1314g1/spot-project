var API_BASE_URL="http://localhost:8181/spot-api";
var stingsURL;
var idmarker;
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
$('#perfil').click(function(e) {
	e.preventDefault();	
	getUser();
	 $(this).toggleClass('active');
	 $(this).children('a').toggleClass('active');
	 $(this).siblings('li').children('a').removeClass('active');
	 $('#home').removeClass('active');
	$('#spots-perfil').fadeIn('slow');
	$("#spot-detail").fadeOut('slow');

});
$('#home').click(function(e) {
	e.preventDefault();	
	getSpots();
	 $(this).toggleClass('active');
	 $(this).children('a').toggleClass('active');
	 $(this).siblings('li').children('a').removeClass('active');
	 $('#perfil').removeClass('active');
	$('#spots-perfil').hide();
	$('#spot-detail').hide();
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
	deleteMarkers();
	
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
					'<strong> ID: </strong> ' + spot.idspot + '<br>'+
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
	$('#actualizaciones').toggleClass('active');
	 $('#actualizaciones').children('a').toggleClass('active');
	 $('#actualizaciones').siblings('li').children('a').removeClass('active');
	 
	 $('#home').toggleClass('active');
	 $('#home').children('a').toggleClass('active');
	 $('#home').siblings('li').children('a').removeClass('active');
	//comprovacion de la cookie
		  if($.cookie('username')) {
			  console.log("Usuario autentificado : " + $.cookie('username'));
			  getSpots();
		  }
		  else{
			  console.log("Usuario no autentificado");
			  window.location.replace("/auth.html");
		  }
	
	
});
