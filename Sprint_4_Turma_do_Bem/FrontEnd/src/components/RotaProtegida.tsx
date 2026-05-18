import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

interface Props {
  children: React.ReactNode
  perfisPermitidos?: string[]
}

// Redireciona para login se não estiver autenticado
// Redireciona para dashboard se não tiver o perfil correto
export default function RotaProtegida({ children, perfisPermitidos }: Props) {
  const { usuario } = useAuth()

  if (!usuario) {
    return <Navigate to="/erp/login" replace />
  }

  if (perfisPermitidos && !perfisPermitidos.includes(usuario.perfil)) {
    return <Navigate to="/erp" replace />
  }

  return <>{children}</>
}
