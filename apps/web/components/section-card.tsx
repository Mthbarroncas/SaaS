export function SectionCard({
  title,
  children,
}: Readonly<{ title: string; children: React.ReactNode }>) {
  return (
    <section className="rounded-[28px] border border-[var(--line)] bg-white/60 p-6 shadow-[0_18px_50px_rgba(15,23,42,0.06)] backdrop-blur">
      <h2 className="font-display text-2xl">{title}</h2>
      <div className="mt-4">{children}</div>
    </section>
  );
}
