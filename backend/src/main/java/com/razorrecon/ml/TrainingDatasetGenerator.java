package com.razorrecon.ml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.razorrecon.benchmark.DatasetGenerator;
import com.razorrecon.engine.CandidateFeatureExtractor;
import com.razorrecon.model.CandidateFeatures;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;

public final class TrainingDatasetGenerator {

    private TrainingDatasetGenerator() {}

    public static void write(Path output, int paymentCount) throws IOException {
        DatasetGenerator.Dataset dataset = DatasetGenerator.generate(paymentCount);
        CandidateFeatureExtractor extractor = new CandidateFeatureExtractor();
        Map<String, Settlement> positiveByReference = new HashMap<>();
        for (Settlement settlement : dataset.settlements()) {
            positiveByReference.putIfAbsent(settlement.getReference(), settlement);
        }
        List<String> lines = new ArrayList<>();
        lines.add("amount_difference,timestamp_difference_seconds,merchant_match,currency_match,payment_method_match,reference_similarity,label");
        for (int index = 0; index < dataset.payments().size(); index++) {
            Payment payment = dataset.payments().get(index);
            Settlement positive = positiveByReference.get(payment.getReference());
            if (positive != null) {
                lines.add(row(extractor.extract(payment, positive), 1));
            }
            Settlement negative = dataset.settlements().get((index + 1) % dataset.settlements().size());
            if (negative != positive) {
                lines.add(row(extractor.extract(payment, negative), 0));
            }
        }
        Files.createDirectories(output.toAbsolutePath().getParent());
        Files.write(output, lines);
    }

    private static String row(CandidateFeatures features, int label) {
        return String.join(",", features.amountDifference().toPlainString(),
                Long.toString(features.timestampDifferenceSeconds()),
                Boolean.toString(features.merchantMatch()), Boolean.toString(features.currencyMatch()),
                Boolean.toString(features.paymentMethodMatch()), Double.toString(features.referenceSimilarity()),
                Integer.toString(label));
    }
}
