export const formatCurrency = (value: number) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value)

export const formatDate = (iso: string) =>
  new Date(iso).toLocaleDateString('pt-BR')

export const formatCPF = (cpf: string) =>
  cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')

export const formatPhone = (tel: string) =>
  tel.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3')

export const statusLabel: Record<string, string> = {
  AGENDADA:  'Agendada',
  REALIZADA: 'Realizada',
  CANCELADA: 'Cancelada',
  FALTA:     'Falta',
}

export const statusColor: Record<string, string> = {
  AGENDADA:  'bg-yellow-100 text-yellow-700',
  REALIZADA: 'bg-green-100 text-green-700',
  CANCELADA: 'bg-gray-100 text-gray-500',
  FALTA:     'bg-red-100 text-red-600',
}

export const riscoColor: Record<string, string> = {
  ALTO:  'text-red-600',
  MEDIO: 'text-yellow-600',
  BAIXO: 'text-green-600',
}
