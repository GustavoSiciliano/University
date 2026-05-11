# 🦷 De Novo Não! — Turma do Bem

> Sistema completo de gestão para a ONG **Turma do Bem**, integrando banco de dados relacional, ERP web, inteligência artificial preditiva e sistema de terminal em Python.

---

## 👥 Equipe

| Nome | RM |
|---|---|
| Gustavo Rodrigues Siciliano | RM568419 |
| Gustavo de Jesus Silva | RM567926 |
| Samuel Keniti Kina de Lima | RM567614 |

---

## 🎯 Sobre o Projeto

A **Turma do Bem** é uma ONG que oferece atendimento odontológico gratuito a jovens em situação de vulnerabilidade social. O sistema **De Novo Não!** foi desenvolvido para digitalizar e otimizar toda a operação da ONG, desde o cadastro de pacientes e dentistas até o controle de campanhas de doação e geração de previsões com Inteligência Artificial.

O projeto é composto por **quatro camadas integradas**:

```
Oracle Database
      ↕
  Java (API REST)  ←→  React (ERP Web)
      ↕
  Python (Terminal)
      ↕
  IA Flask (Predições)
```

---

## 🏗️ Arquitetura Geral

### 🗄️ Banco de Dados — Oracle
- **16 tabelas** relacionais com constraints, chaves estrangeiras e validações
- Entidades principais: `tdb_Pessoa`, `tdb_Paciente`, `tdb_Dentista`, `tdb_Voluntario`, `tdb_Doador`, `tdb_Consulta`, `tdb_Campanha`, `tdb_Doacao`, `tdb_RegistroIA` e mais
- Regras de negócio implementadas via CHECK constraints
- Suporte a dois programas de atendimento: **Dentistas do Bem** e **Apolônias do Bem**

### ☕ Java — API REST (Quarkus)
- API RESTful que expõe todos os recursos do banco
- Endpoints para CRUD completo de todas as entidades
- Consumido pelo front-end React e disponível para integrações externas

### ⚛️ React — ERP Web (TypeScript + Vite)
- Interface de gestão completa para a equipe da ONG
- Módulos: Pacientes, Dentistas, Voluntários, Doadores, Consultas, Prontuários, Campanhas, Doações, Materiais, IA e Relatórios
- Consome a API Java e a API de IA
- Preenchimento automático de endereço via **ViaCEP**

### 🐍 Python — Sistema Terminal
- Sistema de gestão via terminal para operações internas
- CRUD de Pacientes e Consultas direto no Oracle
- Relatórios com filtros e exportação em JSON
- Integração com **ViaCEP** (API pública) para preenchimento de endereço
- Integração com **API de IA** (API própria) para previsões em tempo real

### 🤖 IA — API Flask (Python)
- Dois modelos preditivos treinados com dados reais do banco Oracle
- **Modelo de Falta** (classificação binária): prevê se um paciente irá faltar a uma consulta
- **Modelo de Arrecadação** (regressão): prevê o valor que uma campanha deve arrecadar
- Seleção automática do melhor modelo entre Regressão Logística, Random Forest e XGBoost
- Serialização com `joblib` para portabilidade

---

## 🤖 Inteligência Artificial — Detalhes

### Modelo de Previsão de Falta
Classifica se um paciente irá faltar a uma consulta com base em:
- Distância até a clínica
- Renda familiar
- Histórico de faltas anteriores
- Turno preferencial
- Programa de atendimento

**Resultado:** Probabilidade de falta (%), classificação de risco (ALTO / MEDIO / BAIXO) e recomendação de ação.

### Modelo de Previsão de Arrecadação
Estima o valor total que uma campanha de doações deve arrecadar com base em:
- Meta da campanha
- Duração em dias
- Mês de início (sazonalidade)
- Campanhas anteriores realizadas
- Quantidade de voluntários e doações esperadas

**Resultado:** Valor previsto (R$), tendência (ALTA / ESTAVEL / BAIXA) e percentual da meta.

### Pipeline de Treinamento
```
Oracle → gerar_dados.py → CSVs → treinar_falta.py + treinar_arrecadacao.py → .joblib
```

### Endpoints da API de IA

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/health` | Status da API e modelos |
| POST | `/predict/falta` | Previsão de risco de falta |
| POST | `/predict/arrecadacao` | Previsão de arrecadação |
| GET | `/metricas/falta` | Métricas do modelo de falta |
| GET | `/metricas/arrecadacao` | Métricas do modelo de arrecadação |
| GET | `/resultados` | Histórico de predições |
| GET | `/predict/falta/consulta/<id>` | Predição automática por ID de consulta |
| POST | `/retreinar` | Retreina os modelos sem reiniciar a API |

---

## 🗂️ Estrutura do Repositório

```
/
├── IA/                         # API de Inteligência Artificial
│   ├── app.py                  # Servidor Flask com todos os endpoints
│   ├── run.py                  # Pipeline completa: treino + API
│   ├── requirements.txt        # Dependências Python
│   ├── scripts/
│   │   ├── gerar_dados.py      # Leitura do Oracle e geração de CSVs
│   │   ├── treinar_falta.py    # Treino do modelo de classificação
│   │   └── treinar_arrecadacao.py  # Treino do modelo de regressão
│   ├── data/                   # CSVs gerados automaticamente
│   └── models/                 # Modelos .joblib gerados automaticamente
│
├── dnn-erp/                    # ERP React (TypeScript + Vite + Tailwind)
│   ├── src/
│   │   ├── pages/              # Páginas públicas e ERP
│   │   ├── components/         # Componentes reutilizáveis
│   │   ├── services/api.ts     # Camada de comunicação com as APIs
│   │   └── types/index.ts      # Tipos TypeScript
│   └── package.json
│
├── DataBase/                   # Banco de Dados Oracle
│   ├── Turma_Do_Bem.sql        # Script de criação das 16 tabelas
│   ├── TurmaDoBeM_Carga_Dados.sql  # Script de carga com dados de exemplo
│   ├── DER_&&_MF.pdf           # Diagrama Entidade-Relacionamento
│   ├── Logico.pdf              # Modelo Lógico Relacional
│   ├── DiagramaClasse.pdf      # Diagrama de Classes
│   └── Modelo_Descritivo.pdf   # Documentação descritiva do banco
│
└── Python/
    └── turma_do_bem.py         # Sistema de gestão via terminal
