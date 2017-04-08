package com.github.usmanovbf.grabber.output;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class SparseMatrixGrabberOutput implements GrabberOutput {

    private final transient Map<String, Integer> indexMap;
    private final AtomicInteger freeIndex = new AtomicInteger();
    private final List<AtomicInteger> values;
    private final List<Integer> rows;
    private final List<Integer> cols;
    private final int matrixSize;


    SparseMatrixGrabberOutput( int matrixSize) {
        this.indexMap = new ConcurrentHashMap<>(matrixSize, 1F);
        this.values = new CopyOnWriteArrayList<>();
        this.rows = new CopyOnWriteArrayList<>();
        this.cols = new CopyOnWriteArrayList<>();
        this.matrixSize = matrixSize;
    }

    @Override
    public boolean addLink(String from, String to) {
        if ((!indexMap.containsKey(from) || !indexMap.containsKey(to)) && freeIndex.get() >= matrixSize) {
            return false;
        }

        final AtomicBoolean hasKeys = new AtomicBoolean(true);

        final Integer fromIndex = indexMap.computeIfAbsent(from, k -> {
            hasKeys.set(false);
            return freeIndex.getAndIncrement();
        });
        final Integer toIndex = indexMap.computeIfAbsent(to, k -> {
            hasKeys.set(false);
            return freeIndex.getAndIncrement();
        });

        int findIndex = -1;

        if (hasKeys.get()) {
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).equals(fromIndex) && cols.get(i).equals(toIndex)) {
                    findIndex = i;
                    break;
                }
            }
        }

        if (findIndex != -1) {
            values.get(findIndex).incrementAndGet();
        } else {
            synchronized (this) { //there may be mistakes
                rows.add(fromIndex);
                cols.add(toIndex);
                values.add(new AtomicInteger(1));
            }
        }

        return true;
    }

    @Override
    public Collection<String> allLinks() {
        return indexMap.keySet();
    }

    @Override
    public Collection<String> in(String page) {
        final int col = indexMap.get(page);
        final Collection<String> result = new ArrayList<>();
        for (int i = 0; i < cols.size(); i++) {
            if (col == cols.get(i)) {
                result.add(reverse().get(rows.get(i)));
            }
        }
        return result;
    }

    @Override
    public Collection<String> out(String page) {
        final int row = indexMap.get(page);
        final Collection<String> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            if (row == rows.get(i)) {
                result.add(reverse().get(cols.get(i)));
            }
        }
        return result;
    }

    @Override
    public void printAsMatrix(PrintWriter printWriter) {
        indexMap.forEach((k, v) -> printWriter.println(v + " : " + k));
        printWriter.println();

        int[][] matrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < values.size(); i++) {
            matrix[rows.get(i)][cols.get(i)] = values.get(i).intValue();
        }

        for (int[] links : matrix) {
            printWriter.println(Arrays.toString(links));
        }

        printWriter.println();
    }

    private Map<Integer, String> reverse;

    private Map<Integer, String> reverse() {
        if (reverse == null) {
            reverse = new ConcurrentHashMap<>(matrixSize, 1F);
            indexMap.forEach((k, v) -> reverse.put(v, k));
        }
        return reverse;
    }


}
