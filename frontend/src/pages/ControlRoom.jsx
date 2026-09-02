import { useEffect, useState } from "react";
import { AlertTriangle, CheckCircle2, Database, Zap } from "lucide-react";
import { api } from "../api";
import StatCard from "../components/StatCard";
import StatusBadge from "../components/StatusBadge";

const sample = [
  { id: "TXN_83921", expectedAmount: 10000, actualAmount: 9700, status: "PARTIAL_SETTLEMENT" },
  { id: "TXN_83922", expectedAmount: 5000, actualAmount: 5000, status: "MATCHED" },
  { id: "TXN_83923", expectedAmount: 1200, actualAmount: null, status: "MISSING_SETTLEMENT" },
  { id: "TXN_83924", expectedAmount: 7000, actualAmount: 7000, status: "MANUAL_REVIEW" },
];

export default function ControlRoom({ navigate }) {
  const [exceptions, setExceptions] = useState([]);
  const [healthy, setHealthy] = useState(false);
  useEffect(() => { api.health().then(() => setHealthy(true)).catch(() => setHealthy(false)); api.exceptions().then(setExceptions).catch(() => setExceptions([])); }, []);
  const rows = exceptions.length ? exceptions.slice(0, 4).map((item) => ({ id: item.transactionId, expectedAmount: null, actualAmount: null, status: item.type })) : sample;
  return <div className="page">
    <header className="page-header"><div><span className="eyebrow">CONTROL ROOM / OVERVIEW</span><h1>Reconciliation control room</h1><p>Keep clean matches moving. Focus attention where certainty drops.</p></div><div className="live-indicator"><i className={healthy ? "online" : ""} />{healthy ? "LIVE" : "OFFLINE"}<small>22 AUG 2026</small></div></header>
    <section className="stats-grid"><StatCard label="TRANSACTIONS" value="50,000" detail="Across three source layers" /><StatCard label="MATCHED" value="44,832" detail="89.7% deterministic resolution" tone="positive" /><StatCard label="EXCEPTIONS" value="3,686" detail="Require operator attention" tone="warning" /><StatCard label="AMOUNT AT RISK" value="₹12.4L" detail="Across unresolved records" tone="danger" /></section>
    <div className="content-grid"><section className="panel"><div className="section-heading"><div><span className="eyebrow">RECENT EXCEPTIONS</span><h2>Attention queue</h2></div><button className="text-button" onClick={() => navigate("reconciliations")}>View explorer <span>→</span></button></div><div className="exception-list">{rows.map((row) => <button className="exception-row" key={row.id} onClick={() => navigate("exception", row)}><span className="row-icon"><AlertTriangle size={16} /></span><span className="row-main"><strong>{row.id || "Unidentified record"}</strong><small>Gateway → Settlement → Ledger</small></span><span className="row-amount">{row.expectedAmount == null ? "—" : `₹${row.expectedAmount.toLocaleString()}`} <b>→</b> {row.actualAmount == null ? "—" : `₹${row.actualAmount.toLocaleString()}`}</span><StatusBadge status={row.status} /><span className="row-arrow">→</span></button>)}</div></section><aside className="panel system-panel"><div className="section-heading"><div><span className="eyebrow">SYSTEM STATUS</span><h2>Three layers</h2></div><Zap size={19} /></div><div className="system-row"><Database size={17} /><span>Database</span><b className="healthy">Operational</b></div><div className="system-row"><CheckCircle2 size={17} /><span>Core engine</span><b className="healthy">Operational</b></div><div className="system-row"><AlertTriangle size={17} /><span>AI explanation</span><b className="muted">Optional</b></div><div className="architecture-note">Deterministic rules decide financial truth. Intelligence only explains uncertainty.</div></aside></div>
  </div>;
}
