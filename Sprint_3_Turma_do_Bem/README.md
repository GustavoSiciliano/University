<div align="center">

# 🦷 Turma do Bem — Sistema de Gestão

**Domain Driven Design Using Java · Sprint 3 · FIAP**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-Database-F80000?style=flat&logo=oracle&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-ojdbc8-4479A1?style=flat)
![IntelliJ](https://img.shields.io/badge/IntelliJ-IDEA-000000?style=flat&logo=intellijidea&logoColor=white)

> Sistema desenvolvido em Java para gerenciar as operações da ONG **Turma do Bem**, que oferece atendimento odontológico gratuito a jovens de baixa renda em todo o Brasil.

</div>

---

## 👥 Integrantes

| Nome | RM |
|------|----|
| Gustavo de Jesus Silva | RM567926 |
| Gustavo Rodrigues Siciliano | RM568419 |
| Samuel Keniti Kina de Lima | RM567614 |

---

## 📋 Sobre o Projeto

A aplicação foi construída com **Programação Orientada a Objetos (POO)** e arquitetura em camadas, separando claramente as entidades do sistema, a conexão com o banco e as operações de persistência de dados.

O sistema gerencia os seguintes módulos, todos com **CRUD completo**:

| Módulo | Operações |
|--------|-----------|
| 👤 Pacientes | Cadastrar, listar, atualizar, excluir |
| 🦷 Dentistas | Cadastrar, listar, atualizar, excluir |
| 🙋 Voluntários | Cadastrar, listar, atualizar, excluir |
| 💰 Doadores | Cadastrar, listar, atualizar, excluir |
| 📅 Consultas | Agendar, cancelar, listar, excluir |
| 📋 Prontuários | Criar, atualizar, listar, excluir |
| 🔧 Procedimentos | Cadastrar, listar, atualizar, excluir |
| 📦 Materiais | Cadastrar, listar, atualizar, excluir |
| 💳 Doações | Registrar, listar, atualizar, excluir |
| 📣 Campanhas | Criar, listar, atualizar, excluir |

Tudo é acessado por um **menu interativo no terminal**.

---

## 🏗️ Estrutura do Projeto

```
TurmaDoBem/
└── src/main/java/DeNovoNao/
    ├── conexao/    → Classe de conexão com o banco Oracle (JDBC)
    ├── modelo/     → Entidades do sistema (Pessoa, Paciente, Dentista...)
    ├── dao/        → Operações de banco de dados (CRUD completo)
    └── teste/      → Classe principal com menu interativo (Main.java)
```

---

## ⚙️ Tecnologias Utilizadas

| Tecnologia | Para quê |
|------------|----------|
| **Java 21** | Linguagem principal |
| **Oracle Database** | Banco de dados relacional |
| **JDBC** (`ojdbc8.jar`) | Conexão do Java com o Oracle |
| **IntelliJ IDEA** | IDE utilizada no desenvolvimento |

> ℹ️ O projeto foi criado no IntelliJ e tem uma estrutura Maven gerada automaticamente pela IDE. Você não precisa saber usar Maven — basta abrir o projeto na IDE que ela reconhece tudo sozinha.

---

## 🗄️ Banco de Dados

O sistema se conecta a um banco **Oracle** e utiliza **14 tabelas** com o prefixo `tdb_`:

**Entidades base:**
`tdb_Pessoa`, `tdb_Paciente`, `tdb_Dentista`, `tdb_Voluntario`, `tdb_Doador`

**Entidades operacionais:**
`tdb_Prontuario`, `tdb_Consulta`, `tdb_Procedimento`, `tdb_Material`, `tdb_Doacao`, `tdb_Campanha`

**Tabelas associativas (N:N):**
`tdb_UtilizaMaterial`, `tdb_ParticipaCampanha`, `tdb_AjudaDoacao`

Restrições utilizadas: `PK`, `FK`, `NOT NULL`, `UNIQUE`, `CHECK`.

---

## 🧠 Regras de Negócio

- **CPF** deve conter exatamente 11 dígitos numéricos
- Campos obrigatórios são validados antes de salvar no banco
- **Consultas** possuem controle de status: `AGENDADA`, `REALIZADA`, `CANCELADA`, `REMARCADA`
- Uma consulta com status `REALIZADA` **não pode ser cancelada**
- **Valores de doação** devem ser positivos
- **Quantidade de materiais** não pode ser negativa
- **CRO** do dentista é obrigatório e validado
- **Prontuários** são vinculados exclusivamente ao paciente cadastrado

---

## ▶️ Como Executar

### Pré-requisitos

- Java JDK 21 instalado
- Acesso ao banco Oracle (servidor da FIAP ou outro ambiente)
- Arquivo `ojdbc8.jar` (driver Oracle para Java)
- IntelliJ IDEA (recomendado) ou Eclipse/NetBeans

---

### Passo 1 — Executar o script SQL

Antes de rodar o sistema, execute o script SQL no seu banco Oracle para criar todas as tabelas.

> O sistema não funciona sem as tabelas já criadas no banco.

---

### Passo 2 — Configurar a conexão

Abra o arquivo `Conexao.java` dentro da pasta `conexao/` e preencha com os dados do **seu próprio ambiente**:

```java
private static final String URL  = "jdbc:oracle:thin:@SEU_SERVIDOR:SUA_PORTA:SEU_BANCO";
private static final String USER = "SEU_USUARIO";
private static final String PASS = "SUA_SENHA";
```

| Campo | O que colocar | Exemplo |
|-------|---------------|---------|
| `SEU_SERVIDOR` | Endereço do servidor onde o Oracle está rodando | `oracle.fiap.com.br`, `localhost`, `192.168.0.10` |
| `SUA_PORTA` | Porta do banco (padrão Oracle é `1521`) | `1521` |
| `SEU_BANCO` | Nome, SID ou serviço do banco | `ORCL`, `XE`, `orcl` |
| `SEU_USUARIO` | Seu usuário de acesso ao banco | `rm123456`, `admin` |
| `SUA_SENHA` | Sua senha do banco | `minhasenha` |

> ⚠️ Cada pessoa que for rodar o projeto precisa configurar com os dados do **seu próprio banco**. O servidor pode ser da faculdade, local, remoto, em máquina virtual — não importa, desde que o Oracle esteja acessível e as tabelas já existam.

---

### Passo 3 — Adicionar o driver Oracle (ojdbc8.jar)

O Java precisa desse arquivo para conseguir se comunicar com o banco Oracle.

**No IntelliJ IDEA:**
1. `File` → `Project Structure` → `Libraries`
2. Clique em `+` → `Java`
3. Selecione o arquivo `ojdbc8.jar`
4. Confirme e clique em `OK`

**No Eclipse:**
1. Botão direito no projeto → `Build Path` → `Add External Archives`
2. Selecione o `ojdbc8.jar`

---

### Passo 4 — Importar e executar

1. Abra o projeto na sua IDE
2. Aguarde a IDE carregar a estrutura do projeto
3. Execute a classe principal:

```
DeNovoNao.teste.Main
```

O menu interativo aparecerá no terminal.

---

## 🖥️ Menu do Sistema

```
+=========================================+
|         SISTEMA ONG - TURMA DO BEM      |
+=========================================+
|  1. Pacientes                           |
|  2. Dentistas                           |
|  3. Voluntarios                         |
|  4. Doadores                            |
|  5. Doacoes                             |
|  6. Consultas                           |
|  7. Prontuarios                         |
|  8. Procedimentos                       |
|  9. Materiais                           |
| 10. Campanhas                           |
|  0. Sair                                |
+=========================================+
Escolha:
```

Cada módulo exibe um submenu com:

```
+-------------------------+
|  1. Cadastrar           |
|  2. Listar              |
|  3. Atualizar           |
|  4. Excluir             |
|  0. Voltar              |
+-------------------------+
```

---

---

# 🌍 English Version

<div align="center">

# 🦷 Turma do Bem — Management System

**Domain Driven Design Using Java · Sprint 3 · FIAP**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-Database-F80000?style=flat&logo=oracle&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-ojdbc8-4479A1?style=flat)
![IntelliJ](https://img.shields.io/badge/IntelliJ-IDEA-000000?style=flat&logo=intellijidea&logoColor=white)

> System developed in Java to manage the operations of the NGO **Turma do Bem**, which provides free dental care to low-income youth across Brazil.

</div>

---

## 👥 Team Members

| Name | RM |
|------|----|
| Gustavo de Jesus Silva | RM567926 |
| Gustavo Rodrigues Siciliano | RM568419 |
| Samuel Keniti Kina de Lima | RM567614 |

---

## 📋 About the Project

The application was built using **Object-Oriented Programming (OOP)** and a layered architecture, clearly separating business entities, database connection, and data persistence operations.

The system manages the following modules, all with full **CRUD** support:

| Module | Operations |
|--------|------------|
| 👤 Patients | Create, list, update, delete |
| 🦷 Dentists | Create, list, update, delete |
| 🙋 Volunteers | Create, list, update, delete |
| 💰 Donors | Create, list, update, delete |
| 📅 Appointments | Schedule, cancel, list, delete |
| 📋 Medical Records | Create, update, list, delete |
| 🔧 Procedures | Create, list, update, delete |
| 📦 Materials | Create, list, update, delete |
| 💳 Donations | Register, list, update, delete |
| 📣 Campaigns | Create, list, update, delete |

All features are accessed through an **interactive terminal menu**.

---

## 🏗️ Project Structure

```
TurmaDoBem/
└── src/main/java/DeNovoNao/
    ├── conexao/    → Oracle database connection class (JDBC)
    ├── modelo/     → System entities (Pessoa, Paciente, Dentista...)
    ├── dao/        → Database operations (full CRUD)
    └── teste/      → Main class with interactive menu (Main.java)
```

---

## ⚙️ Technologies

| Technology | Purpose |
|------------|---------|
| **Java 21** | Main language |
| **Oracle Database** | Relational database |
| **JDBC** (`ojdbc8.jar`) | Java → Oracle connection driver |
| **IntelliJ IDEA** | IDE used for development |

> ℹ️ The project was created in IntelliJ with a Maven structure auto-generated by the IDE. You don't need to know Maven — just open the project in the IDE and it handles everything automatically.

---

## 🗄️ Database

The system connects to an **Oracle** database with **14 tables** prefixed with `tdb_`:

**Base entities:**
`tdb_Pessoa`, `tdb_Paciente`, `tdb_Dentista`, `tdb_Voluntario`, `tdb_Doador`

**Operational entities:**
`tdb_Prontuario`, `tdb_Consulta`, `tdb_Procedimento`, `tdb_Material`, `tdb_Doacao`, `tdb_Campanha`

**Associative tables (N:N):**
`tdb_UtilizaMaterial`, `tdb_ParticipaCampanha`, `tdb_AjudaDoacao`

Constraints used: `PK`, `FK`, `NOT NULL`, `UNIQUE`, `CHECK`.

---

## 🧠 Business Rules

- **CPF** must contain exactly 11 numeric digits
- Required fields are validated before saving to the database
- **Appointments** have status control: `AGENDADA`, `REALIZADA`, `CANCELADA`, `REMARCADA`
- An appointment with status `REALIZADA` (completed) **cannot be cancelled**
- **Donation values** must be positive
- **Material quantities** cannot be negative
- **CRO** (dentist license number) is required and validated
- **Medical records** are exclusively linked to registered patients

---

## ▶️ How to Run

### Requirements

- Java JDK 21 installed
- Access to an Oracle database (FIAP server or other environment)
- `ojdbc8.jar` file (Oracle JDBC driver)
- IntelliJ IDEA (recommended) or Eclipse/NetBeans

---

### Step 1 — Run the SQL script

Before running the system, execute the SQL script on your Oracle database to create all the tables.

> The system will not work without the tables already created in the database.

---

### Step 2 — Configure the connection

Open `Conexao.java` inside the `conexao/` folder and fill in **your own environment's** credentials:

```java
private static final String URL  = "jdbc:oracle:thin:@YOUR_SERVER:YOUR_PORT:YOUR_DATABASE";
private static final String USER = "YOUR_USERNAME";
private static final String PASS = "YOUR_PASSWORD";
```

| Field | What to set | Example |
|-------|-------------|---------|
| `YOUR_SERVER` | Address of the server where Oracle is running | `oracle.fiap.com.br`, `localhost`, `192.168.0.10` |
| `YOUR_PORT` | Database port (Oracle default is `1521`) | `1521` |
| `YOUR_DATABASE` | Database name, SID, or service | `ORCL`, `XE`, `orcl` |
| `YOUR_USERNAME` | Your database username | `rm123456`, `admin` |
| `YOUR_PASSWORD` | Your database password | `mypassword` |

> ⚠️ Everyone who runs this project must configure the connection with **their own database details**. The server can be from a university, local machine, remote server, or virtual machine — it doesn't matter, as long as Oracle is accessible and the tables already exist.

---

### Step 3 — Add the Oracle JDBC driver (ojdbc8.jar)

Java needs this file to communicate with the Oracle database.

**In IntelliJ IDEA:**
1. `File` → `Project Structure` → `Libraries`
2. Click `+` → `Java`
3. Select the `ojdbc8.jar` file
4. Confirm and click `OK`

**In Eclipse:**
1. Right-click the project → `Build Path` → `Add External Archives`
2. Select `ojdbc8.jar`

---

### Step 4 — Import and run

1. Open the project in your IDE
2. Wait for the IDE to load the project structure
3. Run the main class:

```
DeNovoNao.teste.Main
```

The interactive terminal menu will appear.

---

## 🖥️ System Menu

```
+=========================================+
|         SISTEMA ONG - TURMA DO BEM      |
+=========================================+
|  1. Pacientes                           |
|  2. Dentistas                           |
|  3. Voluntarios                         |
|  4. Doadores                            |
|  5. Doacoes                             |
|  6. Consultas                           |
|  7. Prontuarios                         |
|  8. Procedimentos                       |
|  9. Materiais                           |
| 10. Campanhas                           |
|  0. Sair                                |
+=========================================+
```

Each module displays a submenu with:

```
+-------------------------+
|  1. Cadastrar           |
|  2. Listar              |
|  3. Atualizar           |
|  4. Excluir             |
|  0. Voltar              |
+-------------------------+
```

---

## 📄 License

Academic project developed for the **Domain Driven Design Using Java** course — FIAP, Systems Analysis and Development (ADS).

---
