import { motion } from "framer-motion";
import { Play } from "lucide-react";
import { useState } from "react";
import Layout from "../components/layout/Layout";
import { reconcile } from "../services/api";

const transactions = [
  { id: "TXN-1001", amount: "₹1,000.00", status: "MATCHED", tier: "TIER_1", confidence: "100%" },
  { id: "TXN-1002", amount: "₹4,210.00", status: "MATCHED", tier: "TIER_2", confidence: "94%" },
  { id: "TXN-1003", amount: "₹2,850.00", status: "AMBIGUOUS", tier: "DECISION", confidence: "72%" },
  { id: "TXN-1004", amount: "₹780.00", status: "UNMATCHED", tier: "DECISION", confidence: "18%" }
];

function Reconciliation() {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  async function runTest() {
    setLoading(true);
    try {
      setResult(await reconcile(
        {
          transactionId: "TXN-1001",
          reference: "INV-1001",
          amount: 1000,
          transactionDate: "2026-08-20T10:00:00"
        },
        [{
          ledgerId: "LEDGER-1001",
          reference: "INV-1001",
          amount: 1000,
          entryDate: "2026-08-20T11:00:00",
          type: "PAYMENT"
        }]
      ));
    } catch (error) {
      setResult({ error: error.message });
    } finally {
      setLoading(false);
    }
  }

  return (
    <Layout>
      <div className="page-heading">
        <div><div className="eyebrow">Transaction ledger</div><h1>Reconciliation</h1><p>Review recent decisions and run a live test against the backend engine.</p></div>
        <button className="primary-button" onClick={runTest} disabled={loading}><Play size={16} />{loading ? "Running..." : "Run test"}</button>
      </div>
      <motion.div className="table-card" initial={{ opacity: 0, y: 12 }} animate={{ opacity: 1, y: 0 }}>
        <table><thead><tr><th>Transaction</th><th>Amount</th><th>Status</th><th>Tier</th><th>Confidence</th></tr></thead><tbody>
          {transactions.map((transaction) => <tr key={transaction.id}><td><strong>{transaction.id}</strong><span className="table-secondary">Payment received</span></td><td>{transaction.amount}</td><td><span className={`status-badge ${transaction.status.toLowerCase()}`}>{transaction.status}</span></td><td>{transaction.tier}</td><td>{transaction.confidence}</td></tr>)}
        </tbody></table>
      </motion.div>
      {result && <pre className="result">{JSON.stringify(result, null, 2)}</pre>}
    </Layout>
  );
}

export default Reconciliation;
