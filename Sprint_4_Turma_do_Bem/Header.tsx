interface CardProps {
  title?: string
  description?: string
  children?: React.ReactNode
  className?: string
  onClick?: () => void
}

export default function Card({ title, description, children, className = '', onClick }: CardProps) {
  return (
    <div
      onClick={onClick}
      className={`bg-white rounded-2xl shadow-sm border border-gray-100 p-6
        ${onClick ? 'cursor-pointer hover:-translate-y-1 hover:shadow-md transition-all duration-300' : ''}
        ${className}`}
    >
      {title && <h3 className="font-display font-bold text-tdb-teal text-lg mb-2">{title}</h3>}
      {description && <p className="text-gray-600 text-sm leading-relaxed">{description}</p>}
      {children}
    </div>
  )
}
