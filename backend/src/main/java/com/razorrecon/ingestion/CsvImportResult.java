package com.razorrecon.ingestion;

import java.util.List;

public record CsvImportResult(List<String[]> validRecords, List<String[]> invalidRecords) {
}