import { useEffect, useState } from 'react'
import type { Doacao } from '../../types'
import { useFormState } from '../../hooks/useFormState'
import Button from '../../components/Button'
import Modal from '../../components/Modal'
import { FormField, inputClass } from '../../components/FormField'
import EmptyState from '../../components/EmptyState'
import { formatCurrency } from '../../utils/formatters'

const mock: Doacao[] = [
  { id: 1, idDoacao: 1, idDoador: 1, nomeDoador: 'Roberto Souza',    nomeCampanha: 'Sorriso Solidário Maio', valor: 500,  formaPgto: 'PIX',      dtDoacao: '2025-05-10', observacoes: '' },
  { id: 2, idDoacao: 2, idDoador: 2, nomeDoador: 'Empresa XYZ Ltda', nomeCampanha: 'Sorriso Solidário Maio', valor: 2000, formaPgto: 'TRANSFERENCIA', dtDoacao: '2025-05-08', observacoes: 'Doação corporativa' },
  { id: 3, idDoacao: 3, idDoador: 3, nomeDoador: 'Anônimo',          nomeCampanha: 'Dia das Mães TDB',       valor: 100,  formaPgto: 'PIX',      dtDoacao: '2025-05-11', observacoes: '' },
]

const FORMAS = ['PIX', 'BOLETO', 'CARTAO', 'TRANSFERENCIA', 'DINHEIRO']
const INICIAL = { nomeDoador: '', nomeCampanha: '', valor: 0, formaPgto: 'PIX', dtDoacao: '', observacoes: '' }

export default function Doacoes() {
  useEffect(() => { document.title = 'Doações | De Novo Não! ERP' }, [])

  const [lista,     setLista]     = useState<Doacao[]>(mock)
  const [search,    setSearch]    = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const { valores, erros, onChange, reset, validar } = useFormState(INICIAL)

  const totalGeral = lista.reduce((s, d) => s + d.valor, 0)
  const filtered   = lista.filter(d =>
    (d.nomeDoador ?? '').toLowerCase().includes(search.toLowerCase()) ||
    (d.nomeCampanha ?? '').toLowerCase().includes(search.toLowerCase())
  )

  const handleDelete = (id: number) => {
    if (!confirm('Remover esta doação?')) return
    setLista(prev => prev.filter(d => d.id !== id))
  }

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!validar({ valor: 'Valor obrigatório', dtDoacao: 'Data obrigatória' })) return
    setLista(prev => [...prev, {
      ...valores, id: Date.now(), idDoacao: Date.now(), idDoador: Date.now(),
      nomeDoador: valores.nomeDoador || 'Anônimo',
    }])
    reset()
    setModalOpen(false)
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Doações</h1>
          <p className="text-gray-400 font-body text-sm">
            {lista.length} registros · Total: <span className="font-semibold" style={{ color: '#7ab800' }}>{formatCurrency(totalGeral)}</span>
          </p>
        </div>
        <Button onClick={() => { reset(); setModalOpen(true) }}>+ Nova Doação</Button>
      </div>

      <input type="text" placeholder="Buscar por doador ou campanha..." value={search}
        onChange={e => setSearch(e.target.value)} className={inputClass + ' max-w-sm'} />

      {filtered.length === 0 ? <EmptyState icon="💰" title="Nenhuma doação encontrada" /> : (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm font-body">
              <thead className="bg-gray-50 border-b border-gray-100">
                <tr className="text-left text-gray-400">
                  <th className="px-6 py-4 font-semibold">Doador</th>
                  <th className="px-6 py-4 font-semibold hidden sm:table-cell">Campanha</th>
                  <th className="px-6 py-4 font-semibold">Valor</th>
                  <th className="px-6 py-4 font-semibold hidden md:table-cell">Forma</th>
                  <th className="px-6 py-4 font-semibold hidden lg:table-cell">Data</th>
                  <th className="px-6 py-4 font-semibold">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map(d => (
                  <tr key={d.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 font-medium text-gray-800">{d.nomeDoador || 'Anônimo'}</td>
                    <td className="px-6 py-4 text-gray-500 hidden sm:table-cell">{d.nomeCampanha}</td>
                    <td className="px-6 py-4 font-semibold" style={{ color: '#7ab800' }}>{formatCurrency(d.valor)}</td>
                    <td className="px-6 py-4 text-gray-500 hidden md:table-cell">{d.formaPgto}</td>
                    <td className="px-6 py-4 text-gray-500 hidden lg:table-cell">{d.dtDoacao}</td>
                    <td className="px-6 py-4">
                      <Button size="sm" variant="danger" onClick={() => handleDelete(d.id)}>Remover</Button>
                    </td>
                  </tr>
                ))}
                <tr className="bg-gray-50">
                  <td className="px-6 py-3 font-display font-bold" style={{ color: '#2d4a1e' }} colSpan={2}>Total</td>
                  <td className="px-6 py-3 font-display font-bold" style={{ color: '#7ab800' }}>
                    {formatCurrency(filtered.reduce((a, d) => a + d.valor, 0))}
                  </td>
                  <td colSpan={3} />
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      )}

      <Modal open={modalOpen} title="Nova Doação" onClose={() => setModalOpen(false)} size="md">
        <form onSubmit={onSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="Nome do doador">
            <input type="text" value={valores.nomeDoador} onChange={onChange('nomeDoador')} className={inputClass} placeholder="Deixe vazio para anônimo" />
          </FormField>
          <FormField label="Campanha">
            <input type="text" value={valores.nomeCampanha} onChange={onChange('nomeCampanha')} className={inputClass} />
          </FormField>
          <FormField label="Valor (R$)" error={erros.valor} required>
            <input type="number" min={0.01} step={0.01} value={valores.valor} onChange={onChange('valor')} className={inputClass} />
          </FormField>
          <FormField label="Forma de pagamento">
            <select value={valores.formaPgto} onChange={onChange('formaPgto')} className={inputClass}>
              {FORMAS.map(f => <option key={f} value={f}>{f}</option>)}
            </select>
          </FormField>
          <FormField label="Data" error={erros.dtDoacao} required>
            <input type="date" value={valores.dtDoacao} onChange={onChange('dtDoacao')} className={inputClass} />
          </FormField>
          <FormField label="Observações">
            <input type="text" value={valores.observacoes} onChange={onChange('observacoes')} className={inputClass} />
          </FormField>
          <div className="md:col-span-2 flex justify-end gap-3 mt-2">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancelar</Button>
            <Button type="submit">Registrar</Button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
