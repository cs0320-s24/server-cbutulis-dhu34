package edu.brown.cs.student.main.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.List;
import java.util.Set;
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
              public Object load(Request key) throws Exception {
                System.out.println("called load for: " + key);
                return wrappedHandler.handle(key, null);
              }
            });
  }

  public void printCacheContents() {
    System.out.println("Cache Contents:");
    for (Object key : cache.asMap().keySet()) {
      System.out.println("Key: " + key + ", Value: " + cache.getIfPresent(key));
    }
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // "get" is designed for concurrent situations; for today, use getUnchecked:
    Set<String> params = request.queryParams();
    Object result = this.cache.getUnchecked(request);

    // For debugging and demo (would remove in a "real" version):
    //System.out.println(cache.stats());
    printCacheContents();
//        return this.wrappedHandler.handle(request, response);
    return result;
  }
}
