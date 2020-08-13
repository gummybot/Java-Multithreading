package com.gummybot.multithreading.bankingexample;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ClassBankAccount {
    private final int accountID;
    private final String accountHolderName;
    private double accountBalance;
    private final Lock lock = new ReentrantLock();

    public ClassBankAccount(int accountID, String accountHolderName, double accountBalance) {
        this.accountID = accountID;
        this.accountHolderName = accountHolderName;
        this.accountBalance = accountBalance;
    }

    public void displayAccountDetails(){
        if(this.lock.tryLock()) {
            System.out.printf("Account ID: %d%n", accountID);
            System.out.printf("Account Holder Name: %s%n", accountHolderName);
            System.out.printf("Current Account Balance: %.2f%n", accountBalance);
            this.lock.unlock();
        }
    }

    public boolean withdraw(double amount) throws InterruptedException {
        if(this.lock.tryLock()) {
            Thread.sleep(100);
            if (amount <= accountBalance) {
                accountBalance -= amount;
                System.out.printf("%s is withdrawing %.2f amount.%n", this.accountHolderName, amount);
                System.out.printf("New balance of account no. %d is %.2f%n", this.accountID, this.accountBalance);
                this.lock.unlock();
                return true;
            } else {
                System.out.printf("Insufficient balance in account no. %d%n", this.accountID);
                System.out.printf("Current Balance: %.2f%n", this.accountBalance);
                this.lock.unlock();
            }
        }
        else{
            System.out.printf("%s:\tAccount activity is currently disabled for account no. %d%n", Thread.currentThread().getName(), this.accountID);
        }
        return false;
    }

    public boolean deposit(double amount) throws InterruptedException {
        if(this.lock.tryLock()) {
            Thread.sleep(100);
            accountBalance += amount;
            System.out.printf("%s is depositing %.2f amount.%n", this.accountHolderName, amount);
            System.out.printf("New balance of account no. %d is %.2f%n", this.accountID, this.accountBalance);
            this.lock.unlock();
            return true;
        }
        System.out.print(Thread.currentThread().getName() + ":\t");
        System.out.printf("Account activity is currently disabled for account no. %d%n", this.accountID);
        return false;
    }

    public boolean transfer(ClassBankAccount transferee, double amount) throws InterruptedException {
        if(this.withdraw(amount)){
            System.out.printf("%s is transferring amount: %.2f to %s%n", this.accountHolderName, amount, transferee.accountHolderName);
            if(transferee.deposit(amount)){
                System.out.println(Thread.currentThread().getName() + ":\tTransfer Successful");
                return true;
            }
            else{
                System.out.println(Thread.currentThread().getName() + ":\tTransfer Failed. Reverting transaction.");
                while(!this.deposit(amount)){
                    continue;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ClassBankAccount obj1 = new ClassBankAccount(111111, "Holder1", 100000);
        ClassBankAccount obj2 = new ClassBankAccount(222222, "Holder2", 300000);

        System.out.println("===============================");
        obj1.displayAccountDetails();
        System.out.println("===============================");
        obj2.displayAccountDetails();
        System.out.println("===============================");


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Thread t = new Thread( () -> {
            System.out.printf("%s says:\t::Initiating Transfer::%n", Thread.currentThread().getName());
            try {
                while (!(obj1.transfer(obj2, 1000))) {
                    Thread.sleep(100);
                    continue;
                }
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
            System.out.printf("%s says:\t::Transfer Complete::%n", Thread.currentThread().getName());
        } );


        for(int i: IntStream.rangeClosed(0,9).toArray()){
            executorService.submit(t);
        }


        executorService.shutdown();


        try {
            while (!executorService.awaitTermination(1L, TimeUnit.HOURS)) {
                System.out.println("Services not yet terminated. Awaiting Termination!!!!!");
            }
        }
        catch(InterruptedException ie){
            ie.printStackTrace();
        }


        System.out.println("All processes complete!!!! Final Balances:");
        System.out.println("===============================");
        obj1.displayAccountDetails();
        System.out.println("===============================");
        obj2.displayAccountDetails();
        System.out.println("===============================");
    }
}
