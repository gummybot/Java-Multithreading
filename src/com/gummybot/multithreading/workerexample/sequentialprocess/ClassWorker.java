package com.gummybot.multithreading.workerexample.sequentialprocess;

class ClassWorker {
    String name;

    public ClassWorker(String name) {
        this.name = name;
    }
    public void executeWork() throws InterruptedException {
        for (int i=0; i<10; i++){
            Thread.sleep(100);
            System.out.println(String.format("Worker %s is executing task no. %d", this.name, i+1));
        }
    }
}
