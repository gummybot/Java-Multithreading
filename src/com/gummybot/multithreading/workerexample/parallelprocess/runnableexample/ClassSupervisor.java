package com.gummybot.multithreading.workerexample.parallelprocess.runnableexample;

import java.util.stream.IntStream;

public class ClassSupervisor {
    public static int[] array = IntStream.rangeClosed(0,5000).toArray();
    public static int sum = 0;
    public static int total = IntStream.rangeClosed(0,5000).sum();

    public static void addSum(int add){
        sum += add;
    }

    public static void main(String[] args) throws InterruptedException {
        //ClassSupervisor sup = new ClassSupervisor();
        ClassWorker w1 = new ClassWorker(array, 0, array.length/2);
        ClassWorker w2 = new ClassWorker(array, array.length/2, array.length);
        Thread t1 = new Thread(w1);
        Thread t2 = new Thread(w2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //sum = sum + w1.getSum() + w2.getSum();
        System.out.println(String.format("Sum calculated with Runnable = %d\nSum calculated with in-built function = %d", sum, total));
    }
}
