var API_BASE_URL="http://localhost:8181/spot-api";
var stingsURL;
var username="albert";
var USER="juan";
var idspot;
// var password = 'albert';
// $.ajaxSetup({
// headers: { 'Authorization': "Basic "+$.base64.btoa(username+':'+password) }
// });

var centermap = new google.maps.LatLng(41.3850639,2.17340349);
var mapOptions = {
  minZoom: 2, 
  maxZoom: 12,
  zoom: 4,
  center: centermap
};

var map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);

function smoothZoom (map, max, cnt) {
    if (cnt >= max) {
            return;
        }
    else {
        z = google.maps.event.addListener(map, 'zoom_changed', function(event){
            google.maps.event.removeListener(z);
            smoothZoom(map, max, cnt + 1);
        });
        setTimeout(function(){map.setZoom(cnt)}, 150);
    }
}  

function initialize(myLatlng, contentString, idspot) {
	 

	  var infowindow = new google.maps.InfoWindow({
	      content: contentString
	  });

	  var marker = new google.maps.Marker({
	      position: myLatlng,
	      map: map,
	  });
	  google.maps.event.addListener(marker, 'click', function() {
		  getSpotId(idspot);
		  //map.setZoom(8);
		  map.setCenter(marker.getPosition());
		  //map.setCenter(overlay.getPosition());
		  smoothZoom(map, 10, map.getZoom());
		  });
	  
	  google.maps.event.addListener(marker, 'mouseover', function() {
	    infowindow.open(map,marker);
	  });
	  google.maps.event.addListener(marker, 'mouseout', function() {
		    infowindow.close(map,marker);
		  });
	  marker.setMap(map);
//	  var strictBounds = new google.maps.LatLngBounds(
//			     new google.maps.LatLng(-72.26740896926444, 156.09375), 
//			     new google.maps.LatLng(72.06166091689721, -149.0625)
//			   );
//	   // Listen for the dragend event
//	   google.maps.event.addListener(map, 'drag', function() {
//	     if (strictBounds.contains(map.getCenter())) return;
//
//	     // We're out of bounds - Move the map back within the bounds
//
//	     var c = map.getCenter(),
//	         x = c.lng(),
//	         y = c.lat(),
//	         maxX = strictBounds.getNorthEast().lng(),
//	         maxY = strictBounds.getNorthEast().lat(),
//	         minX = strictBounds.getSouthWest().lng(),
//	         minY = strictBounds.getSouthWest().lat();
//
//	     if (x < minX) x = minX;
//	     if (x > maxX) x = maxX;
//	     if (y < minY) y = minY;
//	     if (y > maxY) y = maxY;
//
//	     map.setCenter(new google.maps.LatLng(y, x));
//	   });
}



$("#button-list-spots").click(function(e) {
	e.preventDefault();
	getSpotsParam($("#buscar_ciud").val(),$("#buscar_mod").val());
});
$('#buscar-amigo').click(function(e) {
	e.preventDefault();	
	$('#comment-form').hide();
	//getUserParam($("#buscar_campo").val());
});
$("#button-list-one").click(function(e) {
	e.preventDefault();
	getSpotId($("#buscar_id").val());
});
$('#button-delete-comment').click(function(e) {
	e.preventDefault();					
	deleteComment($("#buscar_ciud").val());
});
$('#comment-cancel').click(function(e) {
	e.preventDefault();					
	$("#edit-comment").val('');
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
					var contentString ='<h4> ID: ' + spot.idspot + '</h4>'+ 
					'<strong> Usuario: </strong> ' + spot.usuario + '<br>'+
					'<strong> Ciudad: </strong> ' + spot.ciudad + '<br>'+
					'<strong> Deporte: </strong> ' + spot.deporte + '<br>';
					var myLatlng = new google.maps.LatLng(spot.latitud, spot.longitud);
					initialize(myLatlng, contentString, idmarker);								
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
					var contentString ='<h4> ID: ' + spot.idspot + '</h4>'+ 
					'<strong> Usuario: </strong> ' + spot.usuario + '<br>'+
					'<strong> Ciudad: </strong> ' + spot.ciudad + '<br>'+
					'<strong> Deporte: </strong> ' + spot.deporte + '<br>';
					var myLatlng = new google.maps.LatLng(spot.latitud, spot.longitud);
					initialize(myLatlng, contentString, idmarker);								
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
	getSpots();
});
