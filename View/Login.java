package View;

import java.awt.*;
import javax.swing.*;
import Controller.*;
import Model.Student;

public class Login extends JFrame {
    private JLabel loginLabel, userLabel, passLabel;
    private JTextField userTextField;
    private JPasswordField passTextField;
    private JButton loginButton;

    private Authentication auth;
    private StudentController studentController;

    public Login() {
        setTitle("Login");
        setSize(400, 250);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        auth = new Authentication();
        studentController = new StudentController();

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        loginLabel = new JLabel("Login", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(loginLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        userLabel = new JLabel("Username:");
        passLabel = new JLabel("Password:");
        userTextField = new JTextField(15);
        passTextField = new JPasswordField(15);

        formPanel.add(userLabel);
        formPanel.add(userTextField);
        formPanel.add(passLabel);
        formPanel.add(passTextField);

        panel.add(formPanel, BorderLayout.CENTER);

        loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String username = userTextField.getText().trim();
            String password = new String(passTextField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username or password!");
                return;
            }
            if (auth.isAdmin(username)) {
                JOptionPane.showMessageDialog(this, "Welcome Admin!");
                this.dispose();
                new AdminHome();
            } else {
                Student student = studentController.getAllStudents().stream()
                        .filter(s -> s.getId().equals(username))
                        .findFirst()
                        .orElse(null);

                if(student == null) {
                    JOptionPane.showMessageDialog(this, "Username is not Correct");
                    return;
                }

                JOptionPane.showMessageDialog(this, "Welcome " + student.getFirstName());
                this.dispose();
                new StudentHome(student);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
