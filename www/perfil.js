var API_BASE_URL = "http://localhost:8181/spot-api";
var stingsURL;


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

function initialize(myLatlng, contentString, idspot, imagen) {
	 

	  var infowindow = new google.maps.InfoWindow({
	      content: contentString
	  });

	  var boxText = document.createElement("div");
      boxText.style.cssText = "border:2px solid white;"+
      "margin-top: 6px;"+
      "background:#333;"+
      "color:#FFF;"+
      "font-family:Arial, Helvetica, sans-serif;"+
      "font-size:16px;"+
      "padding: .5em 1em;"+
      "-webkit-border-radius: 2px;"+
      "-moz-border-radius: 2px;"+
      "border-radius: 8px;"+
      "text-shadow:0 -1px #000000;"+
      "-webkit-box-shadow: 0 0  8px #000;"+
      "box-shadow: 0 0 8px #000;";
      boxText.innerHTML = contentString;
	  
	  var infobox = new InfoBox({
	         content: boxText,
	         disableAutoPan: false,
	         maxWidth: 150,
	         pixelOffset: new google.maps.Size(-140, 0),
	         zIndex: null,
	         boxStyle: {
	            background: "url('http://google-maps-utility-library-v3.googlecode.com/svn/trunk/infobox/examples/tipbox.gif') no-repeat",
	            opacity: 0.85,
	            width: "200px"
	        },
	        closeBoxMargin: "-10000000px -100000000px 0px 0px",
	        //closeBoxURL: "http://www.google.com/intl/en_us/mapfiles/close.gif",
	        infoBoxClearance: new google.maps.Size(1, 1)
	    });
	  
	  var marker = new google.maps.Marker({
	      position: myLatlng,
	      map: map,
	      icon:imagen,
	      animation: google.maps.Animation.DROP
	  });
	  google.maps.event.addListener(marker, 'click', function() {
		  getSpotId(idspot);
		  //map.setZoom(8);
		  map.setCenter(marker.getPosition());
		  //map.setCenter(overlay.getPosition());
		  smoothZoom(map, 10, map.getZoom());
		  });
	  
	  	google.maps.event.addListener(marker, 'mouseover', function() {
		    infobox.open(map, marker);
		  });
		  google.maps.event.addListener(marker, 'mouseout', function() {
			    infobox.close(map, marker);
			  });
		  marker.setMap(map);
}

function icnMarker(deporte){
	var image;
	if (deporte == 'bmx'){
		image  = 'icn_marker/bmx.png';
		return 	image;
	}
	
	else if (deporte == 'skate'){
		image  = 'icn_marker/rollerskate.png';
		return 	image;
	}
	else if (deporte == 'parkour'){
		image  = 'icn_marker/parkour.png';
		return 	image;	
	}

}

$('#buscar-amigo').click(function(e) {
	e.preventDefault();	
	$('#comment-form').hide();
	getUserParam($("#buscar_campo").val());
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
	var url = API_BASE_URL + '/user/juan';	
	$('progress').toggle();
	$("#perfil_result").text("");	
	
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
	var url = API_BASE_URL + '/user/juan/spots';
	
	$("#spots-perfil-container").text("");
	$("#spot_result").text("");
	$('#comment-form').hide();
	
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

function getUserParam(user) {
	var url = API_BASE_URL + '/user/'+user;
	$('progress').toggle();

	$("#perfil_result").text("");
	$("#spot_result").text("");
	$("#spots-perfil-container").text("");
	
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

function loadSpott(url){
	getSpot(url, function(spot){
		getSpotId(spot.idspot);
	});
}
