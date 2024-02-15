package edu.brown.cs.student.main.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class CachedHandler implements Route {
    private final Route wrappedHandler;
    private final LoadingCache cache;

    public CachedHandler(Route handler) {
        this.wrappedHandler = handler;
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .recordStats().build(
                        new CacheLoader<Request, Object>() {
                            @Override
                            public Object load(Request request) throws Exception {
                                System.out.println("called load for: " + request);
                                return wrappedHandler.handle(request, null);
                            }
                        });
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return this.wrappedHandler.handle(request, response);
    }
}
