package employees;

import interfaces.Workable;

public class Employee implements Workable, Comparable<Employee> {

    private static int idCounter = 1;
    private int id;
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }

        this.id = idCounter++;
        this.name = name;
        this.salary = salary;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }

    public void setName(String name) { this.name = name; }
    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative.");
        this.salary = salary;
    }

    public void displayInfo() {
        System.out.println("Employee [ID=" + id + ", Name=" + name + ", Salary=" + salary + "]");
    }

    @Override
    public void work() {
        System.out.println(name + " is working...");
    }

    public static void showTotalEmployees() {
        System.out.println("Total employees so far: " + (idCounter - 1));
    }

    // Comparable → compare by ID
    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.id, other.id);
    }
}
