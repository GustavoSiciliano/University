import { useEffect, useState } from 'react'
import type { Paciente } from '../../types'
import { useFormState } from '../../hooks/useFormState'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import Badge from '../../components/Badge'
import EmptyState from '../../components/EmptyState'
import { ViaCEP } from '../../services/api'
import { formatCPF } from '../../utils/formatters'
import { useUFs, useMunicipios } from '../../hooks/useIBGE'

const mock: Paciente[] = [
  { id: 1, idPaciente: 1, nome: 'Maria Oliveira', cpf: '12345678901', email: 'maria@email.com', telefone: '11999990001', dataNascimento: '1990-05-10', programa: 'DENTISTAS_DO_BEM',    ativo: true },
  { id: 2, idPaciente: 2, nome: 'Pedro Santos',   cpf: '23456789012', email: 'pedro@email.com', telefone: '11999990002', dataNascimento: '1985-03-22', programa: 'APOLONICAS_DO_BEM', ativo: true },
  { id: 3, idPaciente: 3, nome: 'Lucia Ferreira', cpf: '34567890123', email: 'lucia@email.com', telefone: '11999990003', dataNascimento: '2000-11-15', programa: 'DENTISTAS_DO_BEM',    ativo: false },
]

const INICIAL = { nome: '', cpf: '', email: '', telefone: '', dataNascimento: '', programa: 'DENTISTAS_DO_BEM',
                  cep: '', logradouro: '', numero: '', bairro: '', cidade: '', uf: '' }

