import { useState, ChangeEvent } from 'react'

// Hook próprio substituto do react-hook-form — sem biblioteca externa
export function useFormState<T extends Record<string, unknown>>(inicial: T) {
  const [valores, setValores] = useState<T>(inicial)
  const [erros, setErros]     = useState<Partial<Record<keyof T, string>>>({})

  const set = (campo: keyof T, valor: unknown) => {
    setValores(prev => ({ ...prev, [campo]: valor }))
    setErros(prev => ({ ...prev, [campo]: undefined }))
  }

  const onChange = (campo: keyof T) =>
    (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
      const val = e.target.type === 'number' ? Number(e.target.value) : e.target.value
      set(campo, val)
    }

  const reset = (novos?: Partial<T>) => {
    setValores(novos ? { ...inicial, ...novos } : inicial)
    setErros({})
  }

  const validar = (regras: Partial<Record<keyof T, string>>) => {
    const novosErros: Partial<Record<keyof T, string>> = {}
    let valido = true
    Object.entries(regras).forEach(([campo, msg]) => {
      const val = valores[campo as keyof T]
      if (!val || (typeof val === 'string' && !val.trim())) {
        novosErros[campo as keyof T] = msg as string
        valido = false
      }
    })
    setErros(novosErros)
    return valido
  }

  return { valores, erros, onChange, set, reset, validar }
}
