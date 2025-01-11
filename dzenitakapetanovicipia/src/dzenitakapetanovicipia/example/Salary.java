package dzenitakapetanovicipia.example;

import java.time.LocalDate;

public class Salary {

    private String employeeID;
    private LocalDate date;
    private Double salary;

    public Salary(String id, LocalDate date, Double salary) {

        this.employeeID = id;
        this.date = date;
        this.salary = salary;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
