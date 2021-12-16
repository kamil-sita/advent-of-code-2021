package pl.ksitarski.adventofcode.aoc2021.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Utils {

    public static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();

        try (
             FileInputStream fileInputStream = new FileInputStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

}
