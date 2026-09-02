CREATE INDEX idx_audit_reconciliation_created ON audit_logs(reconciliation_id, created_at);
CREATE INDEX idx_audit_transaction_created ON audit_logs(transaction_id, created_at);
CREATE INDEX idx_exceptions_type ON exceptions(type);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);
CREATE INDEX idx_settlements_reference ON settlements(reference);
CREATE INDEX idx_ledger_entries_reference ON ledger_entries(reference);
