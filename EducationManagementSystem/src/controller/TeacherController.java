package controller;

import model.Student;
import model.Teacher;
import model.Subject;
import util.DataManager;
import java.util.List;

public class TeacherController {

    public Student getStudentById(String studentId) {
        for (Student student : DataManager.getInstance().getStudents()) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    public Subject getSubjectById(String subjectId) {
        for (Subject subject : DataManager.getInstance().getSubjects()) {
            if (subject.getId().equals(subjectId)) {
                return subject;
            }
        }
        return null;
    }

    public boolean updateTeacher(Teacher teacher) {
        try {
            DataManager.getInstance().updateUser(teacher);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating teacher: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        try {
            DataManager.getInstance().updateUser(student);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Save all students to CSV
     * This method should be called after updating grades
     */
    public boolean saveStudentsToCSV() {
        try {
            DataManager.getInstance().saveStudentsToCSV();
            return true;
        } catch (Exception e) {
            System.err.println("Error saving students to CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Initialize teacher's grades from existing student data
     * This should be called when a teacher logs in or is initialized
     */
    public void initializeTeacherGrades(Teacher teacher) {
        List<Student> allStudents = DataManager.getInstance().getStudents();
        teacher.loadGradesFromStudents(allStudents);
    }

    /**
     * Grade a student and update both teacher and student records
     */
    public boolean gradeStudent(Teacher teacher, String studentId, String subjectId, double grade) {
        try {
            Student student = getStudentById(studentId);
            Subject subject = getSubjectById(subjectId);

            if (student == null || subject == null) {
                return false;
            }

            // Grade the student (this updates both teacher and student records)
            teacher.gradeStudent(student, subject, grade);

            // Update both teacher and student in the system
            updateTeacher(teacher);
            updateStudent(student);

            // Save to CSV
            saveStudentsToCSV();

            return true;
        } catch (Exception e) {
            System.err.println("Error grading student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Save all grades for a teacher
     */
    public boolean saveAllGrades(Teacher teacher) {
        try {
            // Update teacher record
            updateTeacher(teacher);

            // Update all students who have been graded by this teacher
            List<Student> allStudents = DataManager.getInstance().getStudents();
            for (Student student : allStudents) {
                updateStudent(student);
            }

            // Save to CSV
            saveStudentsToCSV();

            return true;
        } catch (Exception e) {
            System.err.println("Error saving all grades: " + e.getMessage());
            return false;
        }
    }
}