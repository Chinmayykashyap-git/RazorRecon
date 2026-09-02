package com.razorrecon.ml;

import java.nio.file.Path;

public final class TrainingDatasetMain {

    private TrainingDatasetMain() {}

    public static void main(String[] args) throws Exception {
        Path output = Path.of(args.length == 0 ? "../data/ml-training.csv" : args[0]);
        TrainingDatasetGenerator.write(output, 50_000);
        System.out.println("Wrote deterministic labeled training pairs to " + output.toAbsolutePath());
    }
}
