import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import type { Doador } from '../../types'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import Badge from '../../components/Badge'
import EmptyState from '../../components/EmptyState'

const mock: Doador[] = [
  { id: 1, idDoador: 1, nome: 'Roberto Souza',    cpf: '77788899900',  email: 'roberto@email.com', telefone: '11966660001', dataNascimento: '1970-01-15', tipoDoador: 'PF', ativo: true },
  { id: 2, idDoador: 2, nome: 'Empresa XYZ Ltda', cpf: '12345678000195', email: 'contato@xyz.com', telefone: '1133330002',  dataNascimento: '',           tipoDoador: 'PJ', ativo: true },
  { id: 3, idDoador: 3, nome: 'Mariana Gomes',    cpf: '88899900011',  email: 'mari@email.com',    telefone: '11966660003', dataNascimento: '1995-08-20', tipoDoador: 'PF', ativo: false },
]
type FormData = Omit<Doador, 'id' | 'idDoador' | 'ativo'>

export default function Doadores() {
  useEffect(() => { document.title = 'Doadores | De Novo Não! ERP' }, [])
  const [lista,     setLista]     = useState<Doador[]>(mock)
  const [search,    setSearch]    = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editing,   setEditing]   = useState<Doador | null>(null)
  const { register, handleSubmit, reset, formState: { errors } } = useForm<FormData>()

  const filtered     = lista.filter(d => d.nome.toLowerCase().includes(search.toLowerCase()))
  const openNew      = () => { setEditing(null); reset(); setModalOpen(true) }
  const openEdit     = (d: Doador) => { setEditing(d); reset({ nome: d.nome, cpf: d.cpf, email: d.email, telefone: d.telefone, dataNascimento: d.dataNascimento, tipoDoador: d.tipoDoador }); setModalOpen(true) }
  const handleToggle = (id: number) => setLista(prev => prev.map(d => d.id === id ? { ...d, ativo: !d.ativo } : d))
  const onSubmit     = (data: FormData) => {
    if (editing) setLista(prev => prev.map(d => d.id === editing.id ? { ...d, ...data } : d))
    else setLista(prev => [...prev, { ...data, id: Date.now(), idDoador: Date.now(), ativo: true }])
    setModalOpen(false)
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Doadores</h1>
          <p className="text-gray-400 font-body text-sm">{lista.filter(d => d.ativo).length} doadores ativos</p>
        </div>
        <Button onClick={openNew}>+ Novo Doador</Button>
      </div>
      <input type="text" placeholder="Buscar por nome..." value={search} onChange={e => setSearch(e.target.value)} className={inputClass + ' max-w-sm'} />
      {filtered.length === 0 ? <EmptyState icon="💛" title="Nenhum doador encontrado" /> : (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm font-body">
              <thead className="bg-gray-50 border-b border-gray-100">
                <tr className="text-left text-gray-400">
                  <th className="px-6 py-4 font-semibold">Nome</th>
                  <th className="px-6 py-4 font-semibold hidden sm:table-cell">Tipo</th>
                  <th className="px-6 py-4 font-semibold hidden md:table-cell">E-mail</th>
                  <th className="px-6 py-4 font-semibold">Status</th>
                  <th className="px-6 py-4 font-semibold">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map(d => (
                  <tr key={d.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 font-medium text-gray-800">{d.nome}</td>
                    <td className="px-6 py-4 hidden sm:table-cell">
                      <Badge label={d.tipoDoador} className={d.tipoDoador === 'PJ' ? 'bg-blue-100 text-blue-700' : 'bg-green-100 text-green-700'} />
                    </td>
                    <td className="px-6 py-4 text-gray-500 hidden md:table-cell">{d.email}</td>
                    <td className="px-6 py-4"><Badge label={d.ativo ? 'Ativo' : 'Inativo'} className={d.ativo ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-500'} /></td>
                    <td className="px-6 py-4">
                      <div className="flex gap-2">
                        <Button size="sm" variant="secondary" onClick={() => openEdit(d)}>Editar</Button>
                        {d.ativo ? <Button size="sm" variant="danger" onClick={() => handleToggle(d.id)}>Desativar</Button>
                                 : <Button size="sm" variant="primary" onClick={() => handleToggle(d.id)}>Ativar</Button>}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
      <Modal open={modalOpen} title={editing ? 'Editar Doador' : 'Novo Doador'} onClose={() => setModalOpen(false)} size="md">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="Nome completo" error={errors.nome?.message} required>
            <input type="text" {...register('nome', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="CPF/CNPJ" error={errors.cpf?.message} required>
            <input type="text" maxLength={14} {...register('cpf', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="Tipo">
            <select {...register('tipoDoador')} className={inputClass}>
              <option value="PF">Pessoa Física</option>
              <option value="PJ">Pessoa Jurídica</option>
            </select>
          </FormField>
          <FormField label="E-mail" error={errors.email?.message} required>
            <input type="email" {...register('email', { required: 'Obrigatório' })} className={inputClass} />
          </FormField>
          <FormField label="Telefone">
            <input type="tel" {...register('telefone')} className={inputClass} />
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