export default function Pacientes() {
  useEffect(() => { document.title = 'Pacientes | De Novo Não! ERP' }, [])

  const [pacientes,    setPacientes]    = useState<Paciente[]>(mock)
  const [search,       setSearch]       = useState('')
  const [modalOpen,    setModalOpen]    = useState(false)
  const [editing,      setEditing]      = useState<Paciente | null>(null)
  const [loadingCep,   setLoadingCep]   = useState(false)
  const { valores, erros, onChange, set, reset, validar } = useFormState(INICIAL)

  const { ufs }                         = useUFs()
  const { municipios }                  = useMunicipios(valores.uf)

  const filtered = pacientes.filter(p =>
    p.nome.toLowerCase().includes(search.toLowerCase()) || p.cpf.includes(search)
  )

  const openNew  = () => { setEditing(null); reset(); setModalOpen(true) }
  const openEdit = (p: Paciente) => {
    setEditing(p)
    reset({ nome: p.nome, cpf: p.cpf, email: p.email ?? '', telefone: p.telefone ?? '',
            dataNascimento: p.dataNascimento ?? '', programa: p.programa,
            cep: p.cep ?? '', logradouro: p.logradouro ?? '', numero: p.numero ?? '',
            bairro: p.bairro ?? '', cidade: p.cidade ?? '', uf: p.uf ?? '' })
    setModalOpen(true)
  }

  const handleDelete = (id: number) => {
    if (!confirm('Desativar este paciente?')) return
    setPacientes(prev => prev.map(p => p.id === id ? { ...p, ativo: false } : p))
  }
  const handleAtivar = (id: number) => setPacientes(prev => prev.map(p => p.id === id ? { ...p, ativo: true } : p))

  const buscarCep = async () => {
    const cep = valores.cep.replace(/\D/g, '')
    if (cep.length !== 8) return
    setLoadingCep(true)
    try {
      const data = await ViaCEP.buscar(cep)
      if (data && !data.erro) {
        set('logradouro', data.logradouro || '')
        set('bairro',     data.bairro     || '')
        set('cidade',     data.localidade || '')
        set('uf',         data.uf         || '')
      }
    } catch {}
    setLoadingCep(false)
  }

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!validar({ nome: 'Nome obrigatório', cpf: 'CPF obrigatório', telefone: 'Telefone obrigatório', dataNascimento: 'Data obrigatória' })) return
    if (editing) {
      setPacientes(prev => prev.map(p => p.id === editing.id ? { ...p, ...valores } : p))
    } else {
      setPacientes(prev => [...prev, { ...valores, id: Date.now(), idPaciente: Date.now(), ativo: true }])
    }
    setModalOpen(false)
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Pacientes</h1>
          <p className="text-gray-400 font-body text-sm">
            {pacientes.filter(p => p.ativo).length} ativos · {pacientes.length} no total
          </p>
        </div>
        <Button onClick={openNew}>+ Novo Paciente</Button>
      </div>

      <input type="text" placeholder="Buscar por nome ou CPF..." value={search}
        onChange={e => setSearch(e.target.value)} className={inputClass + ' max-w-sm'} />

      {filtered.length === 0 ? <EmptyState icon="🦷" title="Nenhum paciente encontrado" /> : (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm font-body">
              <thead className="bg-gray-50 border-b border-gray-100">
                <tr className="text-left text-gray-400">
                  <th className="px-6 py-4 font-semibold">Nome</th>
                  <th className="px-6 py-4 font-semibold hidden md:table-cell">CPF</th>
                  <th className="px-6 py-4 font-semibold hidden sm:table-cell">Programa</th>
                  <th className="px-6 py-4 font-semibold hidden lg:table-cell">Telefone</th>
                  <th className="px-6 py-4 font-semibold">Status</th>
                  <th className="px-6 py-4 font-semibold">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map(p => (
                  <tr key={p.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 font-medium text-gray-800">{p.nome}</td>
                    <td className="px-6 py-4 text-gray-500 hidden md:table-cell">{formatCPF(p.cpf)}</td>
                    <td className="px-6 py-4 hidden sm:table-cell">
                      <Badge variant={p.programa === 'DENTISTAS_DO_BEM' ? 'success' : 'warning'}>
                        {p.programa === 'DENTISTAS_DO_BEM' ? 'Dentistas do Bem' : 'Apolônias do Bem'}
                      </Badge>
                    </td>
                    <td className="px-6 py-4 text-gray-500 hidden lg:table-cell">{p.telefone}</td>
                    <td className="px-6 py-4">
                      <Badge variant={p.ativo ? 'success' : 'error'}>{p.ativo ? 'Ativo' : 'Inativo'}</Badge>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex gap-2">
                        <Button size="sm" variant="secondary" onClick={() => openEdit(p)}>Editar</Button>
                        {p.ativo
                          ? <Button size="sm" variant="danger"  onClick={() => handleDelete(p.id)}>Desativar</Button>
                          : <Button size="sm" variant="primary" onClick={() => handleAtivar(p.id)}>Ativar</Button>}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      <Modal open={modalOpen} title={editing ? 'Editar Paciente' : 'Novo Paciente'} onClose={() => setModalOpen(false)} size="lg">
        <form onSubmit={onSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <FormField label="Nome completo" error={erros.nome} required>
              <input type="text" value={valores.nome} onChange={onChange('nome')} className={inputClass} />
            </FormField>
            <FormField label="CPF" error={erros.cpf} required>
              <input type="text" maxLength={11} value={valores.cpf} onChange={onChange('cpf')} className={inputClass} placeholder="Somente números" />
            </FormField>
            <FormField label="Data de Nascimento" error={erros.dataNascimento} required>
              <input type="date" value={valores.dataNascimento} onChange={onChange('dataNascimento')} className={inputClass} />
            </FormField>
            <FormField label="Telefone" error={erros.telefone} required>
              <input type="tel" value={valores.telefone} onChange={onChange('telefone')} className={inputClass} placeholder="(11) 99999-9999" />
            </FormField>
            <FormField label="E-mail">
              <input type="email" value={valores.email} onChange={onChange('email')} className={inputClass} />
            </FormField>
            <FormField label="Programa" error={erros.programa}>
              <select value={valores.programa} onChange={onChange('programa')} className={inputClass}>
                <option value="DENTISTAS_DO_BEM">Dentistas do Bem</option>
                <option value="APOLONICAS_DO_BEM">Apolônias do Bem</option>
              </select>
            </FormField>
          </div>

          <p className="text-xs font-semibold uppercase tracking-wide text-gray-400">Endereço</p>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <FormField label="CEP">
              <div className="flex gap-2">
                <input type="text" maxLength={9} value={valores.cep} onChange={onChange('cep')}
                  onBlur={buscarCep} className={inputClass} placeholder="00000-000" />
                {loadingCep && <span className="text-xs text-gray-400 self-center">...</span>}
              </div>
            </FormField>
            <FormField label="Logradouro" className="md:col-span-2">
              <input type="text" value={valores.logradouro} onChange={onChange('logradouro')} className={inputClass} />
            </FormField>
            <FormField label="Número">
              <input type="text" value={valores.numero} onChange={onChange('numero')} className={inputClass} />
            </FormField>
            <FormField label="Bairro">
              <input type="text" value={valores.bairro} onChange={onChange('bairro')} className={inputClass} />
            </FormField>
            <FormField label="UF">
              <select value={valores.uf} onChange={onChange('uf')} className={inputClass}>
                <option value="">Selecione</option>
                {ufs.map(uf => <option key={uf.id} value={uf.sigla}>{uf.sigla}</option>)}
              </select>
            </FormField>
            <FormField label="Cidade" className="md:col-span-2">
              <select value={valores.cidade} onChange={onChange('cidade')} className={inputClass}>
                <option value="">Selecione</option>
                {municipios.map(m => <option key={m.id} value={m.nome}>{m.nome}</option>)}
              </select>
            </FormField>
          </div>

          <div className="flex justify-end gap-3 mt-2">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancelar</Button>
            <Button type="submit">{editing ? 'Salvar' : 'Cadastrar'}</Button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
