import { useState } from 'react'
import { NavLink } from 'react-router-dom'

const navItems = [
  { label: 'Início',  to: '/' },
  { label: 'Sobre',   to: '/sobre' },
  { label: 'Equipe',  to: '/integrantes' },
  { label: 'FAQ',     to: '/faq' },
  { label: 'Contato', to: '/contato' },
  { label: 'Solução', to: '/solucao' },
]

export default function Header() {
  const [open, setOpen] = useState(false)

  return (
    <header className="bg-white border-b border-gray-100 shadow-sm">
      <div style={{ backgroundColor: '#7ab800' }} className="text-white text-xs text-center py-1.5 font-body">
        Projeto desenvolvido em parceria com a <strong>ONG Turma do Bem</strong> &amp; FIAP
      </div>

      <div className="max-w-6xl mx-auto px-5 py-3 flex items-center justify-between">
        {/* Logo maior e proporcional */}
        <NavLink to="/" onClick={() => setOpen(false)} className="flex items-center gap-3">
          <img
            src="/image/logo-dnn.png"
            alt="Logo De Novo Não!"
            className="h-14 w-auto"
            
          />
        </NavLink>

        {/* Desktop nav */}
        <nav className="hidden md:flex items-center gap-1">
          {navItems.map(item => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `text-sm px-3 py-1.5 rounded-md transition-colors font-body
                ${isActive ? 'font-semibold' : 'text-gray-600 hover:text-gray-900'}`
              }
              style={({ isActive }) => isActive ? { color: '#7ab800' } : {}}
            >
              {item.label}
            </NavLink>
          ))}
          <NavLink
            to="/erp/login"
            className="ml-3 text-sm px-5 py-2 rounded-full font-semibold text-white hover:opacity-90 transition-opacity font-body"
            style={{ backgroundColor: '#f5821f' }}
          >
            Acessar ERP
          </NavLink>
        </nav>

        {/* Hamburger mobile */}
        <button
          className="md:hidden flex flex-col gap-1.5 p-1"
          onClick={() => setOpen(p => !p)}
          aria-label="Menu"
        >
          <span className={`block w-5 h-0.5 bg-gray-600 transition-all ${open ? 'rotate-45 translate-y-2' : ''}`} />
          <span className={`block w-5 h-0.5 bg-gray-600 transition-all ${open ? 'opacity-0' : ''}`} />
          <span className={`block w-5 h-0.5 bg-gray-600 transition-all ${open ? '-rotate-45 -translate-y-2' : ''}`} />
        </button>
      </div>

      {/* Menu mobile */}
      {open && (
        <div className="md:hidden border-t border-gray-100 px-5 py-3 flex flex-col gap-1 bg-white">
          {navItems.map(item => (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={() => setOpen(false)}
              className={({ isActive }) =>
                `text-sm py-2 px-3 rounded font-body ${isActive ? 'font-semibold' : 'text-gray-600'}`
              }
              style={({ isActive }) => isActive ? { color: '#7ab800' } : {}}
            >
              {item.label}
            </NavLink>
          ))}
          <NavLink
            to="/erp/login"
            onClick={() => setOpen(false)}
            className="mt-2 text-sm text-center py-2 rounded-full font-semibold text-white font-body"
            style={{ backgroundColor: '#f5821f' }}
          >
            Acessar ERP
          </NavLink>
        </div>
      )}
    </header>
  )
}
