type Props = {
  label: string;
  value: string;
  detail: string;
};

export function KpiCard({ label, value, detail }: Props) {
  return (
    <div className="rounded-3xl border border-[var(--line)] bg-[var(--card)] p-6 shadow-[0_20px_60px_rgba(15,23,42,0.08)] backdrop-blur">
      <p className="text-xs uppercase tracking-[0.24em] text-slate-500">{label}</p>
      <p className="mt-3 font-display text-4xl text-ink">{value}</p>
      <p className="mt-2 text-sm text-slate-600">{detail}</p>
    </div>
  );
}
