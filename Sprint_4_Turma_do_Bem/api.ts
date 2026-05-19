import { useEffect, useState } from 'react'
import Card from '../../components/Card'
import Button from '../../components/Button'
import Badge from '../../components/Badge'
import { formatCurrency } from '../../utils/formatters'

interface ReportRow { label: string; value: string | number; sub?: string }

const consultasPorStatus: ReportRow[] = [
  { label: 'Realizadas',  value: 89,  sub: 'maio/2025' },
  { label: 'Agendadas',   value: 23,  sub: 'próximas'  },
  { label: 'Canceladas',  value: 8,   sub: 'maio/2025' },
  { label: 'Faltas',      value: 14,  sub: 'maio/2025' },
]

const arrecadacaoPorCampanha: ReportRow[] = [
  { label: 'Sorriso Solidário Maio', value: formatCurrency(12500), sub: 'meta: R$ 20.000' },
  { label: 'Dia das Mães TDB',       value: formatCurrency(8750),  sub: 'meta: R$ 10.000' },
  { label: 'Campanha Abril',         value: formatCurrency(16200), sub: 'encerrada'        },
]

const pacientesPorPrograma: ReportRow[] = [
  { label: 'Dentistas do Bem',  value: 198, sub: '63%' },
  { label: 'Apolônias do Bem',  value: 114, sub: '37%' },
]

const materiaisEstoqueBaixo: ReportRow[] = [
  { label: 'Luva cirúrgica',   value: '8 cx',  sub: 'mín: 20'  },
  { label: 'Anestésico local', value: '2 cx',  sub: 'mín: 10'  },
  { label: 'Escova de dente',  value: '3 un',  sub: 'mín: 30'  },
]

// Exporta dados como CSV e faz download
function exportCSV(nome: string, rows: ReportRow[]) {
  const header = 'Item,Valor,Detalhe\n'
  const body = rows.map(r => `"${r.label}","${r.value}","${r.sub ?? ''}"`).join('\n')
  const blob = new Blob([header + body], { type: 'text/csv;charset=utf-8;' })
  const url  = URL.createObjectURL(blob)
  const a    = document.createElement('a')
  a.href     = url
  a.download = `${nome.replace(/\s+/g, '_')}_${new Date().toLocaleDateString('pt-BR').replace(/\//g,'-')}.csv`
  a.click()
  URL.revokeObjectURL(url)
}

// Exporta todos os dados em um único CSV
function exportTudo(period: string) {
  const todas = [
    ['Consultas por Status', ...consultasPorStatus.map(r => [r.label, r.value, r.sub ?? ''])],
    ['', ['', '', '']],
    ['Arrecadacao por Campanha', ...arrecadacaoPorCampanha.map(r => [r.label, r.value, r.sub ?? ''])],
    ['', ['', '', '']],
    ['Pacientes por Programa', ...pacientesPorPrograma.map(r => [r.label, r.value, r.sub ?? ''])],
    ['', ['', '', '']],
    ['Materiais Estoque Baixo', ...materiaisEstoqueBaixo.map(r => [r.label, r.value, r.sub ?? ''])],
  ]
  const linhas = [`Relatório Completo - ${period}`, 'Item,Valor,Detalhe']
  todas.forEach(bloco => {
    if (typeof bloco[0] === 'string' && bloco.length === 1) {
      linhas.push(`\n"${bloco[0]}"`)
    } else if (Array.isArray(bloco[0])) {
      linhas.push('')
    } else {
      linhas.push(`"${bloco[0]}"`)
      ;(bloco.slice(1) as any[]).forEach((r: any) => {
        linhas.push(`"${r[0]}","${r[1]}","${r[2]}"`)
      })
    }
  })
  const blob = new Blob([linhas.join('\n')], { type: 'text/csv;charset=utf-8;' })
  const url  = URL.createObjectURL(blob)
  const a    = document.createElement('a')
  a.href     = url
  a.download = `Relatorio_Completo_${new Date().toLocaleDateString('pt-BR').replace(/\//g,'-')}.csv`
  a.click()
  URL.revokeObjectURL(url)
}

