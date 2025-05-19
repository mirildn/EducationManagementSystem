package view.dashboard;

import controller.StudentController;
import model.Student;
import model.Subject;
import model.Teacher;
import util.SubjectRecommender;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Frame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

public class StudentDashboard extends JPanel {
    private Student student;
    private MainFrame parent;
    private JTabbedPane tabbedPane;
    private StudentController controller;

    // Define color scheme - UPDATED
    private final Color OLIVE_GREEN = new Color(128, 128, 0);          // Header background
    private final Color LIGHT_GREEN = new Color(173, 204, 96);         // For table grids and highlights
    private final Color BEIGE = new Color(245, 245, 220);              // Main background
    private final Color DARK_BEIGE = new Color(199, 183, 143);         // For contrast elements
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(70, 65, 50);            // Slightly softened black
    private final Color HIGHLIGHT_GREEN = new Color(107, 142, 35);     // Darker green for highlights
    private final Color BUTTON_RED = new Color(178, 34, 34);           // Darker red for buttons
    private final Color BUTTON_GREEN = new Color(85, 107, 47);         // Darker green for buttons

    // Font definitions for consistency
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 20);
    private final Font SUBHEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    private final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);

    public StudentDashboard(MainFrame parent, Student student) {
        this.parent = parent;
        this.student = student;
        this.controller = new StudentController();

        setLayout(new BorderLayout());
        setBackground(BEIGE);

        initComponents();
    }

    private void initComponents() {
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(OLIVE_GREEN);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(WHITE);

        JLabel userLabel = new JLabel("Welcome, " + student.getName());
        userLabel.setFont(NORMAL_FONT);
        userLabel.setForeground(WHITE);

        JLabel statusLabel = new JLabel(student.getCourse() + " | Status: " + student.getRegistrationStatus());
        statusLabel.setFont(NORMAL_FONT);
        statusLabel.setForeground(BEIGE);

        JButton editButton = new JButton("Edit Profile");
        editButton.setBackground(HIGHLIGHT_GREEN);
        editButton.setForeground(TEXT_COLOR);
        editButton.setFont(NORMAL_FONT);
        editButton.setPreferredSize(new Dimension(120, 30));
        editButton.setFocusPainted(false);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(editButton);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditProfileDialog();
            }
        });

        JPanel leftHeaderPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.add(titleLabel);

        JPanel userInfoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        userInfoPanel.setOpaque(false);
        userInfoPanel.add(userLabel);
        userInfoPanel.add(statusLabel);
        leftHeaderPanel.add(userInfoPanel);

        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Create tabbed pane with custom styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(NORMAL_FONT);
        tabbedPane.setBackground(BEIGE);
        tabbedPane.setForeground(OLIVE_GREEN);

        // Create tabs
        JPanel profilePanel = createProfilePanel();
        JPanel subjectsPanel = createSubjectsPanel();
        JPanel gradesPanel = createGradesPanel();

        tabbedPane.addTab("Profile", profilePanel);
        tabbedPane.addTab("Subject Selection", subjectsPanel);
        tabbedPane.addTab("My Grades", gradesPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        panel.setBackground(BEIGE);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BEIGE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        infoPanel.setBackground(BEIGE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create section title
        JLabel sectionTitle = new JLabel("Student Information");
        sectionTitle.setFont(SUBHEADER_FONT);
        sectionTitle.setForeground(OLIVE_GREEN);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        infoPanel.add(sectionTitle, gbc);

        // Add separator
        JSeparator separator = new JSeparator();
        separator.setForeground(DARK_BEIGE);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 15, 10);
        infoPanel.add(separator, gbc);
        gbc.insets = new Insets(8, 10, 8, 10);

        // Student info fields with improved labels
        // Add field labels with styling
        JLabel[] labels = {
                new JLabel("Student ID:"),
                new JLabel("Full Name:"),
                new JLabel("Email Address:"),
                new JLabel("Enrolled Program:"),
                new JLabel("Year Level:"),        // Added Year Level
                new JLabel("Semester:"),          // Added Semester
                new JLabel("Registration Status:")
        };

        for (JLabel label : labels) {
            label.setFont(NORMAL_FONT);
            label.setForeground(OLIVE_GREEN);
        }

        // Add field values with styling
        JLabel[] values = {
                new JLabel(student.getId()),
                new JLabel(student.getName()),
                new JLabel(student.getEmail()),
                new JLabel(student.getCourse()),
                new JLabel(student.getYearLevel()),   // Added Year Level value
                new JLabel(student.getSemester()),    // Added Semester value
                new JLabel(student.getRegistrationStatus())
        };

        for (JLabel value : values) {
            value.setFont(NORMAL_FONT);
            value.setForeground(TEXT_COLOR);
        }

        // Add status-based color for registration status
        if (student.getRegistrationStatus().equals("Approved")) {
            values[6].setForeground(BUTTON_GREEN);
        } else if (student.getRegistrationStatus().equals("Pending")) {
            values[6].setForeground(new Color(184, 134, 11)); // Dark goldenrod
        }

        // Add fields to panel
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 2;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            infoPanel.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            infoPanel.add(values[i], gbc);
        }

        // Edit profile button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BEIGE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(BUTTON_RED);
        logoutButton.setForeground(TEXT_COLOR);
        logoutButton.setFont(NORMAL_FONT);
        logoutButton.setPreferredSize(new Dimension(120, 30));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showLoginPanel();
            }
        });

        buttonPanel.add(logoutButton);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSubjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(BEIGE);

        // Check if student registration is pending
        if (student.getRegistrationStatus().equals("Pending")) {
            JPanel pendingPanel = new JPanel(new BorderLayout());
            pendingPanel.setBackground(BEIGE);
            pendingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(DARK_BEIGE, 1),
                    BorderFactory.createEmptyBorder(40, 20, 40, 20)
            ));

            JLabel pendingLabel = new JLabel("Your registration is pending approval.");
            pendingLabel.setHorizontalAlignment(JLabel.CENTER);
            pendingLabel.setFont(SUBHEADER_FONT);
            pendingLabel.setForeground(OLIVE_GREEN);

            JLabel infoLabel = new JLabel("Subject selection will be available after approval.");
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            infoLabel.setFont(NORMAL_FONT);
            infoLabel.setForeground(TEXT_COLOR);

            JPanel labelPanel = new JPanel(new GridLayout(2, 1, 0, 10));
            labelPanel.setOpaque(false);
            labelPanel.add(pendingLabel);
            labelPanel.add(infoLabel);

            pendingPanel.add(labelPanel, BorderLayout.CENTER);
            panel.add(pendingPanel, BorderLayout.CENTER);

            return panel;
        }

        // Create a main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(BEIGE);

        // Create a filter panel at the top
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filterPanel.setBackground(BEIGE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, DARK_BEIGE),
                BorderFactory.createEmptyBorder(5, 5, 10, 5)
        ));

        // Filter components
        JLabel courseLabel = new JLabel("Program:");
        courseLabel.setFont(NORMAL_FONT);
        courseLabel.setForeground(OLIVE_GREEN);

        JComboBox<String> courseComboBox = new JComboBox<>();
        courseComboBox.addItem("All Programs");
        courseComboBox.addItem(student.getCourse());  // Default to student's course
        courseComboBox.setSelectedItem(student.getCourse());
        courseComboBox.setFont(NORMAL_FONT);
        courseComboBox.setBackground(WHITE);
        courseComboBox.setPreferredSize(new Dimension(150, 30));

        JLabel yearLabel = new JLabel("Year Level:");
        yearLabel.setFont(NORMAL_FONT);
        yearLabel.setForeground(OLIVE_GREEN);

        JComboBox<String> yearComboBox = new JComboBox<>();
        yearComboBox.addItem("All Years");
        yearComboBox.addItem("1st Year");
        yearComboBox.addItem("2nd Year");
        yearComboBox.addItem("3rd Year");
        yearComboBox.addItem("4th Year");
        yearComboBox.setSelectedItem(student.getYearLevel());  // Default to student's year level
        yearComboBox.setFont(NORMAL_FONT);
        yearComboBox.setBackground(WHITE);
        yearComboBox.setPreferredSize(new Dimension(120, 30));

        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(NORMAL_FONT);
        semesterLabel.setForeground(OLIVE_GREEN);

        JComboBox<String> semesterComboBox = new JComboBox<>();
        semesterComboBox.addItem("All Semesters");
        semesterComboBox.addItem("1st Semester");
        semesterComboBox.addItem("2nd Semester");
        semesterComboBox.addItem("Summer");
        semesterComboBox.setSelectedItem(student.getSemester());  // Default to student's semester
        semesterComboBox.setFont(NORMAL_FONT);
        semesterComboBox.setBackground(WHITE);
        semesterComboBox.setPreferredSize(new Dimension(130, 30));

        JButton filterButton = new JButton("Apply Filter");
        filterButton.setBackground(HIGHLIGHT_GREEN);
        filterButton.setForeground(TEXT_COLOR);
        filterButton.setFont(NORMAL_FONT);
        filterButton.setFocusPainted(false);

        // Add components to filter panel
        filterPanel.add(courseLabel);
        filterPanel.add(courseComboBox);
        filterPanel.add(yearLabel);
        filterPanel.add(yearComboBox);
        filterPanel.add(semesterLabel);
        filterPanel.add(semesterComboBox);
        filterPanel.add(filterButton);

        // Split panel into available and selected subjects
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        splitPane.setDividerSize(8);

        // Available subjects
        JPanel availablePanel = new JPanel(new BorderLayout(0, 10));
        availablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(DARK_BEIGE),
                        "Available Subjects",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        SUBHEADER_FONT,
                        OLIVE_GREEN
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        availablePanel.setBackground(BEIGE);

        DefaultTableModel availableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableModel.addColumn("ID");
        availableModel.addColumn("Subject Name");
        availableModel.addColumn("Program");
        availableModel.addColumn("Year");
        availableModel.addColumn("Semester");
        availableModel.addColumn("Credits");
        availableModel.addColumn("Instructor");

        JTable availableTable = new JTable(availableModel);
        availableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableTable.setFont(SMALL_FONT);
        availableTable.getTableHeader().setFont(SMALL_FONT);
        availableTable.getTableHeader().setBackground(DARK_BEIGE);
        availableTable.getTableHeader().setForeground(OLIVE_GREEN);
        availableTable.setRowHeight(25);
        availableTable.setGridColor(LIGHT_GREEN);
        availableTable.setBackground(BEIGE);

        JScrollPane availableScrollPane = new JScrollPane(availableTable);
        availableScrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));

        // Selected subjects
        JPanel selectedPanel = new JPanel(new BorderLayout(0, 10));
        selectedPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(DARK_BEIGE),
                        "Selected Subjects",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        SUBHEADER_FONT,
                        OLIVE_GREEN
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        selectedPanel.setBackground(BEIGE);

        DefaultTableModel selectedModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        selectedModel.addColumn("ID");
        selectedModel.addColumn("Subject Name");
        selectedModel.addColumn("Year");
        selectedModel.addColumn("Semester");
        selectedModel.addColumn("Credits");
        selectedModel.addColumn("Status");

        JTable selectedTable = new JTable(selectedModel);
        selectedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectedTable.setFont(SMALL_FONT);
        selectedTable.getTableHeader().setFont(SMALL_FONT);
        selectedTable.getTableHeader().setBackground(DARK_BEIGE);
        selectedTable.getTableHeader().setForeground(OLIVE_GREEN);
        selectedTable.setRowHeight(25);
        selectedTable.setGridColor(LIGHT_GREEN);
        selectedTable.setBackground(BEIGE);

        JScrollPane selectedScrollPane = new JScrollPane(selectedTable);
        selectedScrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));

        // Load initial data - filtered by student's course, year, and semester
        List<Subject> allSubjects = controller.getAllSubjects();
        List<Subject> selectedSubjects = student.getSelectedSubjects();
        List<Subject> approvedSubjects = student.getApprovedSubjects();

        // Function to filter and refresh the available subjects table
        Runnable refreshAvailableSubjects = () -> {
            // Clear existing rows
            while (availableModel.getRowCount() > 0) {
                availableModel.removeRow(0);
            }

            String selectedCourse = courseComboBox.getSelectedItem().toString();
            String selectedYear = yearComboBox.getSelectedItem().toString();
            String selectedSemester = semesterComboBox.getSelectedItem().toString();

            // Debug - print selected values
            System.out.println("Selected Course: " + selectedCourse);
            System.out.println("Selected Year: " + selectedYear);
            System.out.println("Selected Semester: " + selectedSemester);

            for (Subject subject : allSubjects) {
                // Skip if already selected or approved
                if (selectedSubjects.contains(subject) || approvedSubjects.contains(subject)) {
                    continue;
                }

                // Skip empty subjects (sometimes CSV files have blank rows)
                if (subject.getId() == null || subject.getId().trim().isEmpty()) {
                    continue;
                }

                // Debug - print current subject info
                System.out.println("Processing subject: " + subject.getId() + ", " + subject.getName() +
                        ", Course: " + subject.getCourse() +
                        ", Year: " + subject.getYearLevel() +
                        ", Semester: " + subject.getSemester());

                // Apply filters - convert to standard format for comparison
                boolean courseMatch = selectedCourse.equals("All Programs") ||
                        (subject.getCourse() != null && subject.getCourse().equals(selectedCourse));

                boolean yearMatch = selectedYear.equals("All Years");
                if (!yearMatch && subject.getYearLevel() != null) {
                    // Handle various year level formats (1st, 1st Year, etc.)
                    String yearLevel = subject.getYearLevel().trim();
                    if (selectedYear.equals("1st Year") && (yearLevel.equals("1st") || yearLevel.equals("1st Year") || yearLevel.startsWith("1"))) {
                        yearMatch = true;
                    } else if (selectedYear.equals("2nd Year") && (yearLevel.equals("2nd") || yearLevel.equals("2nd Year") || yearLevel.startsWith("2"))) {
                        yearMatch = true;
                    } else if (selectedYear.equals("3rd Year") && (yearLevel.equals("3rd") || yearLevel.equals("3rd Year") || yearLevel.startsWith("3"))) {
                        yearMatch = true;
                    } else if (selectedYear.equals("4th Year") && (yearLevel.equals("4th") || yearLevel.equals("4th Year") || yearLevel.startsWith("4"))) {
                        yearMatch = true;
                    }
                }

                boolean semesterMatch = selectedSemester.equals("All Semesters");
                if (!semesterMatch && subject.getSemester() != null) {
                    // Handle various semester formats (1st, 1st Semester, etc.)
                    String semester = subject.getSemester().trim();
                    if (selectedSemester.equals("1st Semester") && (semester.equals("1st") || semester.equals("1st Semester") || semester.startsWith("1"))) {
                        semesterMatch = true;
                    } else if (selectedSemester.equals("2nd Semester") && (semester.equals("2nd") || semester.equals("2nd Semester") || semester.startsWith("2"))) {
                        semesterMatch = true;
                    } else if (selectedSemester.equals("Summer") && semester.equalsIgnoreCase("Summer")) {
                        semesterMatch = true;
                    }
                }

                // Debug - print match results
                System.out.println("Match results - Course: " + courseMatch +
                        ", Year: " + yearMatch +
                        ", Semester: " + semesterMatch);

                if (courseMatch && yearMatch && semesterMatch) {
                    availableModel.addRow(new Object[]{
                            subject.getId(),
                            subject.getName(),
                            subject.getCourse(),
                            subject.getYearLevel(),
                            subject.getSemester(),
                            subject.getCredits(),
                            subject.getTeacher() != null ? subject.getTeacher().getName() : "Not Assigned"
                    });
                }
            }

            // Debug - print total rows after filtering
            System.out.println("Total rows after filtering: " + availableModel.getRowCount());
        };

        // Initial load of selected subjects
        for (Subject subject : selectedSubjects) {
            selectedModel.addRow(new Object[]{
                    subject.getId(),
                    subject.getName(),
                    subject.getYearLevel(),
                    subject.getSemester(),
                    subject.getCredits(),
                    "Pending Approval"
            });
        }

        for (Subject subject : approvedSubjects) {
            selectedModel.addRow(new Object[]{
                    subject.getId(),
                    subject.getName(),
                    subject.getYearLevel(),
                    subject.getSemester(),
                    subject.getCredits(),
                    "Approved"
            });
        }

        // Load initial available subjects
        refreshAvailableSubjects.run();

        // Add filter button action listener
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAvailableSubjects.run();
            }
        });

        // Action buttons
        JPanel availableButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        availableButtonPanel.setBackground(BEIGE);

        JButton selectButton = new JButton("Select Subject");
        selectButton.setBackground(BUTTON_GREEN);
        selectButton.setForeground(TEXT_COLOR);
        selectButton.setFont(NORMAL_FONT);
        selectButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        selectButton.setFocusPainted(false);

        JButton recommendButton = new JButton("Show Recommendations");
        recommendButton.setBackground(HIGHLIGHT_GREEN);
        recommendButton.setForeground(TEXT_COLOR);
        recommendButton.setFont(NORMAL_FONT);
        recommendButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        recommendButton.setFocusPainted(false);

        availableButtonPanel.add(recommendButton);
        availableButtonPanel.add(selectButton);

        // Modified: Create selected button panel with both Submit and Remove buttons
        JPanel selectedButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        selectedButtonPanel.setBackground(BEIGE);

        // New Submit Selection Button
        JButton submitButton = new JButton("Submit Selection");
        submitButton.setBackground(HIGHLIGHT_GREEN);
        submitButton.setForeground(TEXT_COLOR);
        submitButton.setFont(NORMAL_FONT);
        submitButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        submitButton.setFocusPainted(false);

        JButton removeButton = new JButton("Remove Selection");
        removeButton.setBackground(BUTTON_RED);
        removeButton.setForeground(TEXT_COLOR);
        removeButton.setFont(NORMAL_FONT);
        removeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        removeButton.setFocusPainted(false);

        selectedButtonPanel.add(submitButton);
        selectedButtonPanel.add(removeButton);

        // Action listeners
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = availableTable.getSelectedRow();
                if (selectedRow != -1) {
                    String subjectId = (String) availableModel.getValueAt(selectedRow, 0);
                    Subject subject = controller.getSubjectById(subjectId);

                    if (subject != null) {
                        // Check if already at maximum subjects (e.g., 5)
                        if (student.getSelectedSubjects().size() + student.getApprovedSubjects().size() >= 5) {
                            JOptionPane.showMessageDialog(panel,
                                    "You can select a maximum of 5 subjects.",
                                    "Limit Reached", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        student.selectSubject(subject);
                        controller.updateStudent(student);

                        // Refresh tables
                        availableModel.removeRow(selectedRow);
                        selectedModel.addRow(new Object[]{
                                subject.getId(),
                                subject.getName(),
                                subject.getYearLevel(),
                                subject.getSemester(),
                                subject.getCredits(),
                                "Pending Approval"
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a subject first.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = selectedTable.getSelectedRow();
                if (selectedRow != -1) {
                    String subjectId = (String) selectedModel.getValueAt(selectedRow, 0);
                    String status = (String) selectedModel.getValueAt(selectedRow, 5);

                    if (status.equals("Approved")) {
                        JOptionPane.showMessageDialog(panel,
                                "You cannot remove approved subjects. Please contact an administrator.",
                                "Cannot Remove", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Subject subject = controller.getSubjectById(subjectId);

                    if (subject != null) {
                        student.removeSelectedSubject(subject);
                        controller.updateStudent(student);

                        // Refresh tables
                        selectedModel.removeRow(selectedRow);

                        // Check if the subject should be added back to available subjects based on current filters
                        String selectedCourse = courseComboBox.getSelectedItem().toString();
                        String selectedYear = yearComboBox.getSelectedItem().toString();
                        String selectedSemester = semesterComboBox.getSelectedItem().toString();

                        boolean courseMatch = selectedCourse.equals("All Programs") || subject.getCourse().equals(selectedCourse);
                        boolean yearMatch = selectedYear.equals("All Years") || subject.getYearLevel().equals(selectedYear);
                        boolean semesterMatch = selectedSemester.equals("All Semesters") || subject.getSemester().equals(selectedSemester);

                        if (courseMatch && yearMatch && semesterMatch) {
                            availableModel.addRow(new Object[]{
                                    subject.getId(),
                                    subject.getName(),
                                    subject.getCourse(),
                                    subject.getYearLevel(),
                                    subject.getSemester(),
                                    subject.getCredits(),
                                    subject.getTeacher() != null ? subject.getTeacher().getName() : "Not Assigned"
                            });
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a subject first.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // New Submit button action listener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there are any subjects to submit
                if (selectedModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(panel,
                            "You haven't selected any subjects to submit.",
                            "No Subjects Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Check if all subjects are already approved or submitted
                boolean hasSubmittableSubjects = false;
                for (int i = 0; i < selectedModel.getRowCount(); i++) {
                    String status = (String) selectedModel.getValueAt(i, 5);
                    if (status.equals("Pending Approval")) {
                        hasSubmittableSubjects = true;
                        break;
                    }
                }

                if (!hasSubmittableSubjects) {
                    JOptionPane.showMessageDialog(panel,
                            "All selected subjects are already submitted or approved.",
                            "No Changes", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Mark the student's selections as submitted for review
                student.setSubmittedForReview(true);
                controller.updateStudent(student);

                // Update the UI to reflect submission
                for (int i = 0; i < selectedModel.getRowCount(); i++) {
                    String status = (String) selectedModel.getValueAt(i, 5);
                    if (status.equals("Pending Approval")) {
                        selectedModel.setValueAt("Submitted for Review", i, 5);
                    }
                }

                JOptionPane.showMessageDialog(panel,
                        "Your subject selections have been submitted for review.\n" +
                                "An administrator will approve them shortly.",
                        "Submission Successful", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        recommendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Filter recommendations based on student's course, year level, and semester
                List<Subject> recommendedSubjects = SubjectRecommender.recommendSubjectsForStudent(student);

                if (recommendedSubjects.isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                            "No additional subjects to recommend for your program at this time.",
                            "Recommendations", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Highlight recommended subjects in the available table
                for (int i = 0; i < availableModel.getRowCount(); i++) {
                    String subjectId = (String) availableModel.getValueAt(i, 0);
                    boolean isRecommended = false;

                    for (Subject s : recommendedSubjects) {
                        if (s.getId().equals(subjectId)) {
                            isRecommended = true;
                            break;
                        }
                    }

                    if (isRecommended) {
                        availableTable.addRowSelectionInterval(i, i);
                    }
                }


                // Create a nicer recommendation dialog
                JDialog recDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel), "Subject Recommendations", true);
                recDialog.setLayout(new BorderLayout());
                recDialog.setSize(400, 300);
                recDialog.setLocationRelativeTo(panel);

                JPanel recPanel = new JPanel(new BorderLayout(0, 15));
                recPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
                recPanel.setBackground(BEIGE);

                JLabel recTitle = new JLabel("Recommended Subjects");
                recTitle.setFont(SUBHEADER_FONT);
                recTitle.setForeground(OLIVE_GREEN);
                recTitle.setHorizontalAlignment(JLabel.CENTER);

                JLabel recSubtitle = new JLabel("Based on your current program and progress");
                recSubtitle.setFont(NORMAL_FONT);
                recSubtitle.setForeground(TEXT_COLOR);
                recSubtitle.setHorizontalAlignment(JLabel.CENTER);

                JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
                headerPanel.setBackground(BEIGE);
                headerPanel.add(recTitle);
                headerPanel.add(recSubtitle);

                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (Subject s : recommendedSubjects) {
                    listModel.addElement(s.getName() + " (" + s.getId() + ") - " + s.getYearLevel() + ", " + s.getSemester());
                }

                JList<String> recList = new JList<>(listModel);
                recList.setFont(NORMAL_FONT);
                recList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                recList.setSelectionBackground(LIGHT_GREEN);
                recList.setSelectionForeground(OLIVE_GREEN);
                recList.setBackground(BEIGE);

                JScrollPane recScroll = new JScrollPane(recList);
                recScroll.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));

                JButton closeButton = new JButton("Close");
                closeButton.setBackground(OLIVE_GREEN);
                closeButton.setForeground(TEXT_COLOR);
                closeButton.setFont(NORMAL_FONT);
                closeButton.setFocusPainted(false);
                closeButton.addActionListener(e2 -> recDialog.dispose());

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(BEIGE);
                buttonPanel.add(closeButton);

                recPanel.add(headerPanel, BorderLayout.NORTH);
                recPanel.add(recScroll, BorderLayout.CENTER);
                recPanel.add(buttonPanel, BorderLayout.SOUTH);

                recDialog.add(recPanel);
                recDialog.setVisible(true);
            }
        });

        availablePanel.add(availableScrollPane, BorderLayout.CENTER);
        availablePanel.add(availableButtonPanel, BorderLayout.SOUTH);

        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);
        selectedPanel.add(selectedButtonPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(availablePanel);
        splitPane.setRightComponent(selectedPanel);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        panel.add(mainPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(BEIGE);

        if (student.getRegistrationStatus().equals("Pending")) {
            JPanel pendingPanel = new JPanel(new BorderLayout());
            pendingPanel.setBackground(BEIGE);
            pendingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(DARK_BEIGE, 1),
                    BorderFactory.createEmptyBorder(40, 20, 40, 20)
            ));

            JLabel pendingLabel = new JLabel("Your registration is pending approval.");
            pendingLabel.setHorizontalAlignment(JLabel.CENTER);
            pendingLabel.setFont(SUBHEADER_FONT);
            pendingLabel.setForeground(OLIVE_GREEN);

            JLabel infoLabel = new JLabel("Grade information will be available after approval.");
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            infoLabel.setFont(NORMAL_FONT);
            infoLabel.setForeground(TEXT_COLOR);

            JPanel labelPanel = new JPanel(new GridLayout(2, 1, 0, 10));
            labelPanel.setOpaque(false);
            labelPanel.add(pendingLabel);
            labelPanel.add(infoLabel);

            pendingPanel.add(labelPanel, BorderLayout.CENTER);
            panel.add(pendingPanel, BorderLayout.CENTER);

            return panel;
        }

        // Create table model with improved styling
        DefaultTableModel gradesModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gradesModel.addColumn("Subject ID");
        gradesModel.addColumn("Subject Name");
        gradesModel.addColumn("Instructor");
        gradesModel.addColumn("Credits");
        gradesModel.addColumn("Grade");
        gradesModel.addColumn("Status");

        JTable gradesTable = new JTable(gradesModel);
        gradesTable.setFont(SMALL_FONT);
        gradesTable.getTableHeader().setFont(SMALL_FONT);
        gradesTable.getTableHeader().setBackground(DARK_BEIGE);
        gradesTable.getTableHeader().setForeground(OLIVE_GREEN);
        gradesTable.setRowHeight(25);
        gradesTable.setGridColor(LIGHT_GREEN);
        gradesTable.setBackground(BEIGE);

        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));

        // Add title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BEIGE);
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel gradesTitle = new JLabel("Academic Performance");
        gradesTitle.setFont(SUBHEADER_FONT);
        gradesTitle.setForeground(OLIVE_GREEN);

        titlePanel.add(gradesTitle, BorderLayout.WEST);

        // Load subjects and their grades
        double totalPoints = 0;
        int totalCredits = 0;

        // Get the student's grades
        Map<Subject, Double> studentGrades = student.getGrades();

        // First, add enrolled subjects with grades
        List<Subject> approvedSubjects = student.getApprovedSubjects();

        for (Subject subject : approvedSubjects) {
            String subjectId = subject.getId();
            String teacherName = "Not Assigned";

            // Find the teacher for this subject
            Teacher teacher = subject.getTeacher();
            if (teacher != null) {
                teacherName = teacher.getName();
            }

            // Get grade directly from the grades map
            double grade = -1;
            String gradeStr = "Not Graded";
            String status = "Enrolled";

            if (studentGrades.containsKey(subject)) {
                grade = studentGrades.get(subject);
                gradeStr = String.format("%.1f", grade);
                status = grade <= 3.0 ? "Passed" : "Failed";  // Assuming 3.0 is the passing grade, adjust as needed

                // Add to GPA calculation
                totalPoints += grade * subject.getCredits();
                totalCredits += subject.getCredits();
            }

            gradesModel.addRow(new Object[]{
                    subjectId,
                    subject.getName(),
                    teacherName,
                    subject.getCredits(),
                    gradeStr,
                    status
            });
        }

        // Add GPA calculation panel with improved styling
        JPanel gpaPanel = new JPanel(new BorderLayout());
        gpaPanel.setBackground(BEIGE);
        gpaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, DARK_BEIGE),
                BorderFactory.createEmptyBorder(15, 5, 5, 5)
        ));

        DecimalFormat df = new DecimalFormat("#.##");
        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0;

        JPanel gpaInfoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        gpaInfoPanel.setBackground(BEIGE);

        JLabel gpaLabel = new JLabel("Overall GPA:");
        gpaLabel.setFont(NORMAL_FONT);
        gpaLabel.setForeground(OLIVE_GREEN);

        JLabel gpaValueLabel = new JLabel(df.format(gpa));
        gpaValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gpaValueLabel.setForeground(OLIVE_GREEN);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statsPanel.setBackground(BEIGE);

        JLabel creditsLabel = new JLabel("Total Credits: " + totalCredits);
        creditsLabel.setFont(SMALL_FONT);
        creditsLabel.setForeground(TEXT_COLOR);

        statsPanel.add(creditsLabel);

        gpaInfoPanel.add(gpaLabel);
        gpaInfoPanel.add(gpaValueLabel);

        gpaPanel.add(gpaInfoPanel, BorderLayout.WEST);
        gpaPanel.add(statsPanel, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(gpaPanel, BorderLayout.SOUTH);

        return panel;
    }

    private Map<String, Double> parseStudentGrades(String gradesString) {
        Map<String, Double> gradesMap = new HashMap<>();

        if (gradesString == null || gradesString.trim().isEmpty()) {
            return gradesMap;
        }

        // Split by comma to get individual subject:grade pairs
        String[] gradeEntries = gradesString.split(",");

        for (String entry : gradeEntries) {
            // Split each entry by colon to separate subject ID and grade
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                String subjectId = parts[0].trim();
                try {
                    double grade = Double.parseDouble(parts[1].trim());
                    gradesMap.put(subjectId, grade);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid grade format for subject " + parts[0] + ": " + parts[1]);
                }
            }
        }

        return gradesMap;
    }

    private void showEditProfileDialog() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(parentFrame, "Edit Profile", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BEIGE);
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dialog title
        JLabel titleLabel = new JLabel("Update Your Profile Information");
        titleLabel.setFont(SUBHEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Add separator
        JSeparator separator = new JSeparator();
        separator.setForeground(TEXT_COLOR);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 15, 10);
        formPanel.add(separator, gbc);

        // Reset insets
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridwidth = 1;

        // Form fields with improved labels and styling
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(NORMAL_FONT);
        nameLabel.setForeground(TEXT_COLOR);

        JTextField nameField = new JTextField(student.getName(), 20);
        nameField.setFont(NORMAL_FONT);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREEN),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(NORMAL_FONT);
        emailLabel.setForeground(TEXT_COLOR);

        JTextField emailField = new JTextField(student.getEmail(), 20);
        emailField.setFont(NORMAL_FONT);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREEN),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setFont(NORMAL_FONT);
        currentPasswordLabel.setForeground(TEXT_COLOR);

        JPasswordField currentPasswordField = new JPasswordField(20);
        currentPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREEN),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(NORMAL_FONT);
        newPasswordLabel.setForeground(TEXT_COLOR);

        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREEN),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel passwordHintLabel = new JLabel("(Leave blank to keep current password)");
        passwordHintLabel.setFont(SMALL_FONT);
        passwordHintLabel.setForeground(Color.GRAY);

        JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
        confirmPasswordLabel.setFont(NORMAL_FONT);
        confirmPasswordLabel.setForeground(TEXT_COLOR);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREEN),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Add components to form
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(currentPasswordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(currentPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(newPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(passwordHintLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Button panel with improved styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(HIGHLIGHT_GREEN);
        saveButton.setForeground(TEXT_COLOR);
        saveButton.setFont(NORMAL_FONT);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setFont(NORMAL_FONT);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cancelButton.setFocusPainted(false);

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPassword = new String(currentPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Field validation
                if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Name and email cannot be empty.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!currentPassword.equals(student.getPassword())) {
                    JOptionPane.showMessageDialog(dialog,
                            "Current password is incorrect",
                            "Authentication Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(dialog,
                            "New passwords do not match",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update student information
                student.setName(nameField.getText().trim());
                student.setEmail(emailField.getText().trim());

                if (!newPassword.isEmpty()) {
                    student.setPassword(newPassword);
                }

                if (controller.updateStudent(student)) {
                    JOptionPane.showMessageDialog(dialog,
                            "Profile updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    // Refresh profile panel
                    tabbedPane.setComponentAt(0, createProfilePanel());
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Failed to update profile. Please try again later.",
                            "Update Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}