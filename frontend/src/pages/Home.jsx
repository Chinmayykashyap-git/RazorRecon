import { useEffect, useState } from "react";
import { getHealth } from "../services/api";

function Home() {
  const [health, setHealth] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    getHealth().then(setHealth).catch((requestError) => setError(requestError.message));
  }, []);

  return (
    <section className="page-grid">
      <div className="intro">
        <p className="eyebrow">Payment operations / Phase 16</p>
        <h1>Reconcile the money trail with confidence.</h1>
        <p className="lede">Deterministic matching, rule-based explanations, and a guarded investigation path for the exceptions that need human attention.</p>
      </div>
      <div className="status-panel">
        <span className="eyebrow">System status</span>
        {error ? <p className="error">{error}</p> : health ? (
          <>
            <strong><span className="status-dot" /> {health.status}</strong>
            <dl>
              <div><dt>Engine</dt><dd>{health.engine}</dd></div>
              <div><dt>LLM</dt><dd>{health.llm}</dd></div>
            </dl>
          </>
        ) : <p>Connecting to backend...</p>}
      </div>
    </section>
  );
}

export default Home;