function ReportTable({ rows }: { rows: ReportRow[] }) {
  return (
    <table className="w-full text-sm font-body">
      <tbody className="divide-y divide-gray-50">
        {rows.map((row) => (
          <tr key={row.label} className="hover:bg-gray-50 transition-colors">
            <td className="py-3 text-gray-700 font-medium">{row.label}</td>
            {row.sub && <td className="py-3 text-gray-400 text-xs">{row.sub}</td>}
            <td className="py-3 text-right font-display font-bold" style={{ color: '#2d4a1e' }}>{row.value}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

export default function Relatorios() {
  useEffect(() => { document.title = 'Relatórios | De Novo Não! ERP' }, [])

  const [period, setPeriod] = useState<'Mês' | 'Trimestre' | 'Ano'>('Mês')

  return (
    <div className="space-y-8 animate-fade-in">

      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-3xl" style={{ color: '#2d4a1e' }}>Relatórios</h1>
          <p className="text-gray-400 font-body text-sm">Análise consolidada dos dados do ERP</p>
        </div>
        <div className="flex gap-2">
          {(['Mês', 'Trimestre', 'Ano'] as const).map((p) => (
            <button key={p} onClick={() => setPeriod(p)}
              className="px-4 py-2 rounded-xl text-sm font-body font-medium transition-all"
              style={{
                backgroundColor: period === p ? '#2d4a1e' : 'white',
                color:           period === p ? 'white'   : '#6b7280',
                border:          period === p ? 'none'    : '1px solid #e5e7eb',
              }}>
              {p}
            </button>
          ))}
        </div>
      </div>

      {/* KPIs */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: 'Total Atendimentos', value: '134',       cor: '#2d4a1e' },
          { label: 'Arrecadado',         value: 'R$ 37.450', cor: '#7ab800' },
          { label: 'Novos Pacientes',    value: '28',        cor: '#3b82f6' },
          { label: 'Taxa de Falta',      value: '12%',       cor: '#ef4444' },
        ].map((k) => (
          <div key={k.label} className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100 text-center">
            <p className="font-display font-extrabold text-2xl" style={{ color: k.cor }}>{k.value}</p>
            <p className="text-gray-400 text-xs font-body mt-1">{k.label}</p>
          </div>
        ))}
      </div>

      {/* Tabelas */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-display font-bold text-lg" style={{ color: '#2d4a1e' }}>Consultas por Status</h2>
            <Button size="sm" variant="ghost" onClick={() => exportCSV('Consultas_por_Status', consultasPorStatus)}>
              Exportar
            </Button>
          </div>
          <ReportTable rows={consultasPorStatus} />
        </Card>

        <Card>
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-display font-bold text-lg" style={{ color: '#2d4a1e' }}>Arrecadação por Campanha</h2>
            <Button size="sm" variant="ghost" onClick={() => exportCSV('Arrecadacao_por_Campanha', arrecadacaoPorCampanha)}>
              Exportar
            </Button>
          </div>
          <ReportTable rows={arrecadacaoPorCampanha} />
        </Card>

        <Card>
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-display font-bold text-lg" style={{ color: '#2d4a1e' }}>Pacientes por Programa</h2>
            <Button size="sm" variant="ghost" onClick={() => exportCSV('Pacientes_por_Programa', pacientesPorPrograma)}>
              Exportar
            </Button>
          </div>
          <ReportTable rows={pacientesPorPrograma} />
          <div className="mt-4 flex rounded-xl overflow-hidden h-3">
            <div style={{ width: '63%', backgroundColor: '#2d4a1e' }} />
            <div style={{ width: '37%', backgroundColor: '#7ab800' }} />
          </div>
          <div className="flex justify-between text-xs font-body text-gray-400 mt-1">
            <span>Dentistas do Bem 63%</span>
            <span>Apolônias do Bem 37%</span>
          </div>
        </Card>

        <Card>
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-display font-bold text-lg" style={{ color: '#2d4a1e' }}>Materiais com Estoque Baixo</h2>
            <Button size="sm" variant="ghost" onClick={() => exportCSV('Estoque_Baixo', materiaisEstoqueBaixo)}>
              Exportar
            </Button>
          </div>
          <ReportTable rows={materiaisEstoqueBaixo} />
        </Card>
      </div>

      {/* Exportação completa */}
      <Card>
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h3 className="font-display font-bold text-lg" style={{ color: '#2d4a1e' }}>Exportação Completa</h3>
            <p className="text-gray-400 text-sm font-body">Baixa todos os relatórios em um único arquivo CSV</p>
          </div>
          <Button onClick={() => exportTudo(period)} variant="primary">
            Exportar Relatório Completo
          </Button>
        </div>
      </Card>

    </div>
  )
}
