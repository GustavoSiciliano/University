// ─────────────────────────────────────────────────────────────────────────────
// services/api.ts
// Camada de comunicação com o back-end Java e a API de IA (Python/Flask).
// Troque BASE_URL e IA_URL pela URL real do deploy antes de colocar em produção.
// ─────────────────────────────────────────────────────────────────────────────

const BASE_URL = 'https://coffee-shrill-food.ngrok-free.dev'
const IA_URL   = 'https://turma-do-bem-ia.onrender.com'

// ─── Utilitário de fetch ──────────────────────────────────────────────────────
async function request<T>(
  url: string,
  options?: RequestInit,
): Promise<T> {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json', 'ngrok-skip-browser-warning': 'true', ...options?.headers },
    ...options,
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || `Erro ${response.status}`)
  }

  // 204 No Content — retorna null
  if (response.status === 204) return null as T

  return response.json() as Promise<T>
}

// ─── Pacientes ────────────────────────────────────────────────────────────────
export const PacienteService = {
  listar:   ()          => request(`${BASE_URL}/pacientes`),
  buscar:   (id: number)=> request(`${BASE_URL}/pacientes/${id}`),
  criar:    (body: unknown) => request(`${BASE_URL}/pacientes`, { method: 'POST', body: JSON.stringify(body) }),
  atualizar:(id: number, body: unknown) => request(`${BASE_URL}/pacientes/${id}`, { method: 'PUT',  body: JSON.stringify(body) }),
  deletar:  (id: number)=> request(`${BASE_URL}/pacientes/${id}`, { method: 'DELETE' }),
}

// ─── Dentistas ────────────────────────────────────────────────────────────────
export const DentistaService = {
  listar:   ()          => request(`${BASE_URL}/dentistas`),
  buscar:   (id: number)=> request(`${BASE_URL}/dentistas/${id}`),
  criar:    (body: unknown) => request(`${BASE_URL}/dentistas`, { method: 'POST', body: JSON.stringify(body) }),
  atualizar:(id: number, body: unknown) => request(`${BASE_URL}/dentistas/${id}`, { method: 'PUT',  body: JSON.stringify(body) }),
  deletar:  (id: number)=> request(`${BASE_URL}/dentistas/${id}`, { method: 'DELETE' }),
}

// ─── Consultas ────────────────────────────────────────────────────────────────
export const ConsultaService = {
  listar:   ()          => request(`${BASE_URL}/consultas`),
  buscar:   (id: number)=> request(`${BASE_URL}/consultas/${id}`),
  criar:    (body: unknown) => request(`${BASE_URL}/consultas`, { method: 'POST', body: JSON.stringify(body) }),
  atualizar:(id: number, body: unknown) => request(`${BASE_URL}/consultas/${id}`, { method: 'PUT',  body: JSON.stringify(body) }),
  deletar:  (id: number)=> request(`${BASE_URL}/consultas/${id}`, { method: 'DELETE' }),
}

// ─── Campanhas ────────────────────────────────────────────────────────────────
export const CampanhaService = {
  listar:   ()          => request(`${BASE_URL}/campanhas`),
  buscar:   (id: number)=> request(`${BASE_URL}/campanhas/${id}`),
  criar:    (body: unknown) => request(`${BASE_URL}/campanhas`, { method: 'POST', body: JSON.stringify(body) }),
  atualizar:(id: number, body: unknown) => request(`${BASE_URL}/campanhas/${id}`, { method: 'PUT',  body: JSON.stringify(body) }),
  deletar:  (id: number)=> request(`${BASE_URL}/campanhas/${id}`, { method: 'DELETE' }),
}

// ─── Doações ─────────────────────────────────────────────────────────────────
export const DoacaoService = {
  listar:   ()          => request(`${BASE_URL}/doacoes`),
  criar:    (body: unknown) => request(`${BASE_URL}/doacoes`, { method: 'POST', body: JSON.stringify(body) }),
  deletar:  (id: number)=> request(`${BASE_URL}/doacoes/${id}`, { method: 'DELETE' }),
}

// ─── Materiais ────────────────────────────────────────────────────────────────
export const MaterialService = {
  listar:   ()          => request(`${BASE_URL}/materiais`),
  criar:    (body: unknown) => request(`${BASE_URL}/materiais`, { method: 'POST', body: JSON.stringify(body) }),
  atualizar:(id: number, body: unknown) => request(`${BASE_URL}/materiais/${id}`, { method: 'PUT',  body: JSON.stringify(body) }),
  deletar:  (id: number)=> request(`${BASE_URL}/materiais/${id}`, { method: 'DELETE' }),
}

// ─── Dashboard ────────────────────────────────────────────────────────────────
export const DashboardService = {
  stats: () => request(`${BASE_URL}/dashboard/stats`),
}

// ─── IA — Python / FastAPI ────────────────────────────────────────────────────
export const IAService = {
  health:            ()           => request(`${IA_URL}/health`),
  preverFalta:       (body: unknown) => request(`${IA_URL}/predict/falta`,        { method: 'POST', body: JSON.stringify(body) }),
  preverArrecadacao: (body: unknown) => request(`${IA_URL}/predict/arrecadacao`,  { method: 'POST', body: JSON.stringify(body) }),
}

// ─── ViaCEP ──────────────────────────────────────────────────────────────────
export const ViaCEP = {
  buscar: async (cep: string) => {
    const res = await fetch(`https://viacep.com.br/ws/${cep.replace(/\D/g, '')}/json/`)
    const data = await res.json()
    if (data.erro) throw new Error('CEP não encontrado')
    return data
  },
}

// ─── API IBGE — Municípios por UF ────────────────────────────────────────────
// Documentação: https://servicodados.ibge.gov.br/api/docs/localidades
export interface UFItem {
  id: number
  sigla: string
  nome: string
}

export interface MunicipioItem {
  id: number
  nome: string
}

export const IBGE = {
  listarUFs: async (): Promise<UFItem[]> => {
    const resp = await fetch('https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome')
    if (!resp.ok) throw new Error('Erro ao buscar UFs')
    return resp.json()
  },

  listarMunicipios: async (uf: string): Promise<MunicipioItem[]> => {
    if (!uf) return []
    const resp = await fetch(
      `https://servicodados.ibge.gov.br/api/v1/localidades/estados/${uf}/municipios?orderBy=nome`
    )
    if (!resp.ok) throw new Error(`Erro ao buscar municípios de ${uf}`)
    return resp.json()
  },
}

// ─── BrasilAPI — Feriados Nacionais ──────────────────────────────────────────
// Documentação: https://brasilapi.com.br/docs#tag/Feriados-Nacionais
export interface Feriado {
  date: string   // 'YYYY-MM-DD'
  name: string
  type: string
}

export const FeriadosAPI = {
  listar: async (ano: number): Promise<Feriado[]> => {
    const resp = await fetch(`https://brasilapi.com.br/api/feriados/v1/${ano}`)
    if (!resp.ok) throw new Error('Erro ao buscar feriados')
    return resp.json()
  },

  eFeriado: async (data: string): Promise<Feriado | null> => {
    // data no formato 'YYYY-MM-DD'
    const ano = new Date(data).getFullYear()
    const feriados = await FeriadosAPI.listar(ano)
    return feriados.find(f => f.date === data) ?? null
  },
}
