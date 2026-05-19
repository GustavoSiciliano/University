import { useState, useEffect } from 'react'

interface FAQItem { id: number; question: string; answer: string }

const faqItems: FAQItem[] = [
  { id: 1, question: 'O que é o projeto "De Novo Não!"?',    answer: 'É um ERP digital criado para otimizar o registro e armazenamento das informações dos pacientes da Turma do Bem, eliminando perda de dados e retrabalho.' },
  { id: 2, question: 'Qual problema o sistema resolve?',      answer: 'A desorganização dos registros clínicos que compromete a continuidade do atendimento. O ERP centraliza cadastros, prontuários e anotações com segurança.' },
  { id: 3, question: 'Quem pode usar o sistema?',             answer: 'Dentistas voluntários e coordenadores da ONG. Dentistas cadastram pacientes e registram atendimentos; coordenadores geram relatórios e acompanham campanhas.' },
  { id: 4, question: 'Quais programas são suportados?',       answer: '"Dentistas do Bem" e "Apolônias do Bem". As telas do ERP se adaptam conforme o programa selecionado no cadastro do paciente.' },
  { id: 5, question: 'Como a IA é usada no sistema?',         answer: 'O módulo de IA em Python prevê a probabilidade de falta em consultas e estima a arrecadação de campanhas. É um apoio à decisão, não substitui o julgamento humano.' },
  { id: 6, question: 'Como o sistema protege os dados?',      answer: 'Validação de CPF e CRO, identificador único por paciente, histórico imutável com rastreabilidade completa por data e autor, em conformidade com a LGPD.' },
  { id: 7, question: 'Quais tecnologias foram usadas?',       answer: 'Java + Quarkus (API REST), Oracle Database, React + Vite + TypeScript + TailwindCSS (front-end), Python + Flask + scikit-learn (IA), ViaCEP e IBGE (APIs públicas), VLibras e UserWay (acessibilidade).' },
  { id: 8, question: 'O sistema funciona no celular?',        answer: 'Sim! O front-end é totalmente responsivo, funcionando em dispositivos móveis, tablets e desktops sem quebras de layout.' },
]

export default function FAQ() {
  const [openId, setOpenId] = useState<number | null>(null)
  useEffect(() => { document.title = 'FAQ | De Novo Não!' }, [])

  return (
    <>
      <section className="text-center py-20 px-6 text-white"
        style={{ background: 'linear-gradient(135deg, #2d4a1e 0%, #7ab800 100%)' }}>
        <h1 className="font-display font-extrabold text-4xl mb-4 animate-fade-in-up">Perguntas Frequentes</h1>
        <p className="text-white/80 text-lg max-w-2xl mx-auto font-body">
          Dúvidas sobre o De Novo Não! e como ele transforma a Turma do Bem.
        </p>
      </section>

      <section className="max-w-4xl mx-auto px-6 py-14 space-y-3">
        {faqItems.map((item) => (
          <div key={item.id} className="border border-gray-200 rounded-2xl overflow-hidden shadow-sm animate-fade-in-up">
            <button
              onClick={() => setOpenId((prev) => (prev === item.id ? null : item.id))}
              className="w-full flex justify-between items-center px-6 py-4 bg-white hover:bg-gray-50 transition-colors text-left"
            >
              <span className="font-display font-bold text-base" style={{ color: '#2d4a1e' }}>{item.question}</span>
              <span className="text-2xl font-bold ml-4 shrink-0 transition-transform duration-200"
                style={{ color: '#7ab800', transform: openId === item.id ? 'rotate(45deg)' : 'rotate(0)' }}>
                +
              </span>
            </button>
            {openId === item.id && (
              <div className="px-6 py-4 bg-gray-50 border-t border-gray-100 animate-fade-in">
                <p className="text-gray-600 leading-relaxed font-body text-sm">{item.answer}</p>
              </div>
            )}
          </div>
        ))}
      </section>
    </>
  )
}
