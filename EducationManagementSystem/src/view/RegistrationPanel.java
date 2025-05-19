package view;

import controller.AuthController;
import model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class RegistrationPanel extends JPanel {
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JComboBox<String> courseComboBox;
    private JComboBox<String> yearLevelComboBox;
    private JComboBox<String> semesterComboBox; // New field for semester selection
    private JButton registerButton;
    private JButton backButton;
    private MainFrame parent;
    private Color textColor = new Color(50, 50, 50); // Dark gray text for better readability

    // Color scheme (matching LoginPanel)
    private Color beigeBackground = new Color(245, 235, 215); // Main beige background
    private Color darkBeige = new Color(225, 210, 185);       // Slightly darker beige for contrast
    private Color oliveGreen = new Color(85, 107, 47);        // Olive green for header background
    private Color greenAccent = new Color(75, 145, 80);       // Professional green for buttons
    private Color lightGreen = new Color(230, 240, 220);      // Light green for input fields

    public RegistrationPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Main panel with beige background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(beigeBackground);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Header panel with olive green background - smaller height
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(oliveGreen);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Title with smaller font
        JLabel titleLabel = new JLabel("Student Registration", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Center panel to contain form and provide centering
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(beigeBackground);

        // Form panel with card-like appearance
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(darkBeige, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // Setting up GridBagConstraints for compact form layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Create more compact styling for all form elements
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);

        // Create all form components with more compact dimensions
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(textColor);

        nameField = new JTextField();
        nameField.setFont(fieldFont);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        nameField.setBackground(lightGreen);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(textColor);

        usernameField = new JTextField();
        usernameField.setFont(fieldFont);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        usernameField.setBackground(lightGreen);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(textColor);

        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        passwordField.setBackground(lightGreen);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(labelFont);
        confirmPasswordLabel.setForeground(textColor);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(fieldFont);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        confirmPasswordField.setBackground(lightGreen);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(textColor);

        emailField = new JTextField();
        emailField.setFont(fieldFont);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        emailField.setBackground(lightGreen);

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setFont(labelFont);
        courseLabel.setForeground(textColor);

        String[] courses = {"IT", "Computer Science", "Information Systems", "Cybersecurity"};
        courseComboBox = new JComboBox<>(courses);
        courseComboBox.setFont(fieldFont);
        courseComboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        courseComboBox.setBackground(lightGreen);
        ((JComponent) courseComboBox.getRenderer()).setOpaque(true);

        //Create year level selection components
        JLabel yearLevelLabel = new JLabel("Year Level:");
        yearLevelLabel.setFont(labelFont);
        yearLevelLabel.setForeground(textColor);

        String[] yearLevels = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
        yearLevelComboBox = new JComboBox<>(yearLevels);
        yearLevelComboBox.setFont(fieldFont);
        yearLevelComboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        yearLevelComboBox.setBackground(lightGreen);
        ((JComponent) yearLevelComboBox.getRenderer()).setOpaque(true);

        // Create semester selection components
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(labelFont);
        semesterLabel.setForeground(textColor);

        String[] semesters = {"1st Semester", "2nd Semester"};
        semesterComboBox = new JComboBox<>(semesters);
        semesterComboBox.setFont(fieldFont);
        semesterComboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 1, true),
                new EmptyBorder(3, 6, 3, 6)
        ));
        semesterComboBox.setBackground(lightGreen);
        ((JComponent) semesterComboBox.getRenderer()).setOpaque(true);

        // Button panel for more compact buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerButton.setForeground(oliveGreen);
        registerButton.setBackground(greenAccent);
        registerButton.setBorder(new LineBorder(new Color(65, 125, 70), 1, true));
        registerButton.setFocusPainted(false);

        backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setForeground(oliveGreen);
        backButton.setBackground(greenAccent);
        backButton.setBorder(new LineBorder(new Color(65, 125, 70), 1, true));
        backButton.setFocusPainted(false);

        // Add components to form panel with more compact spacing
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 7, 5);
        formPanel.add(nameField, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(3, 5, 3, 5);
        formPanel.add(usernameLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 5, 7, 5);
        formPanel.add(usernameField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(3, 5, 3, 5);
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 5, 7, 5);
        formPanel.add(passwordField, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(3, 5, 3, 5);
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 5, 7, 5);
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(3, 5, 3, 5);
        formPanel.add(emailLabel, gbc);

        gbc.gridy = 9;
        gbc.insets = new Insets(0, 5, 7, 5);
        formPanel.add(emailField, gbc);

        gbc.gridy = 10;
        gbc.insets = new Insets(3, 5, 3, 5);
        formPanel.add(courseLabel, gbc);

        gbc.gridy = 11;
        gbc.insets = new Insets(0, 5, 7, 5);
        formPanel.add(courseComboBox, gbc);

        // Add year level components
        gbc.gridy = 12;
        gbc.insets = new Insets(3, 5, 3, 5);
        formPanel.add(yearLevelLabel, gbc);

        gbc.gridy = 13;
        gbc.insets = new Insets(0, 5, 7, 5);
        formPanel.add(yearLevelComboBox, gbc);

        // Add semester components
        gbc.gridy = 14;
        gbc.insets = new Insets(3, 5, 3, 5);
        formPanel.add(semesterLabel, gbc);

        gbc.gridy = 15;
        gbc.insets = new Insets(0, 5, 12, 5);
        formPanel.add(semesterComboBox, gbc);

        // Button layout
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.insets = new Insets(0, 8, 0, 8);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.weightx = 0.5;
        buttonPanel.add(registerButton, buttonGbc);

        buttonGbc.gridx = 1;
        buttonPanel.add(backButton, buttonGbc);

        gbc.gridy = 16; // Updated to account for all fields
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);

        // Button hover effects
        setupButtonHoverEffect(registerButton);
        setupButtonHoverEffect(backButton);

        // Add action listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    String name = nameField.getText();
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    String email = emailField.getText();
                    String course = (String) courseComboBox.getSelectedItem();
                    String yearLevel = (String) yearLevelComboBox.getSelectedItem(); // Get selected year level
                    String semester = (String) semesterComboBox.getSelectedItem(); // Get selected semester

                    // Create new student with pending registration status
                    String id = "S" + UUID.randomUUID().toString().substring(0, 8);
                    Student student = new Student(id, name, username, password, email, course, yearLevel, semester);

                    if (AuthController.registerStudent(student)) {
                        showSuccess("Registration successful! An admin will review your application.");
                        parent.showLoginPanel();
                    } else {
                        showError("Username already exists. Please choose a different one.");
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showLoginPanel();
            }
        });

        // Add form to the center panel to ensure proper centering
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerGbc.weightx = 1.0;
        centerGbc.weighty = 1.0;
        centerGbc.fill = GridBagConstraints.NONE; // Important for centering
        centerGbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(formPanel, centerGbc);

        // Assemble the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add to the main component
        add(mainPanel, BorderLayout.CENTER);

        // Set preferred sizes for more compact display
        Dimension fieldSize = new Dimension(250, 24);
        nameField.setPreferredSize(fieldSize);
        usernameField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        confirmPasswordField.setPreferredSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        courseComboBox.setPreferredSize(fieldSize);
        yearLevelComboBox.setPreferredSize(fieldSize);
        semesterComboBox.setPreferredSize(fieldSize);

        // Set button sizes
        Dimension buttonSize = new Dimension(120, 28);
        registerButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
    }

    private void setupButtonHoverEffect(JButton button) {
        Color originalColor = button.getBackground();
        Color hoverColor = new Color(85, 165, 90);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    private boolean validateForm() {
        if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                new String(passwordField.getPassword()).isEmpty() || emailField.getText().isEmpty()) {
            showError("All fields are required");
            return false;
        }

        if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
            showError("Passwords do not match");
            return false;
        }

        // Basic email validation
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Please enter a valid email address");
            return false;
        }

        return true;
    }

    private void showSuccess(String message) {
        UIManager.put("OptionPane.background", beigeBackground);
        UIManager.put("Panel.background", beigeBackground);
        UIManager.put("OptionPane.messageForeground", textColor);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(String message) {
        UIManager.put("OptionPane.background", beigeBackground);
        UIManager.put("Panel.background", beigeBackground);
        UIManager.put("OptionPane.messageForeground", textColor);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}