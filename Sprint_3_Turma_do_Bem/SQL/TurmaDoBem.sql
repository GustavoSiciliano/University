-- Sprint 3
-- Gustavo Rodrigues Siciliano: RM568419
-- Gustavo de Jesus Silva: RM567926
-- Samuel Keneti Kina de Lima: 567614

--Script para apagar as tabelas (Se já existentes) para sempre rodar

DROP TABLE tdb_UtilizaMaterial
    CASCADE CONSTRAINTS;
DROP TABLE tdb_ParticipaCampanha
    CASCADE CONSTRAINTS;
DROP TABLE tdb_AjudaDoacao
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Procedimento
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Consulta
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Prontuario
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Campanha
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Doacao
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Material
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Voluntario
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Dentista
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Paciente
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Doador
    CASCADE CONSTRAINTS;
DROP TABLE tdb_Pessoa
    CASCADE CONSTRAINTS;

-- Criando tabela tdb_Pessoa (base para Paciente, Dentista e Voluntario)

CREATE TABLE tdb_Pessoa(
    cpf VARCHAR2(14) NOT NULL,
    nome VARCHAR2(60) NOT NULL,
    data_nasc DATE NOT NULL,
    telefone VARCHAR2(20),
    email VARCHAR2(40),
    CONSTRAINT pk_tdb_Pessoa PRIMARY KEY(cpf),
    CONSTRAINT uk_tdb_Pessoa_email UNIQUE(email),
    CONSTRAINT ck_tdb_Pessoa_email CHECK(email IS NULL OR email LIKE '%@%')
);

-- Criando tabela tdb_Paciente
-- ON DELETE CASCADE: ao deletar Pessoa, deleta Paciente automaticamente

CREATE TABLE tdb_Paciente(
    id_paciente NUMBER NOT NULL,
    endereco VARCHAR2(60),
    convenio VARCHAR2(30),
    cpf VARCHAR2(14) NOT NULL,
    telefone VARCHAR2(20),
    CONSTRAINT pk_tdb_Paciente PRIMARY KEY(id_paciente),
    CONSTRAINT uk_tdb_Paciente_cpf UNIQUE(cpf),
    CONSTRAINT uk_tdb_Paciente_id_cpf UNIQUE(id_paciente, cpf),
    CONSTRAINT fk_tdb_Paciente_pessoa FOREIGN KEY(cpf) REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE
);

-- Criando tabela tdb_Dentista
-- ON DELETE CASCADE: ao deletar Pessoa, deleta Dentista automaticamente

CREATE TABLE tdb_Dentista(
    cro VARCHAR2(20) NOT NULL,
    cpf VARCHAR2(14) NOT NULL,
    especialidade VARCHAR2(30),
    disponibilidade VARCHAR2(50),
    telefone VARCHAR2(20),
    CONSTRAINT pk_tdb_Dentista PRIMARY KEY(cro, cpf),
    CONSTRAINT uk_tdb_Dentista_cpf UNIQUE(cpf),
    CONSTRAINT fk_tdb_Dentista_pessoa FOREIGN KEY(cpf) REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE
);

-- Criando tabela tdb_Voluntario
-- ON DELETE CASCADE: ao deletar Pessoa, deleta Voluntario automaticamente

CREATE TABLE tdb_Voluntario(
    id_voluntario NUMBER NOT NULL,
    atuacao VARCHAR2(50),
    disponibilidade VARCHAR2(50),
    telefone VARCHAR2(20),
    email VARCHAR2(40),
    cpf VARCHAR2(14) NOT NULL,
    CONSTRAINT pk_tdb_Voluntario PRIMARY KEY(id_voluntario),
    CONSTRAINT uk_tdb_Voluntario_cpf UNIQUE(cpf),
    CONSTRAINT fk_tdb_Voluntario_pessoa FOREIGN KEY(cpf) REFERENCES tdb_Pessoa(cpf) ON DELETE CASCADE,
    CONSTRAINT ck_tdb_Voluntario_email CHECK(email IS NULL OR email LIKE '%@%')
);

-- Criando tabela tdb_Doador

