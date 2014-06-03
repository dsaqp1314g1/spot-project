var API_BASE_URL = "http://localhost:8181/spot-api";
var stingsURL;
var username = 'albert';
var idspot;
// var password = 'albert';
// $.ajaxSetup({
// headers: { 'Authorization': "Basic "+$.base64.btoa(username+':'+password) }
// });


$("#button-list-spots").click(function(e) {
	e.preventDefault();
	if ($('#buscar_ciud').val() === '')
	{
	$('#exam-error').show();	
	//getSpotsParam($("#buscar_ciud").val(),$("#buscar_mod").val());
	}
else
{
	getSpotsParam($("#buscar_ciud").val(),$("#buscar_mod").val());
}
});
$('#buscar-amigo').click(function(e) {
	e.preventDefault();	
	$('#comment-form').hide();
	//getUserParam($("#buscar_campo").val());
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

$('#edit-task').click(function(e) {
	e.preventDefault();
	showEditForm();
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
					$('<h4> ID: ' + spot.idspot + '</h4>').appendTo($('#repos_result'));				
					$('<strong> Usuario: </strong> ' + spot.usuario + '<br>').appendTo($('#repos_result'));
					$('<strong> Ciudad: </strong> ' + spot.ciudad + '<br>').appendTo($('#repos_result'));
					$('<strong> Deporte: </strong> ' + spot.deporte + '<br>').appendTo($('#repos_result'));
					var link = $('<a id="sting-link" href="'+spot.getLinks("abrir-spot").href+'">'+"Spot detail" +'</a>');
					link.click(function(e){
						e.preventDefault();
						idspot = spot.idspot;
						loadSpot($(e.target).attr('href'));
						return false;
					});
					var div = $('<div></div>')
					div.append(link);
					$('#repos_result').append(div);
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
					$('<h4> ID: ' + spot.idspot + '</h4>').appendTo($('#repos_result'));				
					$('<strong> Usuario: </strong> ' + spot.usuario + '<br>').appendTo($('#repos_result'));
					$('<strong> Ciudad: </strong> ' + spot.ciudad + '<br>').appendTo($('#repos_result'));
					$('<strong> Deporte: </strong> ' + spot.deporte + '<br>').appendTo($('#repos_result'));
					var link = $('<a id="sting-link" href="'+spot.getLinks("abrir-spot").href+'">'+"Spot detail" +'</a>');
					link.click(function(e){
						e.preventDefault();
						loadSpot($(e.target).attr('href'));
						return false;
					});
					var div = $('<div></div>')
					div.append(link);
					$('#repos_result').append(div);
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

function showEditForm(id) {
	$('#comment-form').show();
	$('#edit-ok').click(
			function(e) {
				if ($('#edit-comment').val() === '') {
					$('#exam-error').show();
				} else {
					e.preventDefault();
					var comment = new Object();
					
					// CANVIAR PER AGAFAR L'USUARI QUE TOQUI!!!
					comment.usuario = 'albert';
					comment.comentario = $('#edit-comment').val();
					// AGAFAR LA DATAAA!!!
					// comment.data = 
					createComentario(id, JSON.stringify(comment));										
				}
			});
}
function createComentario(id , coment){
	var url = API_BASE_URL + "/spots/"+ id +"/comentario";
	
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
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
function hideEditForm() {
	$('#edit-sting-form').hide();
}

var stingsURL;
$(document).ready(function(){	
// loadRootAPI(function(rootAPI){
// // stingsURL = rootAPI.getLink('spots').href;
// // loadStings(rootAPI.getLink('spots').href);
// getTasks();
// });
	getSpots();
});