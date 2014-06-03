drop user 'spot'@'localhost';
create user 'spot'@'localhost' identified by 'spot';
grant all privileges on spotdb.* to 'spot'@'localhost';
flush privileges;