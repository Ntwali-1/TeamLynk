package utils;

import employees.Employee;
import java.util.Comparator;

public class EmployeesSalaryComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee a, Employee b) {
        return Double.compare(a.getSalary(), b.getSalary());
    }
}
