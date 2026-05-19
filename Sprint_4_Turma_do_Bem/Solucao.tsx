import { useEffect, useState } from 'react'
import type { Prontuario } from '../../types'
import { useFormState } from '../../hooks/useFormState'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import EmptyState from '../../components/EmptyState'
import { IAService } from '../../services/api'
import type { PrevisaoFalta } from '../../types'
import { riscoColor } from '../../utils/formatters'

const mock: Prontuario[] = [
  { id: 1, idProntuario: 1, idConsulta: 1, nomePaciente: 'Maria Oliveira', nomeDentista: 'Dr. João Silva',  dtRegistro: '2025-04-18', descricao: 'Limpeza e remoção de tártaro realizada com sucesso.', observacoes: 'Retornar em 6 meses.' },
  { id: 2, idProntuario: 2, idConsulta: 4, nomePaciente: 'Maria Oliveira', nomeDentista: 'Dra. Ana Costa',  dtRegistro: '2025-04-10', descricao: 'Extração do dente 38 sem complicações.',                 observacoes: 'Prescrição de antibiótico.' },
  { id: 3, idProntuario: 3, idConsulta: 5, nomePaciente: 'Pedro Santos',   nomeDentista: 'Dr. Carlos Lima', dtRegistro: '2025-03-22', descricao: 'Restauração com resina composta no dente 16.',            observacoes: '' },
]

const INICIAL = { idConsulta: 0, nomePaciente: '', nomeDentista: '', descricao: '', observacoes: '' }

