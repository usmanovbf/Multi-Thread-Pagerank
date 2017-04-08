package com.github.usmanovbf.processor;

import com.github.usmanovbf.grabber.output.GrabberOutput;
import com.github.usmanovbf.tools.Cursor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class MultiThreadProcessor extends BaseProcessor implements PageRankProcessor {

    private final ExecutorService pool;

    MultiThreadProcessor( int threadCount) {
        this.pool = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public Collection<ProcessorData> process( GrabberOutput result) {
        final int size = result.allLinks().size();
        double[] pageRanks = start(size);
        final Cursor cursor = new Cursor(size);
        double[] prevRanks;

        do {
            prevRanks = Arrays.copyOf(pageRanks, pageRanks.length);
            final double[] prevRanksFinal = prevRanks;
            try {
                final List<Worker> workers = result.allLinks().stream()
                        .map(l -> new Worker(prevRanksFinal, result, l, cursor.getIndex(l)))
                        .collect(Collectors.toList());
                final List<Future<PRCData>> futures = pool.invokeAll(workers);
                for (Future<PRCData> future : futures) {
                    final PRCData prcData = future.get();
                    pageRanks[prcData.index] = prcData.rank;
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            pageRanks = normalizeRanks(pageRanks);
        } while (findDifferences(pageRanks, prevRanks) > ACCURACY);
        pool.shutdown();
        double[] finalPageRanks = pageRanks;
        return cursor.ids().stream()
                .map(id -> new ProcessorData(id, finalPageRanks[cursor.getIndex(id)]))
                .collect(Collectors.toList());
    }

    private static class PRCData {
        final int index;
        final double rank;

        private PRCData(int index, double rank) {
            this.index = index;
            this.rank = rank;
        }
    }

    private static class Worker implements Callable<PRCData> {

        private final double[] prevRanks;
        private final GrabberOutput grabberOutput;
        private final String page;
        private final int index;

        private Worker( double[] prevRanks, GrabberOutput grabberOutput, String page, int index) {
            this.prevRanks = prevRanks;
            this.grabberOutput = grabberOutput;
            this.page = page;
            this.index = index;
        }


        @Override
        public PRCData call() throws Exception {
            final double[] newRank = {(1 - MAJOR_FACTOR ) / grabberOutput.allLinks().size()};
            grabberOutput.out(page).forEach( in -> {
                int out = grabberOutput.in(in).size();
                newRank[0] += MAJOR_FACTOR * prevRanks[index] * 1D / (out == 0 ? 1 : out);
            });
            return new PRCData(index, newRank[0]);
        }
    }

}
