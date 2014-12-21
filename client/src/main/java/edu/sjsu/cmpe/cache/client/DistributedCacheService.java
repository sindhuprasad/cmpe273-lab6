package edu.sjsu.cmpe.cache.client;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.io.InputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * Distributed cache service
 * 
 */
public class DistributedCacheService implements CacheServiceInterface {
    private final String cacheServerUrl;
    public static int completedUrlsCount=0;
   String value = null;
   public static HashMap<String, Long> map = new HashMap<String, Long>();

    public static HashMap<String, HashMap> urlKeyValueMap = new HashMap<String, HashMap>();
    Collector collector;

    public DistributedCacheService(String serverUrl, Collector collector) {
        this.cacheServerUrl = serverUrl;
        this.collector = collector;
    }



    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */


    /*
    @Override
    public String get(long key) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(this.cacheServerUrl + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key)).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }
        String value = response.getBody().getObject().getString("value");

        return value;
    }

*/





    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */
    @Override
    public String get(long key) {
        debug("DistributedCacheService.get:Inside new get method>>>>>>>>>>>>>>>>>");
       // HttpResponse<JsonNode> response = null;
        //String value=null;
        Future<HttpResponse<JsonNode>> future =null;
       // String value = null;
        final String url = this.cacheServerUrl;
        try {
            final long key1 = key;
            future = Unirest
                    .get(this.cacheServerUrl + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .asJsonAsync(new Callback<JsonNode>() {
                        Date date = new Date();

                        public void failed(UnirestException e) {
                            System.out.println("DistributedCacheService.failed:The request has failed");

                        }

                        public void completed(HttpResponse<JsonNode> response) {
                            debug("DistributedCacheService::code, response recvd = " + response);

                            String val =null;
                            try {
                                int code = response.getCode();
                                 val = response.getBody().getObject().getString("value");
                            }catch(Exception e){
                                debug("Printing exception: "+e);
                            }
                            //System.out.println("DistributedCacheService.completed:Response recvd, code " + code + ",time=" + new Timestamp(date.getTime()) + " for url:" + url);
                            collector.update(val,DistributedCacheService.this);
                        }

                        public void cancelled() {
                            System.out.println("DistributedCacheService.failedThe request has been cancelled");
                        }

                    });



            /*
if(future.isDone() && "200".equals(future.get().getCode())) {
    value = future.get().getBody().getObject().getString("value");
    System.out.println("Future get "+ value);
}*/
        } catch (Exception e) {
            debug("DistributedCacheService.get:There was an exception in get");
            System.err.println(e);
        }

       // value = future.get().getBody().getObject().getString("value");
        //System.out.println("future value "+ future.getBody().getObject().getString("value"));

        //sleep for 3 sec
      /*  System.out.println("sleeping..........");
        try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        System.out.println("return value "+value);*/
        return value;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#put(long,
     *      java.lang.String)
     */


    /*
    @Override
    public void put(long key, String value) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .put(this.cacheServerUrl + "/cache/{key}/{value}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .routeParam("value", value).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }

        if (response.getCode() != 200) {
            System.out.println("Failed to add to the cache.");
        }
    }
*/

public void put(long key, String value){


    debug("PUT method called: " + value + ",url = " + this.cacheServerUrl);


    final String url = this.cacheServerUrl;
    Future<HttpResponse<JsonNode>> future = null;
    try {


        final long key1 = key;
        debug("PUT method will be called soon: " + value + ",url = " + this.cacheServerUrl);
        future = Unirest
                .put(this.cacheServerUrl + "/cache/{key}/{value}")
                .header("accept", "application/json")
                .routeParam("key", "1")
                .routeParam("value", value)
                .asJsonAsync(new Callback<JsonNode>() {
                    Date date = new Date();

                    public void failed(UnirestException e) {
                        debug("The request has failed");

                    }

                    public void completed(HttpResponse<JsonNode> response) {
                        try {
                            debug("DistributedCacheService::put code, response recvd = " + response);
                            int code = response.getCode();
                            completedURls(url, key1);
                        } catch(Exception e){
                            debug("DistributedCacheService::put, error occured " + e.toString());
                        }
                        debug("DistributedCacheService::put, done");

                    }

                    public void cancelled() {
                        debug("The request has been cancelled");
                    }

                });
        debug("future getCode"+future.get().getCode());
        debug("future isDone"+future.isDone());
    }
    catch(Exception e) {
        debug("DistributedCacheService::put, outer error occured " + e.toString());

    }
}

    @Override
    public String toString() {
        return "server = " + this.cacheServerUrl;
    }

    public void debug(String arg){
        System.out.println("[Server: " + this.cacheServerUrl  + "]" + arg);
    }


    public void delete(long key)
    {
        HttpResponse<JsonNode> response = null;
        try {
                response  = Unirest.delete(this.cacheServerUrl+"/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        System.out.println("Response code after deletion "+ response.getCode());
    }


    public void completedURls(String url, Long key) {
        Date date = new Date();
        map.put(url, key);
        completedUrlsCount++;
        System.out.println("inside completedURls "+ url+ "and count is"+ completedUrlsCount+ "at" + new Timestamp(date.getTime()));
    }

    public void  sendUrlKeyValue(String url, long key, String val) {
       value = val;
        //System.out.println("send method val  "+val+" send method value  "+value);
         HashMap<Long, String> keyValueMap = new HashMap<Long, String>();
         keyValueMap.put(key, val);
        urlKeyValueMap.put(url, keyValueMap);
    }
}
