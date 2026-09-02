import { AlertCircle, CheckCircle2, FileCheck2, Percent } from "lucide-react";
import Layout from "../components/layout/Layout";
import StatCard from "../components/dashboard/StatCard";
import PerformanceChart from "../components/dashboard/PerformanceChart";
import TierDistribution from "../components/dashboard/TierDistribution";

function Dashboard() {
  return <Layout><div className="page-heading"><div><div className="eyebrow">Operations overview</div><h1>Good morning, here is the money trail.</h1><p>Keep the clean matches moving and focus attention where certainty drops.</p></div><div className="date-chip">August 20, 2026</div></div><div className="stats-grid"><StatCard title="Total Transactions" value="10,248" subtitle="Across 6 connected sources" icon={FileCheck2} delay={0} /><StatCard title="Successfully Matched" value="9,721" subtitle="94.86% of all records" icon={CheckCircle2} delay={0.06} /><StatCard title="Automation Rate" value="94.86%" subtitle="Without LLM intervention" icon={Percent} delay={0.12} /><StatCard title="Exceptions" value="527" subtitle="Requires attention" icon={AlertCircle} delay={0.18} /></div><div className="dashboard-grid"><PerformanceChart /><TierDistribution /></div><div className="ai-banner"><div className="ai-banner-icon">✦</div><div><div className="ai-banner-title">AI is only used when deterministic matching cannot decide.</div><div className="ai-banner-text">Most transactions were resolved without an LLM call.</div></div><div className="ai-banner-stat">8.2%<span>LLM usage</span></div></div></Layout>;
}

export default Dashboard;
