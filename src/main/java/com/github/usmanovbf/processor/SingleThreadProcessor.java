package com.github.usmanovbf.processor;

import com.github.usmanovbf.grabber.output.GrabberOutput;
import com.github.usmanovbf.tools.Cursor;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class SingleThreadProcessor extends BaseProcessor implements PageRankProcessor {

    @Override
    public Collection<ProcessorData> process( final GrabberOutput result) {
        final int size = result.allLinks().size();
        double[] pageRanks = start(size);
        final Cursor cursor = new Cursor(size);
        double[] prevRanks;
        do {
            prevRanks = Arrays.copyOf(pageRanks, pageRanks.length);
            final double[] finalPrevRanks = prevRanks;
            for (String link : result.allLinks()) {
                final double[] newRank = new double[]{(1 - MAJOR_FACTOR ) / size};
                final int index = cursor.getIndex(link);
                result.out(link).forEach(out -> {
                    int in = result.in(out).size();
                    newRank[0] += MAJOR_FACTOR * finalPrevRanks[index] * 1D / (in == 0 ? 1 : in);
                });
                pageRanks[index] = newRank[0];
            }
            pageRanks = normalizeRanks(pageRanks);
        } while (findDifferences(pageRanks, prevRanks) > ACCURACY);

        double[] finalPageRanks = pageRanks;
        return cursor.ids().stream()
                .map(id -> new ProcessorData(id, finalPageRanks[cursor.getIndex(id)]))
                .collect(Collectors.toList());
    }

}
