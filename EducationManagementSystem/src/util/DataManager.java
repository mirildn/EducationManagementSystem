package util;

import model.*;
import java.io.*;
import java.util.*;

public class DataManager {

    private static final String SUBJECTS_FILE = "src/util/Subjects.csv";
    private static final String STUDENTS_FILE = "src/util/Students.csv";
    private static final String TEACHERS_FILE = "src/util/Teachers.csv";

    private List<User> users;
    private List<Subject> subjects;

    private static DataManager instance;

    private DataManager() {
        users = new ArrayList<>();
        subjects = new ArrayList<>();
        loadData();

        // If no users exist, create default admin
        if (getAdmins().isEmpty()) {
            Admin admin = new Admin("admin1", "System Administrator", "admin", "admin123", "admin@school.edu");
            users.add(admin);
        }

        // If no subjects exist, create some default IT subjects
        if (subjects.isEmpty()) {
            System.out.println("Subjects are empty");
        }
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void loadData() {
        loadSubjects();
        loadTeachers();
        loadStudents();
    }

    private void loadSubjects() {
        subjects.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                // Handle potential commas in description by using a CSV parser or manually handling
                List<String> values = parseCSVLine(line);

                if (values.size() >= 7) {  // Ensure we have all 7 fields
                    try {
                        int credits = Integer.parseInt(values.get(3).trim());
                        Subject subject = new Subject(
                                values.get(0).trim(),    // ID
                                values.get(1).trim(),    // Name
                                values.get(2).trim(),    // Description
                                credits,                 // Credits as integer
                                values.get(4).trim(),    // Course
                                values.get(5).trim(),    // Year Level
                                values.get(6).trim()     // Semester
                        );
                        subjects.add(subject);
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Invalid credits value in subject: " + values.get(0) +
                                " - " + values.get(1) + ". Credits value was: " + values.get(3));
                        // Set a default value of 3 credits
                        Subject subject = new Subject(
                                values.get(0).trim(),    // ID
                                values.get(1).trim(),    // Name
                                values.get(2).trim(),    // Description
                                3,                       // Default credits (integer)
                                values.get(4).trim(),    // Course
                                values.get(5).trim(),    // Year Level
                                values.get(6).trim()     // Semester
                        );
                        subjects.add(subject);
                    }
                } else if (values.size() >= 5) {
                    // Handle subjects without Year Level and Semester (provide default values)
                    try {
                        int credits = Integer.parseInt(values.get(3).trim());
                        Subject subject = new Subject(
                                values.get(0).trim(),    // ID
                                values.get(1).trim(),    // Name
                                values.get(2).trim(),    // Description
                                credits,                 // Credits as integer
                                values.get(4).trim(),    // Course
                                "1st",                   // Default Year Level
                                "1st"                    // Default Semester
                        );
                        subjects.add(subject);
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Invalid credits value in subject: " + values.get(0) +
                                " - " + values.get(1) + ". Credits value was: " + values.get(3));
                        // Set a default value of 3 credits
                        Subject subject = new Subject(
                                values.get(0).trim(),    // ID
                                values.get(1).trim(),    // Name
                                values.get(2).trim(),    // Description
                                3,                       // Default credits (integer)
                                values.get(4).trim(),    // Course
                                "1st",                   // Default Year Level
                                "1st"                    // Default Semester
                        );
                        subjects.add(subject);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Subjects file not found or error reading it. Will create default subjects.");
        }
    }

