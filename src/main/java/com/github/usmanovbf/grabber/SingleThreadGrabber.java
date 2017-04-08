package com.github.usmanovbf.grabber;

import com.github.usmanovbf.grabber.output.GrabberOutput;

import java.util.HashSet;
import java.util.Set;


public class SingleThreadGrabber implements Grabber {

    SingleThreadGrabber() {}

    public GrabberOutput work( String startUrl) {
        final GrabberOutput result = GrabberOutput.newInstance();
        work(startUrl, result, new HashSet<>());
        return result;
    }

    private void work( String from, final GrabberOutput result, final Set<String> visitUrl) {
        visitUrl.add(from);
        Grabber.parse(from)
                .filter(url -> result.addLink(from, url))
                .filter(visitUrl::add)
                .forEach(url -> work(url, result, visitUrl));
    }

}
