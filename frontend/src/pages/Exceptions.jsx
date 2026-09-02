import { AlertTriangle } from "lucide-react";
import Layout from "../components/layout/Layout";

const exceptions = [["NO_COUNTERPART", 21], ["AMOUNT_MISMATCH_UNEXPLAINED", 14], ["AMBIGUOUS_MULTIPLE_CANDIDATES", 9], ["DUPLICATE_TRANSACTION", 4]];

function Exceptions() {
  return <Layout><div className="page-heading"><div><div className="eyebrow">Human attention required</div><h1>Exceptions</h1><p>Transactions outside the confidence boundaries of the automated engine.</p></div></div><div className="exception-grid">{exceptions.map(([name, count]) => <div className="exception-card" key={name}><div className="exception-icon"><AlertTriangle size={19} /></div><span>{name}</span><strong>{count}</strong><small>open cases</small></div>)}</div></Layout>;
}

export default Exceptions;
