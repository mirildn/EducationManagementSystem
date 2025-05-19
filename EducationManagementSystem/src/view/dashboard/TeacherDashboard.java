package view.dashboard;

import controller.TeacherController;
import model.Student;
import model.Subject;
import model.Teacher;
import util.DataManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TeacherDashboard extends JPanel {
    private Teacher teacher;
    private MainFrame parent;
    private JTabbedPane tabbedPane;
    private TeacherController controller;

    // Keep the original color scheme
    private final Color FOREST_GREEN = new Color(60, 140, 100);
    private final Color LIGHT_GREEN = new Color(180, 230, 180);    // Table grid color
    private final Color TEXT_COLOR = new Color(70, 65, 50);        // Text color
    private final Color BACKGROUND_COLOR = new Color(240, 250, 240); // Light background color
    private final Color BUTTON_GREEN = new Color(70, 150, 70);
    private final Color BUTTON_RED = new Color(200, 60, 60);
    private final Color TABLE_HEADER_COLOR = new Color(40, 120, 80);

    public TeacherDashboard(MainFrame parent, Teacher teacher) {
        this.parent = parent;
        this.teacher = teacher;
        this.controller = new TeacherController();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        initComponents();
    }

    private void initComponents() {
        // Header panel - improved styling similar to AdminDashboard
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(FOREST_GREEN);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Teacher Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Welcome, " + teacher.getName());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(BUTTON_RED);
        logoutButton.setForeground(TEXT_COLOR);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(120, 30));
        logoutButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showLoginPanel();
            }
        });

        JPanel leftHeaderPanel = new JPanel(new BorderLayout(0, 5));
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.add(titleLabel, BorderLayout.NORTH);
        leftHeaderPanel.add(userLabel, BorderLayout.SOUTH);

        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Create tabs for each assigned subject
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(FOREST_GREEN);

        List<Subject> assignedSubjects = teacher.getAssignedSubjects();
        if (assignedSubjects.isEmpty()) {
            JPanel noSubjectsPanel = new JPanel(new BorderLayout());
            noSubjectsPanel.setBackground(BACKGROUND_COLOR);

            JLabel noSubjectsLabel = new JLabel("You don't have any assigned subjects yet.");
            noSubjectsLabel.setHorizontalAlignment(JLabel.CENTER);
            noSubjectsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noSubjectsLabel.setForeground(TEXT_COLOR);

            noSubjectsPanel.add(noSubjectsLabel, BorderLayout.CENTER);
            add(noSubjectsPanel, BorderLayout.CENTER);
        } else {
            for (Subject subject : assignedSubjects) {
                JPanel subjectPanel = createSubjectPanel(subject);
                tabbedPane.addTab(subject.getName(), subjectPanel);
            }
            add(tabbedPane, BorderLayout.CENTER);
        }
    }

    private JPanel createSubjectPanel(Subject subject) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        // Subject info panel - improved styling
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(FOREST_GREEN, 1),
                "Subject Information"
        ));
        infoPanel.setBackground(BACKGROUND_COLOR);

        JLabel idLabel = new JLabel("ID: " + subject.getId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        idLabel.setForeground(TEXT_COLOR);

        JLabel courseLabel = new JLabel("Course: " + subject.getCourse());
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        courseLabel.setForeground(TEXT_COLOR);

        JLabel creditsLabel = new JLabel("Credits: " + subject.getCredits());
        creditsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        creditsLabel.setForeground(TEXT_COLOR);

        infoPanel.add(idLabel);
        infoPanel.add(courseLabel);
        infoPanel.add(creditsLabel);

        // Add instructions label
        JLabel instructionsLabel = new JLabel("Grade students enrolled in this subject");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(FOREST_GREEN);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(instructionsLabel, BorderLayout.SOUTH);

        // Students enrolled in this subject - improved styling
        JPanel studentsPanel = new JPanel(new BorderLayout());
        studentsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(FOREST_GREEN, 1),
                "Enrolled Students"
        ));
        studentsPanel.setBackground(BACKGROUND_COLOR);

        DefaultTableModel studentsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only the grade column is editable
            }
        };
        studentsModel.addColumn("ID");
        studentsModel.addColumn("Name");
        studentsModel.addColumn("Email");
        studentsModel.addColumn("Grade");

        JTable studentsTable = new JTable(studentsModel);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsTable.setRowHeight(25);
        studentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentsTable.setGridColor(LIGHT_GREEN);
        studentsTable.setBackground(BACKGROUND_COLOR);
        studentsTable.setForeground(TEXT_COLOR);

        // Style the table header
        JTableHeader header = studentsTable.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(FOREST_GREEN));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Get students who have this subject approved (enrolled)
        List<Student> enrolledStudents = getEnrolledStudentsForSubject(subject);

        for (Student student : enrolledStudents) {
            double grade = teacher.getStudentGrade(student.getId(), subject.getId());
            String gradeStr = grade >= 0 ? String.valueOf(grade) : "Not Graded";

            studentsModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    gradeStr
            });
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton saveGradesButton = new JButton("Save Grades");
        saveGradesButton.setBackground(BUTTON_GREEN);
        saveGradesButton.setForeground(TEXT_COLOR);
        saveGradesButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveGradesButton.setFocusPainted(false);
        saveGradesButton.setPreferredSize(new Dimension(130, 30));

        saveGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = true;
                for (int i = 0; i < studentsModel.getRowCount(); i++) {
                    String studentId = (String) studentsModel.getValueAt(i, 0);
                    String gradeStr = (String) studentsModel.getValueAt(i, 3);

                    try {
                        if (!gradeStr.equals("Not Graded")) {
                            double grade = Double.parseDouble(gradeStr);
                            if (grade < 0 || grade > 100) {
                                throw new NumberFormatException();
                            }

                            // Use the controller to grade the student
                            boolean gradeSuccess = controller.gradeStudent(teacher, studentId, subject.getId(), grade);
                            if (!gradeSuccess) {
                                success = false;
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel,
                                "Invalid grade format for student " + studentsModel.getValueAt(i, 1) +
                                        ". Please enter a number between 0 and 100.",
                                "Invalid Grade", JOptionPane.ERROR_MESSAGE);
                        success = false;
                        break;
                    }
                }

                if (success) {
                    JOptionPane.showMessageDialog(panel, "Grades saved successfully!");
                }
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(BUTTON_RED);
        resetButton.setForeground(TEXT_COLOR);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFocusPainted(false);
        resetButton.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(saveGradesButton);

        studentsPanel.add(scrollPane, BorderLayout.CENTER);
        studentsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Assemble the panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(studentsPanel, BorderLayout.CENTER);

        return panel;
    }

    private List<Student> getEnrolledStudentsForSubject(Subject subject) {
        List<Student> enrolledStudents = new ArrayList<>();
        List<Student> allStudents = DataManager.getInstance().getStudents();

        for (Student student : allStudents) {
            // Check if the student has this subject in their approved subjects
            List<Subject> approvedSubjects = student.getApprovedSubjects();
            for (Subject approvedSubject : approvedSubjects) {
                if (approvedSubject.getId().equals(subject.getId())) {
                    enrolledStudents.add(student);
                    break; // Subject found, no need to check further
                }
            }
        }

        return enrolledStudents;
    }
}