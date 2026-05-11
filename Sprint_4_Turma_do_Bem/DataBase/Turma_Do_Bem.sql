-- RM567926 Gustavo de Jesus Silva
-- RM568419 Gustavo Rodrigues Siciliano
-- RM567614 Samuel Keniti Kina de Lima

-- Apagando Tabelas
DROP TABLE tdb_RegistroIA 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_UtilizaMaterial 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_ParticipaCampanha 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_AjudaDoacao 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Prontuario 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Procedimento 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Consulta 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Doacao 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Campanha 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Material 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Usuario 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Doador 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Voluntario 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Dentista 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Paciente 
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Pessoa 
    CASCADE CONSTRAINTS;

-- Criando Tabelas

CREATE TABLE tdb_Pessoa (
    cpf VARCHAR2(14) NOT NULL,
    nome VARCHAR2(80) NOT NULL,
    data_nasc DATE,
    telefone VARCHAR2(15),
    email VARCHAR2(80),
    cep VARCHAR2(9),
    logradouro VARCHAR2(80),
    numero VARCHAR2(8),
    bairro VARCHAR2(50),
    cidade VARCHAR2(50),
    uf CHAR(2),
    ativo CHAR(1) DEFAULT 'S' NOT NULL,
    dt_cadastro DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT pk_tdb_Pessoa PRIMARY KEY (cpf),
    CONSTRAINT uk_tdb_Pessoa_email UNIQUE (email),
    CONSTRAINT ck_tdb_Pessoa_email CHECK (email IS NULL OR email LIKE '%@%'),
    CONSTRAINT ck_tdb_Pessoa_ativo CHECK (ativo IN ('S','N')),
    CONSTRAINT ck_tdb_Pessoa_uf CHECK (uf IS NULL OR LENGTH(TRIM(uf)) = 2)
);

CREATE TABLE tdb_Paciente (
    id_paciente NUMBER NOT NULL,
    cpf VARCHAR2(14) NOT NULL,
    programa VARCHAR2(20) NOT NULL,
    renda_familiar NUMBER(10,2),
    convenio VARCHAR2(50),
    distancia_km NUMBER(6,2),
    turno_preferencial VARCHAR2(5),
    observacoes VARCHAR2(150),
    CONSTRAINT pk_tdb_Paciente PRIMARY KEY (id_paciente),
    CONSTRAINT uk_tdb_Paciente_cpf UNIQUE (cpf),
    CONSTRAINT uk_tdb_Paciente_id_cpf UNIQUE (id_paciente, cpf),
    CONSTRAINT fk_tdb_Paciente_pessoa FOREIGN KEY (cpf)
        REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE,
    CONSTRAINT ck_tdb_Pac_renda CHECK (renda_familiar IS NULL OR renda_familiar >= 0),
    CONSTRAINT ck_tdb_Pac_distancia CHECK (distancia_km IS NULL OR distancia_km >= 0),
    CONSTRAINT ck_tdb_Pac_programa CHECK (programa IN ('DENTISTAS_DO_BEM','APOLONICAS_DO_BEM')),
    CONSTRAINT ck_tdb_Pac_turno CHECK (turno_preferencial IS NULL OR turno_preferencial IN ('MANHA','TARDE','NOITE'))
);

CREATE TABLE tdb_Dentista (
    cro VARCHAR2(20) NOT NULL,
    cpf VARCHAR2(14) NOT NULL,
    especialidade VARCHAR2(50),
    disponibilidade VARCHAR2(80),
    CONSTRAINT pk_tdb_Dentista PRIMARY KEY (cro,cpf),
    CONSTRAINT uk_tdb_Dentista_cpf UNIQUE (cpf),
    CONSTRAINT fk_tdb_Dentista_pessoa FOREIGN KEY (cpf)
        REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE
);

CREATE TABLE tdb_Voluntario (
    id_voluntario NUMBER NOT NULL,
    cpf VARCHAR2(14) NOT NULL,
    area VARCHAR2(50),
    disponibilidade VARCHAR2(80),
    CONSTRAINT pk_tdb_Voluntario PRIMARY KEY (id_voluntario),
    CONSTRAINT uk_tdb_Voluntario_cpf UNIQUE (cpf),
    CONSTRAINT fk_tdb_Voluntario_pessoa FOREIGN KEY (cpf)
        REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE
);

CREATE TABLE tdb_Doador (
    id_doador NUMBER NOT NULL,
    cpf VARCHAR2(14) NOT NULL,
    tipo_doador CHAR(2) NOT NULL,
    CONSTRAINT pk_tdb_Doador PRIMARY KEY (id_doador),
    CONSTRAINT uk_tdb_Doador_cpf UNIQUE (cpf),
    CONSTRAINT fk_tdb_Doador_pessoa FOREIGN KEY (cpf)
        REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE,
    CONSTRAINT ck_tdb_Doador_tipo CHECK (tipo_doador IN ('PF','PJ'))
);

