package model;

import java.util.ArrayList;
import java.util.List;

public class Admin extends User {

    private String adminUser = "admin";
    private String adminPass = "admin123";
    // Track subject approval history
    private List<ApprovalRecord> approvalRecords;

    public Admin(String id, String name, String username, String password, String email) {
        super(id, name, username, password, email);
        this.approvalRecords = new ArrayList<>();
    }

    @Override
    public String getUserType() {
        return "model.Admin";
    }

    public void confirmStudentRegistration(Student student) {
        student.setRegistrationStatus("Confirmed");
    }

    public void assignTeacherToSubject(Teacher teacher, Subject subject) {
        teacher.assignSubject(subject);
        subject.setTeacher(teacher);
    }

    /**
     * Approves a student's subject selection and records the approval action
     * @param student The student whose subject is being approved
     * @param subject The subject being approved
     * @return true if the approval was successful
     */
    public boolean approveSubjectSelection(Student student, Subject subject) {
        // Add approval record
        approvalRecords.add(new ApprovalRecord(student, subject));
        return true;
    }

    /**
     * Gets the history of all subject approvals made by this admin
     * @return List of approval records
     */
    public List<ApprovalRecord> getApprovalHistory() {
        return approvalRecords;
    }

    /**
     * Record class to track subject approval actions
     */
    public class ApprovalRecord {
        private Student student;
        private Subject subject;
        private long timestamp;

        public ApprovalRecord(Student student, Subject subject) {
            this.student = student;
            this.subject = subject;
            this.timestamp = System.currentTimeMillis();
        }

        public Student getStudent() {
            return student;
        }

        public Subject getSubject() {
            return subject;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}