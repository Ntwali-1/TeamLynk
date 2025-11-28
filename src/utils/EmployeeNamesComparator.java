package utils;

import employees.Employee;
import java.util.Comparator;

public class EmployeeNamesComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee a, Employee b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