CREATE TABLE tdb_Usuario (
    id_usuario NUMBER NOT NULL,
    cpf VARCHAR2(14) NOT NULL,
    login VARCHAR2(50) NOT NULL,
    senha_hash VARCHAR2(100) NOT NULL,
    perfil VARCHAR2(20) NOT NULL,
    ativo CHAR(1) DEFAULT 'S' NOT NULL,
    dt_cadastro DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT pk_tdb_Usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uk_tdb_Usuario_login UNIQUE (login),
    CONSTRAINT uk_tdb_Usuario_cpf UNIQUE (cpf),
    CONSTRAINT fk_tdb_Usuario_pessoa FOREIGN KEY (cpf)
        REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE,
    CONSTRAINT ck_tdb_Usuario_perfil CHECK (perfil IN ('ADMIN','DENTISTA','VOLUNTARIO','GESTOR')),
    CONSTRAINT ck_tdb_Usuario_ativo CHECK (ativo IN ('S','N'))
);

CREATE TABLE tdb_Material (
    id_material NUMBER NOT NULL,
    nome VARCHAR2(60) NOT NULL,
    descricao VARCHAR2(150),
    quantidade NUMBER(10,2) DEFAULT 0 NOT NULL,
    quantidade_minima NUMBER(10,2) DEFAULT 10,
    unidade VARCHAR2(20),
    validade DATE,
    CONSTRAINT pk_tdb_Material PRIMARY KEY (id_material),
    CONSTRAINT ck_tdb_Mat_qtd CHECK (quantidade >= 0),
    CONSTRAINT ck_tdb_Mat_qtd_min CHECK (quantidade_minima IS NULL OR quantidade_minima >= 0)
);

CREATE TABLE tdb_Campanha (
    id_campanha NUMBER NOT NULL,
    nome VARCHAR2(80) NOT NULL,
    descricao VARCHAR2(150),
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    meta_valor NUMBER(10,2),
    total_arrecadado NUMBER(10,2) DEFAULT 0,
    ativo CHAR(1) DEFAULT 'S' NOT NULL,
    CONSTRAINT pk_tdb_Campanha PRIMARY KEY (id_campanha),
    CONSTRAINT ck_tdb_Camp_datas CHECK (data_fim > data_inicio),
    CONSTRAINT ck_tdb_Camp_meta CHECK (meta_valor IS NULL OR meta_valor > 0),
    CONSTRAINT ck_tdb_Camp_ativo CHECK (ativo IN ('S','N')),
    CONSTRAINT ck_tdb_Camp_arrec CHECK (total_arrecadado >= 0)
);

CREATE TABLE tdb_Doacao (
    id_doacao NUMBER NOT NULL,
    id_doador NUMBER,
    id_campanha NUMBER,
    valor NUMBER(10,2) NOT NULL,
    data_doacao DATE DEFAULT SYSDATE NOT NULL,
    forma_pgto VARCHAR2(15) NOT NULL,
    observacoes VARCHAR2(150),
    CONSTRAINT pk_tdb_Doacao PRIMARY KEY (id_doacao),
    CONSTRAINT fk_tdb_Doacao_doador FOREIGN KEY (id_doador)
        REFERENCES tdb_Doador(id_doador),
    CONSTRAINT fk_tdb_Doacao_campanha FOREIGN KEY (id_campanha)
    REFERENCES tdb_Campanha(id_campanha),
    CONSTRAINT ck_tdb_Doacao_valor CHECK (valor > 0),
    CONSTRAINT ck_tdb_Doacao_pgto CHECK (forma_pgto IN ('PIX','BOLETO','CARTAO','TRANSFERENCIA','DINHEIRO'))
);

CREATE TABLE tdb_Consulta (
    id_consulta NUMBER NOT NULL,
    id_paciente NUMBER NOT NULL,
    cpf_paciente VARCHAR2(14) NOT NULL,
    cro_dentista VARCHAR2(20) NOT NULL,
    cpf_dentista VARCHAR2(14) NOT NULL,
    data_consulta DATE NOT NULL,
    horario VARCHAR2(5),
    turno VARCHAR2(5),
    status VARCHAR2(15) DEFAULT 'AGENDADA' NOT NULL,
    tipo VARCHAR2(50),
    distancia_km NUMBER(6,2),
    observacoes VARCHAR2(150),
    CONSTRAINT pk_tdb_Consulta PRIMARY KEY (id_consulta),
    CONSTRAINT fk_tdb_Cons_paciente FOREIGN KEY (id_paciente, cpf_paciente)
        REFERENCES tdb_Paciente(id_paciente, cpf) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_Cons_dentista FOREIGN KEY (cro_dentista, cpf_dentista)
        REFERENCES tdb_Dentista(cro, cpf),
    CONSTRAINT ck_tdb_Cons_status CHECK (status IN ('AGENDADA','REALIZADA','CANCELADA','FALTA')),
    CONSTRAINT ck_tdb_Cons_turno CHECK (turno IS NULL OR turno IN ('MANHA','TARDE','NOITE')),
    CONSTRAINT ck_tdb_Cons_distancia CHECK (distancia_km IS NULL OR distancia_km >= 0),
    CONSTRAINT ck_tdb_Cons_horario CHECK (horario IS NULL OR REGEXP_LIKE(horario,'^([01][0-9]|2[0-3]):[0-5][0-9]$'))
);

