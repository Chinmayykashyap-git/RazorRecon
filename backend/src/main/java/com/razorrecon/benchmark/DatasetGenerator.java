package com.razorrecon.benchmark;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;

public final class DatasetGenerator {

    private static final long SEED = 42L;
    private static final LocalDateTime START = LocalDateTime.of(2026, 8, 21, 10, 0);

    private DatasetGenerator() {}

    public static Dataset generate(int paymentCount) {
        Random random = new Random(SEED);
        List<Payment> payments = new ArrayList<>(paymentCount);
        List<Settlement> settlements = new ArrayList<>(paymentCount);
        List<LedgerEntry> ledger = new ArrayList<>(paymentCount);

        for (int index = 1; index <= paymentCount; index++) {
            String transactionId = id("TXN", index);
            String reference = id("REF", index);
            BigDecimal amount = BigDecimal.valueOf(500L + random.nextInt(950001), 2);
            LocalDateTime timestamp = START.plusSeconds(random.nextInt(86400));
                Payment payment = new Payment(transactionId, id("ORD", index), "M001", amount, "INR",
                    "SUCCESS", timestamp, "UPI", reference);
            payments.add(payment);
                ledger.add(new LedgerEntry(id("LED", index), reference, amount,
                    timestamp.plusSeconds(30), "CREDIT"));

            int scenario = index % 100;
            if (scenario >= 90 && scenario < 92) {
                settlements.add(new Settlement(id("SET", index), reference, "M001",
                    amount.multiply(new BigDecimal("0.97")), "INR", timestamp.plusMinutes(2), "SETTLED"));
            } else if (scenario >= 92 && scenario < 94) {
                settlements.add(new Settlement(id("SET", index), reference, "M001",
                    amount.add(new BigDecimal("25.00")), "INR", timestamp.plusMinutes(2), "SETTLED"));
            } else if (scenario >= 94 && scenario < 96) {
                // Missing settlement is intentional.
            } else if (scenario == 96) {
                settlements.add(new Settlement(id("SET", index), reference, "M001", amount,
                    "INR", timestamp.plusMinutes(2), "SETTLED"));
                settlements.add(new Settlement(id("SET-DUP", index), reference, "M001", amount,
                    "INR", timestamp.plusMinutes(2), "SETTLED"));
            } else if (scenario >= 97 && scenario < 99) {
                settlements.add(new Settlement(id("SET", index), reference, "M001", amount,
                    "INR", timestamp.plusMinutes(2), "SETTLED"));
                settlements.add(new Settlement(id("SET-AMB", index), reference, "M001", amount,
                    "INR", timestamp.plusMinutes(3), "SETTLED"));
            } else {
                settlements.add(new Settlement(id("SET", index), reference, "M001", amount,
                    "INR", timestamp.plusMinutes(2), "SETTLED"));
            }
        }

        int missingPaymentCount = Math.max(1, paymentCount / 100);
        for (int offset = 1; offset <= missingPaymentCount; offset++) {
            String reference = id("REF-MISSING", offset);
            BigDecimal amount = BigDecimal.valueOf(1000L + offset);
            LocalDateTime timestamp = START.plusMinutes(offset);
                settlements.add(new Settlement(id("SET-MISSING", offset), reference, "M001", amount,
                    "INR", timestamp, "SETTLED"));
            ledger.add(new LedgerEntry(id("LED-MISSING", offset), reference, amount,
                    timestamp, "CREDIT"));
        }
        return new Dataset(payments, settlements, ledger);
    }

    public static void writeCsv(Dataset dataset, Path directory) throws IOException {
        Files.createDirectories(directory);
        List<String> paymentLines = new ArrayList<>();
        paymentLines.add("transaction_id,order_id,merchant_id,amount,currency,status,timestamp,payment_method,reference");
        for (Payment payment : dataset.payments()) {
            paymentLines.add(String.join(",", payment.getPaymentId(), payment.getOrderId(), "M001",
                    payment.getAmount().toPlainString(), payment.getCurrency(), payment.getStatus(),
                    payment.getCreatedAt().toString(), "UPI", payment.getReference()));
        }
        List<String> settlementLines = new ArrayList<>();
        settlementLines.add("settlement_id,transaction_id,merchant_id,amount,currency,timestamp,status,reference");
        for (Settlement settlement : dataset.settlements()) {
            settlementLines.add(String.join(",", settlement.getSettlementId(), settlement.getReference(), "M001",
                    settlement.getAmount().toPlainString(), "INR", settlement.getSettlementDate().toString(),
                    settlement.getStatus(), settlement.getReference()));
        }
        List<String> ledgerLines = new ArrayList<>();
        ledgerLines.add("ledger_id,transaction_id,merchant_id,amount,currency,timestamp,entry_type,reference");
        for (LedgerEntry entry : dataset.ledger()) {
            ledgerLines.add(String.join(",", entry.getLedgerId(), entry.getReference(), "M001",
                    entry.getAmount().toPlainString(), "INR", entry.getEntryDate().toString(),
                    entry.getType(), entry.getReference()));
        }
        Files.write(directory.resolve("payments-50k.csv"), paymentLines);
        Files.write(directory.resolve("settlements-50k.csv"), settlementLines);
        Files.write(directory.resolve("ledger-50k.csv"), ledgerLines);
    }

    private static String id(String prefix, int index) {
        return String.format(Locale.ROOT, "%s_%06d", prefix, index);
    }

    public record Dataset(List<Payment> payments, List<Settlement> settlements, List<LedgerEntry> ledger) {}
}
