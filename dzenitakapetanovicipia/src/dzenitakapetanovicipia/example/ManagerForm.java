package dzenitakapetanovicipia.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManagerForm extends JFrame {
    private JTable managerTable;
    private JPanel managerPanel;
    private JScrollPane managerScrollPane;
    private JButton editSalaryButton;

    UserService userService = new UserService();

    private void editSalary() {
        int selectedRow = managerTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit the salary.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String employeeId = managerTable.getValueAt(selectedRow, 0).toString();
        String employeeUsername = managerTable.getValueAt(selectedRow, 1).toString();
        String currentSalary = managerTable.getValueAt(selectedRow, 2).toString();


        JTextField salaryField = new JTextField(currentSalary);
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(new JLabel("Editing salary for: " + employeeUsername));
        panel.add(salaryField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Salary",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newSalary = salaryField.getText().trim();


            if (newSalary.isEmpty() || !newSalary.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid salary.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            List<User> users = userService.getAllUsers();
            Double salary1 = 0.0;

            for(User user : users) {

                if(user.getId().equals(employeeId)) {

                    Employee emp = (Employee) user;
                    emp.setSalary(Double.valueOf(newSalary));
                    userService.updateUser(employeeId, emp);
                }
            }

            managerTable.setValueAt(newSalary, selectedRow, 2);

            JOptionPane.showMessageDialog(this, "Salary updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public ManagerForm() {
        setTitle("Manager Panel");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "id", "username", "salary" }, 0);
        managerTable = new JTable(tableModel);
        managerScrollPane = new JScrollPane(managerTable);


        add(managerScrollPane, BorderLayout.CENTER);


        editSalaryButton = new JButton("Edit Salary");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(editSalaryButton);
        add(buttonPanel, BorderLayout.SOUTH);


        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.getRole().equals("employee")) {
                Employee emp = (Employee) user; // Cast User to Employee
                tableModel.addRow(new Object[] { emp.getId(), emp.getUsername(), emp.getSalary() });
            }
        }

        editSalaryButton.addActionListener(e -> editSalary());
    }


}
