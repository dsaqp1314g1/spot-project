var BEETER_API_HOME="http://localhost:8181/spot-api";

//FALTA SI YA HAS DADO HA MEGUSTA QUE YA NO PUEDAS HACERLO ************************************
$('#NOmegustas').click(function(e){
	e.preventDefault();
	 NOMegustaSpot(spot.idspot);
	 $('#NOmegustas'+spot.idspot).hide();
	 $('#megustas'+spot.idspot).show();

	return false;
});
$('#megustas').click(function(e){
	e.preventDefault();
	 MegustaSpot(spot.idspot);
	 $('#megustas'+spot.idspot).hide();
	 $('#NOmegustas'+spot.idspot).show();

	return false;
});


function Link(rel, linkheader){
	this.rel = rel;
	this.href = decodeURIComponent(linkheader.find(rel).template().template);
	this.type = linkheader.find(rel).attr('type');
	this.title = linkheader.find(rel).attr('title');
}

function buildLinks(linkheaders){
	var links = {};
	$.each(linkheaders, function(i,linkheader){
		var linkhdr = $.linkheaders(linkheader);
		var rels = linkhdr.find().attr('rel').split(' ');
		$.each(rels, function(key,rel){
			var link = new Link(rel, linkhdr);
			links[rel] = link;
		});
	});

	return links;
}

function Spot(spot){
	this.usuario = spot.usuario;
	this.idspot = spot.idspot;
	this.title = spot.title;
	this.megusta = spot.megusta;
	this.deporte = spot.deporte;
	this.latitud = spot.latitud;
	this.longitud = spot.longitud;
	this.ciudad = spot.ciudad;
	this.links = buildLinks(spot.links);
	var instance = this;
	this.getLinks = function(rel){
		return this.links[rel];
	}
}

function User(user){
	this.username = user.username;
	this.name = user.name;
	this.userpass = user.userpass;
	this.email = user.email;
	this.links = buildLinks(user.links);
	this.getLinks = function(rel){
		return this.links[rel];
	}
}

function StingCollection(stingCollection){
	this.oldestTimestamp = stingCollection.oldestTimestamp;
	this.newestTimestamp = stingCollection.newestTimestamp;
	this.stings = stingCollection.stings;

	this.links = buildLinks(stingCollection.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}
}

function RootAPI(rootAPI){
	this.links = buildLinks(rootAPI.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}
}


function loadRootAPI(success){
	$.ajax({
		url : BEETER_API_HOME,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		var rootAPI = new RootAPI(response);
    	success(rootAPI);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
	
}

function getStings(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		var stingCollection = new StingCollection(response);
		//success(response.stings);
		success(stingCollection);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function getSpot(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	}).done(function (data, status, jqxhr) {
		var spot = $.parseJSON(jqxhr.responseText);
		success(spot);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function updateSting(url, type, sting, success){
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		contentType: type, 
		data: sting
	})
	.done(function (data, status, jqxhr) {
		var sting = $.parseJSON(jqxhr.responseText);
		success(sting);
		//console.log("SUCCESS ON PUT!!");
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}
function deleteComment(idspot,idcoment) {
	var url = API_BASE_URL +"/spots/"+idspot+"/comentario/"+idcoment;
	
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {			
		getSpotId(idspot);
	}).fail(function() {
		alert("ERROR");
	});
}

function getSpotId(id) {
	var url = API_BASE_URL + '/spots/'+id;
	
	$("#spot_result").text("");	
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		
				var spot =data;
				$('<img id="uploadedImage" class="img-responsive"><br>').appendTo($('#spot_result'));
				$('#uploadedImage').attr('src', spot.imageURL);
				$('<strong> Usuario: </strong> ' + spot.usuario + '<br>').appendTo($('#spot_result'));
				$('<strong> Ciudad: </strong> ' + spot.ciudad + '<br>').appendTo($('#spot_result'));
				$('<strong> Deporte: </strong> ' + spot.deporte + '<br>').appendTo($('#spot_result'));
				$('<strong> Megustas: </strong> ' + spot.megusta + '<br><hr>').appendTo($('#spot_result'));
				$('<strong> Comentarios: </strong><br>').appendTo($('#spot_result'));				
					$.each(spot.comentario, function(i, v) {
						var comentario = v;
						$('<strong> User: </strong>' + comentario.usuario + '<br>').appendTo($('#spot_result'));				
						$('<strong> Texto: </strong> ' + comentario.comentario + '<br>').appendTo($('#spot_result'));
						$('<strong> Fecha creacion: </strong> ' + comentario.fechacreacion + '<br><br>').appendTo($('#spot_result'));
						if (comentario.usuario === USUARIO)
							{
						$('<button id="delete-coms'+comentario.idcomentario+'" class="btn btn-default">'+"Delete"+'</button><br><br>').appendTo($('#spot_result'));												
						$('#delete-coms'+comentario.idcomentario).click(function(e){
							e.preventDefault();
							deleteComment(spot.idspot,comentario.idcomentario);
							return false;
						});
							}

					});
				$('<hr>').appendTo($('#spot_result'));
				 showEditForm(id) 
	}).fail(function() {
		$("#spot_result").text("NO RESULT");
	});
}

function NOMegustaSpot(id) {
	var url = API_BASE_URL + '/spots/'+id+ '/NOmegustas';
	
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		getSpotId(id);
	}).fail(function() {
		$("#spot_result").text("NO RESULT");
	});
}

function MegustaSpot(id) {
	var url = API_BASE_URL + '/spots/'+id+ '/megustas';
	
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		getSpotId(id);
	}).fail(function() {
		$("#spot_result").text("NO RESULT");
	});
}

function deleteSting(url, success){
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true
	})
	.done(function (data, status, jqxhr) {
		//var sting = $.parseJSON(jqxhr.responseText);
		success();
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}
function showEditForm(id) {
	$('#comment-form').show();	

	$('#comment-ok').click(
			function(e) {
				if ($('#edit-comment').val() === '') {
					$('#exam-error').show();
				} else {
					e.preventDefault();
					var comment = new Object();				
					// CANVIAR PER AGAFAR L'USUARI QUE TOQUI!!!
					comment.usuario = 'juan';
					comment.comentario = $('#edit-comment').val();
					// AGAFAR LA DATAAA!!!
					// comment.data = 
					createComentario(id, JSON.stringify(comment));										
				}
			});
}
function createComentario(id,coment){
	var url = API_BASE_URL + "/spots/"+ id +"/comentario";	
	$.ajax({
		url : url,
		type : 'POST', 
		crossDomain : true,
		contentType: 'application/vnd.catalogo.spot.comentario+json',
		data: coment
	})
	.done(function (data, status, jqxhr) {
		var coment = $.parseJSON(jqxhr.responseText);
		$('#edit-comment').val('');
		getSpotId(id);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}
