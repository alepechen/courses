package com.example.course.utils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtil {
    public static <T> byte[] generateCsv(List<T> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return new byte[0];
        }

        Class<?> data = dataList.get(0).getClass();
        Field[] fields = data.getDeclaredFields();

        StringBuilder csvBuilder = new StringBuilder();

        // Header
        String header = List.of(fields).stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        csvBuilder.append(header).append("\n");
        // Rows
        for (T item : dataList) {
            String row = List.of(fields).stream()
                    .map(field -> {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(item);
                            return value != null ? value.toString().replace(",", "") : ""; // optional: escape commas
                        } catch (IllegalAccessException e) {
                            return "";
                        }
                    })
                    .collect(Collectors.joining(","));
            csvBuilder.append(row).append("\n");
        }
        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
