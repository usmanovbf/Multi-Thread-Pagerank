package com.github.usmanovbf.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Options {

    private Options() {}

    public final static String CRAWLER_THREADS = "crawlerthreads";
    public final static String PAGES_NUMBER = "pagesnumber";
    public final static String SPARSED_MATRIX = "sparsedmatrix";
    public final static String CALCULATOR_THREADS = "calculatorthreads";


    private final static Map<String, String> values = new ConcurrentHashMap<>();

    public static String get(String key) {
        return values.get(key);
    }

    public static <T> void put(String key, T value) {
        values.put(key, value.toString());
    }
}
