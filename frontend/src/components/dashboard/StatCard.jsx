import { motion } from "framer-motion";

function StatCard({ title, value, subtitle, icon: Icon, delay = 0 }) {
  return (
    <motion.div className="stat-card" initial={{ opacity: 0, y: 12 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.45, delay }} whileHover={{ y: -3 }}>
      <div className="stat-card-header"><span className="stat-title">{title}</span><div className="stat-icon"><Icon size={18} /></div></div>
      <div className="stat-value">{value}</div>
      <div className="stat-subtitle">{subtitle}</div>
    </motion.div>
  );
}

export default StatCard;
