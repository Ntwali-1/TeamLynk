import employees.*;
import utils.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String DATA_FILE = "employees.dat";

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
            System.out.println("6. Simulate Work (multithreading)");
            System.out.println("7. Save Employees To File");
            System.out.println("8. Load Employees From File");
            System.out.println("9. Search Employees By Name (Regex)");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Enter a number.");
                scanner.nextLine();
                continue;
            }

            scanner.nextLine();

            switch (choice) {
                case 1 -> addManager();
                case 2 -> addDeveloper();
                case 3 -> showEmployees();
                case 4 -> sortByName();
                case 5 -> sortBySalary();
                case 6 -> runMultithreadWork();
                case 7 -> saveEmployees();
                case 8 -> loadEmployees();
                case 9 -> searchEmployeesByNameRegex();
                case 10 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye!");
    }

    private static String readNonEmptyLine(String label) {
        while (true) {
            System.out.print(label);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Value cannot be empty.");
        }
    }

    private static String readValidatedName(String label) {
        Pattern pattern = Pattern.compile("^[A-Za-z ]{2,40}$");
        while (true) {
            String name = readNonEmptyLine(label);
            if (pattern.matcher(name).matches()) {
                return name;
            }
            System.out.println("Name can only contain letters and spaces, and must be 2 to 40 characters long.");
        }
    }

    private static String readValidatedLanguage(String label) {
        Pattern pattern = Pattern.compile("^[A-Za-z+#]{2,20}$");
        while (true) {
            String language = readNonEmptyLine(label);
            if (pattern.matcher(language).matches()) {
                return language;
            }
            System.out.println("Language can only contain letters and characters like + or #.");
        }
    }

    private static double readPositiveDouble(String label) {
        while (true) {
            System.out.print(label);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Value must be zero or positive.");
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static int readPositiveInt(String label) {
        while (true) {
            System.out.print(label);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Value must be zero or positive.");
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private static void addManager() {
        try {
            String name = readValidatedName("Enter Manager name: ");
            double salary = readPositiveDouble("Enter salary: ");
            int team = readPositiveInt("Team size: ");
            employees.add(new Manager(name, salary, team));
            System.out.println("Manager added.");
        } catch (Exception e) {
            System.out.println("Error adding manager.");
        }
    }

    private static void addDeveloper() {
        try {
            // Check if there are any managers available
            List<Manager> managers = getAvailableManagers();
            if (managers.isEmpty()) {
                System.out.println("No managers available. Please add a manager first.");
                return;
            }

            String name = readValidatedName("Enter Developer name: ");
            double salary = readPositiveDouble("Enter salary: ");
            String lang = readValidatedLanguage("Language: ");

            // Display available managers
            System.out.println("\nAvailable Managers:");
            for (int i = 0; i < managers.size(); i++) {
                System.out.println((i + 1) + ". " + managers.get(i).getName());
            }

            // Select a manager
            int managerIndex = readManagerSelection("Select a manager (1-" + managers.size() + "): ", managers.size());
            Manager selectedManager = managers.get(managerIndex - 1);

            employees.add(new Developer(name, salary, lang, selectedManager));
            System.out.println("Developer added with manager: " + selectedManager.getName());
        } catch (Exception e) {
            System.out.println("Error adding developer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Manager> getAvailableManagers() {
        List<Manager> managers = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp instanceof Manager) {
                managers.add((Manager) emp);
            }
        }
        return managers;
    }

    private static int readManagerSelection(String prompt, int maxValue) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= 1 && value <= maxValue) {
                    return value;
                }
                System.out.println("Please enter a number between 1 and " + maxValue + ".");
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static void showEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        Collections.sort(employees);
        employees.forEach(Employee::displayInfo);
        Employee.showTotalEmployees();
    }

    private static void sortByName() {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        employees.sort(new EmployeeNamesComparator());
        System.out.println("Sorted by name:");
        showEmployees();
    }

    private static void sortBySalary() {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        employees.sort(new EmployeesSalaryComparator());
        System.out.println("Sorted by salary:");
        showEmployees();
    }

    private static void runMultithreadWork() {
        if (employees.isEmpty()) {
            System.out.println("No employees to run.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(4, employees.size()));
        List<Callable<String>> tasks = new ArrayList<>();

        for (Employee e : employees) {
            tasks.add(new WorkerTask(e));
        }

        try {
            List<Future<String>> results = executor.invokeAll(tasks);
            System.out.println("Results from background work:");
            for (Future<String> future : results) {
                try {
                    System.out.println(future.get());
                } catch (Exception ex) {
                    System.out.println("Task failed.");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }

    private static void saveEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees to save.");
            return;
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(new ArrayList<>(employees));
            System.out.println("Employees saved to " + DATA_FILE);
        } catch (Exception e) {
            System.out.println("Error saving employees.");
        }
    }

    private static void loadEmployees() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Object obj = in.readObject();
            if (obj instanceof List) {
                List<?> loaded = (List<?>) obj;
                List<Employee> restored = new ArrayList<>();
                for (Object o : loaded) {
                    if (o instanceof Employee) {
                        restored.add((Employee) o);
                    }
                }
                employees = restored;
                System.out.println("Employees loaded from " + DATA_FILE);
            } else {
                System.out.println("File did not contain a list of employees.");
            }
        } catch (Exception e) {
            System.out.println("Error loading employees.");
        }
    }

    private static void searchEmployeesByNameRegex() {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        System.out.print("Enter regex pattern for name search: ");
        String patternText = scanner.nextLine();
        try {
            Pattern pattern = Pattern.compile(patternText, Pattern.CASE_INSENSITIVE);
            boolean found = false;
            for (Employee employee : employees) {
                Matcher matcher = pattern.matcher(employee.getName());
                if (matcher.find()) {
                    employee.displayInfo();
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No employees matched the pattern.");
            }
        } catch (Exception e) {
            System.out.println("Invalid regex pattern.");
        }
    }
}
