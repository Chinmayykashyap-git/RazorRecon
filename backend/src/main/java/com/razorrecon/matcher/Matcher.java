package com.razorrecon.matcher;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;

public interface Matcher {

    MatchResult match(
            BankTransaction transaction,
            LedgerEntry ledgerEntry
    );
}