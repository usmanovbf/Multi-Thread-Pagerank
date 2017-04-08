package com.github.usmanovbf.grabber;

import com.github.usmanovbf.grabber.output.GrabberOutput;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;


public class MultiThreadGrabber implements Grabber {

    private final ForkJoinPool service;

    MultiThreadGrabber( int threadCount) {
        service = new ForkJoinPool(threadCount);
    }

    @Override
    public GrabberOutput work( String startUrl) {
        final GrabberOutput result = GrabberOutput.newInstance();
        service.invoke(new Worker(result, new ConcurrentHashMap<>(), startUrl));
        service.shutdown();
        return result;
    }

    private static class Worker extends RecursiveAction {

        final GrabberOutput result;
        final Map<String, Boolean> visitUrl;
        final String from;

        private Worker( GrabberOutput result, Map<String, Boolean> visitUrl, String from) {
            this.result = result;
            this.visitUrl = visitUrl;
            this.from = from;
        }


        @Override
        public void compute() {
            addIfAbsent(visitUrl, from, Boolean.TRUE);
            final List<Worker> workers = Grabber.parse(from)
                    .filter(url -> result.addLink(from, url))
                    .filter(u -> addIfAbsent(visitUrl, u, Boolean.TRUE))
                    .map(url -> new Worker(result, visitUrl, url)).collect(Collectors.toList());
            invokeAll(workers);
        }

        static <K, V> boolean addIfAbsent(Map<K, V> map, K key, V value) {
            if (map.containsKey(key)) {
                return false;
            }
            map.put(key, value);
            return true;
        }

    }
}
