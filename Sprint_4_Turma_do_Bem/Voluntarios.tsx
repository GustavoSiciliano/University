import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import type { Dentista } from '../../types'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import Badge from '../../components/Badge'
import EmptyState from '../../components/EmptyState'
import { formatCPF } from '../../utils/formatters'

const mock: Dentista[] = [
  { id: 1, idDentista: 1, nome: 'Dr. João Silva',  cpf: '11122233344', email: 'joao@dent.com',   telefone: '11988880001', dataNascimento: '1975-02-14', cro: 'SP-12345', especialidade: 'Clínico Geral', ativo: true },
  { id: 2, idDentista: 2, nome: 'Dra. Ana Costa',  cpf: '22233344455', email: 'ana@dent.com',    telefone: '11988880002', dataNascimento: '1982-07-30', cro: 'SP-67890', especialidade: 'Ortodontia',     ativo: true },
  { id: 3, idDentista: 3, nome: 'Dr. Carlos Lima', cpf: '33344455566', email: 'carlos@dent.com', telefone: '11988880003', dataNascimento: '1979-11-05', cro: 'SP-11111', especialidade: 'Periodontia',    ativo: true },
]
type FormData = Omit<Dentista, 'id' | 'idDentista' | 'ativo'>

export default function Dentistas() {
  useEffect(() => { document.title = 'Dentistas | De Novo Não! ERP' }, [])
  const [dentistas, setDentistas] = useState<Dentista[]>(mock)
  const [search,    setSearch]    = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editing,   setEditing]   = useState<Dentista | null>(null)
  const { register, handleSubmit, reset, formState: { errors } } = useForm<FormData>()

  const filtered = dentistas.filter(d => d.nome.toLowerCase().includes(search.toLowerCase()) || d.cro.includes(search))
  const openNew  = () => { setEditing(null); reset(); setModalOpen(true) }
  const openEdit = (d: Dentista) => { setEditing(d); reset({ nome: d.nome, cpf: d.cpf, email: d.email, telefone: d.telefone, dataNascimento: d.dataNascimento, cro: d.cro, especialidade: d.especialidade }); setModalOpen(true) }
  const handleDelete = (id: number) => { if (!confirm('Desativar?')) return; setDentistas(prev => prev.map(d => d.id === id ? { ...d, ativo: false } : d)) }
  const handleAtivar = (id: number) => setDentistas(prev => prev.map(d => d.id === id ? { ...d, ativo: true } : d))
  const onSubmit = (data: FormData) => {
    if (editing) setDentistas(prev => prev.map(d => d.id === editing.id ? { ...d, ...data } : d))
    else setDentistas(prev => [...prev, { ...data, id: Date.now(), idDentista: Date.now(), ativo: true }])
    setModalOpen(false)
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Dentistas</h1>
          <p className="text-gray-400 font-body text-sm">{dentistas.filter(d => d.ativo).length} dentistas voluntários ativos</p>
        </div>
        <Button onClick={openNew}>+ Novo Dentista</Button>
      </div>
      <input type="text" placeholder="Buscar por nome ou CRO..." value={search} onChange={e => setSearch(e.target.value)} className={inputClass + ' max-w-sm'} />
      {filtered.length === 0 ? <EmptyState icon="👨‍⚕️" title="Nenhum dentista encontrado" /> : (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm font-body">
              <thead className="bg-gray-50 border-b border-gray-100">
                <tr className="text-left text-gray-400">
                  <th className="px-6 py-4 font-semibold">Nome</th>
                  <th className="px-6 py-4 font-semibold hidden md:table-cell">CPF</th>
                  <th className="px-6 py-4 font-semibold hidden sm:table-cell">CRO</th>
                  <th className="px-6 py-4 font-semibold hidden lg:table-cell">Especialidade</th>
                  <th className="px-6 py-4 font-semibold">Status</th>
                  <th className="px-6 py-4 font-semibold">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map(d => (
                  <tr key={d.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 font-medium text-gray-800">{d.nome}</td>
                    <td className="px-6 py-4 text-gray-500 hidden md:table-cell">{formatCPF(d.cpf)}</td>
                    <td className="px-6 py-4 text-gray-500 hidden sm:table-cell">{d.cro}</td>
                    <td className="px-6 py-4 text-gray-500 hidden lg:table-cell">{d.especialidade}</td>
                    <td className="px-6 py-4"><Badge label={d.ativo ? 'Ativo' : 'Inativo'} className={d.ativo ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-500'} /></td>
                    <td className="px-6 py-4">
                      <div className="flex gap-2">
                        <Button size="sm" variant="secondary" onClick={() => openEdit(d)}>Editar</Button>
                        {d.ativo ? <Button size="sm" variant="danger" onClick={() => handleDelete(d.id)}>Desativar</Button>
                                 : <Button size="sm" variant="primary" onClick={() => handleAtivar(d.id)}>Ativar</Button>}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
      <Modal open={modalOpen} title={editing ? 'Editar Dentista' : 'Novo Dentista'} onClose={() => setModalOpen(false)} size="md">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="Nome completo" error={errors.nome?.message} required>
            <input type="text" {...register('nome', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="CPF" error={errors.cpf?.message} required>
            <input type="text" maxLength={11} {...register('cpf', { required: 'Obrigatório' })} className={inputClass} placeholder="Somente números" />
          </FormField>
          <FormField label="CRO" error={errors.cro?.message} required>
            <input type="text" {...register('cro', { required: 'CRO é obrigatório' })} className={inputClass} placeholder="SP-00000" />
          </FormField>
          <FormField label="Especialidade">
            <input type="text" {...register('especialidade')} className={inputClass} placeholder="Ex: Clínico Geral" />
          </FormField>
          <FormField label="E-mail" error={errors.email?.message} required>
            <input type="email" {...register('email', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="Telefone">
            <input type="tel" {...register('telefone')} className={inputClass} placeholder="(11) 99999-9999" />
          </FormField>
          <FormField label="Data de Nascimento">
            <input type="date" {...register('dataNascimento')} className={inputClass} />
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
