export default function AuditTimeline({ events = [] }) {
  const fallback = ["INGESTION_STARTED", "PAYMENT_PERSISTED", "SETTLEMENT_PERSISTED", "LEDGER_PERSISTED", "RECONCILIATION_STARTED", "MATCH_ATTEMPT", "EXCEPTION_CREATED", "RECONCILIATION_COMPLETED"];
  const items = events.length ? events : fallback.map((action) => ({ action, createdAt: null }));
  return <div className="timeline">{items.map((event, index) => <div className="timeline-item" key={`${event.action}-${index}`}><div className="timeline-marker">{index === items.length - 1 ? "✓" : ""}</div><div><strong>{event.action?.replaceAll("_", " ") || event}</strong><span>{event.createdAt ? new Date(event.createdAt).toLocaleTimeString() : "Awaiting event"}</span></div></div>)}</div>;
}
