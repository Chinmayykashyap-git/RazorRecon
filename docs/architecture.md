# Razor Recon Architecture

Razor Recon reconciles three independent financial records: a payment, its settlement, and the merchant ledger entry.

## Decision path

```text
POST /api/v1/reconcile
  -> validate and persist source records
  -> index settlements by reference
  -> index ledger entries by reference
  -> scan payments and classify each decision
  -> persist result and create an exception for non-MATCHED decisions
  -> append lifecycle audit events
```

The engine is deterministic. It computes `expectedAmount - actualAmount` and returns machine-readable statuses including `MATCHED`, `PARTIAL_SETTLEMENT`, `AMOUNT_MISMATCH`, `MISSING_PAYMENT`, `MISSING_SETTLEMENT`, `DUPLICATE`, `INVALID_RECORD`, and `MANUAL_REVIEW`.

## Complexity

Settlements and ledger entries are indexed once, then payments are scanned once. For `P` payments, `S` settlements, and `L` ledger entries, the matching work is O(P + S + L) expected time and O(S + L) auxiliary memory. The reported benchmark is an in-memory engine measurement; it is not a PostgreSQL latency claim.

## Persistence and reliability

Each request is hashed from its canonical source records. The hash is protected by a database uniqueness constraint, so retrying an identical request returns `ALREADY_PROCESSED` instead of writing duplicate financial results. The service method is transactional, so source records, decisions, exceptions, and audit events commit or roll back together.

The current implementation guarantees idempotent retries within the persistence boundary. A multi-instance deployment should additionally use database locking or an idempotency service around request ownership to coordinate simultaneous first-seen requests.

## ML boundary

`ml/train.py` trains a local Random Forest from `data/ml-training.csv` using amount difference, timestamp difference, merchant/currency/payment-method matches, and reference similarity. It reports held-out evaluation metrics and stores the model with probability thresholds.

The Java reconciliation service does not use the model to decide financial truth. ML is an optional candidate-ranking layer for ambiguity; if model inference is unavailable, deterministic reconciliation remains operational. Training metrics must be generated with the script and must not be presented as production performance.
