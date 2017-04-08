package com.github.usmanovbf.grabber;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import com.github.usmanovbf.tools.Options;
import com.github.usmanovbf.grabber.output.GrabberOutput;

import java.util.Objects;
import java.util.stream.Stream;


public interface Grabber {

    String ANCHOR = "#";

    static Grabber newInstance() {
        final String crawlerThreads = Options.get( Options.CRAWLER_THREADS );
        boolean useMulti = StringUtils.isNumeric( crawlerThreads ) && Integer.valueOf( crawlerThreads ) > 1;
        return useMulti ? new MultiThreadGrabber( Integer.valueOf( crawlerThreads ) ) : new SingleThreadGrabber();
    }

    static Stream<String> parse( String page ) {
        try {
            return Jsoup.connect( page ).get()
                    .body()
                    .select( "a" )
                    .stream()
                    .map( l -> l.attr( "abs:href" ) )
                    .map( l -> l.contains( ANCHOR ) ? l.substring( 0, l.indexOf( ANCHOR ) ) : l )
                    .filter( Objects::nonNull ).filter( s -> !s.isEmpty() );
        } catch (Exception e) {
            return Stream.empty();
        }

    }

    GrabberOutput work( String startUrl );


}
