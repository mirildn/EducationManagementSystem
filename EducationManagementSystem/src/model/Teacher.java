package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher extends User implements Serializable {
    private List<Subject> assignedSubjects;
    private Map<String, Map<String, Double>> grades; // Student ID -> (Subject ID -> Grade)

    public Teacher(String id, String name, String username, String password, String email) {
        super(id, name, username, password, email);
        this.assignedSubjects = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    @Override
    public String getUserType() {
        return "model.Teacher";
    }

    public List<Subject> getAssignedSubjects() {
        return assignedSubjects;
    }

    public void setAssignedSubjects(List<Subject> subjects) {
        this.assignedSubjects = new ArrayList<>(subjects);
    }

    public void assignSubject(Subject subject) {
        if (!assignedSubjects.contains(subject)) {
            assignedSubjects.add(subject);
        }
    }

    public void addAssignedSubject(Subject subject) {
        assignSubject(subject);
    }

    public void removeAssignedSubject(Subject subject) {
        assignedSubjects.remove(subject);
    }

    /**
     * Checks if this teacher is assigned to a subject with the given ID
     * @param subjectId The subject ID to check
     * @return true if the teacher is assigned to the subject, false otherwise
     */
    public boolean isAssignedToSubject(String subjectId) {
        for (Subject subject : assignedSubjects) {
            if (subject.getId().equals(subjectId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Grade a student for a specific subject
     * @param student The student to grade
     * @param subject The subject to grade
     * @param grade The grade to assign
     */
    public void gradeStudent(Student student, Subject subject, double grade) {
        // Update our internal grade record
        String studentId = student.getId();
        String subjectId = subject.getId();

        if (!grades.containsKey(studentId)) {
            grades.put(studentId, new HashMap<>());
        }

        grades.get(studentId).put(subjectId, grade);

        // Update the student's grade directly
        student.setGrade(subject, grade);
    }

    /**
     * Get the grade assigned by this teacher to a student for a subject
     * @param studentId The student ID
     * @param subjectId The subject ID
     * @return The grade, or -1.0 if no grade exists
     */
    public double getStudentGrade(String studentId, String subjectId) {
        if (grades.containsKey(studentId) && grades.get(studentId).containsKey(subjectId)) {
            return grades.get(studentId).get(subjectId);
        }
        return -1.0;  // Indicates no grade
    }

    /**
     * Load grades from student objects to sync teacher's grade map
     * This method should be called when the teacher logs in or when initializing
     */
    public void loadGradesFromStudents(List<Student> allStudents) {
        for (Student student : allStudents) {
            String studentId = student.getId();
            Map<Subject, Double> studentGrades = student.getGrades();

            for (Map.Entry<Subject, Double> entry : studentGrades.entrySet()) {
                Subject subject = entry.getKey();
                Double grade = entry.getValue();

                // Only load grades for subjects this teacher is assigned to
                if (isAssignedToSubject(subject.getId()) && grade != null && grade > 0) {
                    if (!grades.containsKey(studentId)) {
                        grades.put(studentId, new HashMap<>());
                    }
                    grades.get(studentId).put(subject.getId(), grade);
                }
            }
        }
    }

    /**
     * Get all grades assigned by this teacher for a specific student
     * @param studentId The student ID
     * @return Map of subject IDs to grades
     */
    public Map<String, Double> getAllGradesForStudent(String studentId) {
        return grades.getOrDefault(studentId, new HashMap<>());
    }

    /**
     * Get all grades assigned by this teacher for a specific subject
     * @param subjectId The subject ID
     * @return Map of student IDs to grades
     */
    public Map<String, Double> getAllGradesForSubject(String subjectId) {
        Map<String, Double> subjectGrades = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> studentEntry : grades.entrySet()) {
            String studentId = studentEntry.getKey();
            Map<String, Double> studentGrades = studentEntry.getValue();

            if (studentGrades.containsKey(subjectId)) {
                subjectGrades.put(studentId, studentGrades.get(subjectId));
            }
        }

        return subjectGrades;
    }

    /**
     * Calculate the average grade for a specific subject
     * @param subjectId The subject ID
     * @return The average grade, or 0.0 if no grades exist
     */
    public double calculateAverageGradeForSubject(String subjectId) {
        Map<String, Double> subjectGrades = getAllGradesForSubject(subjectId);

        if (subjectGrades.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Double grade : subjectGrades.values()) {
            sum += grade;
        }

        return sum / subjectGrades.size();
    }

    /**
     * Get all students who have been graded by this teacher for a specific subject
     * @param subjectId The subject ID
     * @param allStudents List of all students in the system
     * @return List of students who have grades for the subject
     */
    public List<Student> getGradedStudentsForSubject(String subjectId, List<Student> allStudents) {
        List<Student> gradedStudents = new ArrayList<>();
        Map<String, Double> subjectGrades = getAllGradesForSubject(subjectId);

        for (Student student : allStudents) {
            if (subjectGrades.containsKey(student.getId())) {
                gradedStudents.add(student);
            }
        }

        return gradedStudents;
    }


    //Get all ungraded students for a specific subject
    public List<Student> getUngradedStudentsForSubject(String subjectId, List<Student> allStudents) {
        List<Student> ungradedStudents = new ArrayList<>();
        Map<String, Double> subjectGrades = getAllGradesForSubject(subjectId);

        for (Student student : allStudents) {
            if (student.hasApprovedSubject(subjectId) && !subjectGrades.containsKey(student.getId())) {
                ungradedStudents.add(student);
            }
        }

        return ungradedStudents;
    }
}