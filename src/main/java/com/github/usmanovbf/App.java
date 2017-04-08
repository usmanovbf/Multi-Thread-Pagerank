package com.github.usmanovbf;

import com.github.usmanovbf.grabber.Grabber;
import com.github.usmanovbf.processor.PageRankProcessor;
import com.github.usmanovbf.grabber.output.GrabberOutput;
import com.github.usmanovbf.processor.ProcessorData;
import com.github.usmanovbf.tools.Options;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.IntStream;


public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please, type the site");
        }

        final String host = args[0];

        if (args.length > 1) {
            IntStream.range(1, args.length)
                    .mapToObj(i -> args[i])
                    .map(command -> command.split("="))
                    .forEach(command -> {
                        switch (command[0].toLowerCase()) {
                            case Options.SPARSED_MATRIX:
                                Options.put( Options.SPARSED_MATRIX, Boolean.TRUE);
                                break;
                            case Options.PAGES_NUMBER:
                                Options.put( Options.PAGES_NUMBER, command[1]);
                                break;
                            case Options.CRAWLER_THREADS :
                                Options.put( Options.CRAWLER_THREADS, command[1]);
                                break;
                            case Options.CALCULATOR_THREADS:
                                Options.put( Options.CALCULATOR_THREADS, command[1]);
                                break;
                            default :

                        }
                    });
        }

        final long start = System.currentTimeMillis();
        final GrabberOutput grabberOutput = Grabber.newInstance().work(host);
        final Collection<ProcessorData> result = PageRankProcessor.createInstance().process( grabberOutput );
        final long end = System.currentTimeMillis();

        System.out.println("Grabber Results:");
        grabberOutput.printAsMatrix(new PrintWriter(System.out));
        System.out.println("Calculated page ranks");
        result.stream().sorted(Comparator.comparing( ProcessorData::rank))
                .forEach(r -> System.out.println(r.page() + " : " + r.rank()));
        System.out.println("\n all time of process" + (end - start) + "ms");
    }

}
