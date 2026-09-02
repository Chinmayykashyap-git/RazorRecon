CREATE INDEX idx_reconciliation_status ON reconciliation_results(status);
CREATE INDEX idx_reconciliation_request ON reconciliation_results(reconciliation_id, created_at);
CREATE INDEX idx_exceptions_reconciliation ON exceptions(reconciliation_id, created_at);
