import { useEffect, useState } from "react";
import { Search, SlidersHorizontal } from "lucide-react";
import { api } from "../api";
import StatusBadge from "../components/StatusBadge";

const fallback = [
  { id: "TXN_83921", expectedAmount: 10000, actualAmount: 9700, status: "PARTIAL_SETTLEMENT", confidence: .98 },
  { id: "TXN_83922", expectedAmount: 5000, actualAmount: 5000, status: "MATCHED", confidence: 1 },
  { id: "TXN_83923", expectedAmount: 1200, actualAmount: null, status: "MISSING_SETTLEMENT", confidence: 0 },
  { id: "TXN_83924", expectedAmount: 7000, actualAmount: 7000, status: "MANUAL_REVIEW", confidence: .61 },
];

export default function Reconciliations({ navigate }) {
  const [rows, setRows] = useState([]); const [search, setSearch] = useState("");
  useEffect(() => { api.reconciliations().then(setRows).catch(() => setRows([])); }, []);
  const data = rows.length ? rows.map((item) => ({ ...item, id: item.paymentId || item.id, expectedAmount: item.expectedAmount, actualAmount: item.actualAmount })) : fallback;
  const filtered = data.filter((row) => String(row.id || "").toLowerCase().includes(search.toLowerCase()));
  return <div className="page"><header className="page-header"><div><span className="eyebrow">RECONCILIATIONS / EXPLORER</span><h1>Transaction explorer</h1><p>Inspect every decision across the payment, settlement, and ledger layers.</p></div></header><div className="toolbar"><div className="search-wrap"><Search size={17} /><input placeholder="Search transaction ID..." value={search} onChange={(event) => setSearch(event.target.value)} /></div><button className="filter-button"><SlidersHorizontal size={16} />All statuses</button></div><section className="panel table-panel"><div className="table-header"><span>TRANSACTION</span><span>PAYMENT</span><span>SETTLEMENT</span><span>STATUS</span><span>CONFIDENCE</span><span /></div>{filtered.map((row) => <button className="table-row" key={`${row.id}-${row.status}`} onClick={() => navigate("exception", row)}><strong className="transaction-id">{row.id || "UNKNOWN"}</strong><span>₹{Number(row.expectedAmount || 0).toLocaleString()}</span><span>{row.actualAmount == null ? "—" : `₹${Number(row.actualAmount).toLocaleString()}`}</span><StatusBadge status={row.status} /><span className="confidence">{row.confidence ? `${Math.round(row.confidence * 100)}%` : "—"}</span><span className="row-arrow">→</span></button>)}</section></div>;
}
