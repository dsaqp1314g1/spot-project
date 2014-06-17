drop database if exists spotdb;
create database spotdb;

use spotdb;

create table users (
    username	varchar(20) not null primary key,
    userpass 	char(32) not null,
	name		varchar(70) not null,
	email		varchar(255) not null
);

create table user_roles (
	username			varchar(20) not null,
	rolename 			varchar(20) not null,
	foreign key(username) references users(username) on delete cascade,
	primary key (username, rolename)
);

create table spots(
	idspot 				int not null auto_increment primary key,
	title	char(50) not null,
	latitud 			double not null,
	longitud				double not null,
	megustas			int not null,
	usuario				varchar(20) not null,
	deporte				varchar(20) not null,
	ciudad				varchar(35) not null,
	fechaSubida			timestamp,
	foreign key(usuario) references users(username)
);

create table comentarios(

	idcomentario 		int not null auto_increment primary key,
	idspot 				int not null,
	usuario				varchar(20) not null,
	comentario			varchar(140) not null,
	fechacreacion       timestamp not null,
	foreign key(idspot) references spots(idspot),
	foreign key(usuario) references users(username)

);

create table actualizaciones(

	idcomentario 		int not null auto_increment primary key,
	idspot 				int not null,
	nombrespot				varchar(20) not null,
	userspot				varchar(20) not null,
	usercomentario			varchar(20) not null,
	fechacreacion       timestamp not null,
	foreign key(idspot) references spots(idspot),
	foreign key(userspot) references users(username),
	foreign key(usercomentario) references users(username)
	
);