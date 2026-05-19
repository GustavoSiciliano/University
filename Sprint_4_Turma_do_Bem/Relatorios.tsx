import { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { formatCurrency } from '../../utils/formatters'

const stats = {
  totalPacientes:        312,
  totalDentistas:        47,
  consultasHoje:         8,
  consultasMes:          134,
  totalArrecadadoMes:    18750,
  campanhasAtivas:       3,
  materiaisEstoqueBaixo: 5,
  taxaFalta:             12.4,
}

const ultimasConsultas = [
  { id: 1, paciente: 'Maria Oliveira', dentista: 'Dr. João Silva',   data: '18/04/2025', status: 'REALIZADA' },
  { id: 2, paciente: 'Pedro Santos',   dentista: 'Dra. Ana Costa',   data: '18/04/2025', status: 'AGENDADA'  },
  { id: 3, paciente: 'Lucia Ferreira', dentista: 'Dr. Carlos Lima',  data: '17/04/2025', status: 'FALTA'     },
  { id: 4, paciente: 'Bruno Alves',    dentista: 'Dra. Ana Costa',   data: '17/04/2025', status: 'REALIZADA' },
]

const statusStyle: Record<string, React.CSSProperties> = {
  AGENDADA:  { backgroundColor: '#dbeafe', color: '#1d4ed8' },
  REALIZADA: { backgroundColor: '#dcfce7', color: '#15803d' },
  CANCELADA: { backgroundColor: '#fee2e2', color: '#dc2626' },
  FALTA:     { backgroundColor: '#fef9c3', color: '#a16207' },
}

const statusLabel: Record<string, string> = {
  AGENDADA: 'Agendada', REALIZADA: 'Realizada', CANCELADA: 'Cancelada', FALTA: 'Falta',
}

interface KpiProps {
  label: string
  value: string | number
  sub?: string
  icon: string
  bg: string
}

function Kpi({ label, value, sub, icon, bg }: KpiProps) {
  return (
    <div className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm flex items-start gap-4">
      <div className="text-2xl w-11 h-11 rounded-xl flex items-center justify-center shrink-0"
        style={{ backgroundColor: bg }}>
        {icon}
      </div>
      <div>
        <p className="text-gray-400 text-xs font-body uppercase tracking-wide">{label}</p>
        <p className="font-display font-extrabold text-2xl leading-tight" style={{ color: '#2d4a1e' }}>{value}</p>
        {sub && <p className="text-gray-400 text-xs font-body">{sub}</p>}
      </div>
    </div>
  )
}

export default function Dashboard() {
  useEffect(() => { document.title = 'Dashboard | De Novo Não! ERP' }, [])

  return (
    <div className="space-y-8">

      <div>
        <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Dashboard</h1>
        <p className="text-gray-400 font-body text-sm mt-1">Visão geral da Turma do Bem</p>
      </div>

      {/* KPIs */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <Kpi label="Total de Pacientes"  value={stats.totalPacientes}                    icon="🦷" bg="#f4f9ec" />
        <Kpi label="Dentistas Ativos"    value={stats.totalDentistas}                    icon="👨‍⚕️" bg="#f4f9ec" />
        <Kpi label="Consultas Hoje"      value={stats.consultasHoje}                     icon="📅" bg="#fef9c3" />
        <Kpi label="Consultas no Mês"    value={stats.consultasMes}                      icon="📊" bg="#dbeafe" />
        <Kpi label="Arrecadado no Mês"   value={formatCurrency(stats.totalArrecadadoMes)} icon="💰" bg="#dcfce7" />
        <Kpi label="Campanhas Ativas"    value={stats.campanhasAtivas}                   icon="📣" bg="#fff8f2" />
        <Kpi label="Estoque Baixo"       value={stats.materiaisEstoqueBaixo} sub="itens abaixo do mínimo" icon="🧰" bg="#fee2e2" />
        <Kpi label="Taxa de Falta"       value={`${stats.taxaFalta}%`}       sub="no último mês"          icon="⚠️" bg="#fef9c3" />
      </div>

      {/* Últimas Consultas + IA Insights */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

        {/* Tabela */}
        <div className="lg:col-span-2 bg-white rounded-2xl border border-gray-100 shadow-sm p-6">
          <h2 className="font-display font-bold text-lg mb-4" style={{ color: '#2d4a1e' }}>
            Últimas Consultas
          </h2>
          <div className="overflow-x-auto">
            <table className="w-full text-sm font-body">
              <thead>
                <tr className="text-left text-gray-400 border-b border-gray-100">
                  <th className="pb-3 font-semibold">Paciente</th>
                  <th className="pb-3 font-semibold hidden md:table-cell">Dentista</th>
                  <th className="pb-3 font-semibold hidden sm:table-cell">Data</th>
                  <th className="pb-3 font-semibold">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {ultimasConsultas.map(c => (
                  <tr key={c.id} className="hover:bg-gray-50 transition-colors">
                    <td className="py-3 font-medium text-gray-700">{c.paciente}</td>
                    <td className="py-3 text-gray-500 hidden md:table-cell">{c.dentista}</td>
                    <td className="py-3 text-gray-500 hidden sm:table-cell">{c.data}</td>
                    <td className="py-3">
                      <span className="px-2.5 py-0.5 rounded-full text-xs font-semibold"
                        style={statusStyle[c.status]}>
                        {statusLabel[c.status]}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* IA Insights — fundo sólido verde escuro, sem dependência de classes Tailwind customizadas */}
        <div className="rounded-2xl p-6 flex flex-col gap-4"
          style={{ backgroundColor: '#2d4a1e', color: 'white' }}>
          <h2 className="font-display font-bold text-lg">IA Insights</h2>

          <div className="rounded-xl p-4 space-y-1"
            style={{ backgroundColor: 'rgba(255,255,255,0.10)' }}>
            <p className="text-xs font-semibold uppercase tracking-wide"
              style={{ color: 'rgba(255,255,255,0.60)' }}>Previsão de Faltas</p>
            <p className="text-sm font-semibold text-white">Risco alto em 3 consultas de amanhã</p>
          </div>

          <div className="rounded-xl p-4 space-y-1"
            style={{ backgroundColor: 'rgba(255,255,255,0.10)' }}>
            <p className="text-xs font-semibold uppercase tracking-wide"
              style={{ color: 'rgba(255,255,255,0.60)' }}>Arrecadação</p>
            <p className="text-sm font-semibold text-white">Campanha maio: previsão +12% vs abril</p>
          </div>

          <div className="rounded-xl p-4 space-y-1"
            style={{ backgroundColor: 'rgba(255,255,255,0.10)' }}>
            <p className="text-xs font-semibold uppercase tracking-wide"
              style={{ color: 'rgba(255,255,255,0.60)' }}>Estoque</p>
            <p className="text-sm font-semibold text-white">Luva cirúrgica: repor em até 5 dias</p>
          </div>

          <Link to="/erp/ia"
            className="block text-center rounded-xl font-semibold text-sm py-2.5 mt-auto transition-colors font-body"
            style={{ backgroundColor: '#7ab800', color: 'white' }}>
            Ver análise completa
          </Link>
        </div>

      </div>
    </div>
  )
}
