package dzenitakapetanovicipia.example;

public class Employee extends User{

    private Double salary;

    public Employee(String id, String username, String password) {

        super(id, username, password, "employee");
        this.salary = 6.0;
        this.setId(id);
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
