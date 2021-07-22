package com.test;

public class test_Thread implements Runnable {
    public static void main(String[] args) {
        new test_Thread();
    }
    test_Thread(){
        Thread a = new Thread(this,"a");
        Thread b = new Thread(this,"b");
        a.setPriority(Thread.MIN_PRIORITY);
        b.setPriority(Thread.MAX_PRIORITY);
        a.start();
        b.start();
    }

    @Override
    public void run() {
        switch (Thread.currentThread().getName()){
            case "b":
                System.out.println("��b��������");
                break;
            case "a":
                System.out.println("��a��������");
                break;
        }
        for(int i =0;i<11;i++){
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}