CREATE TABLE tdb_Doador(
    id_doador NUMBER NOT NULL,
    nome VARCHAR2(60) NOT NULL,
    tipo VARCHAR2(50),
    cpf  VARCHAR2(14),
    email VARCHAR2(40),
    telefone VARCHAR2(20),
    CONSTRAINT pk_tdb_Doador PRIMARY KEY(id_doador),
    CONSTRAINT uk_tdb_Doador_cpf UNIQUE(cpf),
    CONSTRAINT ck_tdb_Doador_tipo CHECK(tipo IN ('PF', 'PJ'))
);

-- Criando tabela tdb_Prontuario
-- ON DELETE CASCADE: ao deletar Paciente, deleta Prontuario automaticamente

CREATE TABLE tdb_Prontuario(
    id_prontuario NUMBER NOT NULL,
    descricao VARCHAR2(200),
    data DATE NOT NULL,
    ultima_alt DATE,
    observacoes VARCHAR2(150),
    id_paciente NUMBER NOT NULL,
    cpf_paciente VARCHAR2(14) NOT NULL,
    CONSTRAINT pk_tdb_Prontuario PRIMARY KEY(id_prontuario),
    CONSTRAINT uk_tdb_Prontuario_paciente UNIQUE(id_paciente),
    CONSTRAINT fk_tdb_Prontuario_paciente FOREIGN KEY(id_paciente, cpf_paciente) REFERENCES tdb_Paciente(id_paciente, cpf) ON DELETE CASCADE
);

-- Criando tabela tdb_Material

CREATE TABLE tdb_Material(
    id_material NUMBER NOT NULL,
    nome VARCHAR2(80) NOT NULL,
    tipo VARCHAR2(50),
    quantidade NUMBER(10,2) NOT NULL,
    unidade VARCHAR2(20),
    validade DATE,
    CONSTRAINT pk_tdb_Material PRIMARY KEY(id_material),
    CONSTRAINT ck_tdb_Material_qtd CHECK(quantidade >= 0)
);

-- Criando tabela tdb_Consulta
-- ON DELETE CASCADE: ao deletar Paciente, deleta Consultas automaticamente

CREATE TABLE tdb_Consulta(
    id_consulta NUMBER NOT NULL,
    data DATE NOT NULL,
    horario VARCHAR2(10),
    status VARCHAR2(15) NOT NULL,
    tipo VARCHAR2(50),
    id_paciente NUMBER NOT NULL,
    cpf_paciente VARCHAR2(14) NOT NULL,
    cro_dentista VARCHAR2(20) NOT NULL,
    cpf_dentista VARCHAR2(14) NOT NULL,
    CONSTRAINT pk_tdb_Consulta PRIMARY KEY (id_consulta),
    CONSTRAINT ck_tdb_Consulta_status CHECK (status IN('AGENDADA','REALIZADA','CANCELADA','REMARCADA')),
    CONSTRAINT fk_tdb_Consulta_paciente FOREIGN KEY(id_paciente, cpf_paciente) REFERENCES tdb_Paciente(id_paciente, cpf) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_Consulta_dentista FOREIGN KEY(cro_dentista, cpf_dentista) REFERENCES tdb_Dentista(cro, cpf)
);

-- Criando tabela tdb_Procedimento
-- ON DELETE CASCADE: ao deletar Consulta, deleta Procedimentos automaticamente

CREATE TABLE tdb_Procedimento(
    id_procedimento NUMBER NOT NULL,
    nome VARCHAR2(60) NOT NULL,
    tipo VARCHAR2(50),
    duracao NUMBER(5),
    orientacao VARCHAR2(150),
    custo NUMBER(10,2),
    id_consulta NUMBER NOT NULL,
    cro_dentista VARCHAR2(20) NOT NULL,
    cpf_dentista VARCHAR2(14) NOT NULL,
    CONSTRAINT pk_tdb_Procedimento PRIMARY KEY(id_procedimento),
    CONSTRAINT ck_tdb_Proc_custo CHECK(custo IS NULL OR custo >= 0),
    CONSTRAINT ck_tdb_Proc_duracao CHECK(duracao IS NULL OR duracao > 0),
    CONSTRAINT fk_tdb_Proc_consulta FOREIGN KEY(id_consulta) REFERENCES tdb_Consulta(id_consulta) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_Proc_dentista  FOREIGN KEY(cro_dentista, cpf_dentista) REFERENCES tdb_Dentista(cro, cpf)
);

