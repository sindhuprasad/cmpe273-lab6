package edu.sjsu.cmpe.cache.client;

/**
 * Cache Service Interface
 * 
 */
public interface CacheServiceInterface {
    public String get(long key);

    public void put(long key, String value);

    public void delete(long key);

    public void completedURls(String url, Long key);

    public void sendUrlKeyValue(String url, long key, String value);

}
