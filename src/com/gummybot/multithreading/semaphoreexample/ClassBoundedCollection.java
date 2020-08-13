package com.gummybot.multithreading.semaphoreexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class ClassBoundedCollection {
    private final Semaphore semaphore;
    private List<Integer> arrayList;

    public ClassBoundedCollection(int limit){
        this.semaphore = new Semaphore(limit);
        //this will serialize all operations on arrayList making it synchronized
        this.arrayList = Collections.synchronizedList(new ArrayList<Integer>());
    }

    public boolean addElement(Integer element) throws InterruptedException {
        boolean elementAdded = false;
        semaphore.acquire();
        elementAdded = this.arrayList.add(element);
        if(!elementAdded)
            semaphore.release();
        return elementAdded;
    }

    public boolean removeElement(Integer element){
        boolean elementRemoved = this.arrayList.remove(element);
        if(elementRemoved)
            semaphore.release();
        return elementRemoved;
    }

    public static void main(String[] args) {
        ClassBoundedCollection obj = new ClassBoundedCollection(7);

        new Thread( () -> {
            for(int i = 1; i<=20; i++){
                try {
                    if(obj.addElement(i))
                        System.out.println(Thread.currentThread().getName() + " is adding value: "+ i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } ).start();

        new Thread( () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 1; i<=20; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(obj.removeElement(i))
                    System.out.println(Thread.currentThread().getName() + " is removing value: "+ i);
            }
        } ).start();
    }
}
