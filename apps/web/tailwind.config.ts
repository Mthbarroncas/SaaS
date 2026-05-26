import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./app/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        ink: "#0F172A",
        sand: "#F5EFE4",
        ember: "#DB5A42",
        moss: "#2F5D50",
        fog: "#D8DEE9"
      },
      fontFamily: {
        display: ["Georgia", "serif"],
        body: ["ui-sans-serif", "system-ui"]
      }
    },
  },
  plugins: [],
};

export default config;
