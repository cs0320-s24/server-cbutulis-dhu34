package edu.brown.cs.student.main.datasource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class which wraps the Datasource to store each of its outputs into a cache. Instantiate by
 * passing in a Datasource OR passing in a Datasource, the max size of the cache, and the number of
 * minutes the cache is meant to wait for each entry before clearing the entry.
 */
public class CachedDatasource implements Datasource {

  private final LoadingCache<List<String>, String> cache;

  /**
   * Default constructor of CachedDatasource, takes in a Datasource and wraps it.
   *
   * @param datasource - the DataSource to wrap
   */
  public CachedDatasource(Datasource datasource) {
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @Override
                  public String load(List<String> params) throws Exception {
                    System.out.println("called load for params: " + params);
                    return datasource.sendRequest(params);
                  }
                });
  }

  /**
   * Overloaded constructor for the CachedDatasource, includes extra parameters to specify the
   * cache's size and how long it should wait before clearing contents.
   *
   * @param datasource - the DataSource to wrap
   * @param maxSize - the maximum size of the cache
   * @param minToExpel - time after an element is added after which to clear it from the cache
   */
  public CachedDatasource(final ApiDatasource datasource, int maxSize, int minToExpel) {
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(minToExpel, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @Override
                  public String load(List<String> params) throws Exception {
                    System.out.println("called load for params: " + params);
                    return datasource.sendRequest(params);
                  }
                });
  }

  /**
   * Fetches the results of the query from the cache if available, otherwise sends a request to the
   * API.
   *
   * @param params - parameters of the API request
   * @return - the result of the API/cache query
   */
  public String sendRequest(List<String> params) {
    // Use get method to fetch value from cache
    return cache.getUnchecked(params);
  }
}
