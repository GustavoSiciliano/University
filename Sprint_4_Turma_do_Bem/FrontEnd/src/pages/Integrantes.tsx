import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

export interface Member {
  id: string
  name: string
  rm: string
  turma: string
  github: string
  linkedin: string
  photo: string
  role: string
}

export const members: Member[] = [
  {
    id: 'gustavo-jesus',
    name: 'Gustavo de Jesus Silva',
    rm: '567926',
    turma: '1TDSPS',
    github: 'https://github.com/xGuga13x',
    linkedin: 'https://www.linkedin.com/in/gustavo-de-jesus-silva-57716320b/',
    photo: '/image/G_JESUS.jpeg',
    role: 'Desenvolvedor Front-End',
  },
  {
    id: 'gustavo-siciliano',
    name: 'Gustavo Rodrigues Siciliano',
    rm: '568419',
    turma: '1TDSPS',
    github: 'https://github.com/Gxst456',
    linkedin: 'https://www.linkedin.com/in/gustavo-siciliano-78242224a/',
    photo: '/image/Siciliano.jpg',
    role: 'Desenvolvedor Back-End',
  },
  {
    id: 'samuel-keniti',
    name: 'Samuel Keniti Kina de Lima',
    rm: '567614',
    turma: '1TDSPS',
    github: 'https://github.com/swordoffiresof-coder',
    linkedin: 'https://www.linkedin.com/in/samuel-keniti-kina-de-lima-1b7566228/',
    photo: '/image/Samuel_Keniti.jpg',
    role: 'Desenvolvedor Full-Stack',
  },
]

export default function Integrantes() {
  const navigate = useNavigate()
  useEffect(() => { document.title = 'Equipe | De Novo Não!' }, [])

  return (
    <>
      <section style={{ backgroundColor: '#2d4a1e' }} className="py-14 px-6 text-white text-center">
        <h1 className="font-display font-extrabold text-4xl mb-3 fade-up">Nossa Equipe</h1>
        <p className="text-white/65 text-sm max-w-xl mx-auto font-body fade-up delay-1">
          Três estudantes do 1º ano de ADS na FIAP. Clique em um card para ver mais detalhes.
        </p>
      </section>

      <section className="max-w-5xl mx-auto px-6 py-14">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-7">
          {members.map((m, i) => (
            <div key={m.rm}
              onClick={() => navigate(`/integrantes/${m.id}`)}
              className={`bg-white rounded-2xl p-6 text-center border border-gray-100 shadow-sm
                cursor-pointer hover:-translate-y-1 hover:shadow-md transition-all duration-300
                fade-up delay-${i + 1}`}>

              <img src={m.photo} alt={`Foto de ${m.name}`}
                className="w-24 h-24 rounded-full object-cover mx-auto mb-4 border-4"
                style={{ borderColor: '#f4f9ec' }} />

              <h2 className="font-display font-bold text-base mb-0.5" style={{ color: '#2d4a1e' }}>
                {m.name}
              </h2>
              <p className="text-xs font-semibold mb-1 font-body" style={{ color: '#7ab800' }}>
                {m.role}
              </p>
              <p className="text-gray-400 text-xs mb-4 font-body">RM {m.rm} · {m.turma}</p>

              {/* Ícones GitHub e LinkedIn usando SVG inline — sem dependência de imagem */}
              <div className="flex justify-center gap-3 mb-4">
                <a href={m.github} target="_blank" rel="noopener noreferrer"
                  onClick={e => e.stopPropagation()}
                  title="GitHub"
                  className="w-8 h-8 rounded-full flex items-center justify-center hover:opacity-80 transition-opacity"
                  style={{ backgroundColor: '#f3f4f6' }}>
                  <svg viewBox="0 0 24 24" className="w-5 h-5" fill="#2d4a1e">
                    <path d="M12 0C5.37 0 0 5.37 0 12c0 5.3 3.44 9.8 8.21 11.38.6.11.82-.26.82-.58v-2.03c-3.34.72-4.04-1.61-4.04-1.61-.55-1.39-1.34-1.76-1.34-1.76-1.09-.74.08-.73.08-.73 1.2.09 1.84 1.24 1.84 1.24 1.07 1.83 2.81 1.3 3.49 1 .11-.78.42-1.3.76-1.6-2.67-.3-5.47-1.33-5.47-5.93 0-1.31.47-2.38 1.24-3.22-.13-.3-.54-1.52.12-3.18 0 0 1.01-.32 3.3 1.23a11.5 11.5 0 0 1 3-.4c1.02.005 2.04.14 3 .4 2.28-1.55 3.29-1.23 3.29-1.23.66 1.66.25 2.88.12 3.18.77.84 1.24 1.91 1.24 3.22 0 4.61-2.81 5.63-5.49 5.92.43.37.81 1.1.81 2.22v3.29c0 .32.22.69.83.57C20.57 21.8 24 17.3 24 12c0-6.63-5.37-12-12-12z"/>
                  </svg>
                </a>
                <a href={m.linkedin} target="_blank" rel="noopener noreferrer"
                  onClick={e => e.stopPropagation()}
                  title="LinkedIn"
                  className="w-8 h-8 rounded-full flex items-center justify-center hover:opacity-80 transition-opacity"
                  style={{ backgroundColor: '#e8f4fd' }}>
                  <svg viewBox="0 0 24 24" className="w-5 h-5" fill="#0077b5">
                    <path d="M20.45 20.45h-3.55v-5.57c0-1.33-.03-3.04-1.85-3.04-1.85 0-2.14 1.45-2.14 2.94v5.67H9.36V9h3.41v1.56h.05c.47-.9 1.63-1.85 3.36-1.85 3.59 0 4.26 2.37 4.26 5.45v6.29zM5.34 7.43a2.06 2.06 0 1 1 0-4.12 2.06 2.06 0 0 1 0 4.12zm1.78 13.02H3.56V9h3.56v11.45zM22.22 0H1.77C.79 0 0 .77 0 1.73v20.54C0 23.23.79 24 1.77 24h20.45c.98 0 1.78-.77 1.78-1.73V1.73C24 .77 23.2 0 22.22 0z"/>
                  </svg>
                </a>
              </div>

              <span className="inline-block text-xs px-4 py-1.5 rounded-full font-semibold font-body"
                style={{ backgroundColor: '#f4f9ec', color: '#2d4a1e' }}>
                Ver perfil →
              </span>
            </div>
          ))}
        </div>
      </section>
    </>
  )
}
