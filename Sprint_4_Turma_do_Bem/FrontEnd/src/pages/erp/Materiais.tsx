import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import type { Material } from '../../types'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import Badge from '../../components/Badge'
import EmptyState from '../../components/EmptyState'

const mock: Material[] = [
  { id: 1, idMaterial: 1, nome: 'Luva cirúrgica',   descricao: 'Caixa c/ 100un', quantidade: 8,  quantidadeMinima: 20, unidade: 'cx', validade: '2026-12-31' },
  { id: 2, idMaterial: 2, nome: 'Anestésico local',  descricao: 'Caixa c/ 50amp', quantidade: 2,  quantidadeMinima: 10, unidade: 'cx', validade: '2025-09-30' },
  { id: 3, idMaterial: 3, nome: 'Escova de dente',   descricao: 'Unidade',        quantidade: 3,  quantidadeMinima: 30, unidade: 'un', validade: '2027-06-30' },
  { id: 4, idMaterial: 4, nome: 'Fio dental',        descricao: 'Rolo 50m',       quantidade: 45, quantidadeMinima: 20, unidade: 'rl', validade: '2027-01-15' },
  { id: 5, idMaterial: 5, nome: 'Máscara cirúrgica', descricao: 'Caixa c/ 50un',  quantidade: 12, quantidadeMinima: 10, unidade: 'cx', validade: '2026-03-01' },
]
type FormData = Omit<Material, 'id' | 'idMaterial'>

export default function Materiais() {
  useEffect(() => { document.title = 'Materiais | De Novo Não! ERP' }, [])
  const [lista,     setLista]     = useState<Material[]>(mock)
  const [search,    setSearch]    = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editing,   setEditing]   = useState<Material | null>(null)
  const { register, handleSubmit, reset, formState: { errors } } = useForm<FormData>()

  const estoqueBaixo = lista.filter(m => m.quantidade < m.quantidadeMinima).length
  const filtered     = lista.filter(m => m.nome.toLowerCase().includes(search.toLowerCase()))
  const openNew      = () => { setEditing(null); reset(); setModalOpen(true) }
  const openEdit     = (m: Material) => { setEditing(m); reset({ nome: m.nome, descricao: m.descricao, quantidade: m.quantidade, quantidadeMinima: m.quantidadeMinima, unidade: m.unidade, validade: m.validade }); setModalOpen(true) }
  const handleDelete = (id: number) => { if (!confirm('Remover?')) return; setLista(prev => prev.filter(m => m.id !== id)) }
  const onSubmit     = (data: FormData) => {
    if (editing) setLista(prev => prev.map(m => m.id === editing.id ? { ...m, ...data } : m))
    else setLista(prev => [...prev, { ...data, id: Date.now(), idMaterial: Date.now() }])
    setModalOpen(false)
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Materiais</h1>
          <p className="text-gray-400 font-body text-sm">
            {lista.length} itens cadastrados
            {estoqueBaixo > 0 && <span className="text-red-500 font-semibold"> · ⚠️ {estoqueBaixo} com estoque baixo</span>}
          </p>
        </div>
        <Button onClick={openNew}>+ Novo Material</Button>
      </div>
      <input type="text" placeholder="Buscar por nome..." value={search} onChange={e => setSearch(e.target.value)} className={inputClass + ' max-w-sm'} />
      {filtered.length === 0 ? <EmptyState icon="🧰" title="Nenhum material encontrado" /> : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {filtered.map(m => {
            const baixo = m.quantidade < m.quantidadeMinima
            return (
              <div key={m.id} className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
                <div className="flex items-start justify-between mb-3">
                  <h3 className="font-display font-bold text-base leading-tight" style={{ color: '#2d4a1e' }}>{m.nome}</h3>
                  {baixo && <Badge label="Baixo" className="bg-red-100 text-red-600 shrink-0 ml-2" />}
                </div>
                <div className="flex justify-between items-end">
                  <div>
                    <p className={`font-display font-extrabold text-3xl ${baixo ? 'text-red-500' : 'text-green-600'}`}>{m.quantidade}</p>
                    <p className="text-xs text-gray-400">{m.unidade} · mín. {m.quantidadeMinima}</p>
                  </div>
                  <div className="flex gap-2">
                    <Button size="sm" variant="secondary" onClick={() => openEdit(m)}>Editar</Button>
                    <Button size="sm" variant="danger"    onClick={() => handleDelete(m.id)}>Remover</Button>
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      )}
      <Modal open={modalOpen} title={editing ? 'Editar Material' : 'Novo Material'} onClose={() => setModalOpen(false)} size="md">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="Nome" error={errors.nome?.message} required>
            <input type="text" {...register('nome', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="Unidade">
            <input type="text" {...register('unidade')} className={inputClass} placeholder="cx, un, rl..." />
          </FormField>
          <FormField label="Quantidade">
            <input type="number" min={0} {...register('quantidade', { valueAsNumber: true })} className={inputClass} />
          </FormField>
          <FormField label="Qtd. mínima">
            <input type="number" min={0} {...register('quantidadeMinima', { valueAsNumber: true })} className={inputClass} />
          </FormField>
          <FormField label="Validade">
            <input type="date" {...register('validade')} className={inputClass} />
          </FormField>
          <FormField label="Descrição">
            <input type="text" {...register('descricao')} className={inputClass} />
          </FormField>
          <div className="md:col-span-2 flex justify-end gap-3 mt-2">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancelar</Button>
            <Button type="submit">{editing ? 'Salvar' : 'Cadastrar'}</Button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
