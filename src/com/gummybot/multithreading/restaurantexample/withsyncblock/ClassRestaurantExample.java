package com.gummybot.multithreading.restaurantexample.withsyncblock;

public class ClassRestaurantExample {
    private String lastCustomer;
    private int noOfBurgers;

    public String getLastCustomer() {
        return lastCustomer;
    }

    private void setLastCustomer(String lastCustomer) {
        this.lastCustomer = lastCustomer;
    }

    public int getNoOfBurgers() {
        return noOfBurgers;
    }

    private void setNoOfBurgers(int noOfBurgers) {
        this.noOfBurgers = noOfBurgers;
    }

    public ClassRestaurantExample() {
        this.setNoOfBurgers(0);
    }

    private void prepBurger(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //adding the code in synchronized block will make thread executions serializable
    //This will give the correct output
    public void sellBurger(String name){
        //Since below both statements do not act on shared variables, they are not part of the critical section
        //therefore they can be kept out of the sync block and the code will still function properly
        //also moving the long wait process out of the block reduces execution time
        this.prepBurger();
        System.out.println(name + " bought a burger");
        synchronized (this) {
            this.setLastCustomer(name);
            this.setNoOfBurgers(this.getNoOfBurgers() + 1);
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ClassRestaurantExample restObj = new ClassRestaurantExample();

        Thread t1 = new Thread( ()-> restObj.sellBurger("Alpha"));

        Thread t2 = new Thread( ()-> restObj.sellBurger("Beta"));

        Thread t3 = new Thread( ()-> restObj.sellBurger("Gamma"));

        Thread t4 = new Thread( ()-> restObj.sellBurger("Delta"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();


        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total burgers sold are " + restObj.getNoOfBurgers());
        System.out.println(restObj.getLastCustomer() + " bought the last burger");
        System.out.println("Process ran total for " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }
}
