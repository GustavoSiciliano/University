import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

const GREEN  = '#7ab800'
const DARK   = '#2d4a1e'
const ORANGE = '#f5821f'

// ─── Usuários simulados — remover após integração com a API Java ──────────────
const USUARIOS_MOCK: Record<string, { idUsuario: number; nome: string; perfil: 'ADMIN' | 'DENTISTA' | 'VOLUNTARIO' | 'GESTOR'; senha: string }> = {
  'admin.tdb':     { idUsuario: 1, nome: 'Admin TDB',      perfil: 'ADMIN',      senha: 'hash_admin'    },
  'joao.silva':    { idUsuario: 2, nome: 'João Silva',     perfil: 'DENTISTA',   senha: 'hash_joao'     },
  'rafael.gomes':  { idUsuario: 3, nome: 'Rafael Gomes',   perfil: 'GESTOR',     senha: 'hash_rafael'   },
  'fernanda.lima': { idUsuario: 4, nome: 'Fernanda Lima',  perfil: 'VOLUNTARIO', senha: 'hash_fernanda' },
  'ana.costa':     { idUsuario: 5, nome: 'Ana Costa',      perfil: 'DENTISTA',   senha: 'hash_ana'      },
  'carlos.lima':   { idUsuario: 6, nome: 'Carlos Lima',    perfil: 'GESTOR',     senha: 'hash_carlos'   },
  'bruno.alves':   { idUsuario: 7, nome: 'Bruno Alves',    perfil: 'VOLUNTARIO', senha: 'hash_bruno'    },
}

