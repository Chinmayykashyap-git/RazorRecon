# Razor Recon Benchmark

| Dataset | Time | Throughput | Outcomes |
|---:|---:|---:|---|
| 1,000 | 0.007 s | 150,957/sec | {MATCHED=910, AMOUNT_MISMATCH=20, MISSING_PAYMENT=130, MISSING_SETTLEMENT=20, DUPLICATE=30, PARTIAL_SETTLEMENT=20} |
| 10,000 | 0.026 s | 441,684/sec | {MATCHED=9100, AMOUNT_MISMATCH=200, MISSING_PAYMENT=1300, MISSING_SETTLEMENT=200, DUPLICATE=300, PARTIAL_SETTLEMENT=200} |
| 50,000 | 0.053 s | 1,060,629/sec | {MATCHED=45500, AMOUNT_MISMATCH=1000, MISSING_PAYMENT=6500, MISSING_SETTLEMENT=1000, DUPLICATE=1500, PARTIAL_SETTLEMENT=1000} |
| 100,000 | 0.084 s | 1,340,963/sec | {MATCHED=91000, AMOUNT_MISMATCH=2000, MISSING_PAYMENT=13000, MISSING_SETTLEMENT=2000, DUPLICATE=3000, PARTIAL_SETTLEMENT=2000} |

## PostgreSQL Persistence Benchmark

Target: `RazorReconDB` on `localhost:5432` using the existing Spring PostgreSQL configuration. The connection succeeded and Flyway validated and applied migrations through version 3 on PostgreSQL 18.6.

| Profile | 10K | 50K |
|---|---:|---:|
| Batching off | Not recorded: run did not complete | Not recorded |
| Batch size 50 | Not run | Not run |

No PostgreSQL timing is reported because the batching-off persistence run did not complete. The process was stopped before producing measurements; batching-on was not started. The in-memory figures above are not PostgreSQL results.
