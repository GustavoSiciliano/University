import { useForm } from 'react-hook-form'
import { useEffect, useState } from 'react'
import { IAService } from '../../services/api'
import type { PrevisaoFalta, PrevisaoArrecadacao } from '../../types'
import Button from '../../components/Button'
import Card from '../../components/Card'
import { riscoColor, formatCurrency } from '../../utils/formatters'

// ─── Form types ──────────────────────────────────────────────────────────────
interface FaltaForm {
  distanciaKm: number
  faltasAnteriores: number
  diasAteConsulta: number
  rendaFamiliar: number
  turno: number
}

interface ArrecadacaoForm {
  duracaoDias: number
  metaValor: number
  campanhasAnteriores: number
  mesDoAno: number
}

export default function IA() {
  useEffect(() => { document.title = 'IA | De Novo Não! ERP' }, [])

  // ─── Health check ─────────────────────────────────────────────────────────
  const [apiOnline, setApiOnline] = useState<boolean | null>(null)

  useEffect(() => {
    IAService.health()
      .then(() => setApiOnline(true))
      .catch(() => setApiOnline(false))
  }, [])

  // ─── Previsão de Falta ────────────────────────────────────────────────────
  const [previsaoFalta, setPrevisaoFalta]         = useState<PrevisaoFalta | null>(null)
  const [loadingFalta, setLoadingFalta]           = useState(false)
  const [errorFalta, setErrorFalta]               = useState<string | null>(null)

  const faltaForm = useForm<FaltaForm>({ defaultValues: { distanciaKm: 5, faltasAnteriores: 0, diasAteConsulta: 3, rendaFamiliar: 1500, turno: 0 } })

  const handlePreverFalta = faltaForm.handleSubmit(async (data) => {
    setLoadingFalta(true); setErrorFalta(null)
    try {
      const res = await IAService.preverFalta(data) as PrevisaoFalta
      setPrevisaoFalta(res)
    } catch {
      // Fallback mock quando a API Python ainda não está no ar
      const prob = Math.min(0.99, Math.max(0.01,
        (data.faltasAnteriores * 0.2) + (data.distanciaKm > 20 ? 0.3 : 0.1) + (data.diasAteConsulta > 7 ? 0.2 : 0.05)
      ))
      setPrevisaoFalta({
        probabilidadeFalta: prob,
        risco: prob > 0.6 ? 'ALTO' : prob > 0.3 ? 'MEDIO' : 'BAIXO',
        recomendacao: prob > 0.6 ? 'Enviar lembrete reforçado' : prob > 0.3 ? 'Lembrete padrão' : 'Sem ação necessária',
      })
      setErrorFalta('⚠️ API Python offline — resultado simulado localmente.')
    } finally { setLoadingFalta(false) }
  })

  // ─── Previsão de Arrecadação ──────────────────────────────────────────────
  const [previsaoArrec, setPrevisaoArrec]         = useState<PrevisaoArrecadacao | null>(null)
  const [loadingArrec, setLoadingArrec]           = useState(false)
  const [errorArrec, setErrorArrec]               = useState<string | null>(null)

  const arrecForm = useForm<ArrecadacaoForm>({ defaultValues: { duracaoDias: 30, metaValor: 15000, campanhasAnteriores: 3, mesDoAno: 5 } })

  const handlePreverArrecadacao = arrecForm.handleSubmit(async (data) => {
    setLoadingArrec(true); setErrorArrec(null)
    try {
      const res = await IAService.preverArrecadacao(data) as PrevisaoArrecadacao
      setPrevisaoArrec(res)
    } catch {
      const previsto = data.metaValor * (0.7 + Math.random() * 0.6)
      setPrevisaoArrec({
        valorPrevisto: previsto,
        confianca: 0.72,
        tendencia: previsto > data.metaValor ? 'ALTA' : previsto > data.metaValor * 0.8 ? 'ESTAVEL' : 'BAIXA',
      })
      setErrorArrec('⚠️ API Python offline — resultado simulado localmente.')
    } finally { setLoadingArrec(false) }
  })

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-tdb-teal text-3xl">Inteligência Artificial</h1>
          <p className="text-gray-400 font-body text-sm">Modelos preditivos de Machine Learning via Python + Flask</p>
        </div>
        <div className="flex items-center gap-2 text-sm font-body">
          <span className={`w-2.5 h-2.5 rounded-full ${apiOnline === true ? 'bg-green-500' : apiOnline === false ? 'bg-red-500' : 'bg-yellow-400 animate-pulse-slow'}`} />
          <span className={apiOnline === true ? 'text-green-600' : apiOnline === false ? 'text-red-500' : 'text-yellow-500'}>
            {apiOnline === true ? 'API Online' : apiOnline === false ? 'API Offline' : 'Verificando...'}
          </span>
        </div>
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-8">

        {/* ── Previsão de Falta ─────────────────────────────────────────────── */}
        <Card>
          <h2 className="font-display font-bold text-tdb-teal text-xl mb-5">🦷 Previsão de Falta em Consulta</h2>
          <form onSubmit={handlePreverFalta} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div><label className="text-xs text-gray-500 font-body block mb-1">Distância (km)</label><input type="number" step="0.1" min="0" value={faltaForm.distanciaKm} onChange={e=>setFaltaForm(p=>({...p,distanciaKm:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
              <div><label className="text-xs text-gray-500 font-body block mb-1">Faltas anteriores</label><input type="number" min="0" value={faltaForm.faltasAnteriores} onChange={e=>setFaltaForm(p=>({...p,faltasAnteriores:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
              <div><label className="text-xs text-gray-500 font-body block mb-1">Dias até a consulta</label><input type="number" min="0" value={faltaForm.diasAteConsulta} onChange={e=>setFaltaForm(p=>({...p,diasAteConsulta:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
              <div><label className="text-xs text-gray-500 font-body block mb-1">Renda familiar (R$)</label><input type="number" min="0" value={faltaForm.rendaFamiliar} onChange={e=>setFaltaForm(p=>({...p,rendaFamiliar:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
              <div><label className="text-xs text-gray-500 font-body block mb-1">Turno</label><select value={faltaForm.turno} onChange={e=>setFaltaForm(p=>({...p,turno:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none"><option value={0}>Manhã</option><option value={1}>Tarde</option><option value={2}>Noite</option></select></div>
            </div>
            <Button type="submit" disabled={loadingFalta} className="w-full">
              {loadingFalta ? 'Calculando...' : 'Prever risco de falta'}
            </Button>
          </form>

          {/* Resultado */}
          {previsaoFalta && (
            <div className="mt-6 bg-gray-50 rounded-2xl p-5 space-y-3 animate-fade-in">
              <div className="flex justify-between items-center">
                <span className="font-body text-sm text-gray-500">Probabilidade de falta</span>
                <span className={`font-display font-extrabold text-3xl ${riscoColor[previsaoFalta.risco]}`}>
                  {(previsaoFalta.probabilidadeFalta * 100).toFixed(0)}%
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className={`h-2 rounded-full transition-all duration-700 ${
                    previsaoFalta.risco === 'ALTO' ? 'bg-red-500' : previsaoFalta.risco === 'MEDIO' ? 'bg-yellow-400' : 'bg-green-500'
                  }`}
                  style={{ width: `${previsaoFalta.probabilidadeFalta * 100}%` }}
                />
              </div>
              <div className="flex justify-between text-sm font-body">
                <span>Risco: <strong className={riscoColor[previsaoFalta.risco]}>{previsaoFalta.risco}</strong></span>
                <span className="text-gray-500">{previsaoFalta.recomendacao}</span>
              </div>
            </div>
          )}
          {errorFalta && <p className="mt-3 text-xs text-yellow-600 font-body">{errorFalta}</p>}
        </Card>

        {/* ── Previsão de Arrecadação ───────────────────────────────────────── */}
        <Card>
          <h2 className="font-display font-bold text-tdb-teal text-xl mb-5">📣 Previsão de Arrecadação</h2>
          <form onSubmit={handlePreverArrecadacao} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div><label className="text-xs text-gray-500 font-body block mb-1">Duração (dias)</label><input type="number" min="1" value={arrecForm.duracaoDias} onChange={e=>setArrecForm(p=>({...p,duracaoDias:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
              <div><label className="text-xs text-gray-500 font-body block mb-1">Meta (R$)</label><input type="number" min="0" value={arrecForm.metaValor} onChange={e=>setArrecForm(p=>({...p,metaValor:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
              <div><label className="text-xs text-gray-500 font-body block mb-1">Campanhas anteriores</label><input type="number" min="0" value={arrecForm.campanhasAnteriores} onChange={e=>setArrecForm(p=>({...p,campanhasAnteriores:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
              <div><label className="text-xs text-gray-500 font-body block mb-1">Mês do ano (1-12)</label><input type="number" min="1" max="12" value={arrecForm.mesDoAno} onChange={e=>setArrecForm(p=>({...p,mesDoAno:+e.target.value}))} className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none" /></div>
            </div>
            <Button type="submit" disabled={loadingArrec} className="w-full">
              {loadingArrec ? 'Calculando...' : 'Prever arrecadação'}
            </Button>
          </form>

          {/* Resultado */}
          {previsaoArrec && (
            <div className="mt-6 bg-gray-50 rounded-2xl p-5 space-y-3 animate-fade-in">
              <div className="flex justify-between items-center">
                <span className="font-body text-sm text-gray-500">Valor previsto</span>
                <span className="font-display font-extrabold text-3xl text-tdb-green">
                  {formatCurrency(previsaoArrec.valorPrevisto)}
                </span>
              </div>
              <div className="flex justify-between text-sm font-body">
                <span>Confiança: <strong className="text-tdb-teal">{(previsaoArrec.confianca * 100).toFixed(0)}%</strong></span>
                <span className={`font-semibold ${
                  previsaoArrec.tendencia === 'ALTA' ? 'text-green-600'
                  : previsaoArrec.tendencia === 'BAIXA' ? 'text-red-500' : 'text-yellow-600'
                }`}>
                  Tendência: {previsaoArrec.tendencia === 'ALTA' ? '📈 Alta' : previsaoArrec.tendencia === 'BAIXA' ? '📉 Baixa' : '➡️ Estável'}
                </span>
              </div>
            </div>
          )}
          {errorArrec && <p className="mt-3 text-xs text-yellow-600 font-body">{errorArrec}</p>}
        </Card>

      </div>
    </div>
  )
}