-- Criando tabela tdb_Doacao
-- ON DELETE CASCADE: ao deletar Doador, deleta Doações automaticamente

CREATE TABLE tdb_Doacao(
    id_doacao NUMBER NOT NULL,
    valor NUMBER(12,2) NOT NULL,
    data DATE NOT NULL,
    tipo VARCHAR2(50),
    destino VARCHAR2(100),
    id_doador NUMBER NOT NULL,
    CONSTRAINT pk_tdb_Doacao PRIMARY KEY(id_doacao),
    CONSTRAINT ck_tdb_Doacao_valor CHECK(valor > 0),
    CONSTRAINT fk_tdb_Doacao_doador FOREIGN KEY(id_doador) REFERENCES tdb_Doador(id_doador) ON DELETE CASCADE
);

-- Criando tabela tdb_Campanha
-- ON DELETE CASCADE: ao deletar Doacao, deleta Campanha automaticamente

CREATE TABLE tdb_Campanha(
    id_campanha NUMBER NOT NULL,
    nome VARCHAR2(100) NOT NULL,
    objetivo VARCHAR2(100),
    data_inicio DATE NOT NULL,
    data_fim DATE,
    id_doacao NUMBER,
    CONSTRAINT pk_tdb_Campanha PRIMARY KEY(id_campanha),
    CONSTRAINT ck_tdb_Campanha_datas  CHECK(data_fim IS NULL OR data_fim >= data_inicio),
    CONSTRAINT fk_tdb_Campanha_doacao FOREIGN KEY(id_doacao) REFERENCES tdb_Doacao(id_doacao) ON DELETE CASCADE
);

-- Criando tabela tdb_UtilizaMaterial
-- ON DELETE CASCADE: ao deletar Procedimento ou Material, remove o vínculo automaticamente

CREATE TABLE tdb_UtilizaMaterial (
    id_procedimento NUMBER NOT NULL,
    id_material NUMBER NOT NULL,
    quantidade_usada NUMBER(10,2),
    CONSTRAINT pk_tdb_UtilizaMaterial PRIMARY KEY(id_procedimento, id_material),
    CONSTRAINT fk_tdb_UtilizaMat_proc FOREIGN KEY(id_procedimento) REFERENCES tdb_Procedimento(id_procedimento) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_UtilizaMat_mat  FOREIGN KEY(id_material) REFERENCES tdb_Material(id_material) ON DELETE CASCADE
);

-- Criando tabela tdb_ParticipaCampanha
-- ON DELETE CASCADE: ao deletar Campanha ou Voluntario, remove o vínculo automaticamente

CREATE TABLE tdb_ParticipaCampanha(
    id_campanha NUMBER NOT NULL,
    id_voluntario NUMBER NOT NULL,
    CONSTRAINT pk_tdb_ParticipaCampanha PRIMARY KEY(id_campanha, id_voluntario),
    CONSTRAINT fk_tdb_PartCamp_campanha FOREIGN KEY(id_campanha) REFERENCES tdb_Campanha(id_campanha) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_PartCamp_voluntario FOREIGN KEY(id_voluntario) REFERENCES tdb_Voluntario(id_voluntario) ON DELETE CASCADE
);

-- Criando tabela tdb_AjudaDoacao
-- ON DELETE CASCADE: ao deletar Voluntario ou Doacao, remove o vínculo automaticamente

CREATE TABLE tdb_AjudaDoacao(
    id_voluntario NUMBER NOT NULL,
    id_doacao NUMBER NOT NULL,
    CONSTRAINT pk_tdb_AjudaDoacao PRIMARY KEY(id_voluntario, id_doacao),
    CONSTRAINT fk_tdb_AjudaDoacao_voluntario FOREIGN KEY(id_voluntario) REFERENCES tdb_Voluntario(id_voluntario) ON DELETE CASCADE,
    CONSTRAINT fk_tdb_AjudaDoacao_doacao FOREIGN KEY(id_doacao) REFERENCES tdb_Doacao(id_doacao) ON DELETE CASCADE
);