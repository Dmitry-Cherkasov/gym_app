package com.gym_app.core.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ResourceFileReader {

    public static ArrayList<String> readFile(String filePath) throws IOException {
        ArrayList<String> strings = new ArrayList<>();

        // Try-with-resources to ensure the reader is closed after use
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }
        }
        return strings;
    }
}
