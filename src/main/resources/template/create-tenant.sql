create database ${dbName};
create user '${username}'@'%' identified by '${password}';
grant select,insert,update,delete,create on ${dbName}.* to ${username};