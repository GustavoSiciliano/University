// ─── Pessoa base ────────────────────────────────────────────────────────────
export interface Pessoa {
  id: number
  nome: string
  cpf: string
  email: string
  telefone: string
  dataNascimento: string
  cep?: string
  logradouro?: string
  numero?: string
  bairro?: string
  cidade?: string
  uf?: string
  ativo: boolean
  dtCadastro?: string
}

// ─── Paciente ────────────────────────────────────────────────────────────────
export interface Paciente extends Pessoa {
  idPaciente: number
  rendaFamiliar?: number
  observacoes?: string
  programa: 'DENTISTAS_DO_BEM' | 'APOLONICAS_DO_BEM'
}

// ─── Dentista ────────────────────────────────────────────────────────────────
export interface Dentista extends Pessoa {
  idDentista: number
  cro: string
  especialidade?: string
}

// ─── Voluntário ──────────────────────────────────────────────────────────────
export interface Voluntario extends Pessoa {
  idVoluntario: number
  area?: string
}

// ─── Doador ──────────────────────────────────────────────────────────────────
export interface Doador extends Pessoa {
  idDoador: number
  tipoDoador: 'PF' | 'PJ'
}

// ─── Consulta ────────────────────────────────────────────────────────────────
export type StatusConsulta = 'AGENDADA' | 'REALIZADA' | 'CANCELADA' | 'FALTA'

export interface Consulta {
  idConsulta: number
  idPaciente: number
  nomePaciente?: string
  idDentista: number
  nomeDentista?: string
  dtConsulta: string
  status: StatusConsulta
  observacoes?: string
}

// ─── Prontuário ──────────────────────────────────────────────────────────────
export interface Prontuario {
  idProntuario: number
  idConsulta: number
  descricao: string
  dtRegistro: string
}

// ─── Campanha ────────────────────────────────────────────────────────────────
export interface Campanha {
  idCampanha: number
  nome: string
  descricao?: string
  dtInicio: string
  dtFim: string
  metaValor?: number
  totalArrecadado?: number
  ativo: boolean
}

// ─── Doação ──────────────────────────────────────────────────────────────────
export interface Doacao {
  idDoacao: number
  idDoador?: number
  nomeDoador?: string
  idCampanha?: number
  nomeCampanha?: string
  valor: number
  dtDoacao: string
  formaPgto: string
}

// ─── Material ────────────────────────────────────────────────────────────────
export interface Material {
  idMaterial: number
  nome: string
  descricao?: string
  quantidade: number
  unidade?: string
}

// ─── Predição IA ─────────────────────────────────────────────────────────────
export interface PrevisaoFalta {
  probabilidadeFalta: number
  risco: 'ALTO' | 'MEDIO' | 'BAIXO'
  recomendacao: string
}

export interface PrevisaoArrecadacao {
  valorPrevisto: number
  confianca: number
  tendencia: 'ALTA' | 'ESTAVEL' | 'BAIXA'
}

// ─── API Response wrapper ────────────────────────────────────────────────────
export interface ApiResponse<T> {
  data: T
  message?: string
  success: boolean
}

// ─── Dashboard stats ─────────────────────────────────────────────────────────
export interface DashboardStats {
  totalPacientes: number
  totalDentistas: number
  consultasHoje: number
  consultasMes: number
  totalArrecadadoMes: number
  campanhasAtivas: number
  materiaisEstoqueBaixo: number
  taxaFalta: number
}

// ─── Nav items ───────────────────────────────────────────────────────────────
export interface NavItem {
  label: string
  to: string
  icon?: string
}

// ─── Union Types ──────────────────────────────────────────────────────────────
export type Programa      = 'DENTISTAS_DO_BEM' | 'APOLONICAS_DO_BEM'
export type TipoDoador    = 'PF' | 'PJ'
export type TipoRisco     = 'ALTO' | 'MEDIO' | 'BAIXO'
export type TipoTendencia = 'ALTA' | 'ESTAVEL' | 'BAIXA'
export type TurnoConsulta = 'MANHA' | 'TARDE' | 'NOITE'
export type LoadingState  = 'idle' | 'loading' | 'success' | 'error'
export type MetodoHttp    = 'GET' | 'POST' | 'PUT' | 'DELETE'

// ─── Intersection Types ───────────────────────────────────────────────────────
export type PacienteComEndereco = Paciente & {
  cidade: string
  uf:     string
  cep:    string
}

export type ConsultaDetalhada = Consulta & {
  programa:     Programa
  turno:        TurnoConsulta
  distanciaKm?: number
}

export type ResultadoIA = PrevisaoFalta & {
  idPaciente: number
  dtPredicao: string
}

// ─── Tipos utilitários ────────────────────────────────────────────────────────
export type ID          = number
export type Nullable<T> = T | null
export type Optional<T> = T | undefined

// ─── Interfaces de filtro ─────────────────────────────────────────────────────
export interface FiltroBase {
  search?:   string
  ativo?:    boolean
  page?:     number
  pageSize?: number
}

export interface FiltroPaciente extends FiltroBase {
  programa?: Programa
}

export interface FiltroConsulta extends FiltroBase {
  status?:     StatusConsulta
  idPaciente?: ID
  idDentista?: ID
  dtInicio?:   string
  dtFim?:      string
}

export interface PaginatedResponse<T> {
  data:       T[]
  total:      number
  page:       number
  pageSize:   number
  totalPages: number
}
