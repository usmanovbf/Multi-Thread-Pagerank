package com.github.usmanovbf;


import java.util.HashMap;

public class WebSite {

    /**
     * Матрица смежности
     *
     * WebPage
     *  source    a    b    c    d    f    e
     *       a    1   null
     *
     *       b    1    1
     *
     *       c              1         1
     *
     *       d                   1
     *
     *       f            null        1
     *
     *       e                             1
     *
     *  HashMap<LEFT_SOURCE, HashMap<RIGHT_SOURCE, IS_CONNECTED>>
     */

    private HashMap<WebPage, HashMap<WebPage, Boolean>> adjacencyMatrix;

    public WebSite() {
        this.adjacencyMatrix = new HashMap<WebPage, HashMap<WebPage, Boolean>>();
    }

    public HashMap<WebPage, HashMap<WebPage, Boolean>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(HashMap<WebPage, HashMap<WebPage, Boolean>> adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }



}
