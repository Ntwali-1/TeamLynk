package employees;

public class Developer extends Employee {

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
}
