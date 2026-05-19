import { useState, useEffect } from 'react'
import { IBGE, type UFItem, type MunicipioItem } from '../services/api'

export function useUFs() {
  const [ufs, setUFs]         = useState<UFItem[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError]     = useState<string | null>(null)

  useEffect(() => {
    setLoading(true)
    IBGE.listarUFs()
      .then(setUFs)
      .catch(() => setError('Não foi possível carregar os estados.'))
      .finally(() => setLoading(false))
  }, [])

  return { ufs, loading, error }
}

export function useMunicipios(uf: string) {
  const [municipios, setMunicipios] = useState<MunicipioItem[]>([])
  const [loading, setLoading]       = useState(false)
  const [error, setError]           = useState<string | null>(null)

  useEffect(() => {
    if (!uf) { setMunicipios([]); return }
    setLoading(true)
    setMunicipios([])
    IBGE.listarMunicipios(uf)
      .then(setMunicipios)
      .catch(() => setError('Não foi possível carregar as cidades.'))
      .finally(() => setLoading(false))
  }, [uf])

  return { municipios, loading, error }
}
