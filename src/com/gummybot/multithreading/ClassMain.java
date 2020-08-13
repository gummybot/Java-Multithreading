package com.gummybot.multithreading;

import com.gummybot.multithreading.workerexample.parallelprocess.ClassSupervisor;
//import com.gummybot.multithreading.workerexample.sequentialprocess.ClassSupervisor;

public class ClassMain {
    public static void main(String[] args) {
        System.out.println("Test");
        ClassSupervisor supervisor = new ClassSupervisor("Supervisor");
        supervisor.startWork();
    }
}
