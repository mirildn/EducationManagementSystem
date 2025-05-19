package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User implements Serializable {
    private String course;
    private String yearLevel;
    private String semester;
    private String registrationStatus;
    private List<Subject> selectedSubjects;
    private List<Subject> approvedSubjects;
    private Map<Subject, Double> grades;
    private boolean subjectsSubmittedForReview;

    public Student(String id, String name, String username, String password, String email, String course, String yearLevel, String semester) {
        super(id, name, username, password, email);
        this.course = course;
        this.yearLevel = yearLevel;
        this.semester = semester;
        this.registrationStatus = "Pending";
        this.selectedSubjects = new ArrayList<>();
        this.approvedSubjects = new ArrayList<>();
        this.grades = new HashMap<>();
        this.subjectsSubmittedForReview = false;
    }

    @Override
    public String getUserType() {
        return "model.Student";
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public List<Subject> getSelectedSubjects() {
        return selectedSubjects;
    }

    public void addSelectedSubject(Subject subject) {
        if (!selectedSubjects.contains(subject)) {
            selectedSubjects.add(subject);
        }
    }

    public void selectSubject(Subject subject) {
        addSelectedSubject(subject);
    }

    public void removeSelectedSubject(Subject subject) {
        selectedSubjects.remove(subject);
    }

    public List<Subject> getApprovedSubjects() {
        return approvedSubjects;
    }

    public void addApprovedSubject(Subject subject) {
        if (!approvedSubjects.contains(subject)) {
            approvedSubjects.add(subject);
        }
    }

    public Map<Subject, Double> getGrades() {
        return grades;
    }

    public Double getGrade(Subject subject) {
        return grades.getOrDefault(subject, 0.0);
    }

    public void setGrade(Subject subject, Double grade) {
        grades.put(subject, grade);
    }

    /**
     * Checks if a subject with the given ID has been selected by this student
     * @param subjectId The ID of the subject to check
     * @return true if the subject has been selected, false otherwise
     */
    public boolean hasSelectedSubject(String subjectId) {
        for (Subject subject : selectedSubjects) {
            if (subject.getId().equals(subjectId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a subject with the given ID has been approved for this student
     * @param subjectId The ID of the subject to check
     * @return true if the subject has been approved, false otherwise
     */
    public boolean hasApprovedSubject(String subjectId) {
        for (Subject subject : approvedSubjects) {
            if (subject.getId().equals(subjectId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves a subject from selected to approved subjects list
     * @param subject The subject to approve
     * @return true if the subject was approved, false if it wasn't in the selected list
     */
    public boolean approveSubject(Subject subject) {
        if (selectedSubjects.contains(subject)) {
            selectedSubjects.remove(subject);
            addApprovedSubject(subject);
            return true;
        }
        return false;
    }

    /**
     * Calculate the GPA for all subjects with grades
     * @return The GPA value
     */
    public double calculateGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;

        for (Map.Entry<Subject, Double> entry : grades.entrySet()) {
            Subject subject = entry.getKey();
            Double grade = entry.getValue();

            totalPoints += grade * subject.getCredits();
            totalCredits += subject.getCredits();
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    /**
     * Checks if the student has submitted subjects for review
     * @return true if subjects have been submitted for review, false otherwise
     */
    public boolean areSubjectsSubmittedForReview() {
        return subjectsSubmittedForReview;
    }

    /**
     * Sets whether the student's subjects are submitted for review
     * @param submitted true if subjects are submitted for review, false otherwise
     */
    public void setSubmittedForReview(boolean submitted) {
        this.subjectsSubmittedForReview = submitted;
    }

    /**
     * Gets a comma-separated list of approved subject IDs
     * @return String of approved subject IDs
     */
    public String getApprovedSubjectsAsString() {
        if (approvedSubjects.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Subject subject : approvedSubjects) {
            sb.append(subject.getId()).append(",");
        }

        // Remove trailing comma
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /**
     * Gets grades as a formatted string for CSV storage
     * Format: subjectId:grade;subjectId:grade;...
     * @return String representation of grades
     */
    public String getGradesAsString() {
        if (grades.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Subject, Double> entry : grades.entrySet()) {
            Subject subject = entry.getKey();
            Double grade = entry.getValue();

            if (grade != null && grade > 0) {
                sb.append(subject.getId()).append(":").append(grade).append(";");
            }
        }

        // Remove trailing semicolon
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /**
     * Sets grades from a formatted string (for CSV loading)
     * Format: subjectId:grade;subjectId:grade;...
     * @param gradesString String representation of grades
     * @param allSubjects List of all subjects to match IDs
     */
    public void setGradesFromString(String gradesString, List<Subject> allSubjects) {
        if (gradesString == null || gradesString.trim().isEmpty()) {
            return;
        }

        // Create a map for quick subject lookup
        Map<String, Subject> subjectMap = new HashMap<>();
        for (Subject subject : allSubjects) {
            subjectMap.put(subject.getId(), subject);
        }

        String[] gradeEntries = gradesString.split(";");
        for (String entry : gradeEntries) {
            if (entry.contains(":")) {
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    String subjectId = parts[0].trim();
                    try {
                        double grade = Double.parseDouble(parts[1].trim());
                        Subject subject = subjectMap.get(subjectId);
                        if (subject != null) {
                            grades.put(subject, grade);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid grade format: " + entry);
                    }
                }
            }
        }
    }

    /**
     * Gets the grade for a subject by subject ID
     * @param subjectId The subject ID
     * @return The grade, or 0.0 if no grade exists
     */
    public double getGradeBySubjectId(String subjectId) {
        for (Map.Entry<Subject, Double> entry : grades.entrySet()) {
            if (entry.getKey().getId().equals(subjectId)) {
                return entry.getValue();
            }
        }
        return 0.0;
    }

    /**
     * Sets approved subjects from a string (for CSV loading)
     * Format: subjectId;subjectId;...
     * @param subjectsString String representation of subject IDs
     * @param allSubjects List of all subjects to match IDs
     */
    public void setApprovedSubjectsFromString(String subjectsString, List<Subject> allSubjects) {
        if (subjectsString == null || subjectsString.trim().isEmpty()) {
            return;
        }

        // Create a map for quick subject lookup
        Map<String, Subject> subjectMap = new HashMap<>();
        for (Subject subject : allSubjects) {
            subjectMap.put(subject.getId(), subject);
        }

        String[] subjectIds = subjectsString.split(";");
        for (String subjectId : subjectIds) {
            subjectId = subjectId.trim();
            Subject subject = subjectMap.get(subjectId);
            if (subject != null) {
                addApprovedSubject(subject);
            }
        }
    }
}