interface LoadingSpinnerProps {
  message?: string
}

export default function LoadingSpinner({ message = 'Carregando...' }: LoadingSpinnerProps) {
  return (
    <div className="flex flex-col items-center justify-center py-20 gap-4">
      <div className="w-10 h-10 border-4 border-tdb-green border-t-tdb-teal rounded-full animate-spin" />
      <p className="text-gray-400 text-sm font-body">{message}</p>
    </div>
  )
}
