insert into USUARIOS (id, username, password, role) values (100, 'davids@david2.com','$2a$12$UGVPHOHYl/SQLnc1G4b5WeXMlc8ayuvRUAyCQEIIKtFj/6KlgjbBm','ROLE_ADMIN');
insert into USUARIOS (id, username, password, role) values (101, 'pacific@david2.com','$2a$12$UGVPHOHYl/SQLnc1G4b5WeXMlc8ayuvRUAyCQEIIKtFj/6KlgjbBm','ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role) values (102, 'walter@david2.com','$2a$12$UGVPHOHYl/SQLnc1G4b5WeXMlc8ayuvRUAyCQEIIKtFj/6KlgjbBm','ROLE_CLIENTE');

insert into CLIENTES (id, nome, cpf, id_usuario) values (10, 'davids@email.com', '19796804859', 100);
insert into CLIENTES (id, nome, cpf, id_usuario) values (20, 'Davidson Pacifico', '92476215007', 101);

insert into VAGAS (id, codigo, status) values (10, 'A-01', 'OCUPADA');
insert into VAGAS (id, codigo, status) values (20, 'A-02', 'OCUPADA');
insert into VAGAS (id, codigo, status) values (30, 'A-03', 'OCUPADA');
insert into VAGAS (id, codigo, status) values (40, 'A-04', 'OCUPADA');
insert into VAGAS (id, codigo, status) values (50, 'A-05', 'OCUPADA');

insert into CLIENTES_TEM_VAGAS(numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values('20240803-212400', 'JDM-3432','MAZDA', 'MAZDA RX7', 'PRETO', '2024-03-15 10:23:43', 10, 10);
insert into CLIENTES_TEM_VAGAS( numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values('20240415-153254', 'TTP-3432','NISSAN', 'SKYLINE R34', 'PRETO', '2024-08-14 10:23:43', 20, 20);
insert into CLIENTES_TEM_VAGAS(numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
     values('20240803-212500', 'JDM-9393','TOYOTA', 'SUPRA', 'PRETO', '2024-03-15 10:23:43', 10, 30);
insert into CLIENTES_TEM_VAGAS( numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
     values('20240415-153255', 'TTP-3432','NISSAN', 'SKYLINE R34', 'PRETO', '2024-08-14 10:23:43', 20, 40);
insert into CLIENTES_TEM_VAGAS(numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
          values('20240803-222500', 'JDM-9393','TOYOTA', 'SUPRA', 'PRETO', '2024-03-15 10:23:43', 10, 50);
