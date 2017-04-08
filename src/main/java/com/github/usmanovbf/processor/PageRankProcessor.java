package com.github.usmanovbf.processor;

import org.apache.commons.lang3.StringUtils;
import com.github.usmanovbf.tools.Options;
import com.github.usmanovbf.grabber.output.GrabberOutput;

import java.util.Collection;


public interface PageRankProcessor {

    double MAJOR_FACTOR = 0.85;
    double ACCURACY = 0.000001;


    Collection<ProcessorData> process( GrabberOutput result);

    static PageRankProcessor createInstance() {
        final String property = Options.get( Options.CALCULATOR_THREADS );
        boolean useMulti = StringUtils.isNumeric(property) && Integer.valueOf(property) > 1;
        return useMulti ? new MultiThreadProcessor(Integer.valueOf(property)) : new SingleThreadProcessor();
    }

}
