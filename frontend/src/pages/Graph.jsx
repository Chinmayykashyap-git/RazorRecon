import { ArrowDown, CircleDollarSign } from "lucide-react";
import Layout from "../components/layout/Layout";

function Graph() {
  return <Layout><div className="page-heading"><div><div className="eyebrow">Relationship explorer</div><h1>Money graph</h1><p>Trace the lifecycle of a payment from customer intent to bank settlement.</p></div></div><div className="money-graph"><div className="graph-node"><span>Customer</span><strong>Acme Retail</strong></div><ArrowDown className="graph-arrow" /><div className="graph-node"><span>Order</span><strong>#8291</strong></div><ArrowDown className="graph-arrow" /><div className="graph-node payment-node"><CircleDollarSign size={22} /><span>Payment</span><strong>₹4,210</strong></div><ArrowDown className="graph-arrow" /><div className="graph-node"><span>Settlement</span><strong>SET-5512</strong></div><ArrowDown className="graph-arrow" /><div className="graph-node"><span>Bank transaction</span><strong>TXN-10293</strong></div></div></Layout>;
}

export default Graph;
