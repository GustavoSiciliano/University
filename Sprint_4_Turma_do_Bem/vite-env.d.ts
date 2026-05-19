import { useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { members } from './Integrantes'

export default function IntegranteDetalhe() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const member = members.find(m => m.id === id)

  useEffect(() => {
    document.title = member ? `${member.name} | De Novo Não!` : 'Não encontrado | De Novo Não!'
  }, [member])

  if (!member) return (
    <div className="text-center py-32 px-6">
      <p className="font-display text-2xl font-bold mb-4" style={{ color: '#f5821f' }}>
        Integrante não encontrado.
      </p>
      <button onClick={() => navigate('/integrantes')}
        className="text-sm font-body font-semibold hover:underline" style={{ color: '#7ab800' }}>
        ← Voltar para o time
      </button>
    </div>
  )

  return (
    <>
      <section style={{ backgroundColor: '#2d4a1e' }} className="py-14 px-6 text-center text-white">
        <h1 className="font-display font-bold text-3xl mb-2">Perfil do Integrante</h1>
        <p className="text-white/65 font-body text-sm">Membro da equipe De Novo Não!</p>
      </section>

      <section className="max-w-lg mx-auto px-6 py-14">
        <div className="bg-white rounded-2xl shadow-sm p-10 text-center border border-gray-100 fade-in">
          <img src={member.photo} alt={member.name}
            className="w-32 h-32 rounded-full object-cover mx-auto mb-5 border-4"
            style={{ borderColor: '#f4f9ec' }} />

          <h2 className="font-display font-bold text-2xl mb-1" style={{ color: '#2d4a1e' }}>
            {member.name}
          </h2>
          <p className="font-semibold font-body mb-5 text-sm" style={{ color: '#7ab800' }}>
            {member.role}
          </p>

          <div className="rounded-xl p-5 text-left space-y-2 mb-6" style={{ backgroundColor: '#f4f9ec' }}>
            {[
              { label: 'RM',    value: member.rm },
              { label: 'Turma', value: member.turma },
              { label: 'Curso', value: 'Análise e Desenvolvimento de Sistemas — FIAP' },
            ].map(row => (
              <p key={row.label} className="text-gray-600 font-body text-sm">
                <span className="font-semibold" style={{ color: '#2d4a1e' }}>{row.label}: </span>
                {row.value}
              </p>
            ))}
          </div>

          {/* Botões com SVG inline */}
          <div className="flex justify-center gap-4 mb-8">
            <a href={member.github} target="_blank" rel="noreferrer"
              className="flex items-center gap-2 px-5 py-2 rounded-full border border-gray-200 hover:bg-gray-50 transition-colors font-body text-sm font-semibold text-gray-700">
              <svg viewBox="0 0 24 24" className="w-4 h-4" fill="#2d4a1e">
                <path d="M12 0C5.37 0 0 5.37 0 12c0 5.3 3.44 9.8 8.21 11.38.6.11.82-.26.82-.58v-2.03c-3.34.72-4.04-1.61-4.04-1.61-.55-1.39-1.34-1.76-1.34-1.76-1.09-.74.08-.73.08-.73 1.2.09 1.84 1.24 1.84 1.24 1.07 1.83 2.81 1.3 3.49 1 .11-.78.42-1.3.76-1.6-2.67-.3-5.47-1.33-5.47-5.93 0-1.31.47-2.38 1.24-3.22-.13-.3-.54-1.52.12-3.18 0 0 1.01-.32 3.3 1.23a11.5 11.5 0 0 1 3-.4c1.02.005 2.04.14 3 .4 2.28-1.55 3.29-1.23 3.29-1.23.66 1.66.25 2.88.12 3.18.77.84 1.24 1.91 1.24 3.22 0 4.61-2.81 5.63-5.49 5.92.43.37.81 1.1.81 2.22v3.29c0 .32.22.69.83.57C20.57 21.8 24 17.3 24 12c0-6.63-5.37-12-12-12z"/>
              </svg>
              GitHub
            </a>
            <a href={member.linkedin} target="_blank" rel="noreferrer"
              className="flex items-center gap-2 px-5 py-2 rounded-full border border-gray-200 hover:bg-gray-50 transition-colors font-body text-sm font-semibold text-gray-700">
              <svg viewBox="0 0 24 24" className="w-4 h-4" fill="#0077b5">
                <path d="M20.45 20.45h-3.55v-5.57c0-1.33-.03-3.04-1.85-3.04-1.85 0-2.14 1.45-2.14 2.94v5.67H9.36V9h3.41v1.56h.05c.47-.9 1.63-1.85 3.36-1.85 3.59 0 4.26 2.37 4.26 5.45v6.29zM5.34 7.43a2.06 2.06 0 1 1 0-4.12 2.06 2.06 0 0 1 0 4.12zm1.78 13.02H3.56V9h3.56v11.45zM22.22 0H1.77C.79 0 0 .77 0 1.73v20.54C0 23.23.79 24 1.77 24h20.45c.98 0 1.78-.77 1.78-1.73V1.73C24 .77 23.2 0 22.22 0z"/>
              </svg>
              LinkedIn
            </a>
          </div>

          <button onClick={() => navigate('/integrantes')}
            className="text-sm font-body font-semibold hover:underline" style={{ color: '#7ab800' }}>
            ← Voltar para o time
          </button>
        </div>
      </section>
    </>
  )
}
