package util;

import model.Student;
import model.Subject;

import java.util.*;
import java.util.Comparator;

public class SubjectRecommender {

    // Recommend subjects based on student's course, year, semester, and performance
    public static List<Subject> recommendSubjects(Student student) {
        List<Subject> recommendedSubjects = new ArrayList<>();
        List<Subject> courseSubjects = DataManager.getInstance().getSubjectsByCourse(student.getCourse());

        // Get student's current subjects and grades
        List<Subject> selectedSubjects = student.getSelectedSubjects();
        List<Subject> approvedSubjects = student.getApprovedSubjects();
        Map<Subject, Double> grades = student.getGrades();

        // Calculate student's GPA
        double gpa = student.calculateGPA();

        // Score each available subject
        Map<Subject, Double> subjectScores = new HashMap<>();

        for (Subject subject : courseSubjects) {
            // Skip subjects the student has already selected or completed
            if (selectedSubjects.contains(subject) || approvedSubjects.contains(subject)) {
                continue;
            }

            // Calculate a score for this subject based on various factors
            double score = calculateSubjectScore(subject, student, gpa);
            subjectScores.put(subject, score);
        }

        // Sort subjects by score in descending order
        List<Map.Entry<Subject, Double>> sortedSubjects = new ArrayList<>(subjectScores.entrySet());
        sortedSubjects.sort(Map.Entry.<Subject, Double>comparingByValue().reversed());

        // Add top subjects to recommendation list
        for (Map.Entry<Subject, Double> entry : sortedSubjects) {
            recommendedSubjects.add(entry.getKey());
        }

        return recommendedSubjects;
    }

    /**
     * Recommends subjects for a student to take in the next semester based on:
     * 1. Failed subjects that need to be retaken
     * 2. Appropriate subjects for the upcoming semester/year
     *
     * @param student The student for whom to recommend subjects
     * @return A list of recommended subjects in priority order
     */
    public static List<Subject> recommendSubjectsForStudent(Student student) {
        List<Subject> recommendations = new ArrayList<>();

        // First priority: Add failed subjects that need to be retaken
        List<Subject> failedSubjects = getFailedSubjects(student);
        recommendations.addAll(failedSubjects);

        // Second priority: Get suitable subjects for next semester/year
        List<Subject> nextPeriodSubjects = getNextPeriodSubjects(student);

        // Filter out subjects that are already in recommendations (failed subjects)
        nextPeriodSubjects.removeAll(recommendations);

        // Add these subjects to recommendations
        recommendations.addAll(nextPeriodSubjects);

        return recommendations;
    }

    /**
     * Gets a list of subjects the student has failed and needs to retake
     */
    private static List<Subject> getFailedSubjects(Student student) {
        List<Subject> failedSubjects = new ArrayList<>();
        Map<Subject, Double> grades = student.getGrades();

        // Assuming passing grade is 60.0 (adjust as needed)
        double passingGrade = 60.0;

        for (Map.Entry<Subject, Double> entry : grades.entrySet()) {
            Subject subject = entry.getKey();
            Double grade = entry.getValue();

            // If the student has a grade for this subject but it's below passing
            // and the subject is not already in their approved subjects
            if (grade != null && grade < passingGrade &&
                    !student.getApprovedSubjects().contains(subject)) {
                failedSubjects.add(subject);
            }
        }

        return failedSubjects;
    }

