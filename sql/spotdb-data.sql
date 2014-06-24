insert into users values('albert', MD5('albert'), 'Albert Anguela', 'albert@spotgram.com');
insert into user_roles values ('albert', 'administrador');
 
insert into users values('juan', MD5('juan'), 'Juan Gordo', 'juan@spotgram.com');
insert into user_roles values ('juan', 'administrador');

insert into users values('einar', MD5('einar'), 'Einar Mellerson', 'einar@spotgram.com');
insert into user_roles values ('einar', 'administrador');

insert into users values('flouri', MD5('flouri'), 'Flouri de Koojonalio', 'flourinn@spotgram.com');
insert into user_roles values ('flouri', 'registrado');

insert into users values('manifest', MD5('manifest'), 'Hijo de Manifest', 'manifest@spotgram.com');
insert into user_roles values ('manifest', 'registrado');

insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Olie', 41.385063900000000000, 2.173403499999949400, 0, 'albert', 'skate', 'Barcelona', '04-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Magician', 41.3856319, 2.1687202000000525, 0, 'albert', 'bmx', 'Barcelona', '08-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Rocas la Tuca', 42.7882105, -0.5269639000000552, 0, 'albert', 'ski', 'Candanchu', '04-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Jump', 40.714352800000000000, -74.005973100000000000, 0, 'juan', 'bmx', 'New York', '04-05-24');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('El Babybel', 42.80995069999999, -0.5071777000000566, 0, 'juan', 'ski', 'Astun', '04-05-25');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Skiiile', 41.279059900000000000, 1.973474300000020700, 0, 'juan', 'skate', 'Casteldefels', '04-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('How posision', 40.782864700000000000, -73.965355100000010000, 0, 'einar', 'parkour', 'New York', '04-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Hell flip', 41.399603900000000000, 2.119103399999971800, 0, 'einar', 'bmx', 'Barcelona', '08-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Die Wurst', 52.520006599999990000, 13.404953999999975000, 0, 'einar', 'skate', 'Berlin', '04-05-24');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Croquette', 45.468322600000000000, 6.905578500000047000, 0, 'flouri', 'snow', 'Tignes', '04-05-24');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Joonker Bigest', 41.392206000000000000, 2.143175000000042000, 0, 'flouri', 'parkour', 'Barcelona', '04-05-25');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Snow Parck', 42.776654400000000000, -0.359537199999977000, 0, 'flouri', 'snow', 'Formigal', '04-05-25');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Betrunken', 52.514126700000000000, 13.349273799999991000, 0, 'manifest', 'bmx', 'Berlin', '04-05-24');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Todos son Hijos de Manifest', 59.328930000000010000, 18.064910000000054000, 0, 'manifest', 'snow', 'Stockholm', '04-05-25');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Las estacas pibe', -34.603723200000000000, -58.381593100000030000, 0, 'manifest', 'parkour', 'Buenos Aires', '04-05-25');