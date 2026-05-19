import { createContext, useContext, useState, ReactNode } from 'react'

export interface UsuarioLogado {
  idUsuario: number
  nome:      string
  login:     string
  perfil:    'ADMIN' | 'DENTISTA' | 'VOLUNTARIO' | 'GESTOR'
}

interface AuthContextType {
  usuario:  UsuarioLogado | null
  login:    (u: UsuarioLogado) => void
  logout:   () => void
  temAcesso: (perfis: string[]) => boolean
}

const AuthContext = createContext<AuthContextType | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<UsuarioLogado | null>(() => {
    const salvo = sessionStorage.getItem('dnn_usuario')
    return salvo ? JSON.parse(salvo) : null
  })

  const login = (u: UsuarioLogado) => {
    setUsuario(u)
    sessionStorage.setItem('dnn_usuario', JSON.stringify(u))
  }

  const logout = () => {
    setUsuario(null)
    sessionStorage.removeItem('dnn_usuario')
  }

  // Verifica se o usuário logado tem um dos perfis permitidos
  const temAcesso = (perfis: string[]) => {
    if (!usuario) return false
    return perfis.includes(usuario.perfil)
  }

  return (
    <AuthContext.Provider value={{ usuario, login, logout, temAcesso }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth deve ser usado dentro de AuthProvider')
  return ctx
}

// Define quais menus cada perfil pode ver
export const acessosPorPerfil: Record<string, string[]> = {
  ADMIN:      ['dashboard', 'pacientes', 'dentistas', 'voluntarios', 'doadores',
               'consultas', 'prontuarios', 'campanhas', 'doacoes', 'materiais', 'ia', 'relatorios'],
  GESTOR:     ['dashboard', 'pacientes', 'consultas', 'campanhas', 'doacoes', 'materiais', 'relatorios'],
  DENTISTA:   ['dashboard', 'pacientes', 'consultas', 'prontuarios', 'ia'],
  VOLUNTARIO: ['dashboard', 'pacientes', 'campanhas', 'materiais'],
}
