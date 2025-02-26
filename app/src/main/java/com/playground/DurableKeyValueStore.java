package com.playground;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class DurableKeyValueStore {
    private String FILE = "data.log";
    private ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap();

    public DurableKeyValueStore() {
        //load the file and add the entry into map.
        loadMapFromFile();
    }

    private void loadMapFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while (true) {
                try {
                    if (!((line = br.readLine()) != null)) break;
                    String[] kv = line.split(",");
                    map.put(Integer.parseInt(kv[0]), kv[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void put(int key, String value) {
        map.put(key, value);
        // write to file
        writeToFile(map);
    }

    private void writeToFile(ConcurrentHashMap<Integer, String> map) {
        try (FileWriter writer = new FileWriter(FILE)) {
            map.entrySet().forEach(item -> {
                try {
                    writer.write(item.getKey() + ", " + item.getValue() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String get(int key) {
        // return from file
        return map.get(key);
    }

    public static void main(String[] args) {
        DurableKeyValueStore kvStore = new DurableKeyValueStore();
        kvStore.put(1, "A");
        kvStore.put(2, "B");
        kvStore.put(3, "C");
        System.out.println(kvStore.get(1));
    }
}
