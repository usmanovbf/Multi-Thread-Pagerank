package com.github.usmanovbf;

public class WebPage {
    private String url;
    private double transitionProbability;

    public WebPage(String url) {

        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getTransitionProbability() {
        return transitionProbability;
    }

    public void setTransitionProbability( double transitionProbability ) {
        this.transitionProbability = transitionProbability;
    }
}
