package com.razorrecon.ingestion;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CsvImporter {

    public List<String[]> read(Path filePath) throws IOException {

        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {

            String line;

            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                records.add(line.split(","));
            }
        }

        return records;
    }

    public CsvImportResult readValidated(Path filePath, int requiredColumns) throws IOException {
        List<String[]> validRecords = new ArrayList<>();
        List<String[]> invalidRecords = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split(",", -1);
                boolean hasRequiredFields = fields.length >= requiredColumns;
                for (String field : fields) {
                    hasRequiredFields = hasRequiredFields && !field.isBlank();
                }
                (hasRequiredFields ? validRecords : invalidRecords).add(fields);
            }
        }
        return new CsvImportResult(validRecords, invalidRecords);
    }
}