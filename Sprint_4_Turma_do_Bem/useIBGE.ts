interface Props {
  text: string
  children: React.ReactNode
}

// Tooltip simples — aparece ao passar o mouse
export default function Tooltip({ text, children }: Props) {
  return (
    <span className="relative group inline-flex">
      {children}
      <span className="pointer-events-none absolute bottom-full left-1/2 -translate-x-1/2 mb-2 z-50
                       bg-gray-800 text-white text-xs rounded-lg px-3 py-1.5 whitespace-nowrap
                       opacity-0 group-hover:opacity-100 transition-opacity duration-150 shadow-lg">
        {text}
        <span className="absolute top-full left-1/2 -translate-x-1/2 border-4 border-transparent border-t-gray-800" />
      </span>
    </span>
  )
}
