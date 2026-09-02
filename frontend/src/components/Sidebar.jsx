import { Activity, LayoutDashboard, ShieldCheck } from "lucide-react";

export default function Sidebar({ page, navigate }) {
  const items = [
    ["dashboard", "Control room", LayoutDashboard],
    ["reconciliations", "Reconciliations", Activity],
    ["resilience", "Resilience lab", ShieldCheck],
  ];

  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brand-mark">R</div>
        <div><strong>RAZOR RECON</strong><span>DECISION ENGINE</span></div>
      </div>
      <div className="system-status"><span className="status-dot" /><div><strong>CORE OPERATIONAL</strong><small>All systems nominal</small></div></div>
      <nav className="side-nav">
        {items.map(([key, label, Icon]) => <button key={key} className={page === key ? "nav-active" : ""} onClick={() => navigate(key)}><Icon size={17} />{label}</button>)}
      </nav>
      <div className="sidebar-bottom">
        <span className="architecture-label">DECISION ARCHITECTURE</span>
        <div><b>01</b> Deterministic</div><div><b>02</b> ML ambiguity</div><div><b>03</b> AI explanation</div>
      </div>
    </aside>
  );
}
