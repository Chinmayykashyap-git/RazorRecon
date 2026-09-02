const API_BASE = "http://localhost:8080/api";

async function request(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`Request failed with HTTP ${response.status}`);
  }
  return response.json();
}

export function getHealth() {
  return request(`${API_BASE}/dashboard/health`);
}

export function reconcile(transaction, ledgerEntries) {
  return request(`${API_BASE}/reconciliation/match`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ transaction, ledgerEntries })
  });
}