CREATE TABLE tdb_Prontuario (
    id_prontuario NUMBER NOT NULL,
    id_consulta NUMBER NOT NULL,
    descricao VARCHAR2(255) NOT NULL,
    observacoes VARCHAR2(150),
    dt_registro DATE DEFAULT SYSDATE NOT NULL,
    ultima_alt DATE,
    CONSTRAINT pk_tdb_Prontuario PRIMARY KEY (id_prontuario),
    CONSTRAINT uk_tdb_Pront_consulta UNIQUE (id_consulta),
    CONSTRAINT fk_tdb_Pront_consulta FOREIGN KEY (id_consulta)
        REFERENCES tdb_Consulta(id_consulta) ON DELETE CASCADE
);

CREATE TABLE tdb_Procedimento (
    id_procedimento NUMBER NOT NULL,
    id_consulta NUMBER NOT NULL,
    cro_dentista VARCHAR2(20) NOT NULL,
    cpf_dentista VARCHAR2(14) NOT NULL,
    nome VARCHAR2(60) NOT NULL,
    tipo VARCHAR2(50),
    duracao_min NUMBER(5),
    custo NUMBER(10,2) DEFAULT 0,
    orientacao VARCHAR2(150),
    CONSTRAINT pk_tdb_Procedimento PRIMARY KEY (id_procedimento),
    CONSTRAINT fk_tdb_Proc_consulta FOREIGN KEY (id_consulta)
        REFERENCES tdb_Consulta(id_consulta) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_Proc_dentista FOREIGN KEY (cro_dentista,cpf_dentista)
        REFERENCES tdb_Dentista(cro,cpf),
    CONSTRAINT ck_tdb_Proc_custo CHECK (custo IS NULL OR custo >= 0),
    CONSTRAINT ck_tdb_Proc_duracao CHECK (duracao_min IS NULL OR duracao_min > 0)
);

CREATE TABLE tdb_RegistroIA (
    id_registro NUMBER NOT NULL,
    id_consulta NUMBER,
    tipo_predicao VARCHAR2(15) NOT NULL,
    entrada_json CLOB NOT NULL,
    prob_resultado NUMBER(5,4),
    classe_prevista VARCHAR2(20),
    risco VARCHAR2(10),
    modelo_versao VARCHAR2(20) DEFAULT 'v1.0',
    acerto CHAR(1),
    dt_predicao DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT pk_tdb_RegistroIA PRIMARY KEY (id_registro),
    CONSTRAINT fk_tdb_RegIA_consulta FOREIGN KEY (id_consulta)
        REFERENCES tdb_Consulta(id_consulta) ON DELETE SET NULL,
    CONSTRAINT ck_tdb_RegIA_tipo CHECK (tipo_predicao IN ('FALTA','ARRECADACAO')),
    CONSTRAINT ck_tdb_RegIA_risco CHECK (risco IS NULL OR risco IN ('ALTO','MEDIO','BAIXO')),
    CONSTRAINT ck_tdb_RegIA_prob CHECK (prob_resultado IS NULL OR (prob_resultado >= 0 AND prob_resultado <= 1)),
    CONSTRAINT ck_tdb_RegIA_acerto CHECK (acerto IS NULL OR acerto IN ('S','N'))
);

CREATE TABLE tdb_UtilizaMaterial (
    id_procedimento NUMBER NOT NULL,
    id_material NUMBER NOT NULL,
    quantidade_usada NUMBER(10,2) NOT NULL,
    CONSTRAINT pk_tdb_UtilizaMat PRIMARY KEY (id_procedimento,id_material),
    CONSTRAINT fk_tdb_UtilMat_proc FOREIGN KEY (id_procedimento)
        REFERENCES tdb_Procedimento(id_procedimento) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_UtilMat_mat FOREIGN KEY (id_material)
        REFERENCES tdb_Material(id_material) ON DELETE CASCADE,
    CONSTRAINT ck_tdb_UtilMat_qtd CHECK (quantidade_usada > 0)
);

CREATE TABLE tdb_ParticipaCampanha (
    id_campanha NUMBER NOT NULL,
    id_voluntario NUMBER NOT NULL,
    CONSTRAINT pk_tdb_PartCamp PRIMARY KEY (id_campanha,id_voluntario),
    CONSTRAINT fk_tdb_PartCamp_camp FOREIGN KEY (id_campanha)
        REFERENCES tdb_Campanha(id_campanha) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_PartCamp_vol FOREIGN KEY (id_voluntario)
        REFERENCES tdb_Voluntario(id_voluntario) ON DELETE CASCADE
);

CREATE TABLE tdb_AjudaDoacao (
    id_voluntario NUMBER NOT NULL,
    id_doacao NUMBER NOT NULL,
    CONSTRAINT pk_tdb_AjudaDoacao PRIMARY KEY (id_voluntario,id_doacao),
    CONSTRAINT fk_tdb_AjudaDoc_vol FOREIGN KEY (id_voluntario)
        REFERENCES tdb_Voluntario(id_voluntario) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_AjudaDoc_doac FOREIGN KEY (id_doacao)
        REFERENCES tdb_Doacao(id_doacao) ON DELETE CASCADE
);

