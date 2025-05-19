    package controller;
    
    import model.Admin;
    import model.Student;
    import model.Subject;
    import model.Teacher;
    import util.DataManager;
    
    import java.io.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;
    
    public class AdminController {
    
        public List<Student> getPendingStudents() {
            return DataManager.getInstance().getPendingStudents();
        }

        public List<Student> getPendingSubjectSelections() {
            List<Student> allStudents = DataManager.getInstance().getStudents();
            List<Student> studentsWithPendingSelections = new ArrayList<>();

            for (Student student : allStudents) {
                // Include students with confirmed registration who have:
                // 1. Selected subjects that haven't been approved yet
                // 2. Have submitted their selections for review
                if (student.getRegistrationStatus().equals("Approved") && // Changed from "Confirmed" to "Approved"
                        !student.getSelectedSubjects().isEmpty() &&
                        student.areSubjectsSubmittedForReview()) {
                    studentsWithPendingSelections.add(student);
                }
            }

            return studentsWithPendingSelections;
        }
    
        public boolean approveStudentRegistration(Student student) {
            // Set the student's registration status to Approved
            student.setRegistrationStatus("Approved");
    
            // Update the student in the DataManager
            DataManager.getInstance().updateUser(student);
    
            // Update the CSV file to persist the status change
            updateStudentStatusInCSV(student);
    
            // Save data to ensure the registration status is persisted
            DataManager.getInstance().saveData();
    
            return true;
        }
    
        public boolean rejectStudentRegistration(Student student) {
            // Remove the student from the system
            DataManager.getInstance().removeUser(student);
    
            // Save data to ensure changes are persisted
            DataManager.getInstance().saveData();
    
            return true;
        }
    
        /**
         * Updates a student's status in the CSV file
         * @param student The student whose status needs updating
         * @return true if the update was successful, false otherwise
         */
        public boolean updateStudentStatusInCSV(Student student) {
            String csvFile = "src/util/Students.csv";
            String tempFile = "temp.csv";
            String line;
            String cvsSplitBy = ",";
            boolean updated = false;
    
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
    
                // Read the header line and write it to the temp file
                line = br.readLine();
                if (line != null) {
                    bw.write(line);
                    bw.newLine();
                }
    
                // Process each line
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy, -1); // Use -1 to preserve empty trailing fields
    
                    // Check if this is the line for our student
                    if (data.length > 0 && data[0].trim().equals(student.getId())) {
                        // Ensure we have enough fields (ID, Name, Username, Password, Email, Course, Year, Semester, Subjects, Grades, Status)
                        StringBuilder newLine = new StringBuilder();
    
                        // Copy existing fields or use empty strings if not present
                        String[] newData = new String[11];
                        for (int i = 0; i < Math.min(data.length, 11); i++) {
                            newData[i] = data[i];
                        }
    
                        // Fill remaining fields with empty strings if necessary
                        for (int i = data.length; i < 11; i++) {
                            newData[i] = "";
                        }
    
                        // Update the status (last field)
                        newData[10] = student.getRegistrationStatus();
    
                        // If we have subject selections and they're approved, update the subjects field
                        if (!student.getApprovedSubjects().isEmpty()) {
                            newData[8] = student.getApprovedSubjectsAsString();
                        }
    
                        // Build the new line
                        for (int i = 0; i < newData.length; i++) {
                            if (i > 0) {
                                newLine.append(",");
                            }
                            newLine.append(newData[i]);
                        }
    
                        bw.write(newLine.toString());
                        updated = true;
                    } else {
                        // Write the original line
                        bw.write(line);
                    }
                    bw.newLine();
                }
    
            } catch (IOException e) {
                System.err.println("Error updating student status in CSV: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
    
            // Replace the original file with the temp file
            try {
                File originalFile = new File(csvFile);
                File newFile = new File(tempFile);
    
                if (originalFile.delete()) {
                    if (!newFile.renameTo(originalFile)) {
                        System.err.println("Could not rename temp file to original file name");
                        return false;
                    }
                } else {
                    System.err.println("Could not delete original file");
                    return false;
                }
            } catch (Exception e) {
                System.err.println("Error replacing CSV file: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
    
            return updated;
        }
    
        /**
         * Approves the subject selected by a student
         * @param admin The admin performing the approval
         * @param studentId The ID of the student whose subject selection is being approved
         * @param subjectId The ID of the subject to approve
         * @return true if the subject was successfully approved, false otherwise
         */
        public boolean approveStudentSubjectSelection(Admin admin, String studentId, String subjectId) {
            // Get the student and subject by their IDs
            Student student = getStudentById(studentId);
            Subject subject = getSubjectById(subjectId);
    
            // Check if both student and subject exist
            if (student == null || subject == null) {
                return false;
            }
    
            // Check if the student has selected this subject
            if (!student.hasSelectedSubject(subjectId)) {
                return false;
            }
    
            // Use the student's approveSubject method to move the subject from selected to approved
            boolean approved = student.approveSubject(subject);
            if (!approved) {
                return false;
            }
    
            // Admin records the approval action
            admin.approveSubjectSelection(student, subject);
    
            // Update the student in the data store
            DataManager.getInstance().updateUser(student);
    
            // Save the approved subjects to the CSV file
            updateStudentSubjectsInCSV(student);
    
            return true;
        }
    
        /**
         * Approves all pending subject selections for a student
         * @param admin The admin performing the approval
         * @param studentId The ID of the student whose subject selections are being approved
         * @return true if at least one subject was approved, false otherwise
         */
        public boolean approveAllStudentSubjectSelections(Admin admin, String studentId) {
            Student student = getStudentById(studentId);
            if (student == null) {
                return false;
            }
    
            boolean anyApproved = false;
    
            // Create a copy of the selected subjects to avoid concurrent modification
            List<Subject> selectedSubjects = new ArrayList<>(student.getSelectedSubjects());
    
            for (Subject subject : selectedSubjects) {
                boolean approved = student.approveSubject(subject);
                if (approved) {
                    admin.approveSubjectSelection(student, subject);
                    anyApproved = true;
                }
            }
    
            if (anyApproved) {
                // Reset the submitted for review flag since all subjects have been processed
                student.setSubmittedForReview(false);
    
                // Update the student in the data store
                DataManager.getInstance().updateUser(student);
    
                // Save the approved subjects to the CSV file
                updateStudentSubjectsInCSV(student);
            }
    
            return anyApproved;
        }
    
        /**
         * Updates the student's subjects in the CSV file
         * @param student The student whose subjects are being updated
         * @return true if update was successful, false otherwise
         */
        private boolean updateStudentSubjectsInCSV(Student student) {
            String csvFile = "src/util/Students.csv";
            String tempFile = "temp.csv";
            String line;
            String cvsSplitBy = ",";
    
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
    
                // Read the header line and write it to the temp file
                line = br.readLine();
                bw.write(line);
                bw.newLine();
    
                // Process each line
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);
    
                    // Check if this is the line for our student
                    if (data.length > 0 && data[0].equals(student.getId())) {
                        // Update the subjects field with approved subjects
                        StringBuilder newLine = new StringBuilder();
    
                        // Copy all existing fields up to the subjects field
                        for (int i = 0; i < Math.min(data.length, 8); i++) {
                            newLine.append(data[i]).append(",");
                        }
    
                        // Append the approved subjects
                        newLine.append(student.getApprovedSubjectsAsString());
    
                        // Append the grades field if it exists
                        if (data.length > 9) {
                            newLine.append(",").append(data[9]);
                        } else {
                            newLine.append(","); // Empty grades field
                        }
    
                        // Append the status field
                        newLine.append(",").append(student.getRegistrationStatus());
    
                        bw.write(newLine.toString());
                    } else {
                        // Write the original line
                        bw.write(line);
                    }
                    bw.newLine();
                }
    
                // Close resources
                br.close();
                bw.close();
    
                // Replace the original file with the temp file
                File originalFile = new File(csvFile);
                File newFile = new File(tempFile);
    
                if (originalFile.delete()) {
                    if (!newFile.renameTo(originalFile)) {
                        System.err.println("Could not rename temp file to original file name");
                        return false;
                    }
                } else {
                    System.err.println("Could not delete original file");
                    return false;
                }
    
                return true;
    
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    
        /**
         * Gets a student by their ID
         * @param studentId The ID of the student to retrieve
         * @return The Student object or null if not found
         */
        public Student getStudentById(String studentId) {
            List<Student> students = DataManager.getInstance().getStudents();
            for (Student student : students) {
                if (student.getId().equals(studentId)) {
                    return student;
                }
            }
            return null;
        }
    
        public List<Teacher> getAllTeachers() {
            return DataManager.getInstance().getTeachers();
        }
    
        public List<Subject> getAllSubjects() {
            return DataManager.getInstance().getSubjects();
        }
    
        public List<Student> getAllStudents() {
            return DataManager.getInstance().getStudents();
        }
    
        public List<Subject> getAvailableSubjects() {
            List<Subject> allSubjects = DataManager.getInstance().getSubjects();
            List<Subject> availableSubjects = new ArrayList<>();
    
            for (Subject subject : allSubjects) {
                if (subject.getTeacher() == null) {
                    availableSubjects.add(subject);
                }
            }
    
            return availableSubjects;
        }
    
        public Teacher getTeacherById(String teacherId) {
            List<Teacher> teachers = DataManager.getInstance().getTeachers();
            for (Teacher teacher : teachers) {
                if (teacher.getId().equals(teacherId)) {
                    return teacher;
                }
            }
            return null;
        }
    
        public Subject getSubjectById(String subjectId) {
            List<Subject> subjects = DataManager.getInstance().getSubjects();
            for (Subject subject : subjects) {
                if (subject.getId().equals(subjectId)) {
                    return subject;
                }
            }
            return null;
        }
    
        public boolean addTeacher(String name, String username, String password, String email) {
            if (DataManager.getInstance().usernameExists(username)) {
                return false;
            }
    
            String id = "T" + UUID.randomUUID().toString().substring(0, 8);
            Teacher teacher = new Teacher(id, name, username, password, email);
            DataManager.getInstance().addUser(teacher);
            return true;
        }
    
        public boolean assignSubjectToTeacher(Admin admin, Teacher teacher, Subject subject) {
            admin.assignTeacherToSubject(teacher, subject);
            DataManager.getInstance().updateUser(teacher);
            DataManager.getInstance().updateSubject(subject);
            return true;
        }
    
        public boolean addSubject(String id, String name, String description, int credits, String course, String yearLevel, String semester) {
            List<Subject> subjects = DataManager.getInstance().getSubjects();
            for (Subject subject : subjects) {
                if (subject.getId().equals(id)) {
                    return false;
                }
            }
    
            Subject subject = new Subject(id, name, description, credits, course, yearLevel, semester);
            DataManager.getInstance().addSubject(subject);
            return true;
        }
    }