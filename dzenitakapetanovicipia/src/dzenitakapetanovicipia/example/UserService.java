package dzenitakapetanovicipia.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;
    private final MongoCollection<Document> payrollCollection;

    public UserService() {
        this.database = MongoDBController.getInstance().getDatabase();
        this.collection = database.getCollection("Users");
        this.payrollCollection = database.getCollection("Salary");
    }

    public void addUser(User user) {
        try {
            Document userDoc = new Document("_id", user.getId())
                    .append("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("role", user.getRole());

            if (user instanceof Employee) {
                Employee emp = (Employee) user;
                userDoc.append("salary", emp.getSalary());
            }

            collection.insertOne(userDoc);
            System.out.println("User created successfully!");
        } catch (Exception e) {
            System.err.println("Error adding user: " + e.getMessage());
            throw new RuntimeException("Error adding user", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                String id = doc.getString("_id");
                String username = doc.getString("username");
                String password = doc.getString("password");
                String role = doc.getString("role");
                Double salary = doc.containsKey("salary") ? doc.getDouble("salary") : null;

                User user = createUserByRole(role, id, username, password, salary);
                users.add(user);
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }

    private User createUserByRole(String role, String id, String username, String password, Double salary) {
        switch (role.toLowerCase()) {
            case "employee":
                Employee employee = new Employee(id, username, password);
                if (salary != null) employee.setSalary(salary);
                return employee;
            case "manager":
                return new Manager(id, username, password);
            case "superadmin":
                return new SuperAdmin(id, username, password);
            default:
                throw new IllegalStateException("Unexpected role: " + role);
        }
    }

    public void addPayroll(String userID) {
        try {
            Document doc = collection.find(Filters.eq("_id", userID)).first();
            if (doc != null) {
                Salary payroll = new Salary(userID, LocalDate.now(), doc.getDouble("salary"));
                Document newPayroll = new Document()
                        .append("employeeID", payroll.getEmployeeID())
                        .append("date", payroll.getDate())
                        .append("wage", payroll.getSalary());
                payrollCollection.insertOne(newPayroll);
                System.out.println("Payroll for " + userID + " added successfully.");
            } else {
                System.err.println("User not found: " + userID);
            }
        } catch (Exception e) {
            System.err.println("Error adding payroll: " + e.getMessage());
        }
    }

    public void updateUser(String userId, User user) {
        try {
            Document updatedData = new Document("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("role", user.getRole());

            if (user instanceof Employee) {
                Employee emp = (Employee) user;
                updatedData.append("salary", emp.getSalary());
            }

            collection.updateOne(Filters.eq("_id", userId), new Document("$set", updatedData));
            System.out.println("User updated!");
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw new RuntimeException("Error updating user", e);
        }
    }

    public void deleteUser(String userId) {
        try {
            collection.deleteOne(Filters.eq("_id", userId));
            System.out.println("User with ID: " + userId + " deleted!");
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw new RuntimeException("Error deleting user", e);
        }
    }
}