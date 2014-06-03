var API_BASE_URL = "http://localhost:8181/spot-api";
var stingsURL;

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
					var link = $('<a id="user-link" href="'+ user.getLinks("abrir-spots-user").href+'">'+ "Your Spots" +'</a>');
					link.click(function(e){
						e.preventDefault();
						loadSpot($(e.target).attr('href'));
						return false;
					});
					var div = $('<div></div>')
					div.append(link);
					$('#perfil_result').append(div);
														

	}).fail(function() {
		$('progress').toggle();

		$("#perfil_result").text("NO RESULT");
	});
}

function loadSpot(url){
	
	getSpot(url, function(user){ 
		getSpotByUser(user.username);
	});
}

function getSpotByUser(username) {
	var url = API_BASE_URL + '/user/juan/spots';
	
	$("#spots-perfil-container").text("");	
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		var repos = data;
		
		$.each(repos.spots, function(i, v) {
				var spot = new Spot(v);
				$('<strong> Ciudad: </strong> ' + spot.ciudad + '<br>').appendTo($('#spots-perfil-container'));
				$('<strong> Deporte: </strong> ' + spot.deporte + '<br><hr>').appendTo($('#spots-perfil-container'));
				//$('#uploadedImage').attr('src', spot.imageURL);
				var link = $('<a id="sting-link" href="'+spot.getLinks("abrir-spot").href+'">'+spot.idspot +'</a>');
				link.click(function(e){
					e.preventDefault();
					idspot = spot.idspot;
					loadSpot($(e.target).attr('href'));
					return false;
				});
				var div = $('<div></div>')
				div.append(link);
				$('<hr>').appendTo($('#spots-perfil-container'));
		});
	}).fail(function() {
		$("#spots-perfil-container").text("NO RESULT");
	});
}