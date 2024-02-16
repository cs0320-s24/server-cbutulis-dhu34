package edu.brown.cs.student.main.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class CachedDatasource {

    private final LoadingCache<LinkedList<String>, String> cache;

    public CachedDatasource(final Datasource datasource) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .recordStats()
                .build(new CacheLoader<LinkedList<String>, String>() {
                    @Override
                    public String load(LinkedList<String> params) throws Exception {
                        System.out.println("called load for params: " + params);
                        return datasource.sendRequest(params);
                    }
                });
    }

    public void printCacheContents() {
        System.out.println("Cache Contents:");
        for (var entry : cache.asMap().entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

    public String sendRequest(LinkedList<String> params) throws Exception {
        // "get" is designed for concurrent situations; for today, use getUnchecked:
        String result = cache.getUnchecked(params);

        // For debugging and demo (would remove in a "real" version):
        System.out.println(cache.stats());
//        printCacheContents();
        return result;
    }
}
