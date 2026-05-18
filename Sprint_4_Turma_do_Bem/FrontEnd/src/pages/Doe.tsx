import { useState } from 'react'
import { Link } from 'react-router-dom'

const GREEN  = '#7ab800'
const DARK   = '#2d4a1e'
const ORANGE = '#f5821f'

const valoresPredefinidos = [25, 50, 100, 250]

const formas = [
  { id: 'pix',           label: 'PIX'          },
  { id: 'cartao',        label: 'Cartão'        },
  { id: 'boleto',        label: 'Boleto'        },
  { id: 'transferencia', label: 'Transferência' },
]

const impactos = [
  { valor: 25,  desc: 'Kit de higiene bucal completo para 1 paciente' },
  { valor: 50,  desc: 'Consulta de avaliação odontológica' },
  { valor: 100, desc: 'Tratamento completo de cárie' },
  { valor: 250, desc: 'Prótese dentária para adulto em vulnerabilidade' },
]

export default function Doe() {
  const [valor,     setValor]     = useState<number | ''>(50)
  const [valorCustom, setValorCustom] = useState('')
  const [forma,     setForma]     = useState('pix')
  const [nome,      setNome]      = useState('')
  const [email,     setEmail]     = useState('')
  const [cpf,       setCpf]       = useState('')
  const [anonimo,   setAnonimo]   = useState(false)
  const [recorrente, setRecorrente] = useState(false)
  const [enviado,   setEnviado]   = useState(false)
  const [loading,   setLoading]   = useState(false)
  const [erro,      setErro]      = useState('')

  const valorFinal = valorCustom ? parseFloat(valorCustom) : (valor || 0)

  const impactoAtual = impactos.reduce((prev, curr) =>
    Math.abs(curr.valor - valorFinal) < Math.abs(prev.valor - valorFinal) ? curr : prev
  )

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setErro('')

    if (valorFinal <= 0) { setErro('Informe um valor válido.'); return }
    if (!anonimo && (!nome.trim() || !email.trim())) {
      setErro('Preencha nome e e-mail ou marque doação anônima.'); return
    }

    setLoading(true)
    await new Promise(r => setTimeout(r, 1500)) // simulação
    setLoading(false)
    setEnviado(true)
  }

  if (enviado) {
    return (
      <div className="min-h-screen flex items-center justify-center px-6"
        style={{ backgroundColor: '#f4f9ec' }}>
        <div className="max-w-md w-full text-center">
          <div className="text-6xl mb-6">🎉</div>
          <h1 className="font-display font-extrabold text-3xl mb-3" style={{ color: DARK }}>
            Obrigado pela sua doação!
          </h1>
          <p className="text-gray-500 mb-2">
            Recebemos sua contribuição de{' '}
            <strong style={{ color: GREEN }}>
              R$ {valorFinal.toFixed(2).replace('.', ',')}
            </strong>
            {recorrente && ' (mensal)'}.
          </p>
          <p className="text-gray-400 text-sm mb-8">
            Você receberá a confirmação em <strong>{email || 'seu e-mail'}</strong> em breve.
          </p>
          <div className="flex flex-col sm:flex-row gap-3 justify-center">
            <Link to="/"
              className="px-6 py-3 rounded-full font-semibold text-sm text-white"
              style={{ backgroundColor: GREEN }}>
              Voltar ao site
            </Link>
            <button onClick={() => { setEnviado(false); setValor(50); setNome(''); setEmail('') }}
              className="px-6 py-3 rounded-full font-semibold text-sm border"
              style={{ borderColor: GREEN, color: GREEN }}>
              Fazer outra doação
            </button>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen" style={{ backgroundColor: '#f4f9ec' }}>

      {/* Header */}
      <header className="bg-white border-b border-gray-100 sticky top-0 z-10">
        <div className="max-w-5xl mx-auto px-6 py-4 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2">
            <img src="/image/logo-dnn.png" alt="De Novo Não!" className="h-8"
              style={{ mixBlendMode: 'multiply' }} />
            <span className="font-display font-bold text-sm hidden sm:block" style={{ color: DARK }}>
              De Novo Não!
            </span>
          </Link>
          <Link to="/"
            className="text-sm font-body text-gray-400 hover:text-gray-600 transition-colors">
            ← Voltar ao site
          </Link>
        </div>
      </header>

      <div className="max-w-5xl mx-auto px-6 py-12">

        {/* Título */}
        <div className="text-center mb-12">
          <span className="inline-block text-xs font-semibold px-3 py-1 rounded-full mb-4"
            style={{ backgroundColor: '#fff', color: GREEN, border: `1px solid ${GREEN}` }}>
            Doação segura
          </span>
          <h1 className="font-display font-extrabold text-4xl mb-3" style={{ color: DARK }}>
            Doe e transforme um sorriso
          </h1>
          <p className="text-gray-500 max-w-lg mx-auto text-base leading-relaxed">
            Sua contribuição garante atendimento odontológico gratuito para jovens
            em situação de vulnerabilidade social em todo o Brasil.
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-5 gap-8">

          {/* Formulário */}
          <form onSubmit={handleSubmit} className="lg:col-span-3 space-y-6">

            {/* Valor */}
            <div className="bg-white rounded-2xl p-6 border border-gray-100">
              <h2 className="font-display font-bold text-lg mb-4" style={{ color: DARK }}>
                1. Escolha o valor
              </h2>
              <div className="grid grid-cols-4 gap-2 mb-4">
                {valoresPredefinidos.map(v => (
                  <button key={v} type="button"
                    onClick={() => { setValor(v); setValorCustom('') }}
                    className="py-3 rounded-xl font-semibold text-sm border-2 transition-all"
                    style={{
                      borderColor: valor === v && !valorCustom ? GREEN : '#e5e7eb',
                      backgroundColor: valor === v && !valorCustom ? '#f4f9ec' : '#fff',
                      color: valor === v && !valorCustom ? DARK : '#6b7280',
                    }}>
                    R$ {v}
                  </button>
                ))}
              </div>
              <div className="relative">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-sm font-semibold">
                  R$
                </span>
                <input
                  type="number" min="1" placeholder="Outro valor"
                  value={valorCustom}
                  onChange={e => { setValorCustom(e.target.value); setValor('') }}
                  className="w-full border-2 rounded-xl pl-9 pr-4 py-3 text-sm focus:outline-none transition-colors"
                  style={{ borderColor: valorCustom ? GREEN : '#e5e7eb' }}
                />
              </div>

              {/* Recorrente */}
              <label className="flex items-center gap-3 mt-4 p-3 rounded-xl cursor-pointer border border-gray-100 hover:bg-gray-50 transition-colors">
                <input type="checkbox" checked={recorrente}
                  onChange={e => setRecorrente(e.target.checked)} className="rounded" />
                <div>
                  <p className="text-sm font-semibold" style={{ color: DARK }}>Doação mensal recorrente</p>
                  <p className="text-xs text-gray-400">Cancele quando quiser, sem burocracia.</p>
                </div>
              </label>
            </div>

            {/* Forma de pagamento */}
            <div className="bg-white rounded-2xl p-6 border border-gray-100">
              <h2 className="font-display font-bold text-lg mb-4" style={{ color: DARK }}>
                2. Forma de pagamento
              </h2>
              <div className="grid grid-cols-2 gap-2">
                {formas.map(f => (
                  <button key={f.id} type="button"
                    onClick={() => setForma(f.id)}
                    className="flex items-center gap-2 p-3 rounded-xl border-2 transition-all text-sm font-semibold"
                    style={{
                      borderColor: forma === f.id ? GREEN : '#e5e7eb',
                      backgroundColor: forma === f.id ? '#f4f9ec' : '#fff',
                      color: forma === f.id ? DARK : '#6b7280',
                    }}>
                    {f.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Dados pessoais */}
            <div className="bg-white rounded-2xl p-6 border border-gray-100">
              <div className="flex items-center justify-between mb-4">
                <h2 className="font-display font-bold text-lg" style={{ color: DARK }}>
                  3. Seus dados
                </h2>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input type="checkbox" checked={anonimo}
                    onChange={e => setAnonimo(e.target.checked)} className="rounded" />
                  <span className="text-xs text-gray-400">Doação anônima</span>
                </label>
              </div>

              {!anonimo && (
                <div className="space-y-3">
                  <input type="text" placeholder="Nome completo" value={nome}
                    onChange={e => setNome(e.target.value)}
                    className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:border-green-400 transition-colors" />
                  <input type="email" placeholder="E-mail" value={email}
                    onChange={e => setEmail(e.target.value)}
                    className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:border-green-400 transition-colors" />
                  <input type="text" placeholder="CPF (opcional)" value={cpf}
                    onChange={e => setCpf(e.target.value.replace(/\D/g, '').slice(0, 11))}
                    className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:border-green-400 transition-colors" />
                </div>
              )}

              {anonimo && (
                <p className="text-sm text-gray-400 text-center py-2">
                  Sua identidade não será divulgada. 💚
                </p>
              )}
            </div>

            {erro && (
              <div className="flex items-center gap-2 p-3 rounded-xl text-sm"
                style={{ backgroundColor: '#fef2f2', color: '#dc2626' }}>
                <span>⚠️</span> {erro}
              </div>
            )}

            <button type="submit" disabled={loading}
              className="w-full py-4 rounded-2xl font-bold text-base text-white
                         hover:opacity-90 transition-all disabled:opacity-50"
              style={{ backgroundColor: ORANGE }}>
              {loading
                ? 'Processando...'
                : `Doar R$ ${valorFinal > 0 ? valorFinal.toFixed(2).replace('.', ',') : '0,00'}${recorrente ? '/mês' : ''}`}
            </button>

            <p className="text-center text-xs text-gray-400">
              🔒 Pagamento 100% seguro · Dados criptografados · Certificado SSL
            </p>

          </form>

          {/* Painel lateral */}
          <div className="lg:col-span-2 space-y-4">

            {/* Impacto */}
            <div className="bg-white rounded-2xl p-6 border border-gray-100">
              <h3 className="font-display font-bold text-base mb-4" style={{ color: DARK }}>
                Seu impacto com R$ {valorFinal > 0 ? valorFinal.toFixed(0) : '0'}
              </h3>
              <div className="p-3 rounded-xl text-sm" style={{ backgroundColor: '#f4f9ec', color: DARK }}>
                <span className="text-2xl">🦷</span>
                <p className="mt-2 font-medium">{impactoAtual.desc}</p>
              </div>
            </div>

            {/* Stats */}
            <div className="bg-white rounded-2xl p-6 border border-gray-100">
              <h3 className="font-display font-bold text-base mb-4" style={{ color: DARK }}>
                Turma do Bem em números
              </h3>
              <div className="space-y-3">
                {[
                  { num: '2 milhões+', label: 'sorrisos transformados' },
                  { num: '7 mil+',     label: 'dentistas voluntários' },
                  { num: '400+',       label: 'cidades atendidas' },
                  { num: '27 anos',    label: 'de impacto social' },
                ].map(s => (
                  <div key={s.label} className="flex items-center gap-3">
                    <span className="font-display font-extrabold text-base w-20 shrink-0"
                      style={{ color: GREEN }}>{s.num}</span>
                    <span className="text-sm text-gray-400">{s.label}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Segurança */}
            <div className="rounded-2xl p-4 text-xs text-gray-400 space-y-2"
              style={{ backgroundColor: '#fff', border: '1px solid #e5e7eb' }}>
              <p className="font-semibold text-gray-500">Por que confiar?</p>
              <p>✅ CNPJ verificado — Turma do Bem</p>
              <p>✅ Certificado de entidade filantrópica</p>
              <p>✅ Relatórios de transparência públicos</p>
              <p>✅ Recibo de doação por e-mail</p>
            </div>

          </div>
        </div>
      </div>
    </div>
  )
}
