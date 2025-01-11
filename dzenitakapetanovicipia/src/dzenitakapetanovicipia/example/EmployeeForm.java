package dzenitakapetanovicipia.example;

import javax.swing.*;

public class EmployeeForm {
    private JPanel employeePanel;
    private JLabel idLabel;
    private JLabel salaryLabel;

    public EmployeeForm(Employee emp) {
        JFrame frame = new JFrame("Employee Panel");
        frame.setContentPane(employeePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        idLabel.setText(emp.getId());
        salaryLabel.setText(emp.getSalary().toString());
    }
}
