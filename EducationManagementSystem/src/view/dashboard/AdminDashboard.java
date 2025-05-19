package view.dashboard;

import controller.AdminController;
import model.Admin;
import model.Student;
import model.Subject;
import model.Teacher;
import view.MainFrame;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JViewport;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Frame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboard extends JPanel {
    // Updated color scheme constants
    private static final Color OLIVE_GREEN = new Color(85, 107, 47);      // Header panel background
    private static final Color LIGHT_GREEN = new Color(173, 195, 147);    // Table grid color
    private static final Color DARK_BEIGE = new Color(155, 143, 118);     // Contrast color
    private static final Color BEIGE = new Color(245, 235, 220);          // Background color
    private static final Color APPROVE_GREEN = new Color(40, 130, 70);
    private static final Color REJECT_RED = new Color(170, 60, 60);
    private static final Color TEXT_COLOR = new Color(70, 65, 50);        // Adjusted text color

    private Admin admin;
    private MainFrame parent;
    private JTabbedPane tabbedPane;
    private JTable pendingStudentsTable;
    private JTable teachersTable;
    private JTable subjectsTable;
    private DefaultTableModel pendingStudentsModel;
    private DefaultTableModel teachersModel;
    private DefaultTableModel subjectsModel;
    private DefaultTableModel approvedStudentsModel;
    private JTable approvedStudentsTable;
    private AdminController controller;

    public AdminDashboard(MainFrame parent, Admin admin) {
        this.parent = parent;
        this.admin = admin;
        this.controller = new AdminController();

        setLayout(new BorderLayout());
        setBackground(BEIGE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(OLIVE_GREEN);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(BEIGE);

        JLabel userLabel = new JLabel("Welcome, " + admin.getName());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setForeground(BEIGE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(REJECT_RED);
        logoutButton.setForeground(TEXT_COLOR);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(120, 30));
        logoutButton.setFocusPainted(false);
        JPanel upButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        upButtonPanel.setOpaque(false);
        upButtonPanel.add(logoutButton);
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
        headerPanel.add(upButtonPanel, BorderLayout.EAST);

        // Main content
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setBackground(BEIGE);
        tabbedPane.setForeground(OLIVE_GREEN);

        // Pending Students Tab
        JPanel pendingStudentsPanel = createPendingStudentsPanel();
        tabbedPane.addTab("Pending Students", pendingStudentsPanel);

        // Approved Students Tab - ADD THIS NEW TAB
        JPanel approvedStudentsPanel = createApprovedStudentsPanel();
        tabbedPane.addTab("Approved Students", approvedStudentsPanel);

        // Subject Approval Tab
        JPanel subjectApprovalPanel = createStudentSubjectApprovalPanel();
        tabbedPane.addTab("Subject Approvals", subjectApprovalPanel);

        // Teachers Tab
        JPanel teachersPanel = createTeachersPanel();
        tabbedPane.addTab("Teachers", teachersPanel);

        // Add subjects tab
        JPanel subjectsPanel = createSubjectsPanel();
        tabbedPane.addTab("Subjects", subjectsPanel);

        // Add to the main panel
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    private JPanel createPendingStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BEIGE);

        // Create table model with columns
        pendingStudentsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pendingStudentsModel.addColumn("ID");
        pendingStudentsModel.addColumn("Name");
        pendingStudentsModel.addColumn("Username");
        pendingStudentsModel.addColumn("Email");
        pendingStudentsModel.addColumn("Course");
        pendingStudentsModel.addColumn("Year");
        pendingStudentsModel.addColumn("Semester");
        pendingStudentsModel.addColumn("Status");

        pendingStudentsTable = new JTable(pendingStudentsModel);
        pendingStudentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendingStudentsTable.setRowHeight(25);
        pendingStudentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        pendingStudentsTable.setGridColor(LIGHT_GREEN);
        pendingStudentsTable.setShowVerticalLines(true);
        pendingStudentsTable.setBackground(BEIGE);
        pendingStudentsTable.setForeground(TEXT_COLOR);

        // Style the table header
        JTableHeader header = pendingStudentsTable.getTableHeader();
        header.setBackground(DARK_BEIGE);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(pendingStudentsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));
        scrollPane.getViewport().setBackground(BEIGE);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BEIGE);

        JButton approveButton = new JButton("Approve Registration");
        approveButton.setBackground(APPROVE_GREEN);
        approveButton.setForeground(TEXT_COLOR);
        approveButton.setFont(new Font("Arial", Font.BOLD, 14));
        approveButton.setFocusPainted(false);
        approveButton.setPreferredSize(new Dimension(185, 30));

        JButton rejectButton = new JButton("Reject Registration");
        rejectButton.setBackground(REJECT_RED);
        rejectButton.setForeground(TEXT_COLOR);
        rejectButton.setFont(new Font("Arial", Font.BOLD, 14));
        rejectButton.setFocusPainted(false);
        rejectButton.setPreferredSize(new Dimension(185, 30));

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);

        // Action listeners
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pendingStudentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String studentId = (String) pendingStudentsModel.getValueAt(selectedRow, 0);
                    Student student = controller.getStudentById(studentId);
                    if (student != null) {
                        if (controller.approveStudentRegistration(student)) {
                            JOptionPane.showMessageDialog(panel, "Student registration approved successfully!");
                            loadPendingStudents(); // Refresh the table
                        } else {
                            JOptionPane.showMessageDialog(panel, "Failed to approve student registration.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to find student with ID: " + studentId,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a student to approve.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pendingStudentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String studentId = (String) pendingStudentsModel.getValueAt(selectedRow, 0);
                    Student student = controller.getStudentById(studentId);
                    if (student != null) {
                        // Show confirmation dialog
                        int choice = JOptionPane.showConfirmDialog(panel,
                                "Are you sure you want to reject this student's registration?\nThis action cannot be undone.",
                                "Confirm Rejection",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                        if (choice == JOptionPane.YES_OPTION) {
                            if (controller.rejectStudentRegistration(student)) {
                                JOptionPane.showMessageDialog(panel, "Student registration rejected successfully!");
                                loadPendingStudents(); // Refresh the table
                            } else {
                                JOptionPane.showMessageDialog(panel, "Failed to reject student registration.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to find student with ID: " + studentId,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a student to reject.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Add instructions label
        JLabel instructionsLabel = new JLabel("Select a student from the list below to approve or reject their registration");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(OLIVE_GREEN);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BEIGE);
        topPanel.add(instructionsLabel, BorderLayout.WEST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTeachersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BEIGE);

        // Create table model with columns
        teachersModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teachersModel.addColumn("ID");
        teachersModel.addColumn("Name");
        teachersModel.addColumn("Username");
        teachersModel.addColumn("Email");
        teachersModel.addColumn("Assigned Subjects");

        teachersTable = new JTable(teachersModel);
        teachersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teachersTable.setRowHeight(25);
        teachersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        teachersTable.setGridColor(LIGHT_GREEN);
        teachersTable.setBackground(BEIGE);
        teachersTable.setForeground(TEXT_COLOR);

        // Style the table header
        JTableHeader header = teachersTable.getTableHeader();
        header.setBackground(DARK_BEIGE);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(teachersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));
        scrollPane.getViewport().setBackground(BEIGE);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BEIGE);

        JButton addTeacherButton = new JButton("Add New Teacher");
        addTeacherButton.setBackground(APPROVE_GREEN);
        addTeacherButton.setForeground(TEXT_COLOR);
        addTeacherButton.setFont(new Font("Arial", Font.BOLD, 14));
        addTeacherButton.setFocusPainted(false);
        addTeacherButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JButton assignSubjectButton = new JButton("Assign Subject");
        assignSubjectButton.setBackground(APPROVE_GREEN);
        assignSubjectButton.setForeground(TEXT_COLOR);
        assignSubjectButton.setFont(new Font("Arial", Font.BOLD, 14));
        assignSubjectButton.setFocusPainted(false);
        assignSubjectButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        buttonPanel.add(addTeacherButton);
        buttonPanel.add(assignSubjectButton);

        // Action listeners
        addTeacherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddTeacherDialog();
            }
        });

        assignSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = teachersTable.getSelectedRow();
                if (selectedRow != -1) {
                    String teacherId = (String) teachersModel.getValueAt(selectedRow, 0);
                    showAssignSubjectDialog(teacherId);
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a teacher to assign a subject.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Add instructions label
        JLabel instructionsLabel = new JLabel("Manage teachers and their subject assignments");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(OLIVE_GREEN);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BEIGE);
        topPanel.add(instructionsLabel, BorderLayout.WEST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSubjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BEIGE);

        // Create table model with columns
        subjectsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectsModel.addColumn("ID");
        subjectsModel.addColumn("Name");
        subjectsModel.addColumn("Course");
        subjectsModel.addColumn("Credits");
        subjectsModel.addColumn("Teacher");

        subjectsTable = new JTable(subjectsModel);
        subjectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectsTable.setRowHeight(25);
        subjectsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        subjectsTable.setGridColor(LIGHT_GREEN);
        subjectsTable.setBackground(BEIGE);
        subjectsTable.setForeground(TEXT_COLOR);

        // Style the table header
        JTableHeader header = subjectsTable.getTableHeader();
        header.setBackground(DARK_BEIGE);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(subjectsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));
        scrollPane.getViewport().setBackground(BEIGE);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BEIGE);

        JButton addSubjectButton = new JButton("Add New Subject");
        addSubjectButton.setBackground(APPROVE_GREEN);
        addSubjectButton.setForeground(TEXT_COLOR);
        addSubjectButton.setFont(new Font("Arial", Font.BOLD, 14));
        addSubjectButton.setFocusPainted(false);
        addSubjectButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        buttonPanel.add(addSubjectButton);

        // Action listeners
        addSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddSubjectDialog();
            }
        });

        // Add instructions label
        JLabel instructionsLabel = new JLabel("Manage course subjects and their assignments");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(OLIVE_GREEN);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BEIGE);
        topPanel.add(instructionsLabel, BorderLayout.WEST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudentSubjectApprovalPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BEIGE);

        // Create table model with columns
        DefaultTableModel studentSubjectModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentSubjectModel.addColumn("Student ID");
        studentSubjectModel.addColumn("Student Name");
        studentSubjectModel.addColumn("Subject ID");
        studentSubjectModel.addColumn("Subject Name");
        studentSubjectModel.addColumn("Course");
        studentSubjectModel.addColumn("Credits");
        studentSubjectModel.addColumn("Status");

        JTable studentSubjectTable = new JTable(studentSubjectModel);
        studentSubjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentSubjectTable.setRowHeight(25);
        studentSubjectTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentSubjectTable.setGridColor(LIGHT_GREEN);
        studentSubjectTable.setShowVerticalLines(true);
        studentSubjectTable.setBackground(BEIGE);
        studentSubjectTable.setForeground(TEXT_COLOR);

        // Style the table header
        JTableHeader header = studentSubjectTable.getTableHeader();
        header.setBackground(DARK_BEIGE);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(studentSubjectTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));
        scrollPane.getViewport().setBackground(BEIGE);

        // Load pending student subject selections
        loadPendingStudentSubjects(studentSubjectModel);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BEIGE);

        JButton approveButton = new JButton("Approve Subject Selection");
        approveButton.setBackground(APPROVE_GREEN);
        approveButton.setForeground(TEXT_COLOR);
        approveButton.setFont(new Font("Arial", Font.BOLD, 14));
        approveButton.setFocusPainted(false);
        approveButton.setPreferredSize(new Dimension(220, 30));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(DARK_BEIGE);
        refreshButton.setForeground(TEXT_COLOR);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(120, 30));

        buttonPanel.add(approveButton);
        buttonPanel.add(refreshButton);

        // Action listeners
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentSubjectTable.getSelectedRow();
                if (selectedRow != -1) {
                    String studentId = (String) studentSubjectModel.getValueAt(selectedRow, 0);
                    String subjectId = (String) studentSubjectModel.getValueAt(selectedRow, 2);

                    if (controller.approveStudentSubjectSelection(admin, studentId, subjectId)) {
                        JOptionPane.showMessageDialog(panel, "Subject selection approved successfully!");
                        loadPendingStudentSubjects(studentSubjectModel);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to approve subject selection.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a subject selection to approve.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPendingStudentSubjects(studentSubjectModel);
            }
        });

        // Add instructions label
        JLabel instructionsLabel = new JLabel("Approve student subject selections");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(OLIVE_GREEN);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BEIGE);
        topPanel.add(instructionsLabel, BorderLayout.WEST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Fixed method to load pending subject selections
    private void loadPendingStudentSubjects(DefaultTableModel model) {
        // Clear table
        model.setRowCount(0);

        // Get students with pending subject selections
        List<Student> studentsWithPendingSelections = controller.getPendingSubjectSelections();

        // Populate table with pending subject selections
        for (Student student : studentsWithPendingSelections) {
            // Only include subjects that are selected (not yet approved) and submitted for review
            List<Subject> pendingSubjects = student.getSelectedSubjects();

            for (Subject subject : pendingSubjects) {
                model.addRow(new Object[]{
                        student.getId(),
                        student.getName(),
                        subject.getId(),
                        subject.getName(),
                        subject.getCourse(),
                        subject.getCredits(),
                        "Submitted for Review" // Show the correct status
                });
            }
        }
    }

    private JPanel createApprovedStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BEIGE);

        // Create table model with columns
        approvedStudentsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        approvedStudentsModel.addColumn("ID");
        approvedStudentsModel.addColumn("Name");
        approvedStudentsModel.addColumn("Username");
        approvedStudentsModel.addColumn("Email");
        approvedStudentsModel.addColumn("Course");
        approvedStudentsModel.addColumn("Year");
        approvedStudentsModel.addColumn("Semester");
        approvedStudentsModel.addColumn("Subjects");
        approvedStudentsModel.addColumn("GPA");

        approvedStudentsTable = new JTable(approvedStudentsModel);
        approvedStudentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        approvedStudentsTable.setRowHeight(25);
        approvedStudentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        approvedStudentsTable.setGridColor(LIGHT_GREEN);
        approvedStudentsTable.setShowVerticalLines(true);
        approvedStudentsTable.setBackground(BEIGE);
        approvedStudentsTable.setForeground(TEXT_COLOR);

        // Style the table header
        JTableHeader header = approvedStudentsTable.getTableHeader();
        header.setBackground(DARK_BEIGE);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(approvedStudentsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BEIGE));
        scrollPane.getViewport().setBackground(BEIGE);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BEIGE);

        JButton viewDetailsButton = new JButton("View Student Details");
        viewDetailsButton.setBackground(OLIVE_GREEN);
        viewDetailsButton.setForeground(TEXT_COLOR);
        viewDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setPreferredSize(new Dimension(185, 30));

        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setBackground(LIGHT_GREEN);
        refreshButton.setForeground(TEXT_COLOR);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(185, 30));

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(refreshButton);

        // Action listeners
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = approvedStudentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String studentId = (String) approvedStudentsModel.getValueAt(selectedRow, 0);
                    Student student = controller.getStudentById(studentId);
                    if (student != null) {
                        showStudentDetailsDialog(student);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to find student with ID: " + studentId,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a student to view details.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadApprovedStudents();
            }
        });

        // Add instructions label
        JLabel instructionsLabel = new JLabel("List of approved students in the system");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionsLabel.setForeground(OLIVE_GREEN);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Add search component
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BEIGE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        searchLabel.setForeground(TEXT_COLOR);

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // Add row filter for search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }

            private void filterTable(String searchText) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(approvedStudentsTable.getModel());
                approvedStudentsTable.setRowSorter(sorter);

                if (searchText.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BEIGE);
        topPanel.add(instructionsLabel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Load approved students when panel is created
        loadApprovedStudents();

        return panel;
    }

    /**
     * Load approved students into the table
     */
    private void loadApprovedStudents() {
        // Clear existing data
        approvedStudentsModel.setRowCount(0);

        // Get all students from the controller
        List<Student> allStudents = controller.getAllStudents();

        // Debug
        System.out.println("Total students: " + allStudents.size());
        for (Student s : allStudents) {
            System.out.println("Student: " + s.getName() + ", Status: " + s.getRegistrationStatus());
        }

        // Filter and add only approved students to the table
        boolean foundApproved = false;
        for (Student student : allStudents) {
            if (student.getRegistrationStatus() != null && student.getRegistrationStatus().equalsIgnoreCase("Approved")) {
                foundApproved = true;
                Object[] rowData = new Object[9];
                rowData[0] = student.getId();
                rowData[1] = student.getName();
                rowData[2] = student.getUsername();
                rowData[3] = student.getEmail();
                rowData[4] = student.getCourse();
                rowData[5] = student.getYearLevel();
                rowData[6] = student.getSemester();

                // Count approved subjects
                List<Subject> approvedSubjects = student.getApprovedSubjects();
                int subjectCount = approvedSubjects != null ? approvedSubjects.size() : 0;
                rowData[7] = subjectCount > 0 ? subjectCount + " subjects" : "No subjects";

                // Calculate GPA
                double gpa = student.calculateGPA();
                rowData[8] = gpa > 0 ? String.format("%.2f", gpa) : "N/A";

                approvedStudentsModel.addRow(rowData);
            }
        }

        // If table is empty, add a message
        if (!foundApproved) {
            Object[] emptyMessage = new Object[9];
            emptyMessage[1] = "No approved students found";
            approvedStudentsModel.addRow(emptyMessage);
        }

        // Force table to refresh
        approvedStudentsTable.repaint();
    }

    /**
     * Show detailed information for the selected student
     */
    private void showStudentDetailsDialog(Student student) {
        JDialog detailsDialog = new JDialog();
        detailsDialog.setTitle("Student Details: " + student.getName());
        detailsDialog.setModal(true);
        detailsDialog.setSize(600, 500);
        detailsDialog.setLocationRelativeTo(null);

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(BEIGE);

        // Student info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBackground(BEIGE);

        // Add student information fields
        addInfoField(infoPanel, "Student ID:", student.getId());
        addInfoField(infoPanel, "Name:", student.getName());
        addInfoField(infoPanel, "Username:", student.getUsername());
        addInfoField(infoPanel, "Email:", student.getEmail());
        addInfoField(infoPanel, "Course:", student.getCourse());
        addInfoField(infoPanel, "Year Level:", student.getYearLevel());
        addInfoField(infoPanel, "Semester:", student.getSemester());
        addInfoField(infoPanel, "Status:", student.getRegistrationStatus());
        addInfoField(infoPanel, "GPA:", String.format("%.2f", student.calculateGPA()));

        // Subject list panel
        JPanel subjectsPanel = new JPanel(new BorderLayout(0, 10));
        subjectsPanel.setBackground(BEIGE);

        JLabel subjectsLabel = new JLabel("Approved Subjects:");
        subjectsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableModel subjectsModel = new DefaultTableModel();
        subjectsModel.addColumn("Subject ID");
        subjectsModel.addColumn("Subject Name");
        subjectsModel.addColumn("Credits");
        subjectsModel.addColumn("Grade");

        JTable subjectsTable = new JTable(subjectsModel);
        subjectsTable.setBackground(BEIGE);
        JScrollPane subjectsScrollPane = new JScrollPane(subjectsTable);

        // Add approved subjects to the table
        List<Subject> approvedSubjects = student.getApprovedSubjects();
        if (!approvedSubjects.isEmpty()) {
            for (Subject subject : approvedSubjects) {
                double grade = student.getGradeBySubjectId(subject.getId());
                String gradeStr = grade > 0 ? String.format("%.2f", grade) : "Not graded";

                Object[] subjectRow = {
                        subject.getId(),
                        subject.getName(),
                        subject.getCredits(),
                        gradeStr
                };
                subjectsModel.addRow(subjectRow);
            }
        } else {
            Object[] emptyRow = {"No subjects", "", "", ""};
            subjectsModel.addRow(emptyRow);
        }

        subjectsPanel.add(subjectsLabel, BorderLayout.NORTH);
        subjectsPanel.add(subjectsScrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(LIGHT_GREEN);
        closeButton.setForeground(TEXT_COLOR);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> detailsDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BEIGE);
        buttonPanel.add(closeButton);

        // Add panels to content panel
        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(subjectsPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        detailsDialog.add(contentPanel);
        detailsDialog.setVisible(true);
    }

    /**
     * Helper method to add a labeled field to the info panel
     */
    private void addInfoField(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel valueComponent = new JLabel(value != null ? value : "N/A");
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    private void showAddTeacherDialog() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Add New Teacher", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 320);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BEIGE);
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Enter Teacher Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(OLIVE_GREEN);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBackground(new Color(250, 245, 235)); // Lighter beige for text fields

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(new Color(250, 245, 235));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(250, 245, 235));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(TEXT_COLOR);
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBackground(new Color(250, 245, 235));

        // Add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 15, 8);
        formPanel.add(titleLabel, gbc);

        // Reset insets for form fields
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridwidth = 1;

        // Add components to form
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BEIGE);
        buttonPanel.setBorder(new EmptyBorder(0, 10, 15, 10));

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(APPROVE_GREEN);
        saveButton.setForeground(TEXT_COLOR);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(REJECT_RED);
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (controller.addTeacher(name, username, password, email)) {
                    JOptionPane.showMessageDialog(dialog, "Teacher added successfully!");
                    dialog.dispose();
                    loadTeachers();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Username already exists. Please choose a different one.",
                            "Error", JOptionPane.ERROR_MESSAGE);
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

    private void showAssignSubjectDialog(String teacherId) {
        Teacher teacher = controller.getTeacherById(teacherId);
        if (teacher == null) {
            JOptionPane.showMessageDialog(this, "Teacher not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Subject> availableSubjects = controller.getAvailableSubjects();
        if (availableSubjects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subjects available for assignment", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Assign Subject to " + teacher.getName(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(420, 220);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BEIGE);
        panel.setBorder(new EmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Assign Subject to: " + teacher.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(OLIVE_GREEN);

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        subjectLabel.setForeground(TEXT_COLOR);

        JComboBox<Subject> subjectComboBox = new JComboBox<>(availableSubjects.toArray(new Subject[0]));
        subjectComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        subjectComboBox.setBackground(new Color(250, 245, 235));
        subjectComboBox.setForeground(TEXT_COLOR);

        // Add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 15, 8);
        panel.add(titleLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(subjectLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(subjectComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BEIGE);
        buttonPanel.setBorder(new EmptyBorder(0, 10, 15, 10));

        JButton assignButton = new JButton("Assign");
        assignButton.setBackground(APPROVE_GREEN);
        assignButton.setForeground(TEXT_COLOR);
        assignButton.setFont(new Font("Arial", Font.BOLD, 14));
        assignButton.setFocusPainted(false);
        assignButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(REJECT_RED);
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        buttonPanel.add(assignButton);
        buttonPanel.add(cancelButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
                if (selectedSubject != null) {
                    if (controller.assignSubjectToTeacher(admin, teacher, selectedSubject)) {
                        JOptionPane.showMessageDialog(dialog, "Subject assigned successfully!");
                        dialog.dispose();
                        loadTeachers();
                        loadSubjects();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to assign subject", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddSubjectDialog() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Add New Subject", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 450); // Increased height to accommodate new fields
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BEIGE);
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Enter Subject Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(OLIVE_GREEN);

        JLabel idLabel = new JLabel("Subject ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        idLabel.setForeground(TEXT_COLOR);
        JTextField idField = new JTextField(20);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        idField.setBackground(new Color(250, 245, 235));

        JLabel nameLabel = new JLabel("Subject Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBackground(new Color(250, 245, 235));

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabel.setForeground(TEXT_COLOR);
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel creditsLabel = new JLabel("Credits:");
        creditsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JSpinner creditsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
        creditsSpinner.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        String[] courses = {"IT", "Computer Science", "Information Systems", "Cybersecurity"};
        JComboBox<String> courseComboBox = new JComboBox<>(courses);
        courseComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        courseComboBox.setBackground(new Color(250, 245, 235));

        // Add Year Level ComboBox
        JLabel yearLevelLabel = new JLabel("Year Level:");
        yearLevelLabel.setFont(new Font("Arial", Font.BOLD, 14));
        yearLevelLabel.setForeground(TEXT_COLOR);
        String[] yearLevels = {"First Year", "Second Year", "Third Year", "Fourth Year"};
        JComboBox<String> yearLevelComboBox = new JComboBox<>(yearLevels);
        yearLevelComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        yearLevelComboBox.setBackground(new Color(250, 245, 235));

        // Add Semester ComboBox
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        semesterLabel.setForeground(TEXT_COLOR);
        String[] semesters = {"First Semester", "Second Semester", "Summer"};
        JComboBox<String> semesterComboBox = new JComboBox<>(semesters);
        semesterComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        semesterComboBox.setBackground(new Color(250, 245, 235));

        // Add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 15, 8);
        formPanel.add(titleLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridwidth = 1;

        // Add components to form
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(descScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(creditsLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(creditsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(courseLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(courseComboBox, gbc);

        // Add Year Level to form
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(yearLevelLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(yearLevelComboBox, gbc);

        // Add Semester to form
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(semesterLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(semesterComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(250, 245, 235));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 15, 10));

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(APPROVE_GREEN);
        saveButton.setForeground(TEXT_COLOR);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(REJECT_RED);
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                String description = descriptionArea.getText();
                int credits = (Integer) creditsSpinner.getValue();
                String course = (String) courseComboBox.getSelectedItem();
                String yearLevel = (String) yearLevelComboBox.getSelectedItem();
                String semester = (String) semesterComboBox.getSelectedItem();

                if (id.isEmpty() || name.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (controller.addSubject(id, name, description, credits, course, yearLevel, semester)) {
                    JOptionPane.showMessageDialog(dialog, "Subject added successfully!");
                    dialog.dispose();
                    loadSubjects();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Subject ID already exists. Please choose a different one.",
                            "Error", JOptionPane.ERROR_MESSAGE);
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

    private void loadData() {
        loadPendingStudents();
        loadTeachers();
        loadSubjects();

        // Since we can't directly access the table in the tab component,
        // we'll handle refreshing subject approvals when the tab is selected
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // If the "Subject Approvals" tab is selected (index 1)
                if (tabbedPane.getSelectedIndex() == 1) {
                    // Find the table in the selected component and refresh it
                    JPanel panel = (JPanel)tabbedPane.getSelectedComponent();
                    for (Component c : panel.getComponents()) {
                        if (c instanceof JScrollPane) {
                            JScrollPane scrollPane = (JScrollPane)c;
                            JViewport viewport = scrollPane.getViewport();
                            if (viewport.getView() instanceof JTable) {
                                JTable table = (JTable)viewport.getView();
                                loadPendingStudentSubjects((DefaultTableModel)table.getModel());
                                break;
                            }
                        }
                    }
                }
            }
        });

        // The rest of the loadData method remains unchanged
        // Add subject tab if not already added
        boolean hasSubjectsTab = false;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals("Subjects")) {
                hasSubjectsTab = true;
                break;
            }
        }

        if (!hasSubjectsTab) {
            JPanel subjectsPanel = createSubjectsPanel();
            tabbedPane.addTab("Subjects", subjectsPanel);
        }

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadPendingStudents() {
        pendingStudentsModel.setRowCount(0); // Clear existing data

        List<Student> pendingStudents = controller.getPendingStudents();

        for (Student student : pendingStudents) {
            Object[] row = new Object[8]; // Increased to 8 columns to include Year and Semester
            row[0] = student.getId();
            row[1] = student.getName();
            row[2] = student.getUsername();
            row[3] = student.getEmail();
            row[4] = student.getCourse();
            row[5] = student.getYearLevel();     // Adding Year data
            row[6] = student.getSemester(); // Adding Semester data
            row[7] = "Pending";

            pendingStudentsModel.addRow(row);
        }

        // Set preferred column widths for better readability
        if (pendingStudentsTable.getColumnModel().getColumnCount() > 0) {
            pendingStudentsTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
            pendingStudentsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
            pendingStudentsTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Username
            pendingStudentsTable.getColumnModel().getColumn(3).setPreferredWidth(180); // Email
            pendingStudentsTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Course
            pendingStudentsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        }
    }

    private void loadTeachers() {
        // Clear table
        teachersModel.setRowCount(0);

        // Load teachers
        List<Teacher> teachers = controller.getAllTeachers();
        for (Teacher teacher : teachers) {
            teachersModel.addRow(new Object[]{
                    teacher.getId(),
                    teacher.getName(),
                    teacher.getUsername(),
                    teacher.getEmail(),
                    formatSubjectList(teacher.getAssignedSubjects())
            });
        }

        // Set preferred column widths for better readability
        if (teachersTable.getColumnModel().getColumnCount() > 0) {
            teachersTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
            teachersTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
            teachersTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Username
            teachersTable.getColumnModel().getColumn(3).setPreferredWidth(180); // Email
            teachersTable.getColumnModel().getColumn(4).setPreferredWidth(250); // Assigned Subjects
        }
    }

    private void loadSubjects() {
        // Clear table
        subjectsModel.setRowCount(0);

        // Load subjects
        List<Subject> subjects = controller.getAllSubjects();
        for (Subject subject : subjects) {
            subjectsModel.addRow(new Object[]{
                    subject.getId(),
                    subject.getName(),
                    subject.getCourse(),
                    subject.getCredits(),
                    subject.getTeacher() != null ? subject.getTeacher().getName() : "Not Assigned"
            });
        }

        // Set preferred column widths for better readability
        if (subjectsTable.getColumnModel().getColumnCount() > 0) {
            subjectsTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
            subjectsTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Name
            subjectsTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Course
            subjectsTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Credits
            subjectsTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Teacher
        }
    }

    private String formatSubjectList(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            return "None";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subjects.size(); i++) {
            sb.append(subjects.get(i).getName());
            if (i < subjects.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}