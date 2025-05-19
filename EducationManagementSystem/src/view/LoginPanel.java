package view;

import controller.AuthController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private MainFrame parent;
    private Color textColor = new Color(50, 50, 50); // Dark gray text for better readability

    // Color scheme
    private Color beigeBackground = new Color(245, 235, 215); // Main beige background
    private Color darkBeige = new Color(225, 210, 185);       // Slightly darker beige for contrast
    private Color oliveGreen = new Color(85, 107, 47);        // Olive green for header background
    private Color greenAccent = new Color(75, 145, 80);       // Professional green for buttons
    private Color lightGreen = new Color(230, 240, 220);      // Light green for input fields

    public LoginPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Main panel with beige background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(beigeBackground);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Header panel with olive green background
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(oliveGreen);
        headerPanel.setBorder(new EmptyBorder(40, 20, 40, 20));

        // Title with improved typography
        JLabel titleLabel = new JLabel("Education Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE); // White text for better contrast on olive background
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Form container with padding
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(beigeBackground);
        formContainer.setBorder(new EmptyBorder(40, 80, 40, 80));

        // Login form panel with card-like appearance
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(darkBeige, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 10, 12, 10);

        // Username field with styling - LARGER SIZE
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        usernameLabel.setForeground(textColor);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(350, 45)); // Increased width and height
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 2, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        usernameField.setBackground(lightGreen);

        // Password field with styling - LARGER SIZE
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passwordLabel.setForeground(textColor);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(350, 45)); // Increased width and height
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(greenAccent, 2, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setBackground(lightGreen);

        // Login button with green styling
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setForeground(oliveGreen);
        loginButton.setBackground(greenAccent);
        loginButton.setPreferredSize(new Dimension(160, 45));
        loginButton.setBorder(new LineBorder(new Color(65, 125, 70), 1, true));
        loginButton.setFocusPainted(false);

        // Register button with similar styling
        registerButton = new JButton("Register as Student");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerButton.setForeground(oliveGreen);
        registerButton.setBackground(greenAccent);
        registerButton.setPreferredSize(new Dimension(180, 45));
        registerButton.setBorder(new LineBorder(new Color(65, 125, 70), 1, true));
        registerButton.setFocusPainted(false);

        // Button panel for horizontal layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 10);
        formPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(12, 10, 12, 10);
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 25, 10); // Added more space before buttons
        formPanel.add(passwordField, gbc);

        // Add button panel (with buttons on same line)
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        formPanel.add(buttonPanel, gbc);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    showError("Please enter both username and password");
                    return;
                }

                User user = AuthController.login(username, password);
                if (user != null) {
                    parent.showDashboard(user);
                } else {
                    showError("Invalid username or password");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showRegistrationPanel();
            }
        });

        // Button hover effects
        setupButtonHoverEffect(loginButton);
        setupButtonHoverEffect(registerButton);

        // Assemble the main panel
        formContainer.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formContainer, BorderLayout.CENTER);

        add(mainPanel);
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