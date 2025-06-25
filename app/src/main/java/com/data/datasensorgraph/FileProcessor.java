package com.data.datasensorgraph;


import java.io.*;
        import java.nio.file.*;
        import java.util.*;
        import java.util.concurrent.*;


    public class FileProcessor {
        private final ExecutorService executor;
        private final List<DataPoint> sharedList;

        public FileProcessor() {
            executor = Executors.newFixedThreadPool(3);
            sharedList = Collections.synchronizedList(new ArrayList<>());
        }

        public List<DataPoint> readAndProcess(File file) throws IOException, InterruptedException {
            List<String> allLines = Files.readAllLines(file.toPath());
            int chunkSize = allLines.size() / 3;

            for (int i = 0; i < 3; i++) {
                int start = i * chunkSize;
                int end = (i == 2) ? allLines.size() : (i + 1) * chunkSize;
                List<String> chunk = allLines.subList(start, end);

                executor.execute(() -> {
                    for (String line : chunk) {
                        String[] parts = line.split(",");
                        if (parts.length == 3 && parts[1].equals("sensorA")) {
                            try {
                                long timestamp = Long.parseLong(parts[0]);
                                double value = Double.parseDouble(parts[2]);
                                sharedList.add(new DataPoint(timestamp, value));
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

            sharedList.sort(Comparator.comparingLong(dp -> dp.timestamp));
            return sharedList;
        }
    }