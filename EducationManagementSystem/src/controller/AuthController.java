package controller;

import model.Student;
import model.User;
import util.DataManager;

public class AuthController {

    public static User login(String username, String password) {
        return DataManager.getInstance().authenticateUser(username, password);
    }

    public static boolean registerStudent(Student student) {
        if (DataManager.getInstance().usernameExists(student.getUsername())) {
            return false;
        }

        DataManager.getInstance().addUser(student);
        return true;
    }
}