package employees;

public class Developer extends Employee {

    private static final long serialVersionUID = 1L;

    private String programmingLanguage;
    private Manager manager;

    public Developer(String name, double salary, String programmingLanguage, Manager manager) {
        super(name, salary);
        this.programmingLanguage = programmingLanguage;
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void displayInfo() {
        System.out.println("Developer [ID=" + getId() + ", Name=" + getName() +
                ", Salary=" + getSalary() + ", Language=" + programmingLanguage + 
                ", Manager=" + (manager != null ? manager.getName() : "None") + "]");
    }

    @Override
    public void work() {
        System.out.println(getName() + " is writing code in " + programmingLanguage + ".");
    }
}
