import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ------------------------ ABSTRACTION -------------------------
interface Workable {
    void work();
}

// ------------------------ BASE CLASS --------------------------
class Employee implements Workable {
    private static int idCounter = 1; // STATIC: shared among all employees
    private int id;
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.id = idCounter++;
        this.name = name;
        this.salary = salary;
    }

    // ENCAPSULATION: private fields with getters & setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    // POLYMORPHISM: can be overridden
    public void displayInfo() {
        System.out.println("Employee [ID=" + id + ", Name=" + name + ", Salary=" + salary + "]");
    }

    // From interface
    public void work() {
        System.out.println(name + " is working...");
    }

    // Static method
    public static void showTotalEmployees() {
        System.out.println("Total employees so far: " + (idCounter - 1));
    }
}

// ------------------------ INHERITANCE --------------------------
class Manager extends Employee {
    private int teamSize;

    public Manager(String name, double salary, int teamSize) {
        super(name, salary);
        this.teamSize = teamSize;
    }

    @Override
    public void displayInfo() {
        System.out.println("Manager [ID=" + getId() + ", Name=" + getName() +
                ", Salary=" + getSalary() + ", TeamSize=" + teamSize + "]");
    }

    @Override
    public void work() {
        System.out.println(getName() + " is managing a team of " + teamSize + " people.");
    }

    public void approveLeave() {
        System.out.println(getName() + " approved a leave request.");
    }
}

class Developer extends Employee {
    private String programmingLanguage;

    public Developer(String name, double salary, String programmingLanguage) {
        super(name, salary);
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public void displayInfo() {
        System.out.println("Developer [ID=" + getId() + ", Name=" + getName() +
                ", Salary=" + getSalary() + ", Language=" + programmingLanguage + "]");
    }

    @Override
    public void work() {
        System.out.println(getName() + " is writing code in " + programmingLanguage + ".");
    }

    public void debugCode() {
        System.out.println(getName() + " is debugging code.");
    }
}

// ------------------------ MAIN APP --------------------------
public class Main {
    private static List<Employee> employees = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Employee Management System ===");
            System.out.println("1. Add Manager");
            System.out.println("2. Add Developer");
            System.out.println("3. Show All Employees");
            System.out.println("4. Make Employees Work");
            System.out.println("5. Show Total Employees");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addManager();
                case 2 -> addDeveloper();
                case 3 -> showEmployees();
                case 4 -> makeEmployeesWork();
                case 5 -> Employee.showTotalEmployees();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void addManager() {
        System.out.print("Enter Manager name: ");
        String name = scanner.nextLine();
        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();
        System.out.print("Enter team size: ");
        int teamSize = scanner.nextInt();
        employees.add(new Manager(name, salary, teamSize));
        System.out.println("Manager added!");
    }

    private static void addDeveloper() {
        System.out.print("Enter Developer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.print("Enter programming language: ");
        String lang = scanner.nextLine();
        employees.add(new Developer(name, salary, lang));
        System.out.println("Developer added!");
    }

    private static void showEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee e : employees) {
                e.displayInfo(); // POLYMORPHISM in action
            }
        }
    }

    private static void makeEmployeesWork() {
        if (employees.isEmpty()) {
            System.out.println("No employees to work.");
        } else {
            for (Employee e : employees) {
                e.work(); // Interface + Polymorphism in action
            }
        }
    }
}
