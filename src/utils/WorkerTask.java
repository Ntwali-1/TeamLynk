package utils;

import employees.Employee;
import java.util.concurrent.Callable;

public class WorkerTask implements Callable<String> {

    private Employee employee;

    public WorkerTask(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String call() {
        return employee.getName() + " finished work on background thread!";
    }
}