-- Inserindo Dados

INSERT INTO tdb_Pessoa VALUES ('111.111.111-11','Maria Oliveira Santos',DATE'1990-05-10','(11)99999-0001','maria.oliveira@email.com','04120-020',
    'Rua M.F. Klabin','1000','Vila Mariana','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('222.222.222-22','Pedro Henrique Santos',DATE'1985-03-22','(11)99999-0002','pedro.santos@email.com','04038-001',
    'Rua Domingos','200','Vila Clementino','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('333.333.333-33','Lucia Maria Ferreira',DATE'2000-11-15','(11)99999-0003','lucia.ferreira@email.com','01310-100',
    'Av. Paulista','449','Bela Vista','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('444.444.444-44','Bruno Alves Mendes',DATE'1995-06-18','(11)99999-0004','bruno.alves@email.com','04120-020',
    'Rua M.F. Klabin','100','Vila Mariana','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('555.555.555-55','Dr. Joao Carlos Silva',DATE'1975-02-14','(11)98888-0001','joao.silva@dentista.com','01415-001',
    'Rua Augusta','500','Consolacao','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('666.666.666-66','Dra. Ana Paula Costa',DATE'1982-07-30','(11)98888-0002','ana.costa@dentista.com','04038-001',
    'Rua Domingos','300','Vila Clementino','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('777.777.777-77','Dr. Carlos Eduardo Lima',DATE'1979-11-05','(11)98888-0003','carlos.lima@dentista.com','01310-200',
    'Av. Paulista','800','Bela Vista','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('888.888.888-88','Fernanda Lima Souza',DATE'1993-08-20','(11)97777-0001','fernanda.souza@email.com','04038-001',
    'Rua Domingos','50','Vila Clementino','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('999.999.999-99','Rafael Gomes Pereira',DATE'1988-04-12','(11)97777-0002','rafael.pereira@email.com','01310-300',
    'Av. Paulista','1200','Bela Vista','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('100.100.100-10','Marcos Roberto Almeida',DATE'1992-09-08','(11)97777-0003','marcos.almeida@email.com','04538-132',
    'Av. Brigadeiro Faria Lima','1000','Itaim Bibi','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('101.101.101-10','Dra. Fernanda Rocha',DATE'1987-01-19','(11)98888-0004','fernanda.rocha@dentista.com','04546-042',
    'Rua Joaquim Floriano','210','Itaim Bibi','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('102.102.102-10','Dr. Rafael Moreira',DATE'1980-06-11','(11)98888-0005','rafael.moreira@dentista.com','05407-002',
    'Rua dos Pinheiros','350','Pinheiros','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('103.103.103-10','Dra. Marina Alves',DATE'1989-03-25','(11)98888-0006','marina.alves@dentista.com','04011-000',
    'Rua Vergueiro','820','Paraiso','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('104.104.104-10','Dr. Paulo Henrique Costa',DATE'1984-12-03','(11)98888-0007','paulo.costa@dentista.com','01311-200',
    'Alameda Santos','1450','Bela Vista','Sao Paulo','SP','S',SYSDATE);
INSERT INTO tdb_Pessoa VALUES ('105.105.105-10','Juliana Martins Souza',DATE'1991-04-17','(11)97777-0004','juliana.martins@email.com','04109-030',
    'Rua Cubatao','500','Vila Mariana','Sao Paulo','SP','S',SYSDATE);

INSERT INTO tdb_Paciente VALUES (1,'111.111.111-11','DENTISTAS_DO_BEM',1800.00,NULL,3.5,'MANHA','Historico de gengivite');
INSERT INTO tdb_Paciente VALUES (2,'222.222.222-22','APOLONICAS_DO_BEM',1200.00,NULL,22.0,'TARDE','Necessita acomp. ortodontico');
INSERT INTO tdb_Paciente VALUES (3,'333.333.333-33','DENTISTAS_DO_BEM',900.00,NULL,15.0,'NOITE','Primeira consulta na ONG');
INSERT INTO tdb_Paciente VALUES (4,'444.444.444-44','DENTISTAS_DO_BEM',2200.00,'Unimed',5.0,'MANHA','Paciente com plano de saude');
INSERT INTO tdb_Paciente VALUES (5,'888.888.888-88','APOLONICAS_DO_BEM',700.00,NULL,28.0,'TARDE','Encaminhada pelo CRAS');
INSERT INTO tdb_Paciente VALUES (6,'999.999.999-99','DENTISTAS_DO_BEM',1500.00,NULL,8.0,'MANHA','Sensibilidade dentaria');
INSERT INTO tdb_Paciente VALUES (7,'100.100.100-10','APOLONICAS_DO_BEM',600.00,NULL,35.0,'NOITE','Atendimento emergencial');