    // Helper method to parse CSV line handling quoted fields
    private List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        // Add the last value
        result.add(currentValue.toString());
        return result;
    }

    private void loadTeachers() {
        try (BufferedReader br = new BufferedReader(new FileReader(TEACHERS_FILE))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                List<String> values = parseCSVLine(line);
                if (values.size() >= 6) {
                    Teacher teacher = new Teacher(
                            values.get(0).trim(),    // ID
                            values.get(1).trim(),    // Name
                            values.get(2).trim(),    // Username
                            values.get(3).trim(),    // Password
                            values.get(4).trim()     // Email
                    );

                    // Handle assigned subjects
                    if (values.size() > 5 && !values.get(5).trim().isEmpty()) {
                        String[] subjectIds = values.get(5).trim().split(";");
                        for (String subjectId : subjectIds) {
                            Subject subject = findSubjectById(subjectId.trim());
                            if (subject != null) {
                                teacher.addAssignedSubject(subject);
                            }
                        }
                    }
                    users.add(teacher);
                }
            }
        } catch (IOException e) {
            System.out.println("Teachers file not found or error reading it.");
        }
    }

    private void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                List<String> values = parseCSVLine(line);
                if (values.size() >= 10) {
                    try {
                        // Convert numeric year/semester to String with suffix
                        int yearNum = Integer.parseInt(values.get(6).trim());
                        int semesterNum = Integer.parseInt(values.get(7).trim());

                        String yearLevel = yearNum + getYearLevelSuffix(yearNum);
                        String semester = semesterNum + getSemesterSuffix(semesterNum);

                        Student student = new Student(
                                values.get(0).trim(),    // ID
                                values.get(1).trim(),    // Name
                                values.get(2).trim(),    // Username
                                values.get(3).trim(),    // Password
                                values.get(4).trim(),    // Email
                                values.get(5).trim(),    // Course
                                yearLevel,               // Year as String (e.g., "1st")
                                semester                 // Semester as String (e.g., "2nd")
                        );

                        // Set registration status if student was already approved
                        if (values.size() > 8 && !values.get(8).trim().isEmpty()) {
                            student.setRegistrationStatus("Approved");
                        }

                        // Handle selected/approved subjects
                        if (values.size() > 8 && !values.get(8).trim().isEmpty()) {
                            String[] subjectData = values.get(8).trim().split(";");
                            for (String subjectId : subjectData) {
                                Subject subject = findSubjectById(subjectId.trim());
                                if (subject != null) {
                                    student.addSelectedSubject(subject);
                                }
                            }
                        }

                        // Handle grades if available
                        if (values.size() > 9 && !values.get(9).trim().isEmpty()) {
                            String[] gradesData = values.get(9).trim().split(";");
                            for (String gradeInfo : gradesData) {
                                String[] parts = gradeInfo.trim().split(":");
                                if (parts.length == 2) {
                                    try {
                                        String subjectId = parts[0].trim();
                                        double grade = Double.parseDouble(parts[1].trim());
                                        Subject subject = findSubjectById(subjectId);
                                        if (subject != null) {
                                            student.setGrade(subject, grade);

                                            // If student has a grade, move subject to approved list
                                            if (student.hasSelectedSubject(subjectId)) {
                                                student.approveSubject(subject);
                                            } else {
                                                student.addApprovedSubject(subject);
                                            }
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Warning: Invalid grade value: " + parts[1]);
                                    }
                                }
                            }
                        }
                        users.add(student);
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Invalid year or semester value for student: " +
                                values.get(0) + " - " + values.get(1));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Students file not found or error reading it.");
        }
    }
    private String getYearLevelSuffix(int year) {
        switch (year) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

    /**
     * Helper method to get the suffix for semester (e.g., "1" -> "st", "2" -> "nd")
     */
    private String getSemesterSuffix(int semester) {
        switch (semester) {
            case 1: return "st";
            case 2: return "nd";
            default: return "th";
        }
    }
    private Subject findSubjectById(String id) {
        for (Subject subject : subjects) {
            if (subject.getId().equals(id)) {
                return subject;
            }
        }
        return null;
    }

    public void saveData() {
        saveSubjects();
        saveTeachers();
        saveStudents();
    }

    private void saveSubjects() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SUBJECTS_FILE))) {
            writer.println("ID,Name,Description,Credits,Course,Year Level,Semester");
            for (Subject subject : subjects) {
                // Handle potential commas in fields by quoting them
                String description = subject.getDescription().contains(",") ?
                        "\"" + subject.getDescription() + "\"" : subject.getDescription();

                writer.println(
                        subject.getId() + "," +
                                quoteIfNeeded(subject.getName()) + "," +
                                quoteIfNeeded(description) + "," +
                                subject.getCredits() + "," +
                                quoteIfNeeded(subject.getCourse()) + "," +
                                quoteIfNeeded(subject.getYearLevel()) + "," +
                                quoteIfNeeded(subject.getSemester())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveStudentsToCSV() {
        saveStudents();
    }

    // Helper method to add quotes if a string contains commas
    private String quoteIfNeeded(String text) {
        if (text.contains(",")) {
            return "\"" + text + "\"";
        }
        return text;
    }

    private void saveTeachers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEACHERS_FILE))) {
            writer.println("ID,Name,Username,Password,Email,Assigned Subjects");
            for (User user : users) {
                if (user instanceof Teacher) {
                    Teacher teacher = (Teacher) user;
                    StringBuilder subjectsStr = new StringBuilder();
                    List<Subject> assignedSubjects = teacher.getAssignedSubjects();

                    for (int i = 0; i < assignedSubjects.size(); i++) {
                        subjectsStr.append(assignedSubjects.get(i).getId());
                        if (i < assignedSubjects.size() - 1) {
                            subjectsStr.append(";");
                        }
                    }

                    writer.println(
                            teacher.getId() + "," +
                                    quoteIfNeeded(teacher.getName()) + "," +
                                    quoteIfNeeded(teacher.getUsername()) + "," +
                                    quoteIfNeeded(teacher.getPassword()) + "," +
                                    quoteIfNeeded(teacher.getEmail()) + "," +
                                    quoteIfNeeded(subjectsStr.toString())
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStudents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
            writer.println("ID,Name,Username,Password,Email,Course,Year,Semester,Subjects,Grades,Status");
            for (User user : users) {
                if (user instanceof Student) {
                    Student student = (Student) user;

                    // Extract numeric values from formatted yearLevel and semester
                    String yearNumStr = student.getYearLevel().replaceAll("[^0-9]", "");
                    String semesterNumStr = student.getSemester().replaceAll("[^0-9]", "");

                    int yearNum = Integer.parseInt(yearNumStr);
                    int semesterNum = Integer.parseInt(semesterNumStr);

                    // Build subjects string with semicolon separators
                    StringBuilder subjectsStr = new StringBuilder();
                    List<Subject> selectedSubjects = student.getSelectedSubjects();
                    List<Subject> approvedSubjects = student.getApprovedSubjects();

                    // Add selected subjects
                    for (int i = 0; i < selectedSubjects.size(); i++) {
                        subjectsStr.append(selectedSubjects.get(i).getId());
                        if (i < selectedSubjects.size() - 1) {
                            subjectsStr.append(";");
                        }
                    }

                    // Build grades string with semicolon separators
                    StringBuilder gradesStr = new StringBuilder();
                    Map<Subject, Double> grades = student.getGrades();
                    boolean first = true;

                    for (Map.Entry<Subject, Double> entry : grades.entrySet()) {
                        if (!first) {
                            gradesStr.append(";");
                        }
                        gradesStr.append(entry.getKey().getId());
                        gradesStr.append(":");
                        gradesStr.append(entry.getValue());
                        first = false;
                    }

                    writer.println(
                            student.getId() + "," +
                                    quoteIfNeeded(student.getName()) + "," +
                                    quoteIfNeeded(student.getUsername()) + "," +
                                    quoteIfNeeded(student.getPassword()) + "," +
                                    quoteIfNeeded(student.getEmail()) + "," +
                                    quoteIfNeeded(student.getCourse()) + "," +
                                    yearNum + "," +
                                    semesterNum + "," +
                                    quoteIfNeeded(subjectsStr.toString()) + "," +
                                    quoteIfNeeded(gradesStr.toString()) + "," +
                                    student.getRegistrationStatus()
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        users.add(user);
        saveData();
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        saveData();
    }

    public List<Admin> getAdmins() {
        List<Admin> admins = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Admin) {
                admins.add((Admin) user);
            }
        }
        return admins;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Teacher) {
                teachers.add((Teacher) user);
            }
        }
        return teachers;
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }
        return students;
    }

    public List<Student> getPendingStudents() {
        List<Student> allStudents = getStudents();
        List<Student> pendingStudents = new ArrayList<>();

        for (Student student : allStudents) {
            // Only include students with Pending status
            if (student.getRegistrationStatus().equals("Pending")) {
                pendingStudents.add(student);
            }
        }

        return pendingStudents;
    }


    public List<Subject> getSubjects() {
        return subjects;
    }

    public List<Subject> getSubjectsByCourse(String course) {
        List<Subject> courseSubjects = new ArrayList<>();
        for (Subject subject : subjects) {
            if (subject.getCourse().equals(course)) {
                courseSubjects.add(subject);
            }
        }
        return courseSubjects;
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                saveData();
                return;
            }
        }
    }

    public void updateSubject(Subject subject) {
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getId().equals(subject.getId())) {
                subjects.set(i, subject);
                saveData();
                return;
            }
        }
    }

    public void removeUser(User user) {
        users.remove(user);
        saveData();
    }
}