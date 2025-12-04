import employees.*;
import utils.*;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    private static List<Employee> employees = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        boolean running = true;

        while (running) {
            System.out.println("\n=== Employee Management System ===");
            System.out.println("1. Add Manager");
            System.out.println("2. Add Developer");
            System.out.println("3. Show Employees (sorted by ID)");
            System.out.println("4. Sort by Name");
            System.out.println("5. Sort by Salary");
            System.out.println("6. Multithreaded Work (invokeAny)");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Enter a number.");
                scanner.nextLine();
                continue;
            }

            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> addManager();
                case 2 -> addDeveloper();
                case 3 -> showEmployees();
                case 4 -> sortByName();
                case 5 -> sortBySalary();
                case 6 -> runMultithreadWork();
                case 7 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye!");
    }

    private static void addManager() {
        try {
            System.out.print("Enter Manager name: ");
            String name = scanner.nextLine();
            System.out.print("Enter salary: ");
            double salary = scanner.nextDouble();
            System.out.print("Team size: ");
            int team = scanner.nextInt();
            employees.add(new Manager(name, salary, team));
            System.out.println("Manager added!");
        } catch (Exception e) {
            System.out.println("Error adding manager.");
            scanner.nextLine();
        }
    }

    private static void addDeveloper() {
        try {
            System.out.print("Enter Developer name: ");
            String name = scanner.nextLine();
            System.out.print("Enter salary: ");
            double salary = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Language: ");
            String lang = scanner.nextLine();
            employees.add(new Developer(name, salary, lang));
            System.out.println("Developer added!");
        } catch (Exception e) {
            System.out.println("Error adding developer.");
        }
    }

    private static void showEmployees() {
        Collections.sort(employees); // uses Comparable
        employees.forEach(Employee::displayInfo);
    }

    private static void sortByName() {
        employees.sort(new EmployeeNamesComparator());
        System.out.println("Sorted by name:");
        showEmployees();
    }

    private static void sortBySalary() {
        employees.sort(new EmployeesSalaryComparator());
        System.out.println("Sorted by salary:");
        showEmployees();
    }

    private static void runMultithreadWork() {
        if (employees.isEmpty()) {
            System.out.println("No employees to run.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Callable<String>> tasks = new ArrayList<>();

        for (Employee e : employees) {
            tasks.add(new WorkerTask(e));
        }

        try {
            String result = executor.invokeAny(tasks); // returns fastest thread
            System.out.println("Fastest result: " + result);
        } catch (Exception e) {
            System.out.println("Error running threads.");
        }

        executor.shutdown();
    }
}
