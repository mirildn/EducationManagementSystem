package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subject implements Serializable {
    private String id;
    private String name;
    private String description;
    private int credits;
    private String course;  // e.g., "IT", "CS", etc.
    private String yearLevel;  // e.g., "1st", "2nd", "3rd", "4th"
    private String semester;   // e.g., "1st", "2nd", "Summer"
    private Teacher teacher;
    private List<Student> enrolledStudents;

    public Subject(String id, String name, String description, int credits, String course, String yearLevel, String semester) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.course = course;
        this.yearLevel = yearLevel;
        this.semester = semester;
        this.enrolledStudents = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public List<Student> getEnrolledStudents() { return enrolledStudents; }

    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}