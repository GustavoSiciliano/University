interface EmptyStateProps {
  icon?: string
  title: string
  message?: string
  description?: string
}

export default function EmptyState({ icon = '📭', title, message, description }: EmptyStateProps) {
  const text = message ?? description ?? ''
  return (
    <div className="flex flex-col items-center justify-center py-20 gap-3 text-center">
      <span className="text-5xl">{icon}</span>
      <p className="font-display font-bold text-lg" style={{ color: '#2d4a1e' }}>{title}</p>
      {text && <p className="text-gray-400 text-sm max-w-xs">{text}</p>}
    </div>
  )
}
