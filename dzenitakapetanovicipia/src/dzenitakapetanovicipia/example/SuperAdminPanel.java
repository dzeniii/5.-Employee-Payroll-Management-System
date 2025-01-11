package dzenitakapetanovicipia.example;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.UUID;

public class SuperAdminPanel extends JFrame {
    private JTable adminTable;
    private JPanel adminPanel;
    private JButton newButton;
    private JScrollPane adminScrollPane;
    private JButton editButton;
    private JButton deleteButton;
    private JPanel buttonPanel;

    private UserService userService = new UserService();

    public void deleteUser() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1) {
            String selectedUser = adminTable.getValueAt(selectedRow, 0).toString();
            ((DefaultTableModel) adminTable.getModel()).removeRow(selectedRow);
            userService.deleteUser(selectedUser);
        }
    }

    public void newUser(DefaultTableModel model) {
        // Create input fields
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        String[] roles = { "employee", "manager", "superadmin" };
        JComboBox<String> roleComboBox = new JComboBox<>(roles);


        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);


        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add New User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );


        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            System.out.println(username + password + role);

            User newUser;
            if(role.equals("employee")) {
                newUser = new Employee(UUID.randomUUID().toString(), username, password);
                userService.addUser(newUser);
                model.addRow(new Object[] { newUser.getId(), newUser.getUsername(), newUser.getRole() });
            } else if(role.equals("manager")) {
                newUser = new Manager(UUID.randomUUID().toString(), username, password);
                userService.addUser(newUser);
                model.addRow(new Object[] { newUser.getId(), newUser.getUsername(), newUser.getRole() });
            } else if(role.equals("superadmin")) {
                newUser = new SuperAdmin(UUID.randomUUID().toString(), username, password);
                userService.addUser(newUser);
                model.addRow(new Object[] { newUser.getId(), newUser.getUsername(), newUser.getRole() });
            }


            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void editUser() {
        int selectedRow = adminTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String userId = adminTable.getValueAt(selectedRow, 0).toString();
        String username = adminTable.getValueAt(selectedRow, 1).toString();
        String role = adminTable.getValueAt(selectedRow, 2).toString();


        JLabel idLabel = new JLabel("User ID: " + userId, SwingConstants.CENTER);
        JTextField usernameField = new JTextField(username);
        JPasswordField passwordField = new JPasswordField(); // Password will be empty for editing
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"employee", "manager", "superadmin"});
        roleComboBox.setSelectedItem(role);


        List<User> users = userService.getAllUsers();
        Double salary1 = 0.0;

        for(User user : users) {

            if(user.getId().equals(userId) && user.getRole().equals("employee")) {

                Employee emp = (Employee) user;
                salary1 = emp.getSalary();
            }
        }

        JTextField salaryField = new JTextField();
        if (role.equals("Employee")) {
            salaryField.setText(salary1.toString()); // Assume userService can fetch the salary
        }


        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(idLabel);
        panel.add(new JLabel()); // Empty placeholder for spacing
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);


        if (role.equals("employee")) {
            panel.add(new JLabel("Salary:"));
            panel.add(salaryField);
        }


        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();
            String newRole = (String) roleComboBox.getSelectedItem();
            String newSalary = role.equals("employee") ? salaryField.getText().trim() : null;


            if (newUsername.isEmpty() || (role.equals("employee") && newSalary.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if(newRole.equals("employee")) {

                Employee updatedUser = new Employee(userId, newUsername, newPassword);
                updatedUser.setRole(newRole);
                updatedUser.setSalary(Double.valueOf(newSalary));
                userService.updateUser(updatedUser.getId(), updatedUser);
            } else if(newRole.equals("manager")) {

                User updatedUser = new Manager(userId, newUsername, newPassword);
                userService.updateUser(updatedUser.getId(), updatedUser);
            } else if(newRole.equals("superadmin")) {

                User updatedUser = new SuperAdmin(userId, newUsername, newPassword);
                userService.updateUser(updatedUser.getId(), updatedUser);
            }


            DefaultTableModel model = (DefaultTableModel) adminTable.getModel();
            model.setValueAt(newUsername, selectedRow, 1);
            model.setValueAt(newRole, selectedRow, 2);

            JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public SuperAdminPanel() {
        List<User> users = userService.getAllUsers();

        setTitle("Super Admin Control");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Set the layout for JFrame


        DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "id", "username", "role" }, 0);
        adminTable = new JTable(tableModel);
        adminScrollPane = new JScrollPane(adminTable);

        for (User user : users) {
            tableModel.addRow(new Object[] { user.getId(), user.getUsername(), user.getRole() });
        }


        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deleteButton = new JButton("Delete");
        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        newButton.addActionListener(e -> newUser(tableModel));
        deleteButton.addActionListener(e -> deleteUser());
        editButton.addActionListener(e -> editUser());


        add(adminScrollPane, BorderLayout.CENTER); // Table in the center
        add(buttonPanel, BorderLayout.SOUTH); // Buttons at the bottom
    }
}
