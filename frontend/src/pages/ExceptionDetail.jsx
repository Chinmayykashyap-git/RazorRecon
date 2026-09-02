import { useEffect, useState } from "react";
import { ArrowLeft, Bot, CircleAlert } from "lucide-react";
import { api } from "../api";
import ReconciliationGraph from "../components/ReconciliationGraph";
import AuditTimeline from "../components/AuditTimeline";
import StatusBadge from "../components/StatusBadge";

export default function ExceptionDetail({ exception, navigate }) {
  const [audit, setAudit] = useState([]);
  useEffect(() => { if (exception?.id) api.reconciliationAudit(exception.id).then(setAudit).catch(() => setAudit([])); }, [exception]);
  const payment = { transactionId: exception?.id || "TXN_83921", amount: exception?.expectedAmount ?? exception?.paymentAmount ?? 10000 };
  const settlement = { settlementId: "SET_83921", amount: exception?.actualAmount ?? exception?.settlementAmount ?? null };
  const ledger = { ledgerId: "LED_83921", amount: payment.amount };
  return <div className="page"><button className="back-button" onClick={() => navigate("reconciliations")}><ArrowLeft size={16} /> Back to explorer</button><header className="detail-header"><div><span className="eyebrow">EXCEPTION / INVESTIGATION</span><h1>{payment.transactionId}</h1><p>Evidence chain and operator decision context.</p></div><StatusBadge status={exception?.status || "PARTIAL_SETTLEMENT"} /></header><section className="panel evidence-panel"><div className="section-heading"><div><span className="eyebrow">EVIDENCE CHAIN</span><h2>Three sources, one decision</h2></div><CircleAlert size={20} /></div><ReconciliationGraph payment={payment} settlement={settlement} ledger={ledger} /><div className="difference-strip"><span>DIFFERENCE</span><strong>₹{Math.abs((payment.amount || 0) - (settlement.amount || 0)).toLocaleString()}</strong><small>Deterministic analysis: {exception?.status || "PARTIAL_SETTLEMENT"}</small></div></section><div className="content-grid detail-grid"><section className="panel"><div className="section-heading"><div><span className="eyebrow">EXPLANATION</span><h2>Operator context</h2></div><Bot size={19} /></div><p className="explanation">The settlement amount differs from the payment evidence. Merchant and currency should be verified before resolving this exception.</p><div className="evidence-points"><span>✓ Payment record available</span><span>✓ Settlement record available</span><span>! Ledger evidence requires review</span></div></section><section className="panel"><div className="section-heading"><div><span className="eyebrow">AUDIT TRAIL</span><h2>Lifecycle</h2></div></div><AuditTimeline events={audit} /></section></div></div>;
}
