import { Moon, Sun } from "lucide-react";
import { useEffect, useState } from "react";

export default function ThemeToggle() {
  const [dark, setDark] = useState(() => localStorage.getItem("razor-recon-theme") === "dark");

  useEffect(() => {
    document.documentElement.dataset.theme = dark ? "dark" : "light";
    localStorage.setItem("razor-recon-theme", dark ? "dark" : "light");
  }, [dark]);

  return (
    <button className="theme-toggle" type="button" onClick={() => setDark((current) => !current)} aria-label={`Use ${dark ? "light" : "dark"} theme`} title={`Use ${dark ? "light" : "dark"} theme`}>
      {dark ? <Sun size={17} /> : <Moon size={17} />}
    </button>
  );
}