INSERT INTO tdb_Dentista VALUES ('SP-12345','555.555.555-55','Clinico Geral','Segunda a Sexta - Manha');
INSERT INTO tdb_Dentista VALUES ('SP-67890','666.666.666-66','Ortodontia','Terca e Quinta - Tarde');
INSERT INTO tdb_Dentista VALUES ('SP-11111','777.777.777-77','Periodontia','Segunda e Quarta - Tarde');
INSERT INTO tdb_Dentista VALUES ('RJ-22222','101.101.101-10','Endodontia','Sexta - Manha');
INSERT INTO tdb_Dentista VALUES ('SP-33333','102.102.102-10','Cirurgia Buco','Sabado - Manha');
INSERT INTO tdb_Dentista VALUES ('SP-44444','103.103.103-10','Odontopediatria','Quarta e Sexta - Tarde');
INSERT INTO tdb_Dentista VALUES ('MG-55555','104.104.104-10','Protese','Segunda a Quarta - Manha');

INSERT INTO tdb_Voluntario VALUES (1,'888.888.888-88','Recepcao','Sabado - Manha');
INSERT INTO tdb_Voluntario VALUES (2,'999.999.999-99','Logistica','Domingo - Manha');
INSERT INTO tdb_Voluntario VALUES (3,'100.100.100-10','Comunicacao','Sexta - Tarde');
INSERT INTO tdb_Voluntario VALUES (4,'111.111.111-11','TI','Segunda - Manha');
INSERT INTO tdb_Voluntario VALUES (5,'222.222.222-22','Financeiro','Terca - Tarde');
INSERT INTO tdb_Voluntario VALUES (6,'333.333.333-33','Educacao','Quarta - Manha');
INSERT INTO tdb_Voluntario VALUES (7,'444.444.444-44','Saude','Quinta - Tarde');

INSERT INTO tdb_Doador VALUES (1,'100.100.100-10','PF');
INSERT INTO tdb_Doador VALUES (2,'999.999.999-99','PF');
INSERT INTO tdb_Doador VALUES (3,'888.888.888-88','PF');
INSERT INTO tdb_Doador VALUES (4,'777.777.777-77','PF');
INSERT INTO tdb_Doador VALUES (5,'111.111.111-11','PF');
INSERT INTO tdb_Doador VALUES (6,'222.222.222-22','PF');
INSERT INTO tdb_Doador VALUES (7,'333.333.333-33','PF');

INSERT INTO tdb_Usuario VALUES (1,'555.555.555-55','joao.silva','hash_joao','DENTISTA','S',SYSDATE);
INSERT INTO tdb_Usuario VALUES (2,'666.666.666-66','ana.costa','hash_ana','DENTISTA','S',SYSDATE);
INSERT INTO tdb_Usuario VALUES (3,'777.777.777-77','carlos.lima','hash_carlos','DENTISTA','S',SYSDATE);
INSERT INTO tdb_Usuario VALUES (4,'888.888.888-88','fernanda.lima','hash_fernanda','VOLUNTARIO','S',SYSDATE);
INSERT INTO tdb_Usuario VALUES (5,'999.999.999-99','rafael.gomes','hash_rafael','GESTOR','S',SYSDATE);
INSERT INTO tdb_Usuario VALUES (6,'105.105.105-10','admin.tdb','hash_admin','ADMIN','S',SYSDATE);
INSERT INTO tdb_Usuario VALUES (7,'444.444.444-44','bruno.alves','hash_bruno','VOLUNTARIO','S',SYSDATE);

INSERT INTO tdb_Material VALUES (1,'Luva cirurgica','Caixa com 100 unidades',8.00,20.00,'cx',DATE'2026-12-31');
INSERT INTO tdb_Material VALUES (2,'Mascara descartavel','Pacote com 50 unidades',25.00,15.00,'pct',DATE'2026-06-30');
INSERT INTO tdb_Material VALUES (3,'Fio dental 50m','Rolo',120.00,30.00,'un',DATE'2027-01-31');
INSERT INTO tdb_Material VALUES (4,'Escova de dente','Macia adulto',3.00,30.00,'un',DATE'2027-06-30');
INSERT INTO tdb_Material VALUES (5,'Pasta de dente 90g','Fluor ativo',45.00,20.00,'un',DATE'2026-09-30');
INSERT INTO tdb_Material VALUES (6,'Anestesico 1.8ml','Ampola lidocaina',2.00,10.00,'cx',DATE'2025-12-31');
INSERT INTO tdb_Material VALUES (7,'Sugador descartavel','Pacote 100 unidades',15.00,10.00,'pct',DATE'2027-03-31');

INSERT INTO tdb_Campanha VALUES (1,'Sorriso Solidario Maio 2025','Campanha mensal',DATE'2025-05-01',DATE'2025-05-31',20000.00,12500.00,'S');
INSERT INTO tdb_Campanha VALUES (2,'Dia das Maes TDB 2025','Especial mes das maes',DATE'2025-05-05',DATE'2025-05-15',10000.00,8750.00,'S');
INSERT INTO tdb_Campanha VALUES (3,'Campanha Abril 2025','Campanha mensal encerrada',DATE'2025-04-01',DATE'2025-04-30',15000.00,16200.00,'N');
INSERT INTO tdb_Campanha VALUES (4,'Natal Solidario 2024','Arrecadacao de fim de ano',DATE'2024-12-01',DATE'2024-12-31',25000.00,22000.00,'N');
INSERT INTO tdb_Campanha VALUES (5,'Dia da Crianca 2024','Atendimento infantil especial',DATE'2024-10-01',DATE'2024-10-31',12000.00,13500.00,'N');
INSERT INTO tdb_Campanha VALUES (6,'Volta as Aulas 2025','Kits higiene para estudantes',DATE'2025-02-01',DATE'2025-02-28',8000.00,7200.00,'N');
INSERT INTO tdb_Campanha VALUES (7,'Junho Saudavel 2025','Prevencao bucal',DATE'2025-06-01',DATE'2025-06-30',18000.00,0.00,'S');

