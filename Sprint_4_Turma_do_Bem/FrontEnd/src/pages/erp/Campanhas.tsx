import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import type { Campanha } from '../../types'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import Badge from '../../components/Badge'
import EmptyState from '../../components/EmptyState'
import { formatCurrency } from '../../utils/formatters'

const mock: Campanha[] = [
  { id: 1, idCampanha: 1, nome: 'Sorriso Solidário Maio', descricao: 'Campanha maio/2025',    dataInicio: '2025-05-01', dataFim: '2025-05-31', metaValor: 20000, totalArrecadado: 12500, ativo: true },
  { id: 2, idCampanha: 2, nome: 'Dia das Mães TDB',       descricao: 'Campanha Dia das Mães', dataInicio: '2025-05-10', dataFim: '2025-05-12', metaValor: 10000, totalArrecadado: 8750,  ativo: true },
  { id: 3, idCampanha: 3, nome: 'Campanha Abril',          descricao: 'Campanha abril/2025',   dataInicio: '2025-04-01', dataFim: '2025-04-30', metaValor: 15000, totalArrecadado: 16200, ativo: false },
]
type FormData = { nome: string; descricao: string; dataInicio: string; dataFim: string; metaValor: number }

export default function Campanhas() {
  useEffect(() => { document.title = 'Campanhas | De Novo Não! ERP' }, [])
  const [lista,     setLista]     = useState<Campanha[]>(mock)
  const [search,    setSearch]    = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editing,   setEditing]   = useState<Campanha | null>(null)
  const { register, handleSubmit, reset, formState: { errors } } = useForm<FormData>()

  const filtered       = lista.filter(c => c.nome.toLowerCase().includes(search.toLowerCase()))
  const openNew        = () => { setEditing(null); reset(); setModalOpen(true) }
  const openEdit       = (c: Campanha) => { setEditing(c); reset({ nome: c.nome, descricao: c.descricao ?? '', dataInicio: c.dataInicio ?? '', dataFim: c.dataFim ?? '', metaValor: c.metaValor }); setModalOpen(true) }
  const handleEncerrar = (id: number) => { if (!confirm('Encerrar campanha?')) return; setLista(prev => prev.map(c => c.id === id ? { ...c, ativo: false } : c)) }
  const onSubmit       = (data: FormData) => {
    if (editing) setLista(prev => prev.map(c => c.id === editing.id ? { ...c, ...data } : c))
    else setLista(prev => [...prev, { ...data, id: Date.now(), idCampanha: Date.now(), totalArrecadado: 0, ativo: true }])
    setModalOpen(false)
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Campanhas</h1>
          <p className="text-gray-400 font-body text-sm">{lista.filter(c => c.ativo).length} campanhas ativas</p>
        </div>
        <Button onClick={openNew}>+ Nova Campanha</Button>
      </div>
      <input type="text" placeholder="Buscar campanha..." value={search} onChange={e => setSearch(e.target.value)} className={inputClass + ' max-w-sm'} />
      {filtered.length === 0 ? <EmptyState icon="📣" title="Nenhuma campanha encontrada" /> : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {filtered.map(c => {
            const pct = c.metaValor > 0 ? Math.min(100, Math.round((c.totalArrecadado / c.metaValor) * 100)) : 0
            return (
              <div key={c.id} className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
                <div className="flex items-start justify-between mb-2">
                  <h3 className="font-display font-bold text-base leading-tight" style={{ color: '#2d4a1e' }}>{c.nome}</h3>
                  <Badge label={c.ativo ? 'Ativa' : 'Encerrada'} className={c.ativo ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'} />
                </div>
                <p className="text-gray-400 text-xs font-body mb-4">{c.dataInicio} até {c.dataFim}</p>
                <div className="mb-1 flex justify-between text-xs font-body text-gray-500">
                  <span>{formatCurrency(c.totalArrecadado)}</span>
                  <span>meta: {formatCurrency(c.metaValor)}</span>
                </div>
                <div className="w-full bg-gray-100 rounded-full h-2 mb-1">
                  <div className="h-2 rounded-full transition-all" style={{ width: `${pct}%`, backgroundColor: '#7ab800' }} />
                </div>
                <p className="text-xs text-right font-semibold mb-4" style={{ color: '#2d4a1e' }}>{pct}%</p>
                <div className="flex gap-2">
                  <Button size="sm" variant="secondary" onClick={() => openEdit(c)}>Editar</Button>
                  {c.ativo && <Button size="sm" variant="danger" onClick={() => handleEncerrar(c.idCampanha)}>Encerrar</Button>}
                </div>
              </div>
            )
          })}
        </div>
      )}
      <Modal open={modalOpen} title={editing ? 'Editar Campanha' : 'Nova Campanha'} onClose={() => setModalOpen(false)} size="md">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="Nome" error={errors.nome?.message} required className="md:col-span-2">
            <input type="text" {...register('nome', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="Data início" error={errors.dataInicio?.message} required>
            <input type="date" {...register('dataInicio', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="Data fim" error={errors.dataFim?.message} required>
            <input type="date" {...register('dataFim', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="Meta (R$)">
            <input type="number" min={0} {...register('metaValor', { valueAsNumber: true })} className={inputClass} />
          </FormField>
          <FormField label="Descrição">
            <input type="text" {...register('descricao')} className={inputClass} />
          </FormField>
          <div className="md:col-span-2 flex justify-end gap-3 mt-2">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancelar</Button>
            <Button type="submit">{editing ? 'Salvar' : 'Criar'}</Button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
