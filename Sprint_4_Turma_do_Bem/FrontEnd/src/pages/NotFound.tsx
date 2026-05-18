import { useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'

export default function NotFound() {
  const navigate = useNavigate()
  useEffect(() => { document.title = 'Página não encontrada | De Novo Não!' }, [])

  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-6 text-center"
      style={{ backgroundColor: '#f4f9ec' }}>
      <img src="/image/logo-dnn.png" alt="De Novo Não!"
        className="h-16 mb-6" style={{ mixBlendMode: 'multiply' }} />
      <h1 className="font-display font-extrabold text-7xl mb-2" style={{ color: '#7ab800' }}>404</h1>
      <p className="font-display font-bold text-xl mb-2" style={{ color: '#2d4a1e' }}>
        Página não encontrada
      </p>
      <p className="text-gray-500 text-sm font-body mb-8 max-w-xs">
        A página que você está procurando não existe ou foi movida.
      </p>
      <div className="flex gap-3 flex-wrap justify-center">
        <button onClick={() => navigate(-1)}
          className="px-5 py-2.5 rounded-full font-semibold text-sm border font-body hover:bg-gray-50 transition-colors"
          style={{ borderColor: '#7ab800', color: '#7ab800' }}>
          Voltar
        </button>
        <Link to="/"
          className="px-5 py-2.5 rounded-full font-semibold text-sm text-white font-body hover:opacity-90 transition-opacity"
          style={{ backgroundColor: '#f5821f' }}>
          Ir para o início
        </Link>
      </div>
    </div>
  )
}
