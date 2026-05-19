import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

import Layout        from './components/Layout'
import ERPLayout     from './components/ERPLayout'
import RotaProtegida from './components/RotaProtegida'

import Home              from './pages/Home'
import Sobre             from './pages/Sobre'
import FAQ               from './pages/FAQ'
import Contato           from './pages/Contato'
import Integrantes       from './pages/Integrantes'
import IntegranteDetalhe from './pages/IntegranteDetalhe'
import Solucao           from './pages/Solucao'
import Doe               from './pages/Doe'
import NotFound          from './pages/NotFound'

import Login       from './pages/erp/Login'
import Dashboard   from './pages/erp/Dashboard'
import Pacientes   from './pages/erp/Pacientes'
import Dentistas   from './pages/erp/Dentistas'
import Voluntarios from './pages/erp/Voluntarios'
import Doadores    from './pages/erp/Doadores'
import Consultas   from './pages/erp/Consultas'
import Prontuarios from './pages/erp/Prontuarios'
import Campanhas   from './pages/erp/Campanhas'
import Doacoes     from './pages/erp/Doacoes'
import Materiais   from './pages/erp/Materiais'
import IA          from './pages/erp/IA'
import Relatorios  from './pages/erp/Relatorios'

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Site público */}
        <Route path="/" element={<Layout />}>
          <Route index                  element={<Home />} />
          <Route path="sobre"           element={<Sobre />} />
          <Route path="faq"             element={<FAQ />} />
          <Route path="contato"         element={<Contato />} />
          <Route path="integrantes"     element={<Integrantes />} />
          <Route path="integrantes/:id" element={<IntegranteDetalhe />} />
          <Route path="solucao"         element={<Solucao />} />
        </Route>

        {/* Doação — página própria fora do Layout principal */}
        <Route path="/doe" element={<Doe />} />

        {/* Login ERP — sem proteção */}
        <Route path="/erp/login" element={<Login />} />

        {/* ERP — protegido por perfil */}
        <Route path="/erp" element={<ERPLayout />}>
          <Route index element={
            <RotaProtegida><Dashboard /></RotaProtegida>
          } />
          <Route path="pacientes" element={
            <RotaProtegida perfisPermitidos={['ADMIN','GESTOR','DENTISTA','VOLUNTARIO']}>
              <Pacientes />
            </RotaProtegida>
          } />
          <Route path="dentistas" element={
            <RotaProtegida perfisPermitidos={['ADMIN']}>
              <Dentistas />
            </RotaProtegida>
          } />
          <Route path="voluntarios" element={
            <RotaProtegida perfisPermitidos={['ADMIN']}>
              <Voluntarios />
            </RotaProtegida>
          } />
          <Route path="doadores" element={
            <RotaProtegida perfisPermitidos={['ADMIN']}>
              <Doadores />
            </RotaProtegida>
          } />
          <Route path="consultas" element={
            <RotaProtegida perfisPermitidos={['ADMIN','GESTOR','DENTISTA']}>
              <Consultas />
            </RotaProtegida>
          } />
          <Route path="prontuarios" element={
            <RotaProtegida perfisPermitidos={['ADMIN','DENTISTA']}>
              <Prontuarios />
            </RotaProtegida>
          } />
          <Route path="campanhas" element={
            <RotaProtegida perfisPermitidos={['ADMIN','GESTOR','VOLUNTARIO']}>
              <Campanhas />
            </RotaProtegida>
          } />
          <Route path="doacoes" element={
            <RotaProtegida perfisPermitidos={['ADMIN','GESTOR']}>
              <Doacoes />
            </RotaProtegida>
          } />
          <Route path="materiais" element={
            <RotaProtegida perfisPermitidos={['ADMIN','GESTOR','VOLUNTARIO']}>
              <Materiais />
            </RotaProtegida>
          } />
          <Route path="ia" element={
            <RotaProtegida perfisPermitidos={['ADMIN','DENTISTA']}>
              <IA />
            </RotaProtegida>
          } />
          <Route path="relatorios" element={
            <RotaProtegida perfisPermitidos={['ADMIN','GESTOR']}>
              <Relatorios />
            </RotaProtegida>
          } />
        </Route>

        <Route path="/404" element={<NotFound />} />
        <Route path="*"    element={<Navigate to="/404" replace />} />

      </Routes>
    </BrowserRouter>
  )
}

export default App
