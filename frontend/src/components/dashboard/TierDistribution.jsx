import { motion } from "framer-motion";

const tiers = [
  { label: "Tier 1 · Exact", percentage: 63, color: "#d93f35" },
  { label: "Tier 2 · Rules", percentage: 25, color: "#e58c47" },
  { label: "Tier 3 · LLM", percentage: 8, color: "#6876b5" },
  { label: "Unresolved", percentage: 4, color: "#c8ced3" }
];

function TierDistribution() {
  return <div className="tier-card"><div className="section-header"><div><h2>Resolution tiers</h2><p>How decisions were made</p></div></div><div className="tier-list">{tiers.map((tier, index) => <motion.div className="tier-row" key={tier.label} initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ delay: index * 0.1 }}><div className="tier-row-label"><span className="tier-dot" style={{ background: tier.color }} />{tier.label}<strong>{tier.percentage}%</strong></div><div className="tier-track"><motion.div className="tier-progress" style={{ background: tier.color }} initial={{ width: 0 }} animate={{ width: `${tier.percentage}%` }} transition={{ duration: 0.8, delay: index * 0.1 }} /></div></motion.div>)}</div></div>;
}

export default TierDistribution;
