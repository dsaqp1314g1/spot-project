var geocoder;
var markers = [];
var centermap = new google.maps.LatLng(41.3850639,2.17340349);
var mapOptions = {
  minZoom: 2, 
  maxZoom: 30,
  zoom: 4,
  panControl: false,
  zoomControl: false,
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

function initialize(myLatlng, contentString, idspot, image) {
	 
	  geocoder = new google.maps.Geocoder();
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
	      icon:image,
	      animation: google.maps.Animation.DROP
	  });
	  markers.push(marker);
	  google.maps.event.addListener(marker, 'click', function() {
		  getSpotId(idspot);
		  this.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
		  //map.setZoom(8);
		  map.setCenter(marker.getPosition());
		  //map.setCenter(overlay.getPosition());
		  smoothZoom(map, 10, map.getZoom());
		  });
	  
	  google.maps.event.addListener(marker, 'mouseover', function() {
		this.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
	    infobox.open(map, marker);

	    //infowindow.open(map,marker);
	  });
	  google.maps.event.addListener(marker, 'mouseout', function() {
		    infobox.close(map, marker);

		    //infowindow.close(map,marker);
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

//function addMarker(location) {
//	  var marker = new google.maps.Marker({
//	      position: location,
//	      map: map,
//	  });
//	  markers.push(marker);
//}

function codeAddress() {
    var address = document.getElementById("buscar_ciud").value;
    geocoder.geocode( { 'address': address}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        map.setCenter(results[0].geometry.location);
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });
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
	else if (deporte == 'ski'){
		image  = 'icn_marker/skiing.png';
		return 	image;	
	}
	else if (deporte == 'snow'){
		image  = 'icn_marker/snowboarding.png';
		return 	image;	
	}

}

//Sets the map on all markers in the array.
function setAllMap(map) {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(map);
  }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
  setAllMap(null);
}

// Shows any markers currently in the array.
function showMarkers() {
  setAllMap(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
  clearMarkers();
  markers = [];
}