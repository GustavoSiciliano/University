import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

const carouselImages = [
  { src: '/image/carousel_atendimento_humanizado.jpg', caption: 'Atendimento cuidadoso e humanizado' },
  { src: '/image/carousel_mulher_atendimento.jpg',     caption: 'Transformando sorrisos e vidas' },
  { src: '/image/carousel_transformando_sorrisos.jpg', caption: 'Saúde bucal como direito de todos' },
  { src: '/image/carousel_aparelho.jpg',               caption: 'Cuidando do sorriso de cada paciente' },
]

function Carousel() {
  const [current, setCurrent] = useState(0)
  const [loaded, setLoaded]   = useState<Record<number, boolean>>({})
  const [errored, setErrored] = useState<Record<number, boolean>>({})

  useEffect(() => {
    const t = setInterval(() => setCurrent(p => (p + 1) % carouselImages.length), 4500)
    return () => clearInterval(t)
  }, [])

  const prev = () => setCurrent(p => (p - 1 + carouselImages.length) % carouselImages.length)
  const next = () => setCurrent(p => (p + 1) % carouselImages.length)

  return (
    <div className="relative rounded-2xl overflow-hidden shadow-md bg-gray-100" style={{ height: 360 }}>
      {carouselImages.map((img, i) => (
        <div key={i} className="absolute inset-0 transition-opacity duration-700"
          style={{ opacity: i === current ? 1 : 0, pointerEvents: i === current ? 'auto' : 'none' }}>

          {errored[i] ? (
            <div className="w-full h-full flex flex-col items-center justify-center"
              style={{ backgroundColor: '#f4f9ec' }}>
              <span className="text-5xl mb-2">🦷</span>
              <p className="text-sm text-gray-400 font-body">{img.caption}</p>
            </div>
          ) : (
            <>
              {!loaded[i] && !errored[i] && (
                <div className="absolute inset-0 flex items-center justify-center"
                  style={{ backgroundColor: '#f4f9ec' }}>
                  <div className="w-8 h-8 rounded-full border-4 border-gray-200 animate-spin"
                    style={{ borderTopColor: '#7ab800' }} />
                </div>
              )}
              <img
                src={img.src}
                alt={img.caption}
                onLoad={() => setLoaded(p => ({ ...p, [i]: true }))}
                onError={() => setErrored(p => ({ ...p, [i]: true }))}
                className="w-full h-full object-cover"
                style={{ opacity: loaded[i] ? 1 : 0, transition: 'opacity 0.3s' }}
              />
            </>
          )}

          <div className="absolute bottom-0 left-0 right-0 px-5 py-3"
            style={{ background: 'linear-gradient(to top, rgba(45,74,30,0.80), transparent)' }}>
            <p className="text-white text-sm font-body">{img.caption}</p>
          </div>
        </div>
      ))}

      {/* Setas */}
      <button onClick={prev} aria-label="Anterior"
        className="absolute left-3 top-1/2 -translate-y-1/2 w-9 h-9 rounded-full flex items-center justify-center text-white text-lg font-bold z-10 hover:scale-110 transition-transform"
        style={{ backgroundColor: 'rgba(0,0,0,0.40)' }}>‹</button>
      <button onClick={next} aria-label="Próxima"
        className="absolute right-3 top-1/2 -translate-y-1/2 w-9 h-9 rounded-full flex items-center justify-center text-white text-lg font-bold z-10 hover:scale-110 transition-transform"
        style={{ backgroundColor: 'rgba(0,0,0,0.40)' }}>›</button>

      {/* Indicadores */}
      <div className="absolute bottom-3 right-4 flex gap-1.5 z-10">
        {carouselImages.map((_, i) => (
          <button key={i} onClick={() => setCurrent(i)}
            className="w-2 h-2 rounded-full transition-all"
            style={{ backgroundColor: i === current ? '#7ab800' : 'rgba(255,255,255,0.5)' }} />
        ))}
      </div>
    </div>
  )
}

const beneficios = [
  { icon: '📋', titulo: 'Tudo centralizado', texto: 'Cadastros, prontuários e consultas num único lugar. Sem papel perdido.' },
  { icon: '⚡', titulo: 'Mais agilidade',     texto: 'O dentista foca no paciente. O sistema cuida do resto.' },
  { icon: '💙', titulo: 'Cuidado contínuo',  texto: 'Qualquer voluntário acessa o histórico e oferece tratamento personalizado.' },
  { icon: '🔐', titulo: 'Dados seguros',     texto: 'Validação de CPF, CRO e conformidade com a LGPD.' },
]