INSERT INTO tdb_Doacao VALUES (1,1,1,5000.00,DATE'2025-05-02','PIX','Doacao de Marcos Roberto Almeida');
INSERT INTO tdb_Doacao VALUES (2,2,1,200.00,DATE'2025-05-03','PIX',NULL);
INSERT INTO tdb_Doacao VALUES (3,3,2,150.00,DATE'2025-05-06','PIX',NULL);
INSERT INTO tdb_Doacao VALUES (4,4,1,1000.00,DATE'2025-05-07','TRANSFERENCIA','Recorrente mensal');
INSERT INTO tdb_Doacao VALUES (5,5,3,3000.00,DATE'2025-04-10','BOLETO',NULL);
INSERT INTO tdb_Doacao VALUES (6,6,4,500.00,DATE'2024-12-15','CARTAO',NULL);
INSERT INTO tdb_Doacao VALUES (7,7,5,800.00,DATE'2024-10-05','PIX',NULL);

INSERT INTO tdb_Consulta VALUES (1,1,'111.111.111-11','SP-12345','555.555.555-55',DATE'2025-04-18','09:00','MANHA',
    'REALIZADA','Limpeza',3.5,'Sem intercorrencias');
INSERT INTO tdb_Consulta VALUES (2,2,'222.222.222-22','SP-67890','666.666.666-66',DATE'2025-04-18','14:00','TARDE',
    'AGENDADA','Avaliacao',22.0,NULL);
INSERT INTO tdb_Consulta VALUES (3,3,'333.333.333-33','SP-11111','777.777.777-77',DATE'2025-04-17','19:00','NOITE',
    'FALTA','Limpeza',15.0,'Paciente nao compareceu');
INSERT INTO tdb_Consulta VALUES (4,1,'111.111.111-11','SP-67890','666.666.666-66',DATE'2025-04-10','15:00','TARDE',
    'REALIZADA','Extracao',3.5,'Extracao dente 38');
INSERT INTO tdb_Consulta VALUES (5,4,'444.444.444-44','SP-12345','555.555.555-55',DATE'2025-05-02','09:00','MANHA',
    'AGENDADA','Retorno',5.0,NULL);
INSERT INTO tdb_Consulta VALUES (6,5,'888.888.888-88','SP-11111','777.777.777-77',DATE'2025-05-05','14:00','TARDE',
    'AGENDADA','Avaliacao',28.0,NULL);
INSERT INTO tdb_Consulta VALUES (7,2,'222.222.222-22','SP-12345','555.555.555-55',DATE'2025-03-20','10:00','MANHA',
    'CANCELADA','Limpeza',22.0,'Cancelado pelo dentista');

INSERT INTO tdb_Prontuario VALUES (1,1,'Limpeza completa. Paciente orientado sobre higiene. Retorno em 6 meses.','Sem obs.',DATE'2025-04-18',NULL);
INSERT INTO tdb_Prontuario VALUES (2,4,'Extracao dente 38. Sem intercorrencias. Prescrito analgesico e antibiotico 7d.','Retorno em 7 dias',DATE'2025-04-10',NULL);
INSERT INTO tdb_Prontuario VALUES (3,2,'Avaliacao inicial. Indicado aparelho ortodontico. Encaminhado a especialista.',NULL,DATE'2025-04-18',NULL);
INSERT INTO tdb_Prontuario VALUES (4,5,'Retorno pos-extracao. Cicatrizacao dentro do esperado. Sem queixas.','Alta medica',DATE'2025-05-02',NULL);
INSERT INTO tdb_Prontuario VALUES (5,6,'Primeira consulta. Avaliacao geral. Indicada limpeza preventiva.',NULL,DATE'2025-05-05',NULL);
INSERT INTO tdb_Prontuario VALUES (6,7,'Consulta cancelada. Registro gerado para controle.','Reagendar proximo mes',DATE'2025-03-20',NULL);
INSERT INTO tdb_Prontuario VALUES (7,3,'Paciente nao compareceu. Tentativa de contato sem retorno.','Registrar falta no historico',DATE'2025-04-17',NULL);

