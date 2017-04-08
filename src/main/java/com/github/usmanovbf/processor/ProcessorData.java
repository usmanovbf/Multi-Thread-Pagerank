package com.github.usmanovbf.processor;


public class ProcessorData {

    private final String page;
    private final double rank;

    ProcessorData( String page, double rank) {
        this.page = page;
        this.rank = rank;
    }

    public String page() {
        return page;
    }

    public double rank() {
        return rank;
    }

}
