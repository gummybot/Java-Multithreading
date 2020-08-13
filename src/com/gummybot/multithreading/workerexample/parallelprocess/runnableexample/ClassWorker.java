package com.gummybot.multithreading.workerexample.parallelprocess.runnableexample;

public class ClassWorker implements Runnable {
    private int[] array;
    private int sum = 0;
    private int lo, hi;

    public ClassWorker(int[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public void run() {
        for(int i = lo; i < hi; i++){
            sum += array[i];
        }
        ClassSupervisor.addSum(sum);
    }
}
