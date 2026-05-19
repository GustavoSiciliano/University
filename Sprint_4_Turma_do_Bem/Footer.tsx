// Badge aceita tanto variant (uso no ERP) quanto className (uso legado)
type Variant = 'success' | 'warning' | 'error' | 'info' | 'default'

interface BadgeProps {
  children?: React.ReactNode
  label?: string
  variant?: Variant
  className?: string
}

const variantStyle: Record<Variant, React.CSSProperties> = {
  success: { backgroundColor: '#dcfce7', color: '#15803d' },
  warning: { backgroundColor: '#fef9c3', color: '#a16207' },
  error:   { backgroundColor: '#fee2e2', color: '#dc2626' },
  info:    { backgroundColor: '#dbeafe', color: '#1d4ed8' },
  default: { backgroundColor: '#f3f4f6', color: '#374151' },
}

// Mapeia classes antigas para variantes
function classToVariant(cls: string): Variant {
  if (cls.includes('green') || cls.includes('success')) return 'success'
  if (cls.includes('yellow') || cls.includes('warning')) return 'warning'
  if (cls.includes('red')   || cls.includes('error'))   return 'error'
  if (cls.includes('blue')  || cls.includes('info'))    return 'info'
  return 'default'
}

export default function Badge({ children, label, variant, className = '' }: BadgeProps) {
  const resolvedVariant = variant ?? (className ? classToVariant(className) : 'default')
  const text = children ?? label ?? ''

  return (
    <span className="inline-block px-2.5 py-0.5 rounded-full text-xs font-semibold"
      style={variantStyle[resolvedVariant]}>
      {text}
    </span>
  )
}
