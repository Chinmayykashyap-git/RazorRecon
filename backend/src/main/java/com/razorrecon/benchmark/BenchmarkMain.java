package com.razorrecon.benchmark;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.razorrecon.model.ThreeWayReconciliationResult;
import com.razorrecon.model.ThreeWayReconciliationResult.Status;
import com.razorrecon.service.ThreeWayReconciliationService;

public final class BenchmarkMain {

    private BenchmarkMain() {}

    public static void main(String[] args) throws Exception {
        Path output = Path.of(args.length == 0 ? "../data/generated" : args[0]);
        int[] sizes = {1_000, 10_000, 50_000, 100_000};
        StringBuilder report = new StringBuilder("# Razor Recon Benchmark\n\n| Dataset | Time | Throughput | Outcomes |\n|---:|---:|---:|---|\n");
        ThreeWayReconciliationService service = new ThreeWayReconciliationService();

        for (int size : sizes) {
            DatasetGenerator.Dataset dataset = DatasetGenerator.generate(size);
            if (size == 50_000) {
                DatasetGenerator.writeCsv(dataset, output);
            }
            long start = System.nanoTime();
                List<ThreeWayReconciliationResult> results = service.reconcile(
                    dataset.payments(), dataset.settlements(), dataset.ledger());
            long elapsedNanos = System.nanoTime() - start;
            double seconds = elapsedNanos / 1_000_000_000.0;
            double throughput = results.size() / seconds;
            Map<Status, Long> counts = new EnumMap<>(Status.class);
            for (ThreeWayReconciliationResult result : results) {
                counts.put(result.status(), counts.getOrDefault(result.status(), 0L) + 1L);
            }
            report.append(String.format("| %,d | %.3f s | %,.0f/sec | %s |%n", size, seconds, throughput, counts));
        }
        Path reportPath = output.resolve("../benchmark-results.md").normalize();
        java.nio.file.Files.createDirectories(reportPath.getParent());
        java.nio.file.Files.writeString(reportPath, report);
        System.out.print(report);
    }
}
