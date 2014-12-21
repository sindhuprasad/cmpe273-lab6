package edu.sjsu.cmpe.cache.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CRDTClient {

    private  Collector c;
    private  CacheServiceInterface cache1;
    private  CacheServiceInterface cache2;
    private  CacheServiceInterface cache3;

    public CRDTClient(){
        c = new Collector();

        cache1 = new DistributedCacheService(
                "http://localhost:3000", c);

        cache2 = new DistributedCacheService(
                "http://localhost:3001", c);

        cache3 = new DistributedCacheService(
                "http://localhost:3002", c);

    }

    public void put(long key, String value){
        System.out.println("CRDTClient:put,vaue " + value);
        try {
            cache1.put(key, value);
            cache2.put(key, value);
            cache3.put(key, value);


            Thread.sleep(3000);
            System.out.println("CRDTClient:put,The count of completed urls: " +DistributedCacheService.completedUrlsCount);
            System.out.println("CRDTClient:put,Completed urls list: " + DistributedCacheService.map);

            if(DistributedCacheService.completedUrlsCount <2) {
                System.out.println("rollback");
                rollback(DistributedCacheService.map,c);
            }
            else
                System.out.println("No rollback");

        } catch(Exception e){
            System.out.println("CRDTClient:put,error" + e.toString());
        }
    }


    public String get(long key) {
        System.out.println("CRDTClient:get,vaue " + key);
        try {
            cache1.get(1);
            cache2.get(1);
            cache3.get(1);
            System.out.println("CRDTClient:get,waiting for 20 seconds now");
            Thread.sleep(20000);
        } catch(Exception e){
            System.out.println("CRDTClient:get,error" + e.toString());
        }
        System.out.println("CRDTClient:get,finished waiting for 20 seconds");
        return c.correctValue;
    }

    /*

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache CRDTClient...");



       // cache1.put(1, "b");

       try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        cache2.put(1, "a");
        cache3.put(1, "a");



        try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("The count of completed urls: " +DistributedCacheService.completedUrlsCount);
        System.out.println("Completed urls list: " + DistributedCacheService.map);

        if(DistributedCacheService.completedUrlsCount <2) {
            System.out.println("rollback");
            rollback(DistributedCacheService.map,c);

        }
        else
            System.out.println("No rollback");




        System.out.println(">>>>Getting key values from all the servers...");
        String value1 = cache1.get(1);
        //System.out.println("get(1) => " + value1);

        String value2 = cache2.get(1);
        //System.out.println("get(1) => " + value2);

        String value = cache3.get(1);
        //System.out.println("get(1) => " + value);



        //sleep for 3 sec
        try {
            System.out.println("Sleeping for 40 secs, called all three get methods, now waiting for responses");
            Thread.sleep(40000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Existing Cache CRDTClient...");
    }
    */

    public static void rollback(HashMap map,Collector c)
    {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println("Inside rollback method. Printing key value pair..... "+pairs.getKey() + " = " + pairs.getValue());
            System.out.println("Url sent for deletion: "+ pairs.getKey().toString());
            CacheServiceInterface cache1 = new DistributedCacheService(
                    pairs.getKey().toString(),c);

            cache1.delete((Long) pairs.getValue());

         //   it.remove(); // avoids a ConcurrentModificationException
        }
    }

}
