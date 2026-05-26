import "./globals.css";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "FraudShield Commerce",
  description: "Anti-fraud SaaS for e-commerce",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="pt-BR">
      <body className="min-h-screen font-body">{children}</body>
    </html>
  );
}
