import { KpiCard } from "@/components/kpi-card";
import { SectionCard } from "@/components/section-card";
import { getDashboardData } from "@/lib/dashboard";

function formatCurrencyFromCents(value: number) {
  return new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
    maximumFractionDigits: 0,
  }).format(value / 100);
}

function formatTime(value: string) {
  return new Intl.DateTimeFormat("pt-BR", {
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(value));
}

export default async function HomePage() {
  const { summary, assessments, devices, source } = await getDashboardData();

  return (
    <main className="mx-auto flex min-h-screen max-w-7xl flex-col px-6 py-8">
      <header className="rounded-[34px] border border-[var(--line)] bg-slate-950 px-8 py-10 text-white shadow-[0_30px_100px_rgba(15,23,42,0.28)]">
        <div className="flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between">
          <div className="max-w-3xl">
            <p className="text-sm uppercase tracking-[0.3em] text-orange-200">FraudShield Commerce</p>
            <h1 className="mt-3 font-display text-5xl leading-tight">Controle de fraude orientado por risco para operacoes de e-commerce em tempo real.</h1>
            <p className="mt-4 max-w-2xl text-base text-slate-300">
              Risk score, device intelligence, comportamento e regras customizadas em uma unica plataforma SaaS multi-tenant.
            </p>
          </div>
          <div className="rounded-3xl border border-white/10 bg-white/5 p-5">
            <p className="text-sm text-slate-300">{source === "live" ? "Fonte ao vivo" : "Modo fallback"}</p>
            <p className="mt-2 font-display text-3xl">99.95%</p>
            <p className="mt-2 text-sm text-slate-400">
              {source === "live" ? "Dados carregados da API local em tempo real" : "API indisponivel; exibindo dados demonstrativos"}
            </p>
          </div>
        </div>
      </header>

      <section className="mt-8 grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <KpiCard label="Receita Protegida" value={formatCurrencyFromCents(summary.protectedRevenueCents)} detail={`${summary.blockedAssessments} bloqueios com impacto estimado`} />
        <KpiCard label="Chargebacks Evitados" value={String(summary.preventedChargebacks)} detail="estimativa consolidada por tenant" />
        <KpiCard label="Score Medio" value={String(summary.averageScore)} detail={`${summary.reviewAssessments} casos em revisao manual`} />
        <KpiCard label="Eventos / Dia" value={String(summary.totalAssessments)} detail="avaliacoes recentes carregadas do motor de risco" />
      </section>

      <section className="mt-8 grid gap-6 xl:grid-cols-[1.5fr_1fr]">
        <SectionCard title="Timeline Operacional">
          <div className="space-y-3">
            {assessments.map((event) => (
              <div key={event.assessmentId} className="flex items-center justify-between rounded-2xl border border-slate-200 bg-slate-50/80 px-4 py-3">
                <div>
                  <p className="font-medium text-slate-900">{event.assessmentType}</p>
                  <p className="text-sm text-slate-500">{event.reasonCodes.join(" + ")}</p>
                </div>
                <div className="text-right">
                  <p className="text-sm font-semibold text-ember">{event.decision}</p>
                  <p className="text-xs text-slate-500">{formatTime(event.createdAt)}</p>
                </div>
              </div>
            ))}
          </div>
        </SectionCard>

        <SectionCard title="Dispositivos">
          <div className="space-y-3">
            {devices.map((device) => (
              <div key={device.deviceId} className="rounded-2xl border border-slate-200 bg-white px-4 py-3">
                <div className="flex items-center justify-between">
                  <p className="font-medium">{device.deviceId}</p>
                  <span className="rounded-full bg-slate-900 px-3 py-1 text-xs text-white">{device.trustLevel}</span>
                </div>
                <p className="mt-2 text-sm text-slate-500">{device.seenCount} aparicoes correlacionadas</p>
                <p className="text-sm text-moss">{device.blocked ? "bloqueado por risco alto" : `ultimo risco ${device.lastRiskLevel.toLowerCase()}`}</p>
              </div>
            ))}
          </div>
        </SectionCard>
      </section>

      <section className="mt-8 grid gap-6 lg:grid-cols-3">
        <SectionCard title="Motor de Regras">
          <p className="text-slate-600">Pesos deterministicos no MVP com trilha de reason codes, thresholds claros e resposta sincronizada da API.</p>
        </SectionCard>
        <SectionCard title="Multi-Tenancy">
          <p className="text-slate-600">Isolamento por tenant, quotas por plano e caminho de tenancy dedicada para Enterprise.</p>
        </SectionCard>
        <SectionCard title="Observabilidade">
          <p className="text-slate-600">{summary.openAlerts} alertas abertos, {summary.acknowledgedAlerts} reconhecidos e {summary.blockedDevices} dispositivos bloqueados no tenant atual.</p>
        </SectionCard>
      </section>
    </main>
  );
}
