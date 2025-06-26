

# SensorGraphApp

##  Android Java Problem - Sensor Data Visualization

**SensorGraphApp** is a Java-based Android application that reads numerical time-series data from a local file, processes it concurrently, filters it by sensor type, and visualizes it in a line graph.



## Objective

Build an Android app that:

-  Reads `sensor_data.txt` from internal storage
-  Parses and processes the file concurrently using 3 threads
-  Merges and sorts data thread-safely
-  Draws a graph of `sensorA` values vs timestamps using MPAndroidChart


##  Tech Stack

| Component      | Technology / Library                   |
|----------------|-----------------------------------------|
| Language       | Java                                   |
| IDE            | Android Studio                         |
| Chart Library  | MPAndroidChart                         |
| Multithreading | `ExecutorService` with 3 threads       |
| Thread Safety  | `Collections.synchronizedList`         |
| File I/O       | Android internal storage (`openFileOutput`) |



##  Input File Format

- File: `sensor_data.txt`
- Path: Internal Storage (`/data/data/<package>/files/`)
- Format per line:
  ```
  <timestamp>,<sensor_id>,<value>
  e.g. 1624550000,sensorA,34.5
  ```

- Example:
  ```txt
  1624550000,sensorA,34.5
  1624550002,sensorB,22.1
  1624550003,sensorA,34.8
  1624550005,sensorC,12.5
  ```


## Functional Requirements

- Load and parse `sensor_data.txt`
- Use 3 threads to read different chunks of lines
- Only process `sensorA` data
- Merge results into a thread-safe list
- Sort by timestamp
- Plot graph using MPAndroidChart



##  UI Overview

- Line chart with:
  - X-axis: Timestamp (epoch)
  - Y-axis: Sensor value (0–100)
- Filtered view: only `sensorA` data is shown



##  How to Run

1. Open project in Android Studio
2. Sync Gradle (`MPAndroidChart` dependency)
3. Connect emulator or device
4. Run the app — it will:
   - Create sample `sensor_data.txt`
   - Parse file concurrently
   - Plot the results



## Thread Handling Strategy

- File is split into 3 chunks and processed using a fixed thread pool
- Each thread filters and parses `sensorA` data from its chunk
- A `synchronizedList` ensures thread-safe merging
- Sorting and graph drawing happen after all threads complete



## Error Handling

- Malformed lines are skipped gracefully
- UI is updated only after all threads complete
- Data is sorted chronologically before plotting




## Author

Built as part of the **Android Java Take-Home Problem** assignment.

