insert into users values('albert', MD5('albert'), 'caraculo' , 'albert@spotgram.com');
insert into user_roles values ('albert', 'administrador');
 
insert into users values('juan', MD5('juan'), 'ilcapone', 'juan@spotgram.com');
insert into user_roles values ('juan', 'administrador');

insert into users values('einar', MD5('einar'), 'bocadeacero', 'einar@spotgram.com');
insert into user_roles values ('einar', 'administrador');

insert into users values('flouri', MD5('cojonali'), 'flouri', 'flourinn@spotgram.com');
insert into user_roles values ('flouri', 'registrado');

insert into users values('manifest', MD5('12345'), 'manifest', 'manifest@spotgram.com');
insert into user_roles values ('manifest', 'registrado');



insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Baranda', 25.23213, 34.23112, 3, 'albert', 'skate', 'Barcelona', '04-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Mister pedo', 26.23213, 30.23112, 3, 'flouri', 'bmx', 'Barcelona', '08-05-22');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Jump', 45.23213, 23.23112, 4, 'juan', 'bmx', 'Berlin', '04-05-24');
insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad, fechaSubida) values ('Rudeas', 89.23213, 18.23112, 1, 'einar', 'parkour', 'Stockholm', '04-05-25');

insert into comentarios (idspot, usuario, comentario) values (1, 'flouri', 'Unas escaleras que te cagas');
insert into comentarios (idspot, usuario, comentario) values (2, 'manifest', 'Una rampa que te cagas');
insert into comentarios (idspot, usuario, comentario) values (3, 'juan', 'bugi bugi bang bang ');
insert into comentarios (idspot, usuario, comentario) values (1, 'manifest', 'OOOO fucking godnes');
insert into comentarios (idspot, usuario, comentario) values (6, 'einar', 'lijero cual montapuerco');
insert into comentarios (idspot, usuario, comentario) values (3, 'albert', 'ouuda what a hell');