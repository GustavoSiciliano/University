# 🦷 De Novo Não! — ERP

> Sistema ERP desenvolvido para a ONG **Turma do Bem**, centralizando gestão de pacientes, dentistas, consultas, campanhas e doações com suporte a IA preditiva.

---

## 📌 Descrição

O **De Novo Não!** é um ERP web desenvolvido com React + Vite + TypeScript, integrado com back-end Java, banco Oracle e módulo de Inteligência Artificial em Python/FastAPI. O nome reflete o objetivo central: nunca mais perder dados ou repetir informações desnecessariamente.

---

## 🛠 Tecnologias Utilizadas

| Camada       | Tecnologia                                    |
|--------------|-----------------------------------------------|
| Front-end    | React 18, Vite 6, TypeScript 5, TailwindCSS 3 |
| Roteamento   | React Router DOM 6                            |
| Formulários  | React Hook Form 7                             |
| Back-end     | Java 17, JAX-RS                      |
| Banco        | Oracle Database                               |
| IA           | Python 3, FastAPI, scikit-learn, joblib       |
| API externa  | ViaCEP                                        |
| Deploy       | Vercel (front), Railway/Render (back)         |

---

## 📂 Estrutura de Pastas

```
src/
├── components/          # Componentes reutilizáveis
│   ├── Badge.tsx
│   ├── Button.tsx
│   ├── Card.tsx
│   ├── EmptyState.tsx
│   ├── ERPLayout.tsx    # Layout com sidebar do ERP
│   ├── Footer.tsx
│   ├── FormField.tsx
│   ├── Header.tsx
│   ├── Layout.tsx       # Layout público
│   ├── LoadingSpinner.tsx
│   ├── Modal.tsx
│   └── StatCard.tsx
├── hooks/
│   └── useApi.ts        # Hook genérico de fetch
├── pages/
│   ├── Contato.tsx
│   ├── FAQ.tsx
│   ├── Home.tsx
│   ├── Integrantes.tsx
│   ├── IntegranteDetalhe.tsx
│   ├── Sobre.tsx
│   ├── Solucao.tsx
│   └── erp/
│       ├── Dashboard.tsx
│       ├── Pacientes.tsx
│       ├── Dentistas.tsx
│       ├── Voluntarios.tsx
│       ├── Doadores.tsx
│       ├── Consultas.tsx
│       ├── Prontuarios.tsx
│       ├── Campanhas.tsx
│       ├── Doacoes.tsx
│       ├── Materiais.tsx
│       ├── IA.tsx
│       └── Relatorios.tsx
├── services/
│   └── api.ts           # Fetch para Java, Python e ViaCEP
├── types/
│   └── index.ts         # Interfaces TypeScript globais
└── utils/
    └── formatters.ts    # Formatação de moeda, data, CPF
```

---

## 👥 Autores e Créditos

| Nome                         | RM     | Turma  | GitHub | LinkedIn |
|------------------------------|--------|--------|--------|----------|
| Gustavo de Jesus Silva       | 567926 | 1TDSPS | [xGuga13x](https://github.com/xGuga13x) | [LinkedIn](https://www.linkedin.com/in/gustavo-de-jesus-silva-57716320b/) |
| Gustavo Rodrigues Siciliano  | 568419 | 1TDSPS | [Gxst456](https://github.com/Gxst456) | [LinkedIn](https://www.linkedin.com/in/gustavo-siciliano-78242224a/) |
| Samuel Keniti Kina de Lima   | 567614 | 1TDSPS | [swordoffiresof-coder](https://github.com/swordoffiresof-coder) | [LinkedIn](https://www.linkedin.com/in/samuel-keniti-kina-de-lima-1b7566228/) |

---

## 🚀 Como Usar

### Pré-requisitos
- Node.js 18+
- npm ou yarn

### Instalação e execução local

```bash
# Clone o repositório
git clone https://github.com/SEU_USUARIO/de-novo-nao.git
cd de-novo-nao

# Instale as dependências
npm install

# Configure as variáveis de ambiente
cp .env.example .env
# Edite o .env com as URLs da API Java e Python

# Inicie o servidor de desenvolvimento
npm run dev
```

Acesse: `http://localhost:5173`

### Variáveis de ambiente

```env
VITE_API_URL=http://localhost:8080/api   # Java
VITE_IA_URL=http://localhost:8000        # Python / FastAPI
```

### Build para produção

```bash
npm run build
npm run preview
```

---

## 🔗 Links

- 🔗 **Repositório GitHub:** [adicionar link]
- 🎥 **Vídeo YouTube:** [adicionar link]
- 🌐 **Deploy Vercel:** [adicionar link após deploy]
- 📋 **Trello:** https://trello.com/invite/b/68ddc4897bcae36542cb9b4a/de-novo-nao
- 💼 **Modelo de Negócio:** https://www.canva.com/design/DAG4Axfrhkc

---

## 📞 Contato

- **E-mail:** turmadobem@tdb.org.br
- **Site TDB:** https://www.turmadobem.org.br
- **FIAP:** https://www.fiap.com.br
