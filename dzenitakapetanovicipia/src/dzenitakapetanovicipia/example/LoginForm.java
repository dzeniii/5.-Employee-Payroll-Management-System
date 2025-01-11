package dzenitakapetanovicipia.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.List;

import javax.swing.*;

public class LoginForm {
    private JPanel mainpanel;
    private JTextField textField1;
    private JButton loginform;
    private JLabel username;
    private JLabel password;
    private JPasswordField passwordField1;

    private UserService userService = new UserService();

    public void login() {

        String username = textField1.getText();
        String password = passwordField1.getText();

        List<User> users = userService.getAllUsers();

        for(User user : users) {

            if(username.equals(user.getUsername()) && password.equals(user.getPassword())) {

                switch (user.getRole()) {

                    case "employee":
                        new EmployeeForm((Employee) user);
                        break;
                    case "manager":
                        new ManagerForm().setVisible(true);
                        break;
                    case "superadmin":
                        new SuperAdminPanel().setVisible(true);
                        System.out.println("admin");
                        break;
                    default:
                        System.out.println("no user found");
                        break;
                }
            }
        }
    }

    public LoginForm() {
        JFrame frame = new JFrame("LoginForm");
        frame.setContentPane(mainpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        loginform.addActionListener(e -> login());
    }
}
