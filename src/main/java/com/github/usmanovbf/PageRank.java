package com.github.usmanovbf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PageRank {

    public void calculate( WebSite webSite ) {
        int countOfSources = webSite.getAdjacencyMatrix().size();
        double initialR = 1 / (double) countOfSources;
        calculateMatrixOfTransitionProbabilities( webSite );
    }

    private void calculateMatrixOfTransitionProbabilities( WebSite webSite ) {
        Map<String, HashMap<String, Double>> transitionProbabilities = new HashMap<String, HashMap<String, Double>>();
        double toKnownAddressDivided = 0.85 / (double) webSite.getAdjacencyMatrix().size();
        double toNotKnownAddressDivided = 0.15 / (double) webSite.getAdjacencyMatrix().size();
        Set<WebPage> sourceWebPages = webSite.getAdjacencyMatrix().keySet();
        for (WebPage sourceWebPage : sourceWebPages) {

        }

    }
}
