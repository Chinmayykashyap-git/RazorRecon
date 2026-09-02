export default function ReconciliationGraph({ payment, settlement, ledger }) {
  const node = (type, id, amount) => <div className="graph-node"><span>{type}</span><strong>{id || "Not found"}</strong><b>{amount == null ? "—" : `₹${Number(amount).toLocaleString()}`}</b></div>;
  return <div className="reconciliation-graph">{node("PAYMENT", payment?.transactionId, payment?.amount)}<div className="graph-connector">deterministic match</div><div className="graph-engine"><b>R</b><span>RECON ENGINE<small>Evidence comparison</small></span></div><div className="graph-branches">{node("SETTLEMENT", settlement?.settlementId, settlement?.amount)}{node("LEDGER", ledger?.ledgerId, ledger?.amount)}</div></div>;
}
