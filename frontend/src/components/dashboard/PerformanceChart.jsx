import { Area, AreaChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

const data = [
  { day: "Mon", matched: 1200 }, { day: "Tue", matched: 1640 }, { day: "Wed", matched: 1380 },
  { day: "Thu", matched: 1920 }, { day: "Fri", matched: 1780 }, { day: "Sat", matched: 2140 }, { day: "Sun", matched: 2420 }
];

function PerformanceChart() {
  return (
    <div className="chart-card">
      <div className="section-header"><div><h2>Reconciliation performance</h2><p>Successfully reconciled transactions</p></div><div className="chart-period">Last 7 days</div></div>
      <div className="chart-container"><ResponsiveContainer width="100%" height="100%"><AreaChart data={data} margin={{ top: 10, right: 8, left: -22, bottom: 0 }}>
        <defs><linearGradient id="reconciliationGradient" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor="#d93f35" stopOpacity={0.25} /><stop offset="100%" stopColor="#d93f35" stopOpacity={0} /></linearGradient></defs>
        <XAxis dataKey="day" axisLine={false} tickLine={false} tick={{ fill: "#8a929b", fontSize: 12 }} /><YAxis axisLine={false} tickLine={false} tick={{ fill: "#8a929b", fontSize: 12 }} /><Tooltip /><Area type="monotone" dataKey="matched" stroke="#d93f35" strokeWidth={2} fill="url(#reconciliationGradient)" />
      </AreaChart></ResponsiveContainer></div>
    </div>
  );
}

export default PerformanceChart;
