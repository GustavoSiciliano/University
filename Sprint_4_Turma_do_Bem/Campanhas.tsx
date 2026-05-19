import { useState, useEffect } from 'react'
import { FeriadosAPI, type Feriado } from '../services/api'

export function useFeriados(ano: number) {
  const [feriados, setFeriados] = useState<Feriado[]>([])
  const [loading, setLoading]   = useState(false)

  useEffect(() => {
    if (!ano) return
    setLoading(true)
    FeriadosAPI.listar(ano)
      .then(setFeriados)
      .catch(() => setFeriados([]))
      .finally(() => setLoading(false))
  }, [ano])

  const eFeriado = (data: string): Feriado | null =>
    feriados.find(f => f.date === data) ?? null

  return { feriados, loading, eFeriado }
}
