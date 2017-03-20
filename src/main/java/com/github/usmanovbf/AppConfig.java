package com.github.usmanovbf;

public class AppConfig {
    private static int countOfVisited = 0;

    private AppConfig() {
    }

    public static synchronized int incrementCountOfVisited() {
        return countOfVisited++;
    }
}
