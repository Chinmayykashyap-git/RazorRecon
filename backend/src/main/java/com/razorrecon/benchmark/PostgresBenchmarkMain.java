package com.razorrecon.benchmark;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.razorrecon.RazorReconApplication;
import com.razorrecon.service.PersistentReconciliationService;

public final class PostgresBenchmarkMain {

    private PostgresBenchmarkMain() {}

    public static void main(String[] args) {
        String profile = args.length == 0 ? "benchmark-off" : args[0];
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(RazorReconApplication.class)
                .web(WebApplicationType.NONE).profiles(profile).run()) {
            PersistentReconciliationService service = context.getBean(PersistentReconciliationService.class);
            StringBuilder report = new StringBuilder("# PostgreSQL Persistence Benchmark\n\n");
            report.append("Profile: `").append(profile).append("`\n\n");
            report.append("| Dataset | Total time | Results |\n|---:|---:|---:|\n");
            for (int size : List.of(10_000, 50_000)) {
                DatasetGenerator.Dataset dataset = DatasetGenerator.generate(size);
                long start = System.nanoTime();
                var response = service.reconcile(dataset.payments(), dataset.settlements(), dataset.ledger());
                double seconds = (System.nanoTime() - start) / 1_000_000_000.0;
                report.append(String.format("| %,d | %.3f s | %,d |%n", size, seconds, response.results().size()));
            }
            System.out.print(report);
        }
    }
}
