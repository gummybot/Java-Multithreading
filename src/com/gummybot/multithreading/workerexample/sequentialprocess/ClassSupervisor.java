package com.gummybot.multithreading.workerexample.sequentialprocess;

public class ClassSupervisor {
    String name;

    public ClassSupervisor(String name) {
        this.name = name;
    }

    public void startWork(){
        System.out.println(String.format("%s is starting the tasks", this.name));
        ClassWorker worker1 = new ClassWorker("Worker1");
        ClassWorker worker2 = new ClassWorker("Worker2");
        try {
            worker1.executeWork();
            worker2.executeWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
