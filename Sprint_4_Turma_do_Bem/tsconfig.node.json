import { useEffect } from 'react'
import { Link } from 'react-router-dom'

const secoes = [
  {
    titulo: 'O problema',
    texto: 'Os dentistas voluntários da Turma do Bem lidam com muitos dados de pacientes — sem um sistema centralizado, informações se perdem, tratamentos são interrompidos e o atendimento perde qualidade. Isso afeta crianças e mulheres em vulnerabilidade social.',
  },
  {
    titulo: 'A solução',
    texto: 'O "De Novo Não!" é um ERP digital que centraliza cadastros, prontuários e anotações clínicas em uma única plataforma. Qualquer voluntário acessa o histórico completo e continua o tratamento com segurança.',
  },
  {
    titulo: 'Por que esse nome?',
    texto: '"De Novo Não!" é um compromisso de acabar com os problemas recorrentes da ONG: perda de dados, retrabalho e falta de controle. Nunca mais.',
  },
  {
    titulo: 'Como foi desenvolvido',
    texto: 'Projeto criado por três estudantes do 1º ano de ADS na FIAP como entrega da Sprint 4. Usamos Java com  para a API REST, Oracle como banco, React + TypeScript no front, Python para automações e Machine Learning para previsão de faltas e arrecadações.',
  },
]

export default function Sobre() {
  useEffect(() => { document.title = 'Sobre | De Novo Não!' }, [])

  return (
    <>
      <section style={{ backgroundColor: '#2d4a1e' }} className="py-14 px-6 text-white text-center">
        <h1 className="font-display font-extrabold text-4xl mb-3 fade-up">Sobre o Projeto</h1>
        <p className="text-white/65 text-sm max-w-xl mx-auto font-body fade-up delay-1">
          Entenda como e por que o De Novo Não! foi criado.
        </p>
      </section>

      <section className="max-w-3xl mx-auto px-6 py-14 space-y-10">
        {secoes.map((s, i) => (
          <div key={s.titulo} className={`fade-up delay-${i + 1}`}>
            <h2 className="font-display font-bold text-xl mb-2" style={{ color: '#2d4a1e' }}>
              {s.titulo}
            </h2>
            <p className="text-gray-600 leading-relaxed text-sm font-body">{s.texto}</p>
          </div>
        ))}

        <div className="pt-4 border-t border-gray-100">
          <p className="text-gray-400 text-sm font-body">
            Quer ver as tecnologias?{' '}
            <Link to="/solucao" className="font-semibold hover:underline" style={{ color: '#7ab800' }}>
              Acesse a página Solução →
            </Link>
          </p>
        </div>
      </section>

      <section style={{ backgroundColor: '#f4f9ec' }} className="py-12 px-6">
        <div className="max-w-4xl mx-auto grid md:grid-cols-2 gap-8 items-center">
          <div>
            <h2 className="font-display font-bold text-xl mb-3" style={{ color: '#2d4a1e' }}>
              Impacto real
            </h2>
            <p className="text-gray-500 text-sm leading-relaxed font-body mb-4">
              A Turma do Bem já transformou mais de 2 milhões de sorrisos pelo Brasil.
              Com o nosso sistema, a ONG pode atender ainda mais pessoas com mais qualidade.
            </p>
            <a href="https://turmadobem.org.br" target="_blank" rel="noopener noreferrer"
              className="text-sm font-semibold hover:underline font-body" style={{ color: '#7ab800' }}>
              Conhecer a ONG →
            </a>
          </div>
          <div className="rounded-2xl overflow-hidden shadow-sm">
            <img
              src="/image/carousel_atendimento_humanizado.jpg"
              alt="Atendimento odontológico"
              className="w-full h-56 object-cover"
            />
          </div>
        </div>
      </section>
    </>
  )
}
