import { useEffect, useState } from 'react'
import type { Consulta, StatusConsulta } from '../../types'
import { useFormState } from '../../hooks/useFormState'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import Badge from '../../components/Badge'
import EmptyState from '../../components/EmptyState'
import { statusColor, statusLabel, formatDate } from '../../utils/formatters'
import { useFeriados } from '../../hooks/useFeriados'

const mock: Consulta[] = [
  { idConsulta: 1, idPaciente: 1, nomePaciente: 'Maria Oliveira', idDentista: 1, nomeDentista: 'Dr. João Silva',  dtConsulta: '2025-04-18', status: 'REALIZADA', observacoes: 'Limpeza concluída.' },
  { idConsulta: 2, idPaciente: 2, nomePaciente: 'Pedro Santos',   idDentista: 2, nomeDentista: 'Dra. Ana Costa',  dtConsulta: '2025-04-18', status: 'AGENDADA',  observacoes: '' },
  { idConsulta: 3, idPaciente: 3, nomePaciente: 'Lucia Ferreira', idDentista: 3, nomeDentista: 'Dr. Carlos Lima', dtConsulta: '2025-04-17', status: 'FALTA',      observacoes: 'Paciente não compareceu.' },
  { idConsulta: 4, idPaciente: 1, nomePaciente: 'Maria Oliveira', idDentista: 2, nomeDentista: 'Dra. Ana Costa',  dtConsulta: '2025-04-10', status: 'REALIZADA',  observacoes: 'Extração realizada.' },
]

const statusOptions: StatusConsulta[] = ['AGENDADA', 'REALIZADA', 'CANCELADA', 'FALTA']
const INICIAL = { idPaciente: 0, nomePaciente: '', idDentista: 0, nomeDentista: '', dtConsulta: '', status: 'AGENDADA' as StatusConsulta, observacoes: '' }

