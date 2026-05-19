interface FormFieldProps {
  label: string
  error?: string
  required?: boolean
  children: React.ReactNode
}

export function FormField({ label, error, required, children }: FormFieldProps) {
  return (
    <div className="flex flex-col gap-1">
      <label className="text-sm font-semibold text-gray-700 font-body">
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      {children}
      {error && <p className="text-red-500 text-xs">{error}</p>}
    </div>
  )
}

// Classe base reutilizável para inputs
export const inputClass =
  'w-full border-2 border-gray-200 rounded-xl px-4 py-2.5 text-sm font-body ' +
  'focus:outline-none focus:border-tdb-green transition-colors duration-200 bg-gray-50 ' +
  'placeholder:text-gray-400'
