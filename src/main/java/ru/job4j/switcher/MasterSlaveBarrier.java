package ru.job4j.switcher;

public class MasterSlaveBarrier {
    private volatile boolean flag = true;

    public void tryMaster() throws InterruptedException {
            synchronized (this) {
                while (!flag){
                    this.wait();
                }
                flag = true;
            }
    }

    public void trySlave() throws InterruptedException {
        synchronized (this) {
            while (flag){
                this.wait();
            }
            flag = false;
        }
    }

    public void doneMaster() {
        synchronized (this){
            flag = false;
            this.notifyAll();
        }
    }

    public void doneSlave() {
        synchronized (this){
            flag = true;
            this.notifyAll();
        }
    }
}

