export default function StatusBadge({ status }) {
  const label = String(status || "UNKNOWN").replaceAll("_", " ");
  return <span className={`status-badge ${label.toLowerCase().replaceAll(" ", "-")}`}><i />{label}</span>;
}
