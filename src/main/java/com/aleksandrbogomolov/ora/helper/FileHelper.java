package com.aleksandrbogomolov.ora.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by bogomolov_av on 17.11.2017
 */
public class FileHelper {

    public static String[] readQuery() {
        String builder = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/query.sql"));
            builder = reader.lines().collect(Collectors.joining(" "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.split(";");
    }

    public static Map<String, String> parseTns() {
        Map<String, String> result = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/tnsname.ora"));
            reader.lines().forEachOrdered(line -> {
                int delimiter = line.indexOf("=");
                result.put(line.substring(0, delimiter), line.substring(delimiter + 1));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
