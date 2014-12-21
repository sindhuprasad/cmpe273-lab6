package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;

/**
 * Created by sindhu on 12/20/14.
 */
public class Collector {
    ArrayList<CacheServiceInterface> services = new ArrayList<CacheServiceInterface>();
    ArrayList<String> values = new ArrayList<String>();
    public String correctValue = null;

    public void update(String value, CacheServiceInterface service){

        values.add(value);
        services.add(service);

        try {

            if (values.size() == 3) {
                String first = values.get(0);
                String second = values.get(1);
                String three = values.get(2);

                System.out.println("first = " + first + ",second=" + second + ", three = " + three);
                System.out.println("firstServer = " + services.get(0) + ",secondServer=" + services.get(1) + ", threeServer= " + services.get(2));


                if (first != null && second != null && first.equals(second) && second.equals(three)) {
                    System.out.println("Collector:update - all three values are consistent, nothing to do!");
                } else {
                    if (first != null && first.equals(second)) {
                        System.out.println("Collector: fixing third service " + services.get(2).toString());
                        correctValue = first;
                        services.get(2).put(1, first);
                    } else if (first != null && first.equals(three)) {
                        System.out.println("Collector: fixing second service " + services.get(1).toString());
                        correctValue = first;
                        services.get(1).put(1, first);
                    } else {
                        System.out.println("Collector: fixing first service " + services.get(0).toString());
                        correctValue = second;
                        services.get(0).put(1, second);
                    }
                    System.out.println("Collector: invoked put with correct value");


                }

            }

        }catch(Exception e) {
            System.out.println("Collector: error occured = " + e.toString());
        }
    }
}
