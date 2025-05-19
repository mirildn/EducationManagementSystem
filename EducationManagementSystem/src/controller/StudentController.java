package controller;

import model.Student;
import model.Subject;
import util.DataManager;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import java.util.List;

public class StudentController {

    public List<Subject> getAllSubjects() {
        return DataManager.getInstance().getSubjects();
    }

    public Subject getSubjectById(String subjectId) {
        for (Subject subject : DataManager.getInstance().getSubjects()) {
            if (subject.getId().equals(subjectId)) {
                return subject;
            }
        }
        return null;
    }

    public void loadSubjectsFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Skip header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 7) { // Based on CSV structure: ID,Name,Description,Credits,Course,Year Level,Semester
                    String id = values[0];
                    String name = values[1];
                    String description = values[2];
                    int credits = Integer.parseInt(values[3]);
                    String course = values[4];
                    String yearLevel = values[5];
                    String semester = values[6];

                    Subject subject = new Subject(id, name, description, credits, course, yearLevel, semester);

                    // Add to the DataManager
                    DataManager.getInstance().addSubject(subject);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateStudent(Student student) {
        DataManager.getInstance().updateUser(student);
        return true;
    }
}