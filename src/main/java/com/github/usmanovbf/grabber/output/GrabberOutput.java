package com.github.usmanovbf.grabber.output;

import org.apache.commons.lang3.StringUtils;
import com.github.usmanovbf.tools.Options;

import java.io.PrintWriter;
import java.util.Collection;


public interface GrabberOutput {

    int DEFAULT_SIZE = 100;


    boolean addLink(String from, String to);


    Collection<String> allLinks();


    Collection<String> in(String page);


    Collection<String> out(String page);

    void printAsMatrix(PrintWriter printWriter);

    static GrabberOutput newInstance() {
        final String maxSize = Options.get( Options.PAGES_NUMBER );
        boolean useSparse = !StringUtils.isEmpty( Options.get( Options.SPARSED_MATRIX ));
        final int size = StringUtils.isNumeric(maxSize) ? Integer.valueOf(maxSize) : DEFAULT_SIZE;
        return useSparse ? new SparseMatrixGrabberOutput(size) : new SimpleMatrixBasedGrabberOutput(size);
    }

}