```

---

## 🚀 Como Executar

### Pré-requisitos
- Python 3.10+
- Node.js 18+
- Java 17+
- Acesso ao banco Oracle da FIAP (rede interna ou VPN)

---

### 1. Banco de Dados

Execute os scripts na seguinte ordem no SQL Developer:

```sql
-- 1. Cria as 16 tabelas
@Turma_Do_Bem.sql

-- 2. Popula com dados de exemplo
@TurmaDoBeM_Carga_Dados.sql
```

---

### 2. API de IA (Flask)

```bash
cd IA

# Instala dependências
pip install -r requirements.txt

# Executa pipeline completa (treino + API)
python run.py

# Ou apenas sobe a API (modelos já treinados)
python run.py --only-api

# Ou apenas treina sem subir a API
python run.py --only-train
```

API disponível em: `http://localhost:8000`

> **Pré-requisito:** estar conectado à rede da FIAP ou VPN para o `gerar_dados.py` acessar o Oracle.

---

### 3. ERP React

```bash
cd dnn-erp

# Instala dependências
npm install

# Configura as URLs das APIs no .env
cp .env.example .env
# Edite VITE_API_URL e VITE_IA_URL

# Inicia o servidor de desenvolvimento
npm run dev
```

Front disponível em: `http://localhost:5173`

---

### 4. Sistema Terminal Python

```bash
# Instala dependências
pip install oracledb requests

# Executa
python turma_do_bem.py
```

> Para as previsões de IA funcionarem no terminal, a **API Flask precisa estar rodando** em `http://localhost:8000`.

---

## 🔗 APIs Utilizadas

### ViaCEP (API pública externa)
- **URL:** `https://viacep.com.br/ws/{cep}/json/`
- **Uso:** Preenchimento automático de endereço por CEP no cadastro de pacientes (React e Python)
- **Gratuita, sem autenticação**

### API de IA Flask (API própria)
- **URL:** `http://localhost:8000` (local) ou URL pública se hospedada
- **Uso:** Previsões de risco de falta e arrecadação de campanhas
- **Consumida pelo:** React (página de IA), Python terminal e Notebook Jupyter

---

## 📊 Banco de Dados — Tabelas

| Tabela | Descrição |
|--------|-----------|
| `tdb_Pessoa` | Base de todos os indivíduos do sistema |
| `tdb_Paciente` | Pacientes atendidos pela ONG |
| `tdb_Dentista` | Dentistas voluntários |
| `tdb_Voluntario` | Voluntários em geral |
| `tdb_Doador` | Doadores (PF ou PJ) |
| `tdb_Usuario` | Usuários do sistema com perfis de acesso |
| `tdb_Consulta` | Consultas agendadas e realizadas |
| `tdb_Prontuario` | Prontuários das consultas |
| `tdb_Procedimento` | Procedimentos realizados nas consultas |
| `tdb_Material` | Controle de materiais e estoque |
| `tdb_Campanha` | Campanhas de arrecadação |
| `tdb_Doacao` | Doações realizadas |
| `tdb_RegistroIA` | Histórico de predições da IA |
| `tdb_UtilizaMaterial` | Materiais usados em procedimentos |
| `tdb_ParticipaCampanha` | Voluntários em campanhas |
| `tdb_AjudaDoacao` | Voluntários que auxiliaram doações |

---

## 📦 Dependências

### Python (IA + Terminal)
```
flask==3.1.0
flask-cors==5.0.0
scikit-learn==1.6.1
xgboost==2.1.4
joblib==1.4.2
pandas==2.2.3
numpy==1.26.4
imbalanced-learn==0.12.4
oracledb==2.5.0
requests==2.32.3
matplotlib==3.9.0
```

### React
```
react 18 + typescript
react-router-dom 6
tailwindcss 3
react-hook-form
vite
```

---

## 👨‍💻 Tecnologias

![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![Flask](https://img.shields.io/badge/Flask-000000?style=for-the-badge&logo=flask&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![scikit-learn](https://img.shields.io/badge/scikit--learn-F7931E?style=for-the-badge&logo=scikit-learn&logoColor=white)
![XGBoost](https://img.shields.io/badge/XGBoost-F7931E?style=for-the-badge&logo=xgboost&logoColor=white)