    /**
     * Gets appropriate subjects for the student's next semester or year
     */
    private static List<Subject> getNextPeriodSubjects(Student student) {
        List<Subject> nextPeriodSubjects = new ArrayList<>();
        List<Subject> allCourseSubjects = DataManager.getInstance().getSubjectsByCourse(student.getCourse());

        // Parse current year level to determine next year level
        String currentYearLevelStr = student.getYearLevel();
        String currentSemesterStr = student.getSemester();

        // Remove any non-numeric characters (e.g., "1st" becomes "1")
        String yearNumStr = currentYearLevelStr.replaceAll("[^0-9]", "");
        String semesterNumStr = currentSemesterStr.replaceAll("[^0-9]", "");

        int currentYear = Integer.parseInt(yearNumStr);
        int currentSemester = Integer.parseInt(semesterNumStr);

        // Calculate next semester and year
        int nextSemester = currentSemester == 1 ? 2 : 1;
        int nextYear = currentSemester == 1 ? currentYear : currentYear + 1;

        // Format to match the expected String format
        String nextYearLevel = nextYear + getYearLevelSuffix(nextYear);
        String nextSemesterStr = nextSemester + getSemesterSuffix(nextSemester);

        for (Subject subject : allCourseSubjects) {
            // Skip if student has already selected or passed this subject
            if (student.getSelectedSubjects().contains(subject) ||
                    student.getApprovedSubjects().contains(subject)) {
                continue;
            }

            // Check if this subject is appropriate for the next semester/year
            boolean matchesNextPeriod = false;

            // For subjects in the next semester of the same year
            if (subject.getYearLevel().equals(currentYearLevelStr) &&
                    subject.getSemester().equals(nextSemesterStr) &&
                    currentSemester == 1) {
                matchesNextPeriod = true;
            }

            // For subjects in the first semester of the next year
            if (subject.getYearLevel().equals(nextYearLevel) &&
                    subject.getSemester().equals("1st") &&
                    currentSemester == 2) {
                matchesNextPeriod = true;
            }

            // Also consider subjects from the current semester that the student hasn't taken yet
            // This is helpful for transfer students or those catching up on missed subjects
            if (subject.getYearLevel().equals(currentYearLevelStr) &&
                    subject.getSemester().equals(currentSemesterStr)) {
                matchesNextPeriod = true;
            }

            if (matchesNextPeriod && subject.getCourse().equals(student.getCourse())) {
                nextPeriodSubjects.add(subject);
            }
        }

        // Sort subjects by importance (e.g., credits, core vs. elective)
        nextPeriodSubjects.sort(Comparator.comparing(Subject::getCredits).reversed());

        return nextPeriodSubjects;
    }

    /**
     * Helper method to get the suffix for year level (e.g., "1" -> "st", "2" -> "nd")
     */
    private static String getYearLevelSuffix(int year) {
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
    private static String getSemesterSuffix(int semester) {
        switch (semester) {
            case 1: return "st";
            case 2: return "nd";
            default: return "th";
        }
    }

    private static double calculateSubjectScore(Subject subject, Student student, double gpa) {
        double score = 0.0;

        // Factor 1: Subject credits (higher credits might be more important)
        score += subject.getCredits() * 0.5;

        // Factor 2: Student's year and semester (recommend appropriate level subjects)
        // Parse year and semester from strings
        String yearNumStr = student.getYearLevel().replaceAll("[^0-9]", "");
        String semesterNumStr = student.getSemester().replaceAll("[^0-9]", "");

        int currentYear = Integer.parseInt(yearNumStr);
        int currentSemester = Integer.parseInt(semesterNumStr);

        int studentLevel = (currentYear - 1) * 2 + currentSemester;
        int subjectLevel = parseSubjectLevel(subject.getId());

        // Prefer subjects that match student's current level
        double levelDifference = Math.abs(studentLevel - subjectLevel);
        if (levelDifference <= 1) {
            score += 2.0; // Subjects at student's current level or one level up/down
        } else if (levelDifference <= 2) {
            score += 1.0; // Subjects two levels up/down
        }

        // Factor 3: Student's performance (recommend more challenging subjects for high performers)
        if (gpa >= 3.5) { // Assuming grades are on a 4.0 scale
            if (subject.getCredits() >= 4) {
                score += 1.5; // Higher credit subjects for top performers
            }
            if (subjectLevel > studentLevel) {
                score += 1.0; // Advanced subjects for top performers
            }
        } else if (gpa >= 3.0) {
            if (subjectLevel >= studentLevel) {
                score += 0.75; // Current level or slightly advanced subjects for good performers
            }
        } else {
            if (subjectLevel <= studentLevel) {
                score += 1.0; // Lower or current level subjects for students who may need more foundational work
            }
        }

        return score;
    }

    // Parse the subject level from its ID (e.g., CS101 -> 1, CS302 -> 3)
    private static int parseSubjectLevel(String subjectId) {
        if (subjectId != null && subjectId.length() >= 3) {
            char levelChar = subjectId.charAt(2);
            if (Character.isDigit(levelChar)) {
                return Character.getNumericValue(levelChar);
            }
        }
        return 1; // Default to level 1 if parsing fails
    }
}