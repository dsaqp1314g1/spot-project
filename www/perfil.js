var API_BASE_URL = "http://localhost:8181/spot-api";
var stingsURL;

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
					var link = $('<a id="user-link" href="'+ user.getLinks("abrir-spots-user").href+'">'+ "Spots of: "+ user.name +'</a>');
					link.click(function(e){
						e.preventDefault();
						loadSpots($(e.target).attr('href'));
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
			
			$('<strong> Ciudad: </strong> ' + spot.ciudad + '<br>').appendTo($('#spots-perfil-container'));
			$('<strong> Deporte: </strong> ' + spot.deporte + '<br>').appendTo($('#spots-perfil-container'));
			var link = $('<a id="spot-link" href="'+spot.getLinks("abrir-spot").href+'">'+"Spot detail" +'</a>');
			link.click(function(e){
				e.preventDefault();
				idspot = spot.idspot;
				loadSpott($(e.target).attr('href'));
				return false;
			});
			var div = $('<div></div>')
			div.append(link);
			$('#spots-perfil-container').append(div);
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
					var link = $('<a id="user-link" href="'+ user.getLinks("abrir-spots-user").href+'">'+ "Spots of: "+ user.name +'</a>');
					link.click(function(e){
						e.preventDefault();
						loadSpots($(e.target).attr('href'));
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

function loadSpott(url){
	getSpot(url, function(spot){
		getSpotId(spot.idspot);
	});
}
