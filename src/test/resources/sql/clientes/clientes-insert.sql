insert into USUARIOS (id, username, password, role) values (100, 'davids@david2.com','$2a$12$UGVPHOHYl/SQLnc1G4b5WeXMlc8ayuvRUAyCQEIIKtFj/6KlgjbBm','ROLE_ADMIN');
insert into USUARIOS (id, username, password, role) values (101, 'pacific@david2.com','$2a$12$UGVPHOHYl/SQLnc1G4b5WeXMlc8ayuvRUAyCQEIIKtFj/6KlgjbBm','ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role) values (102, 'walter@david2.com','$2a$12$UGVPHOHYl/SQLnc1G4b5WeXMlc8ayuvRUAyCQEIIKtFj/6KlgjbBm','ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role) values (103, 'kakashi@email.com','$2a$12$UGVPHOHYl/SQLnc1G4b5WeXMlc8ayuvRUAyCQEIIKtFj/6KlgjbBm','ROLE_CLIENTE');

insert into CLIENTES (id, nome, cpf, id_usuario) values (10, 'davids@email.com', '19796804859', 100);
insert into CLIENTES (id, nome, cpf, id_usuario) values (20, 'Davidson Pacifico', '92476215007', 101);