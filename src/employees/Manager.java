package employees;

public class Manager extends Employee {

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
}
