package view;

import model.Admin;
import model.Student;
import model.Teacher;
import model.User;
import view.dashboard.AdminDashboard;
import view.dashboard.StudentDashboard;
import view.dashboard.TeacherDashboard;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;

    public MainFrame() {
        setTitle("Education Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        initPanels();

        setContentPane(contentPanel);
    }

    private void initPanels() {
        loginPanel = new LoginPanel(this);
        registrationPanel = new RegistrationPanel(this);

        contentPanel.add(loginPanel, "login");
        contentPanel.add(registrationPanel, "registration");

        cardLayout.show(contentPanel, "login");
    }

    public void showLoginPanel() {
        cardLayout.show(contentPanel, "login");
    }

    public void showRegistrationPanel() {
        cardLayout.show(contentPanel, "registration");
    }

    public void showDashboard(User user) {
        if (user instanceof Admin) {
            AdminDashboard adminDashboard = new AdminDashboard(this, (Admin) user);
            contentPanel.add(adminDashboard, "adminDashboard");
            cardLayout.show(contentPanel, "adminDashboard");
        } else if (user instanceof Teacher) {
            TeacherDashboard teacherDashboard = new TeacherDashboard(this, (Teacher) user);
            contentPanel.add(teacherDashboard, "teacherDashboard");
            cardLayout.show(contentPanel, "teacherDashboard");
        } else if (user instanceof Student) {
            StudentDashboard studentDashboard = new StudentDashboard(this, (Student) user);
            contentPanel.add(studentDashboard, "studentDashboard");
            cardLayout.show(contentPanel, "studentDashboard");
        }
    }
}