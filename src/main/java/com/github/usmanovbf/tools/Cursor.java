package com.github.usmanovbf.tools;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class Cursor {

    private final int maximumCount;

    private final Map<String, Integer> indicies;
    private final AtomicInteger unusedIndex = new AtomicInteger();

    public Cursor( int maximumCount ) {
        this.maximumCount = maximumCount;
        indicies = new ConcurrentHashMap<>( maximumCount, 1F);
    }

    public int getIndex(String id) {
        return indicies.computeIfAbsent(id, k -> unusedIndex.getAndIncrement());
    }

    public boolean contains(String id) {
        return indicies.containsKey(id);
    }

    public boolean hasNext() {
        return unusedIndex.get() < maximumCount;
    }

    public Collection<String> ids() {
        return indicies.keySet();
    }

    public Map<Integer, String> reverse() {
        Map<Integer, String> reverseMap = new ConcurrentHashMap<>( maximumCount, 1F);
        indicies.forEach(( k, v) -> reverseMap.put(v, k));
        return reverseMap;
    }
}
