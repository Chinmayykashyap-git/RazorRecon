import { BrainCircuit, ShieldCheck, TriangleAlert } from "lucide-react";
import Layout from "../components/layout/Layout";

function Investigations() {
  return <Layout><div className="page-heading"><div><div className="eyebrow">Tier 3 review queue</div><h1>Investigations</h1><p>Guarded AI reasoning for transactions deterministic rules could not resolve.</p></div><div className="queue-count">1 active case</div></div><div className="investigation-card"><div className="investigation-header"><div className="ai-icon"><BrainCircuit size={24} /></div><div><h2>TXN-10293</h2><p>₹4,210 · Tier 3 investigation</p></div><span className="confidence-badge">87% confidence</span></div><div className="investigation-columns"><div className="evidence-section"><h3>Evidence</h3><div className="evidence-item"><ShieldCheck size={17} />Amount similarity</div><div className="evidence-item"><ShieldCheck size={17} />Settlement window</div><div className="evidence-item"><ShieldCheck size={17} />Reference similarity</div><div className="evidence-item warning"><TriangleAlert size={17} />Reference partially corrupted</div></div><div className="reasoning-section"><h3>AI reasoning</h3><p>Candidate LEDGER-883 has the strongest combination of amount, settlement date, and reference similarity. The reference contains a minor corruption, but the surrounding transaction context is consistent.</p><button className="secondary-button">Review candidate</button></div></div></div></Layout>;
}

export default Investigations;
