interface StatCardProps {
  label: string
  value: string | number
  sub?: string
  icon: string
  color?: string
}

export default function StatCard({ label, value, sub, icon, color = 'bg-tdb-teal' }: StatCardProps) {
  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-5 flex items-center gap-4 animate-fade-in-up">
      <div className={`${color} rounded-xl w-12 h-12 flex items-center justify-center text-2xl shrink-0`}>
        {icon}
      </div>
      <div>
        <p className="text-gray-500 text-xs font-body uppercase tracking-wide">{label}</p>
        <p className="font-display font-bold text-tdb-teal text-2xl leading-tight">{value}</p>
        {sub && <p className="text-gray-400 text-xs mt-0.5">{sub}</p>}
      </div>
    </div>
  )
}
