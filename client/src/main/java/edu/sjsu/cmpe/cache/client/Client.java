package edu.sjsu.cmpe.cache.client;

/**
 * Created by sindhu on 12/20/14.
 */
public class Client {

    public static  void main(String[] args){
        CRDTClient crdtClient = new CRDTClient();
        crdtClient.put(1, "a");

        System.out.println("Sleeping. Bring down the server A now");

        try {
            Thread.sleep(30000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        crdtClient.put(1, "b");

        System.out.println("Sleeping. Bring up the server A now");


        try {
            Thread.sleep(30000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        System.out.println("Printing get output " + crdtClient.get(1));

    }

}
