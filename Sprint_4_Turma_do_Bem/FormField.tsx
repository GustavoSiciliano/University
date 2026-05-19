import { Link } from 'react-router-dom'

type Variant = 'primary' | 'secondary' | 'danger' | 'ghost'
type Size    = 'sm' | 'md' | 'lg'

interface ButtonProps {
  children: React.ReactNode
  to?: string
  onClick?: () => void
  variant?: Variant
  size?: Size
  type?: 'button' | 'submit' | 'reset'
  disabled?: boolean
  className?: string
}

const variantStyle: Record<Variant, React.CSSProperties> = {
  primary:   { backgroundColor: '#7ab800', color: 'white' },
  secondary: { backgroundColor: 'white', border: '2px solid #7ab800', color: '#2d4a1e' },
  danger:    { backgroundColor: '#ef4444', color: 'white' },
  ghost:     { backgroundColor: 'transparent', color: '#7ab800' },
}

const sizeClass: Record<Size, string> = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-5 py-2.5 text-base',
  lg: 'px-8 py-3.5 text-lg',
}

export default function Button({
  children, to, onClick, variant = 'primary', size = 'md',
  type = 'button', disabled = false, className = '',
}: ButtonProps) {
  const base = `inline-flex items-center justify-center gap-2 font-display font-semibold rounded-xl
    transition-all duration-200 focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed
    ${sizeClass[size]} ${className}`

  if (to) return (
    <Link to={to} className={base} style={variantStyle[variant]}>
      {children}
    </Link>
  )

  return (
    <button type={type} onClick={onClick} disabled={disabled}
      className={base} style={variantStyle[variant]}>
      {children}
    </button>
  )
}