INSERT INTO tdb_Procedimento VALUES (1,1,'SP-12345','555.555.555-55','Limpeza Dental','Preventivo',45,0.00,'Evitar alimentos coloridos 24h');
INSERT INTO tdb_Procedimento VALUES (2,4,'SP-67890','666.666.666-66','Extracao Dente 38','Cirurgico',60,0.00,'Comprimir por 30 min apos');
INSERT INTO tdb_Procedimento VALUES (3,4,'SP-67890','666.666.666-66','Anestesia Local','Anestesia',10,0.00,'Aguardar efeito antes de comer');
INSERT INTO tdb_Procedimento VALUES (4,5,'SP-12345','555.555.555-55','Avaliacao Pos-Cirurgica','Avaliativo',20,0.00,NULL);
INSERT INTO tdb_Procedimento VALUES (5,2,'SP-67890','666.666.666-66','Radiografia Panoramica','Diagnostico',15,0.00,'Resultado em 24h');
INSERT INTO tdb_Procedimento VALUES (6,6,'SP-11111','777.777.777-77','Avaliacao Periodontal','Preventivo',30,0.00,'Higienizacao diaria');
INSERT INTO tdb_Procedimento VALUES (7,1,'SP-12345','555.555.555-55','Aplicacao de Fluor','Preventivo',10,0.00,'Nao ingerir nada por 30 min');

INSERT INTO tdb_RegistroIA VALUES (1,3,'FALTA','{"distanciaKm":15,"faltasAnteriores":2,"diasAteConsulta":7,"rendaFamiliar":900,"turno":2}',
    0.7800,'FALTA','ALTO','v1.0',NULL,DATE'2025-04-16');
INSERT INTO tdb_RegistroIA VALUES (2,2,'FALTA','{"distanciaKm":22,"faltasAnteriores":0,"diasAteConsulta":3,"rendaFamiliar":1200,"turno":1}',
    0.4500,'NAO_FALTA','MEDIO','v1.0',NULL,DATE'2025-04-15');
INSERT INTO tdb_RegistroIA VALUES (3,6,'FALTA','{"distanciaKm":28,"faltasAnteriores":1,"diasAteConsulta":5,"rendaFamiliar":700,"turno":1}',
    0.6100,'FALTA','ALTO','v1.0',NULL,DATE'2025-05-04');
INSERT INTO tdb_RegistroIA VALUES (4,5,'FALTA','{"distanciaKm":5,"faltasAnteriores":0,"diasAteConsulta":2,"rendaFamiliar":2200,"turno":0}',
    0.0800,'NAO_FALTA','BAIXO','v1.0','S',DATE'2025-05-01');
INSERT INTO tdb_RegistroIA VALUES (5,1,'FALTA','{"distanciaKm":3.5,"faltasAnteriores":0,"diasAteConsulta":1,"rendaFamiliar":1800,"turno":0}',
    0.0500,'NAO_FALTA','BAIXO','v1.0','S',DATE'2025-04-17');
INSERT INTO tdb_RegistroIA VALUES (6,NULL,'ARRECADACAO','{"duracaoDias":30,"metaValor":20000,"campanhasAnteriores":4,"mesDoAno":5}',
    0.8200,'ALTA',NULL,'v1.0',NULL,DATE'2025-04-30');
INSERT INTO tdb_RegistroIA VALUES (7,NULL,'ARRECADACAO','{"duracaoDias":30,"metaValor":15000,"campanhasAnteriores":3,"mesDoAno":4}',
    0.9100,'ALTA',NULL,'v1.0','S',DATE'2025-03-31');

INSERT INTO tdb_UtilizaMaterial VALUES (1,1,1.00);
INSERT INTO tdb_UtilizaMaterial VALUES (1,2,2.00);
INSERT INTO tdb_UtilizaMaterial VALUES (1,7,1.00);
INSERT INTO tdb_UtilizaMaterial VALUES (2,1,2.00);
INSERT INTO tdb_UtilizaMaterial VALUES (2,6,1.00);
INSERT INTO tdb_UtilizaMaterial VALUES (3,6,0.50);
INSERT INTO tdb_UtilizaMaterial VALUES (7,5,1.00);

INSERT INTO tdb_ParticipaCampanha VALUES (1,1);
INSERT INTO tdb_ParticipaCampanha VALUES (1,2);
INSERT INTO tdb_ParticipaCampanha VALUES (2,1);
INSERT INTO tdb_ParticipaCampanha VALUES (2,3);
INSERT INTO tdb_ParticipaCampanha VALUES (3,4);
INSERT INTO tdb_ParticipaCampanha VALUES (4,5);
INSERT INTO tdb_ParticipaCampanha VALUES (5,6);

INSERT INTO tdb_AjudaDoacao VALUES (1,1);
INSERT INTO tdb_AjudaDoacao VALUES (1,2);
INSERT INTO tdb_AjudaDoacao VALUES (2,3);
INSERT INTO tdb_AjudaDoacao VALUES (3,4);
INSERT INTO tdb_AjudaDoacao VALUES (4,5);
INSERT INTO tdb_AjudaDoacao VALUES (5,6);
INSERT INTO tdb_AjudaDoacao VALUES (6,7);

-- Atualizando Dados

UPDATE tdb_Consulta
SET status = 'REALIZADA',
observacoes = 'Avaliacao ortodontica realizada com sucesso'
WHERE id_consulta = 2;

UPDATE tdb_Material
SET quantidade = quantidade - 1
WHERE id_material = 6;