export default function Consultas() {
  useEffect(() => { document.title = 'Consultas | De Novo Não! ERP' }, [])

  const [consultas,    setConsultas]    = useState<Consulta[]>(mock)
  const [search,       setSearch]       = useState('')
  const [filterStatus, setFilter]       = useState<StatusConsulta | ''>('')
  const [modalOpen,    setModalOpen]    = useState(false)
  const [editing,      setEditing]      = useState<Consulta | null>(null)
  const [dataSelecionada, setDataSelecionada] = useState('')

  const { valores, erros, onChange, reset, validar } = useFormState(INICIAL)

  const anoSelecionado = dataSelecionada
    ? new Date(dataSelecionada + 'T00:00:00').getFullYear()
    : new Date().getFullYear()

  const { eFeriado, loading: loadingFeriados } = useFeriados(anoSelecionado)
  const feriadoAviso = dataSelecionada ? eFeriado(dataSelecionada) : null

  const filtered = consultas.filter(c => {
    const matchSearch = c.nomePaciente.toLowerCase().includes(search.toLowerCase()) ||
      c.nomeDentista.toLowerCase().includes(search.toLowerCase())
    const matchStatus = filterStatus ? c.status === filterStatus : true
    return matchSearch && matchStatus
  })

  const openNew  = () => { setEditing(null); reset(); setDataSelecionada(''); setModalOpen(true) }
  const openEdit = (c: Consulta) => {
    setEditing(c)
    reset({ idPaciente: c.idPaciente, nomePaciente: c.nomePaciente,
            idDentista: c.idDentista, nomeDentista: c.nomeDentista,
            dtConsulta: c.dtConsulta, status: c.status, observacoes: c.observacoes ?? '' })
    setDataSelecionada(c.dtConsulta ?? '')
    setModalOpen(true)
  }
  const handleDelete = (id: number) => {
    if (!confirm('Cancelar esta consulta?')) return
    setConsultas(prev => prev.map(c => c.idConsulta === id ? { ...c, status: 'CANCELADA' } : c))
  }

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!validar({ nomePaciente: 'Obrigatório', nomeDentista: 'Obrigatório', dtConsulta: 'Obrigatório' })) return
    if (editing) {
      setConsultas(prev => prev.map(c => c.idConsulta === editing.idConsulta ? { ...c, ...valores } : c))
    } else {
      setConsultas(prev => [...prev, { ...valores, idConsulta: Date.now() }])
    }
    setModalOpen(false)
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Consultas</h1>
          <p className="text-gray-400 font-body text-sm">{consultas.length} consultas registradas</p>
        </div>
        <Button onClick={openNew}>+ Nova Consulta</Button>
      </div>

      <div className="flex flex-col sm:flex-row gap-3">
        <input type="text" placeholder="Buscar por paciente ou dentista..." value={search}
          onChange={e => setSearch(e.target.value)} className={inputClass + ' flex-1'} />
        <select value={filterStatus} onChange={e => setFilter(e.target.value as StatusConsulta | '')} className={inputClass + ' sm:w-48'}>
          <option value="">Todos os status</option>
          {statusOptions.map(s => <option key={s} value={s}>{statusLabel[s]}</option>)}
        </select>
      </div>

      {/* Aviso de consultas agendadas em feriados */}
      {(() => {
        const agendadasEmFeriado = consultas.filter(c => c.status === 'AGENDADA' && eFeriado(c.dtConsulta) !== null)
        if (agendadasEmFeriado.length === 0) return null
        return (
          <div className="bg-yellow-50 border border-yellow-200 rounded-xl px-4 py-3 flex gap-3 items-start">
            <span className="text-xl">⚠️</span>
            <div>
              <p className="font-semibold text-yellow-800 text-sm">Consultas agendadas em feriados nacionais</p>
              <ul className="mt-1 space-y-0.5">
                {agendadasEmFeriado.map(c => {
                  const f = eFeriado(c.dtConsulta)!
                  return (
                    <li key={c.idConsulta} className="text-yellow-700 text-xs">
                      • {c.nomePaciente} — {formatDate(c.dtConsulta)} ({f.name})
                    </li>
                  )
                })}
              </ul>
            </div>
          </div>
        )
      })()}

      {filtered.length === 0 ? <EmptyState icon="📅" title="Nenhuma consulta encontrada" /> : (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm font-body">
              <thead className="bg-gray-50 border-b border-gray-100">
                <tr className="text-left text-gray-400">
                  <th className="px-6 py-4 font-semibold">Paciente</th>
                  <th className="px-6 py-4 font-semibold hidden sm:table-cell">Dentista</th>
                  <th className="px-6 py-4 font-semibold hidden md:table-cell">Data</th>
                  <th className="px-6 py-4 font-semibold">Status</th>
                  <th className="px-6 py-4 font-semibold">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map(c => {
                  const feriado = eFeriado(c.dtConsulta)
                  return (
                    <tr key={c.idConsulta} className="hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4 font-medium text-gray-800">{c.nomePaciente}</td>
                      <td className="px-6 py-4 text-gray-500 hidden sm:table-cell">{c.nomeDentista}</td>
                      <td className="px-6 py-4 text-gray-500 hidden md:table-cell">
                        <span>{formatDate(c.dtConsulta)}</span>
                        {feriado && c.status === 'AGENDADA' && (
                          <span className="ml-2 text-xs bg-yellow-100 text-yellow-700 px-2 py-0.5 rounded-full"
                            title={feriado.name}>
                            🎉 Feriado
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4">
                        <Badge label={statusLabel[c.status]} className={statusColor[c.status]} />
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex gap-2">
                          <Button size="sm" variant="secondary" onClick={() => openEdit(c)}>Editar</Button>
                          {c.status === 'AGENDADA' && (
                            <Button size="sm" variant="danger" onClick={() => handleDelete(c.idConsulta)}>Cancelar</Button>
                          )}
                        </div>
                      </td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      <Modal open={modalOpen} title={editing ? 'Editar Consulta' : 'Nova Consulta'}
        onClose={() => setModalOpen(false)} size="lg">
        <form onSubmit={onSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="Paciente" error={erros.nomePaciente} required>
            <input type="text" value={valores.nomePaciente} onChange={onChange('nomePaciente')} className={inputClass} placeholder="Nome do paciente" />
          </FormField>
          <FormField label="Dentista" error={erros.nomeDentista} required>
            <input type="text" value={valores.nomeDentista} onChange={onChange('nomeDentista')} className={inputClass} placeholder="Nome do dentista" />
          </FormField>
          <FormField label="Data da consulta" error={erros.dtConsulta} required>
            <input type="date" value={valores.dtConsulta}
              onChange={e => { onChange('dtConsulta')(e); setDataSelecionada(e.target.value) }}
              className={inputClass} />
            {loadingFeriados && dataSelecionada && (
              <p className="text-xs text-gray-400 mt-1">Verificando feriados...</p>
            )}
            {feriadoAviso && !loadingFeriados && (
              <div className="mt-2 bg-yellow-50 border border-yellow-200 rounded-lg px-3 py-2 flex gap-2 items-center">
                <span>🎉</span>
                <p className="text-xs text-yellow-700">
                  <strong>{feriadoAviso.name}</strong> — esta data é um feriado nacional.
                  Confirme se a consulta será mantida.
                </p>
              </div>
            )}
          </FormField>
          <FormField label="Status">
            <select value={valores.status} onChange={onChange('status')} className={inputClass}>
              {statusOptions.map(s => <option key={s} value={s}>{statusLabel[s]}</option>)}
            </select>
          </FormField>
          <FormField label="Observações" className="md:col-span-2">
            <textarea rows={3} value={valores.observacoes} onChange={onChange('observacoes')}
              className={inputClass + ' resize-none'} placeholder="Observações clínicas..." />
          </FormField>
          <div className="md:col-span-2 flex justify-end gap-3 mt-2">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancelar</Button>
            <Button type="submit">{editing ? 'Salvar' : 'Agendar'}</Button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
