package com.gummybot.multithreading.workerexample.parallelprocess;

public class ClassSupervisor {
    String name;

    public ClassSupervisor(String name) {
        this.name = name;
    }

    public void startWork(){
        System.out.println(String.format("%s is starting the tasks", this.name));
        ClassWorker worker1 = new ClassWorker("Worker1");
        ClassWorker worker2 = new ClassWorker("Worker2");
        worker1.start();
        worker2.start();
    }
}
