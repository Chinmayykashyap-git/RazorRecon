const API_BASE = (import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1").replace(/\/$/, "");

async function request(path, options = {}) {
  let response;
  try {
    response = await fetch(`${API_BASE}${path}`, {
      headers: { "Content-Type": "application/json", ...(options.headers || {}) },
      ...options,
    });
  } catch {
    throw new Error("Razor Recon backend is unavailable");
  }
  if (!response.ok) {
    let detail = "";
    try {
      const body = await response.json();
      detail = body.message || body.error || "";
    } catch {
      detail = "";
    }
    throw new Error(detail || `Request failed (${response.status})`);
  }
  try {
    return await response.json();
  } catch {
    throw new Error("The backend returned an invalid response");
  }
}

export const api = {
  health: () => request("/health"),
  reconciliations: () => request("/reconcile"),
  exceptions: () => request("/reconcile/exceptions"),
  reconciliationAudit: (id) => request(`/reconcile/${id}/audit`),
  reconcile: (payload) => request("/reconcile", { method: "POST", body: JSON.stringify(payload) }),
};
