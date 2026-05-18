import { useEffect } from 'react'

const technologies = [
  {
    name: 'Java + Quarkus',
    icon: '☕',
    description: 'API REST com camadas modelo, DAO, BOO, serviço e API. CRUD completo para todas as entidades do sistema, autenticação por perfil e integração com Oracle via JDBC.',
  },
  {
    name: 'Oracle Database',
    icon: '🗄️',
    description: '16 tabelas com PK, FK, UK, CK e NN. Queries com JOIN, funções de agregação e FETCH FIRST para paginação. Banco hospedado no ambiente FIAP.',
  },
  {
    name: 'React + Vite + TypeScript',
    icon: '⚛️',
    description: 'SPA modular com React Router, hooks customizados e TailwindCSS. Consome a API Java via fetch nativo. Deploy na Vercel com suporte a rotas SPA.',
  },
  {
    name: 'Python + Flask + scikit-learn',
    icon: '🐍',
    description: 'Modelos de Machine Learning para previsão de faltas e arrecadação de campanhas. API REST exposta via Flask, com endpoints /predict/falta e /predict/arrecadacao.',
  },
  {
    name: 'ViaCEP + IBGE',
    icon: '📍',
    description: 'ViaCEP para preenchimento automático de endereço por CEP. IBGE para listagem de estados e municípios em cascata nos formulários do ERP.',
  },
  {
    name: 'VLibras + UserWay',
    icon: '♿',
    description: 'VLibras (Gov.br) para acessibilidade em Língua Brasileira de Sinais. UserWay para ajuste de fonte, alto contraste, leitura de tela e outras ferramentas de acessibilidade.',
  },
]

const roadmap = [
  { stage: '1ª Etapa', title: 'Levantamento de Requisitos', desc: 'Identificação dos problemas: perda de dados, retrabalho e falta de controle nos registros clínicos da ONG.' },
  { stage: '2ª Etapa', title: 'Banco de Dados Oracle',      desc: 'DDL completo com 16 tabelas, entidades, relacionamentos, constraints e dados iniciais.' },
  { stage: '3ª Etapa', title: 'Back-end Java',              desc: 'API REST com Quarkus. Camadas modelo → DAO → BOO → serviço → API. CRUD completo.' },
  { stage: '4ª Etapa', title: 'Front-end React',            desc: 'SPA com ERP integrado, dashboard de KPIs, login por perfil e consumo da API Java.' },
  { stage: '5ª Etapa', title: 'IA com Python',              desc: 'Modelos preditivos treinados com scikit-learn, expostos via Flask para o front consumir.' },
  { stage: '6ª Etapa', title: 'Deploy & Entrega Final',     desc: 'Vercel (front) + Render (Python IA) + documentação e vídeo de demonstração.' },
]

export default function Solucao() {
  useEffect(() => { document.title = 'Solução | De Novo Não!' }, [])

  return (
    <>
      <section className="text-center py-20 px-6 text-white"
        style={{ background: 'linear-gradient(135deg, #2d4a1e 0%, #7ab800 100%)' }}>
        <h1 className="font-display font-extrabold text-4xl mb-4 animate-fade-in-up">Solução do Projeto</h1>
        <p className="text-white/80 text-lg max-w-2xl mx-auto font-body">
          Tecnologias, arquitetura e roadmap do sistema De Novo Não!
        </p>
      </section>

      <div className="max-w-5xl mx-auto px-8 py-14 space-y-16">

        {/* Tecnologias */}
        <section>
          <h2 className="font-display font-bold text-2xl mb-6 text-center" style={{ color: '#2d4a1e' }}>
            Tecnologias Utilizadas
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {technologies.map((t) => (
              <div key={t.name} className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 animate-fade-in-up flex gap-4">
                <span className="text-3xl shrink-0">{t.icon}</span>
                <div>
                  <h3 className="font-display font-bold text-lg mb-1" style={{ color: '#2d4a1e' }}>{t.name}</h3>
                  <p className="text-gray-500 text-sm leading-relaxed font-body">{t.description}</p>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Arquitetura */}
        <section className="rounded-2xl p-8 text-white" style={{ backgroundColor: '#2d4a1e' }}>
          <h2 className="font-display font-bold text-2xl mb-6">Arquitetura do Sistema</h2>
          <div className="space-y-3 font-body text-sm text-white/80">
            <div className="flex items-center gap-3">
              <span className="bg-white/10 rounded-lg px-3 py-1 text-white font-semibold text-xs">Front-end</span>
              <span>React + TypeScript + TailwindCSS → Vercel</span>
            </div>
            <div className="flex items-center gap-3">
              <span className="bg-white/10 rounded-lg px-3 py-1 text-white font-semibold text-xs">Back-end</span>
              <span>Java + Quarkus → REST API → Oracle DB (FIAP)</span>
            </div>
            <div className="flex items-center gap-3">
              <span className="bg-white/10 rounded-lg px-3 py-1 text-white font-semibold text-xs">IA</span>
              <span>Python + Flask + scikit-learn → Render</span>
            </div>
            <div className="flex items-center gap-3">
              <span className="bg-white/10 rounded-lg px-3 py-1 text-white font-semibold text-xs">APIs externas</span>
              <span>ViaCEP · IBGE · VLibras · UserWay</span>
            </div>
          </div>
        </section>

        {/* Roadmap */}
        <section>
          <h2 className="font-display font-bold text-2xl mb-6 text-center" style={{ color: '#2d4a1e' }}>
            Roadmap do Projeto
          </h2>
          <div className="space-y-4">
            {roadmap.map((step, index) => (
              <div key={step.stage} className="flex gap-4 items-start animate-fade-in-up"
                style={{ animationDelay: `${index * 0.08}s` }}>
                <div className="shrink-0 w-10 h-10 rounded-xl text-white flex items-center justify-center font-display font-bold text-sm"
                  style={{ backgroundColor: '#2d4a1e' }}>
                  {index + 1}
                </div>
                <div className="bg-white rounded-xl p-4 flex-1 shadow-sm border border-gray-100">
                  <p className="text-xs font-semibold font-body uppercase tracking-wide mb-0.5" style={{ color: '#7ab800' }}>{step.stage}</p>
                  <h3 className="font-display font-bold text-base" style={{ color: '#2d4a1e' }}>{step.title}</h3>
                  <p className="text-gray-500 text-sm font-body mt-1">{step.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Materiais */}
        <section className="bg-gray-50 rounded-2xl p-8 text-center space-y-4">
          <h2 className="font-display font-bold text-xl" style={{ color: '#2d4a1e' }}>Materiais do Projeto</h2>
          <div className="flex flex-wrap justify-center gap-4">
            <a href="https://trello.com/invite/b/68ddc4897bcae36542cb9b4a/ATTIf42b43a5a6290d2060e49be9265cc490A66FF587/de-novo-nao"
              target="_blank" rel="noreferrer"
              className="text-white px-5 py-2 rounded-xl hover:opacity-90 transition-opacity font-body font-medium text-sm"
              style={{ backgroundColor: '#0079bf' }}>
              Trello — Backlogs
            </a>
            <a href="https://www.canva.com/design/DAG4Axfrhkc/V-gzqKDMzEQVUtIJONnJQQ/view"
              target="_blank" rel="noreferrer"
              className="text-white px-5 py-2 rounded-xl hover:opacity-90 transition-colors font-body font-medium text-sm"
              style={{ backgroundColor: '#7ab800' }}>
              Modelo de Negócio
            </a>
          </div>
        </section>

      </div>
    </>
  )
}
