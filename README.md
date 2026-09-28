# Razor Recon

AI assisted three-way payment reconciliation for systems where `payments`, `settlements`, and the merchant `ledger` can disagree.

## Problem

A gateway may report a successful payment while settlement contains a fee-adjusted amount or the ledger has no corresponding entry. Razor Recon preserves the evidence, applies deterministic financial rules, identifies exceptions, and leaves an auditable lifecycle.

The live demo connects to the Razor Recon frontend and demonstrates the reconciliation workflow, exception detection, and transaction analysis.
https://razorrecon.netlify.app

## Architecture

```text
CSV ingestion
  -> validation and quarantine
  -> PostgreSQL persistence through Flyway
  -> deterministic reconciliation
  -> candidate features for future ML ambiguity scoring
  -> human review
  -> explanation-only local fallback
```

The core API is available at:

```text
POST /api/v1/reconcile
GET  /api/v1/reconcile
GET  /api/v1/reconcile/{id}/audit
GET  /api/v1/reconcile/exceptions
GET  /api/v1/health
```

## AI Judgment

AI is deliberately not used for deterministic financial reconciliation. The deterministic engine remains the source of truth. Candidate features and the local Random Forest trainer are introduced only when exact matching is insufficient. The `ExplanationService` is restricted to summarizing supplied evidence and suggesting investigation steps; it cannot match, settle, delete, refund, or modify financial state.

## Failure Recovery

- Duplicate request: stable request hash returns `ALREADY_PROCESSED` without duplicate financial records.
- Malformed data: validated CSV rows are separated into valid and invalid records so good rows continue.
- Database failure: the reconciliation write path is transactional and rolls back source, result, exception, and audit writes together.
- Missing settlement/payment: classified as a machine-readable exception.
- AI unavailable: deterministic reconciliation remains operational; the local explanation fallback can still provide an evidence-based summary.
- Auditability: each reconciliation request receives an ID and lifecycle events are queryable.

## Performance

The in-memory deterministic engine benchmark uses seeded synthetic data and indexed ledger lookup:

| Dataset | Processing Time | Throughput |
|---:|---:|---:|
| 1,000 | 0.007 s | 150,957/sec |
| 10,000 | 0.026 s | 441,684/sec |
| 50,000 | 0.053 s | 1,060,629/sec |
| 100,000 | 0.084 s | 1,340,963/sec |

These are in-memory measurements, not PostgreSQL timings. The PostgreSQL benchmark harness exists with batching on/off profiles, but was not executed because Docker and PostgreSQL were unavailable in the development environment.

## ML Training Data

`com.razorrecon.ml.TrainingDatasetMain` generates the deterministic `data/ml-training.csv` pair dataset. `ml/train.py` trains a local Random Forest, reports accuracy, precision, recall, F1, ROC-AUC, and a confusion matrix, then saves `ml/model/matcher.joblib`.

Python is an optional local tool and was not available in the current environment, so no model metrics are claimed.

## Run and Test

```text
cd backend
mvn spring-boot:run
mvn clean test
```

The backend uses Flyway migrations under `backend/src/main/resources/db/migration/`; Hibernate is configured for schema validation rather than automatic production schema changes.

## Dataset

The seeded generator uses `Random(42)` and produces exact, partial, mismatch, missing, duplicate, ambiguous, and missing-payment scenarios. Generated CSVs are under `data/generated/`; benchmark outcome counts are in `data/benchmark-results.md`.