export default function Prontuarios() {
  useEffect(() => { document.title = 'Prontuários | De Novo Não! ERP' }, [])

  const [lista,         setLista]         = useState<Prontuario[]>(mock)
  const [search,        setSearch]        = useState('')
  const [modalOpen,     setModalOpen]     = useState(false)
  const [editing,       setEditing]       = useState<Prontuario | null>(null)
  const [previsaoFalta, setPrevisaoFalta] = useState<PrevisaoFalta | null>(null)
  const [loadingIA,     setLoadingIA]     = useState(false)
  const [erroIA,        setErroIA]        = useState<string | null>(null)
  const { valores, erros, onChange, reset, validar } = useFormState(INICIAL)

  const filtered = lista.filter(p =>
    p.nomePaciente.toLowerCase().includes(search.toLowerCase()) ||
    p.nomeDentista.toLowerCase().includes(search.toLowerCase())
  )

  const openNew  = () => { setEditing(null); reset(); setModalOpen(true) }
  const openEdit = (p: Prontuario) => {
    setEditing(p)
    reset({ idConsulta: p.idConsulta, nomePaciente: p.nomePaciente,
            nomeDentista: p.nomeDentista, descricao: p.descricao, observacoes: p.observacoes ?? '' })
    setModalOpen(true)
  }
  const handleDelete = (id: number) => {
    if (!confirm('Excluir este prontuário?')) return
    setLista(prev => prev.filter(p => p.id !== id))
  }

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!validar({ nomePaciente: 'Obrigatório', nomeDentista: 'Obrigatório', descricao: 'Obrigatório' })) return
    if (editing) {
      setLista(prev => prev.map(p => p.id === editing.id ? { ...p, ...valores } : p))
    } else {
      setLista(prev => [...prev, { ...valores, id: Date.now(), idProntuario: Date.now(), dtRegistro: new Date().toISOString().split('T')[0] }])
    }
    setModalOpen(false)
  }

  const preverRisco = async () => {
    setLoadingIA(true); setErroIA(null)
    try {
      const res = await IAService.preverFalta({ distanciaKm: 10, faltasAnteriores: 1, diasAteConsulta: 5, rendaFamiliar: 1200, turno: 0 })
      setPrevisaoFalta(res as PrevisaoFalta)
    } catch {
      setPrevisaoFalta({ probabilidadeFalta: 0.42, risco: 'MEDIO', recomendacao: 'Enviar lembrete padrão' })
      setErroIA('API Python offline — resultado simulado.')
    } finally { setLoadingIA(false) }
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Prontuários</h1>
          <p className="text-gray-400 font-body text-sm">{lista.length} prontuários registrados</p>
        </div>
        <Button onClick={openNew}>+ Novo Prontuário</Button>
      </div>

      <input type="text" placeholder="Buscar por paciente ou dentista..." value={search}
        onChange={e => setSearch(e.target.value)} className={inputClass + ' max-w-sm'} />

      {filtered.length === 0 ? <EmptyState icon="📋" title="Nenhum prontuário encontrado" /> : (
        <div className="space-y-4">
          {filtered.map(p => (
            <div key={p.id} className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
              <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-3 mb-3">
                <div>
                  <p className="font-display font-bold text-base" style={{ color: '#2d4a1e' }}>{p.nomePaciente}</p>
                  <p className="text-sm text-gray-400 font-body">{p.nomeDentista} · {p.dtRegistro}</p>
                </div>
                <div className="flex gap-2">
                  <Button size="sm" variant="secondary" onClick={() => openEdit(p)}>Editar</Button>
                  <Button size="sm" variant="danger" onClick={() => handleDelete(p.id)}>Excluir</Button>
                </div>
              </div>
              <p className="text-gray-700 text-sm font-body leading-relaxed">{p.descricao}</p>
              {p.observacoes && <p className="text-gray-400 text-xs font-body mt-2">Obs: {p.observacoes}</p>}
            </div>
          ))}
        </div>
      )}

      {/* Previsão de risco de falta */}
      <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
        <div className="flex items-center justify-between mb-4">
          <h2 className="font-display font-bold text-lg" style={{ color: '#2d4a1e' }}>Risco de Falta — IA</h2>
          <Button size="sm" onClick={preverRisco} disabled={loadingIA}>
            {loadingIA ? 'Calculando...' : 'Verificar Risco'}
          </Button>
        </div>
        {previsaoFalta && (
          <div className="flex items-center gap-6">
            <span className={`font-display font-extrabold text-4xl ${riscoColor[previsaoFalta.risco]}`}>
              {(previsaoFalta.probabilidadeFalta * 100).toFixed(0)}%
            </span>
            <div>
              <p className="font-body text-sm text-gray-500">Risco: <strong className={riscoColor[previsaoFalta.risco]}>{previsaoFalta.risco}</strong></p>
              <p className="font-body text-sm text-gray-500">{previsaoFalta.recomendacao}</p>
            </div>
          </div>
        )}
        {erroIA && <p className="text-xs text-yellow-600 font-body mt-2">{erroIA}</p>}
      </div>

      <Modal open={modalOpen} title={editing ? 'Editar Prontuário' : 'Novo Prontuário'} onClose={() => setModalOpen(false)} size="lg">
        <form onSubmit={onSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <FormField label="Paciente" error={erros.nomePaciente} required>
              <input type="text" value={valores.nomePaciente} onChange={onChange('nomePaciente')} className={inputClass} />
            </FormField>
            <FormField label="Dentista" error={erros.nomeDentista} required>
              <input type="text" value={valores.nomeDentista} onChange={onChange('nomeDentista')} className={inputClass} />
            </FormField>
          </div>
          <FormField label="Descrição do atendimento" error={erros.descricao} required>
            <textarea rows={4} value={valores.descricao} onChange={onChange('descricao')} className={inputClass + ' resize-none'} placeholder="Descreva o procedimento realizado..." />
          </FormField>
          <FormField label="Observações / Prescrições">
            <textarea rows={2} value={valores.observacoes} onChange={onChange('observacoes')} className={inputClass + ' resize-none'} placeholder="Medicamentos, retorno, cuidados..." />
          </FormField>
          <div className="flex justify-end gap-3">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancelar</Button>
            <Button type="submit">{editing ? 'Salvar' : 'Registrar'}</Button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
