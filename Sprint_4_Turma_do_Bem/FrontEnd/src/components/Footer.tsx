import { NavLink } from 'react-router-dom'

export default function Footer() {
  return (
    <footer style={{ backgroundColor: '#2d4a1e' }} className="text-white">
      <div className="max-w-6xl mx-auto px-6 py-10 grid grid-cols-1 md:grid-cols-3 gap-8">

        <div>
          <img
            src="/image/logo-dnn.png"
            alt="De Novo Não!"
            className="h-12 w-auto mb-3"
            style={{ mixBlendMode: 'screen' }}
          />
          <p className="text-white/60 text-sm leading-relaxed">
            ERP desenvolvido por estudantes da FIAP em parceria com a ONG Turma do Bem.
          </p>
        </div>

        <div>
          <h4 className="font-display font-bold text-sm mb-3" style={{ color: '#7ab800' }}>
            Páginas
          </h4>
          <ul className="flex flex-col gap-1.5">
            {[
              { label: 'Início',   to: '/' },
              { label: 'Sobre',    to: '/sobre' },
              { label: 'Equipe',   to: '/integrantes' },
              { label: 'Solução',  to: '/solucao' },
              { label: 'FAQ',      to: '/faq' },
              { label: 'Contato',  to: '/contato' },
            ].map(item => (
              <li key={item.to}>
                <NavLink to={item.to} className="text-white/60 hover:text-white text-sm transition-colors">
                  {item.label}
                </NavLink>
              </li>
            ))}
          </ul>
        </div>

        <div>
          <h4 className="font-display font-bold text-sm mb-3" style={{ color: '#f5821f' }}>
            Turma do Bem
          </h4>
          <p className="text-white/60 text-sm leading-relaxed mb-3">
            ONG que leva atendimento odontológico gratuito a jovens e mulheres em vulnerabilidade social há mais de 20 anos.
          </p>
          <a
            href="https://turmadobem.org.br"
            target="_blank"
            rel="noopener noreferrer"
            className="text-sm hover:underline font-semibold"
            style={{ color: '#7ab800' }}
          >
            turmadobem.org.br
          </a>
        </div>
      </div>

      {/* Integrantes */}
      <div className="border-t border-white/10 py-5 px-6">
        <div className="max-w-6xl mx-auto flex flex-col md:flex-row md:items-center justify-between gap-3">
          <div className="flex flex-wrap gap-x-6 gap-y-1">
            {[
              { nome: 'Gustavo Rodrigues Siciliano', rm: '568419' },
              { nome: 'Gustavo de Jesus Silva',      rm: '567926' },
              { nome: 'Samuel Keniti Kina de Lima',  rm: '567614' },
            ].map(i => (
              <span key={i.rm} className="text-white/40 text-xs font-body">
                {i.nome} — RM {i.rm}
              </span>
            ))}
          </div>
          <p className="text-white/25 text-xs font-body whitespace-nowrap">
            FIAP · 1TDSPS · 2025
          </p>
        </div>
      </div>
    </footer>
  )
}
