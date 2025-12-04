package utils;

import employees.Employee;

import java.util.Random;
import java.util.concurrent.Callable;

public class WorkerTask implements Callable<String> {

    private final Employee employee;

    public WorkerTask(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String call() {
        try {
            int delay = new Random().nextInt(1000) + 500;
            Thread.sleep(delay);
            employee.work();
            return employee.getName() + " completed work in " + delay + " ms on thread " + Thread.currentThread().getName();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return employee.getName() + " was interrupted.";
        }
    }
}