export default function Login() {
  const { login, usuario } = useAuth()
  const navigate = useNavigate()

  const [loginVal,   setLoginVal]   = useState('')
  const [senha,      setSenha]      = useState('')
  const [showSenha,  setShowSenha]  = useState(false)
  const [lembrar,    setLembrar]    = useState(false)
  const [erro,       setErro]       = useState('')
  const [loading,    setLoading]    = useState(false)
  const [tentativas, setTentativas] = useState(0)
  const [bloqueado,  setBloqueado]  = useState(false)
  const [countdown,  setCountdown]  = useState(0)

  useEffect(() => {
    if (usuario) navigate('/erp', { replace: true })
  }, [usuario])

  useEffect(() => {
    const salvo = localStorage.getItem('dnn_login_salvo')
    if (salvo) { setLoginVal(salvo); setLembrar(true) }
  }, [])

  useEffect(() => {
    if (countdown <= 0) return
    const t = setTimeout(() => {
      setCountdown(c => c - 1)
      if (countdown === 1) { setBloqueado(false); setTentativas(0); setErro('') }
    }, 1000)
    return () => clearTimeout(t)
  }, [countdown])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (bloqueado) return
    if (!loginVal.trim() || !senha.trim()) {
      setErro('Preencha login e senha.')
      return
    }

    setLoading(true)
    setErro('')

    // Simula delay de rede
    await new Promise(res => setTimeout(res, 800))

    const u = USUARIOS_MOCK[loginVal.trim()]

    if (!u || u.senha !== senha) {
      const novasTentativas = tentativas + 1
      setTentativas(novasTentativas)
      if (novasTentativas >= 3) {
        setBloqueado(true)
        setCountdown(30)
        setErro('Muitas tentativas. Aguarde 30 segundos.')
      } else {
        setErro(`Login ou senha inválidos. ${3 - novasTentativas} tentativa(s) restante(s).`)
      }
      setLoading(false)
      return
    }

    if (lembrar) localStorage.setItem('dnn_login_salvo', loginVal.trim())
    else         localStorage.removeItem('dnn_login_salvo')

    login({ idUsuario: u.idUsuario, nome: u.nome, login: loginVal.trim(), perfil: u.perfil })
    navigate('/erp', { replace: true })
    setLoading(false)
  }

  return (
    <div className="min-h-screen flex" style={{ backgroundColor: '#f4f9ec' }}>

      {/* Painel esquerdo */}
      <div className="hidden lg:flex lg:w-1/2 flex-col justify-between p-12"
        style={{ backgroundColor: DARK }}>
        <div>
          <img src="/image/logo-dnn.png" alt="De Novo Não!" className="h-14 mb-12" />
          <h1 className="font-display font-extrabold text-4xl text-white leading-tight mb-4">
            Transformando sorrisos,<br />
            <span style={{ color: GREEN }}>transformando vidas.</span>
          </h1>
          <p className="text-white/60 text-base leading-relaxed max-w-sm">
            O sistema de gestão da Turma do Bem conecta dentistas voluntários
            a pacientes que mais precisam de cuidados odontológicos.
          </p>
        </div>
        <div className="space-y-4">
          {[
            { num: '2M+',   label: 'sorrisos transformados' },
            { num: '7mil+', label: 'dentistas voluntários'  },
            { num: '27+',   label: 'anos de impacto social' },
          ].map(s => (
            <div key={s.label} className="flex items-center gap-4">
              <span className="font-display font-extrabold text-2xl" style={{ color: GREEN }}>{s.num}</span>
              <span className="text-white/50 text-sm">{s.label}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Painel direito */}
      <div className="flex-1 flex items-center justify-center px-6 py-12">
        <div className="w-full max-w-md">

          <div className="lg:hidden text-center mb-8">
            <img src="/image/logo-dnn.png" alt="De Novo Não!" className="h-12 mx-auto mb-2" />
          </div>

          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
            <div className="mb-7">
              <h2 className="font-display font-bold text-2xl mb-1" style={{ color: DARK }}>
                Bem-vindo de volta
              </h2>
              <p className="text-gray-400 text-sm">
                Acesse sua conta para gerenciar o sistema.
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="block text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1.5">
                  Login
                </label>
                <div className="relative">
                  <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm">👤</span>
                  <input
                    type="text"
                    value={loginVal}
                    onChange={e => { setLoginVal(e.target.value); setErro('') }}
                    placeholder="ex: joao.silva"
                    disabled={bloqueado || loading}
                    className="w-full border border-gray-200 rounded-xl pl-9 pr-4 py-2.5 text-sm
                               focus:outline-none focus:border-green-400 transition-colors disabled:opacity-50"
                    autoComplete="username"
                  />
                </div>
              </div>

              <div>
                <div className="flex justify-between items-center mb-1.5">
                  <label className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
                    Senha
                  </label>
                  <button type="button" className="text-xs hover:underline" style={{ color: GREEN }}>
                    Esqueceu a senha?
                  </button>
                </div>
                <div className="relative">
                  <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm">🔒</span>
                  <input
                    type={showSenha ? 'text' : 'password'}
                    value={senha}
                    onChange={e => { setSenha(e.target.value); setErro('') }}
                    placeholder="••••••••"
                    disabled={bloqueado || loading}
                    className="w-full border border-gray-200 rounded-xl pl-9 pr-10 py-2.5 text-sm
                               focus:outline-none focus:border-green-400 transition-colors disabled:opacity-50"
                    autoComplete="current-password"
                  />
                  <button type="button"
                    onClick={() => setShowSenha(s => !s)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 text-xs">
                    {showSenha ? '🙈' : '👁️'}
                  </button>
                </div>
              </div>

              <label className="flex items-center gap-2 cursor-pointer">
                <input type="checkbox" checked={lembrar}
                  onChange={e => setLembrar(e.target.checked)} className="rounded" />
                <span className="text-sm text-gray-500">Lembrar meu login</span>
              </label>

              {erro && (
                <div className="flex items-start gap-2 p-3 rounded-xl text-sm"
                  style={{ backgroundColor: '#fef2f2', color: '#dc2626' }}>
                  <span>⚠️</span>
                  <span>{erro}{bloqueado && countdown > 0 && ` (${countdown}s)`}</span>
                </div>
              )}

              <button
                type="submit"
                disabled={loading || bloqueado}
                className="w-full py-3 rounded-xl font-semibold text-sm text-white
                           hover:opacity-90 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                style={{ backgroundColor: GREEN }}>
                {loading ? 'Verificando...' : bloqueado ? `Aguarde ${countdown}s` : 'Entrar no sistema'}
              </button>
            </form>

            <div className="relative my-6">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-100" />
              </div>
              <div className="relative flex justify-center">
                <span className="px-3 bg-white text-xs text-gray-400">Credenciais de demonstração</span>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-2">
              {[
                { login: 'admin.tdb',     perfil: 'Admin',      cor: '#7ab800', senha: 'hash_admin'    },
                { login: 'joao.silva',    perfil: 'Dentista',   cor: '#3b82f6', senha: 'hash_joao'     },
                { login: 'rafael.gomes',  perfil: 'Gestor',     cor: '#f5821f', senha: 'hash_rafael'   },
                { login: 'fernanda.lima', perfil: 'Voluntário', cor: '#8b5cf6', senha: 'hash_fernanda' },
              ].map(u => (
                <button key={u.login} type="button"
                  onClick={() => { setLoginVal(u.login); setSenha(u.senha); setErro('') }}
                  className="text-left px-3 py-2 rounded-xl border border-gray-100 hover:border-gray-200
                             hover:bg-gray-50 transition-all">
                  <p className="text-xs font-semibold" style={{ color: u.cor }}>{u.perfil}</p>
                  <p className="text-xs text-gray-400 truncate">{u.login}</p>
                </button>
              ))}
            </div>
          </div>

          <p className="text-center mt-6 text-xs text-gray-400">
            <Link to="/" className="hover:underline" style={{ color: GREEN }}>
              ← Voltar ao site
            </Link>
            <span className="mx-2">·</span>
            <Link to="/doe" className="hover:underline" style={{ color: ORANGE }}>
              Fazer uma doação
            </Link>
          </p>

        </div>
      </div>
    </div>
  )
}