UPDATE tdb_Campanha
SET total_arrecadado = (
SELECT NVL(SUM(d.valor),0)
FROM tdb_Doacao d
WHERE d.id_campanha = 1
)
WHERE id_campanha = 1;

-- Excluindo Dados

DELETE FROM tdb_ParticipaCampanha
WHERE id_campanha = 5
AND id_voluntario = 6;

DELETE FROM tdb_AjudaDoacao
WHERE id_voluntario = 6
AND id_doacao = 7;

DELETE FROM tdb_RegistroIA
WHERE acerto = 'S'
AND risco = 'BAIXO';

-- Relatórios

-- Relatório de Classificação
SELECT
    p.nome AS paciente,
    pa.programa,
    pa.renda_familiar,
    pa.distancia_km,
CASE
    WHEN pa.renda_familiar < 1000 OR pa.distancia_km > 20 THEN 'Risco Alto de Falta'
    WHEN pa.renda_familiar < 1500 OR pa.distancia_km > 10 THEN 'Risco Medio de Falta'
    ELSE 'Risco Baixo de Falta'
END AS classificacao_risco,
CASE pa.programa
    WHEN 'DENTISTAS_DO_BEM' THEN 'Dentistas do Bem'
    WHEN 'APOLONICAS_DO_BEM' THEN 'Apolonicas do Bem'
END AS programa_formatado
FROM tdb_Paciente pa
JOIN tdb_Pessoa p ON p.cpf = pa.cpf
ORDER BY pa.distancia_km DESC, pa.renda_familiar ASC;

-- Relatório de Função Numérica
SELECT
    COUNT(*) AS total_pacientes,
    ROUND(AVG(distancia_km), 2) AS distancia_media_km,
    MIN(distancia_km) AS distancia_minima,
    MAX(distancia_km) AS distancia_maxima,
    ROUND(AVG(renda_familiar), 2) AS renda_media,
    MIN(renda_familiar) AS renda_minima,
    MAX(renda_familiar) AS renda_maxima
FROM tdb_Paciente;

-- Relatório de Função do Grupo
SELECT
    c.nome AS campanha,
    c.ativo,
        COUNT(d.id_doacao) AS qtd_doacoes,
        NVL(SUM(d.valor), 0) AS total_arrecadado,
    c.meta_valor,
CASE
    WHEN c.meta_valor IS NULL OR c.meta_valor = 0 THEN NULL
    ELSE ROUND((NVL(SUM(d.valor), 0) / c.meta_valor) * 100, 1)
END AS pct_meta
FROM tdb_Campanha c
LEFT JOIN tdb_Doacao d ON d.id_campanha = c.id_campanha
GROUP BY c.id_campanha, c.nome, c.meta_valor, c.ativo
ORDER BY pct_meta DESC NULLS LAST;

-- Relatório de Subconsulta
SELECT
    p.nome AS paciente,
    p.telefone,
    pa.distancia_km,
    pa.renda_familiar,
    sub.total_faltas,
    sub.total_consultas,
ROUND((sub.total_faltas / sub.total_consultas) * 100, 1) AS taxa_falta_pct
FROM tdb_Pessoa p
JOIN tdb_Paciente pa ON pa.cpf = p.cpf
    JOIN(
    SELECT
    c.id_paciente,
COUNT(*) AS total_consultas,
SUM(CASE WHEN c.status = 'FALTA' THEN 1 ELSE 0 END) AS total_faltas
FROM tdb_Consulta c
GROUP BY c.id_paciente
HAVING SUM(CASE WHEN c.status = 'FALTA' THEN 1 ELSE 0 END) > 0) sub ON sub.id_paciente = pa.id_paciente
ORDER BY sub.total_faltas DESC;

-- Relatório Completo de Consulta
SELECT
    c.id_consulta,
    pac.nome AS paciente,
    pa.programa,
    pa.distancia_km,
    pa.renda_familiar,
    dent.nome AS dentista,
    den.especialidade,
    c.data_consulta,
    c.turno,
    c.status,
    c.tipo,
    c.distancia_km AS dist_consulta_km,
    pr.descricao AS prontuario,
    proc.nome AS procedimento,
    proc.duracao_min,
    ia.prob_resultado AS probabilidade_falta,
    ia.risco AS risco_ia,
    ia.classe_prevista AS predicao_ia,
    ia.modelo_versao
FROM tdb_Consulta c
JOIN tdb_Paciente pa ON pa.id_paciente = c.id_paciente
JOIN tdb_Pessoa pac ON pac.cpf = pa.cpf
JOIN tdb_Dentista den ON den.cro = c.cro_dentista AND den.cpf = c.cpf_dentista
JOIN tdb_Pessoa dent ON dent.cpf = den.cpf
LEFT JOIN tdb_Prontuario pr ON pr.id_consulta = c.id_consulta
LEFT JOIN tdb_Procedimento proc ON proc.id_consulta = c.id_consulta
LEFT JOIN tdb_RegistroIA ia ON ia.id_consulta = c.id_consulta
AND ia.tipo_predicao = 'FALTA'
ORDER BY c.data_consulta DESC;