var BEETER_API_HOME="http://localhost:8000/spot-api";

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
	this.deporte = spot.deporte;
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
				$('<strong> Deporte: </strong> ' + spot.deporte + '<br><hr>').appendTo($('#spot_result'));
				$('<strong> Comentarios: </strong><br>').appendTo($('#spot_result'));
				
					$.each(spot.comentario, function(i, v) {
						var comentario = v;
						$('<strong> User: </strong>' + comentario.usuario + '<br>').appendTo($('#spot_result'));				
						$('<strong> Texto: </strong> ' + comentario.comentario + '<br>').appendTo($('#spot_result'));
						$('<strong> Fecha creacion: </strong> ' + comentario.fechacreacion + '<br><br>').appendTo($('#spot_result'));
						$('<button id="delete-coms'+comentario.idcomentario+'" class="btn btn-default">'+"Delete"+'</button><br><br>').appendTo($('#spot_result'));						
						
						$('#delete-coms'+comentario.idcomentario).click(function(e){
							e.preventDefault();
							deleteComment(spot.idspot,comentario.idcomentario);
							return false;
						});

					});
				$('<hr>').appendTo($('#spot_result'));
				 showEditForm(id) 
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