export default function Home() {
  useEffect(() => { document.title = 'De Novo Não!' }, [])

  return (
    <>
      <section className="bg-white py-16 px-6">
        <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-10 items-center">
          <div className="fade-up">
            <span className="inline-block text-xs font-semibold px-3 py-1 rounded-full mb-5 font-body"
              style={{ backgroundColor: '#f4f9ec', color: '#7ab800' }}>
              Turma do Bem × FIAP
            </span>
            <h1 className="font-display font-extrabold text-4xl md:text-5xl leading-tight mb-4"
              style={{ color: '#2d4a1e' }}>De Novo Não!</h1>
            <p className="text-gray-500 text-base leading-relaxed mb-8 font-body max-w-md">
              Um sistema desenvolvido por estudantes da FIAP para ajudar a ONG Turma do Bem
              a organizar o atendimento odontológico gratuito de jovens e mulheres em situação de vulnerabilidade social.
            </p>
            <div className="flex flex-wrap gap-3">
              <Link to="/erp/login"
                className="px-6 py-2.5 rounded-full font-semibold text-sm text-white hover:opacity-90 transition-opacity font-body"
                style={{ backgroundColor: '#f5821f' }}>Acessar o ERP</Link>
              <Link to="/sobre"
                className="px-6 py-2.5 rounded-full font-semibold text-sm border hover:bg-gray-50 transition-colors font-body"
                style={{ borderColor: '#7ab800', color: '#7ab800' }}>Saiba mais</Link>
            </div>
          </div>
          <div className="fade-up delay-2"><Carousel /></div>
        </div>
      </section>

      <section style={{ backgroundColor: '#f4f9ec' }} className="py-12 px-6">
        <div className="max-w-5xl mx-auto text-center">
          <p className="text-xs font-semibold uppercase tracking-wider mb-6 font-body" style={{ color: '#7ab800' }}>
            O impacto da Turma do Bem
          </p>
          <div className="flex flex-wrap justify-center gap-6">
            {[
              { valor: '27+', label: 'anos de impacto' },
              { valor: '2M+', label: 'sorrisos transformados' },
              { valor: '7mil+', label: 'dentistas voluntários' },
            ].map(stat => (
              <div key={stat.label} className="bg-white rounded-2xl px-10 py-6 shadow-sm text-center w-44">
                <div className="text-3xl font-display font-extrabold mb-1" style={{ color: '#f5821f' }}>{stat.valor}</div>
                <p className="text-gray-400 text-xs font-body">{stat.label}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="py-14 px-6 bg-white">
        <div className="max-w-5xl mx-auto">
          <div className="text-center mb-10">
            <h2 className="font-display font-bold text-2xl mb-2" style={{ color: '#2d4a1e' }}>O que o sistema resolve</h2>
            <p className="text-gray-400 text-sm font-body">Problemas reais da ONG que motivaram o desenvolvimento.</p>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
            {beneficios.map((b, i) => (
              <div key={b.titulo} className={`rounded-xl p-5 border fade-up delay-${i + 1}`}
                style={{ backgroundColor: '#f4f9ec', borderColor: '#e2f0d0' }}>
                <span className="text-3xl mb-3 block">{b.icon}</span>
                <h3 className="font-display font-bold text-base mb-2" style={{ color: '#2d4a1e' }}>{b.titulo}</h3>
                <p className="text-gray-500 text-sm leading-relaxed font-body">{b.texto}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section style={{ backgroundColor: '#fff8f2' }} className="py-14 px-6">
        <div className="max-w-4xl mx-auto">
          <h2 className="font-display font-bold text-2xl mb-8 text-center" style={{ color: '#2d4a1e' }}>Programas atendidos</h2>
          <div className="grid md:grid-cols-2 gap-6">
            <div className="bg-white rounded-2xl p-7 border shadow-sm" style={{ borderColor: '#e2f0d0' }}>
              <div className="text-2xl mb-3">🦷</div>
              <h3 className="font-display font-bold text-lg mb-2" style={{ color: '#7ab800' }}>Dentistas do Bem</h3>
              <p className="text-gray-500 text-sm leading-relaxed font-body">
                Atendimento voltado a crianças e jovens. O cadastro inclui dados do paciente e do responsável legal, com histórico clínico completo.
              </p>
            </div>
            <div className="bg-white rounded-2xl p-7 border shadow-sm" style={{ borderColor: '#fde8d0' }}>
              <div className="text-2xl mb-3">💛</div>
              <h3 className="font-display font-bold text-lg mb-2" style={{ color: '#f5821f' }}>Apolônias do Bem</h3>
              <p className="text-gray-500 text-sm leading-relaxed font-body">
                Focado no atendimento de mulheres em situação de vulnerabilidade. A própria paciente preenche suas informações no sistema.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="py-12 px-6 bg-white">
        <div className="max-w-4xl mx-auto text-center">
          <p className="text-gray-400 text-xs font-body uppercase tracking-wider mb-5">Tecnologias utilizadas</p>
          <div className="flex flex-wrap justify-center gap-3">
            {['Java + Quarkus', 'Python + Flask', 'Oracle DB', 'React + TypeScript', 'scikit-learn', 'ViaCEP', 'IBGE', 'VLibras + UserWay'].map(tech => (
              <span key={tech} className="text-sm px-4 py-1.5 rounded-full border font-body"
                style={{ borderColor: '#7ab800', color: '#2d4a1e' }}>{tech}</span>
            ))}
          </div>
        </div>
      </section>


      {/* Seção de Doação Pública */}
      <section className="py-16 px-6" style={{ backgroundColor: '#fff' }}>
        <div className="max-w-4xl mx-auto text-center">
          <span className="inline-block text-xs font-semibold px-3 py-1 rounded-full mb-4 font-body"
            style={{ backgroundColor: '#f4f9ec', color: '#7ab800' }}>
            Faça a diferença
          </span>
          <h2 className="font-display font-extrabold text-3xl mb-3" style={{ color: '#2d4a1e' }}>
            Doe e transforme sorrisos
          </h2>
          <p className="text-gray-500 text-base leading-relaxed mb-10 font-body max-w-xl mx-auto">
            Sua doação garante atendimento odontológico gratuito para jovens e mulheres
            em situação de vulnerabilidade social. Cada contribuição faz a diferença.
          </p>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-10">
            {[
              { valor: 'R$ 25',  desc: 'Materiais de higiene para 1 paciente' },
              { valor: 'R$ 50',  desc: 'Consulta de avaliação completa' },
              { valor: 'R$ 100', desc: 'Tratamento odontológico completo' },
            ].map(op => (
              <div key={op.valor}
                className="rounded-2xl p-6 border-2 cursor-pointer hover:shadow-md transition-all text-center"
                style={{ borderColor: '#e2f0d0', backgroundColor: '#f4f9ec' }}>
                <p className="font-display font-extrabold text-2xl mb-1" style={{ color: '#f5821f' }}>
                  {op.valor}
                </p>
                <p className="text-gray-500 text-sm font-body">{op.desc}</p>
              </div>
            ))}
          </div>

          <div className="flex flex-col sm:flex-row gap-3 justify-center items-center">
            <Link
              to="/doe"
              className="px-8 py-3 rounded-full font-semibold text-sm text-white hover:opacity-90 transition-opacity font-body"
              style={{ backgroundColor: '#f5821f' }}>
              Quero Doar Agora
            </Link>
            <a
              href="https://turmadobem.org.br"
              target="_blank"
              rel="noopener noreferrer"
              className="px-8 py-3 rounded-full font-semibold text-sm border hover:bg-gray-50 transition-colors font-body"
              style={{ borderColor: '#7ab800', color: '#7ab800' }}>
              Conhecer a ONG
            </a>
          </div>

          <p className="text-gray-400 text-xs font-body mt-6">
            Doações processadas com segurança pelo site oficial da Turma do Bem.
          </p>
        </div>
      </section>

      <section style={{ backgroundColor: '#2d4a1e' }} className="py-12 px-6 text-center text-white">
        <img src="/image/logo-dnn.png" alt="De Novo Não!" className="h-14 mx-auto mb-4"
          style={{ mixBlendMode: 'screen' }} />
        <h2 className="font-display font-bold text-2xl mb-3">Quer conhecer o sistema?</h2>
        <p className="text-white/60 text-sm mb-6 font-body">Acesse o ERP ou saiba mais sobre como o projeto foi desenvolvido.</p>
        <div className="flex flex-wrap justify-center gap-3">
          <Link to="/erp/login"
            className="px-6 py-2.5 rounded-full font-semibold text-sm font-body hover:opacity-90 transition-opacity"
            style={{ backgroundColor: '#f5821f', color: 'white' }}>Acessar o ERP</Link>
          <Link to="/sobre"
            className="px-6 py-2.5 rounded-full font-semibold text-sm border border-white/25 hover:bg-white/10 transition-colors font-body">
            Ver o projeto</Link>
        </div>
      </section>
    </>
  )
}
