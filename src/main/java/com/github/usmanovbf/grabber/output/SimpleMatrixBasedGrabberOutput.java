package com.github.usmanovbf.grabber.output;

import com.github.usmanovbf.tools.Cursor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class SimpleMatrixBasedGrabberOutput implements GrabberOutput {

    private final Cursor cursor;
    private final AtomicInteger[][] matrix;
    private Map<Integer, String> reverse = null;

    SimpleMatrixBasedGrabberOutput( int matrixSize) {
        this.cursor = new Cursor(matrixSize);
        this.matrix = new AtomicInteger[matrixSize][matrixSize];
        initMatrix();
    }

    @Override
    public boolean addLink(String from, String to) {
        if ((!cursor.contains(from) || !cursor.contains(to)) && !cursor.hasNext()) {
            return false;
        }

        final int fromIndex = cursor.getIndex(from);
        final int toIndex = cursor.getIndex(to);
        matrix[fromIndex][toIndex].getAndIncrement();
        return true;
    }

    @Override
    public Collection<String> allLinks() {
        return cursor.ids();
    }

    @Override
    public Collection<String> in(String page) {
        if (reverse == null) {
            reverse = cursor.reverse();
        }

        final int col = cursor.getIndex(page);
        final List<String> result = new ArrayList<>();

        for (int i = 0; i < matrix[col].length; i++) {
            if (matrix[i][col].get() > 0 && i != col) {
                result.add(reverse.get(i));
            }
        }

        return result;
    }

    @Override
    public Collection<String> out(String page) {
        if (reverse == null) {
            reverse = cursor.reverse();
        }

        final int row = cursor.getIndex(page);
        final List<String> result = new ArrayList<>();

        for (int i = 0; i < matrix[row].length; i++) {
            if (matrix[row][i].get() > 0 && i != row) {
                result.add(reverse.get(i));
            }
        }

        return result;
    }

    @Override
    public void printAsMatrix(PrintWriter printWriter) {
        cursor.ids().forEach( id -> printWriter.println( cursor.getIndex(id) + " : " + id));
        printWriter.println();
        printWriter.flush();
        for (AtomicInteger[] links : matrix) {
            printWriter.println(Arrays.toString(links));
            printWriter.flush();
        }
        printWriter.println();
        printWriter.flush();
    }

    private void initMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = new AtomicInteger();
            }
        }
    }
}
