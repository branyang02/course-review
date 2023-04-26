package edu.virginia.cs.hw7.coursereviews;

import java.util.List;

public class CourseReviewsService {
    private final StudentManager studentManager;
    private final CourseManager classManager;
    private final ReviewsManager reviewManager;
    private Student loggedInStudent;
    public CourseReviewsService() {
        DatabaseManager db = new DatabaseManager();
        studentManager = new StudentManager(db);
        classManager = new CourseManager(db);
        reviewManager = new ReviewsManager(db);
    }

    public boolean register(Student student) {
        if (studentManager.checkStudent(student)) {
            return false;
        }
        studentManager.addStudent(student);
        this.loggedInStudent = student;
        return true;
    }

    public boolean login(Student student) {
        if (!studentManager.checkStudent(student)) {
            return false;
        }
        this.loggedInStudent = student;
        return true;
    }

    public void submitReview(String classInfo, String review, int rating) {
        if (loggedInStudent == null) {
            System.out.println("You must be logged in to submit a review.");
            return;
        }
    }

    private String[] getCourseInfo(String input) {
        try {
            String[] courseInfo = input.split(" ");
            String subject = courseInfo[0];
            if (subject.length() < 2 || subject.length() > 4) {
                System.out.println("Invalid course name");
                return null;
            }
            String catalogNumber = courseInfo[1];
            if (catalogNumber.length() != 4) {
                System.out.println("Invalid course number");
                return null;
            }
            if (Integer.parseInt(catalogNumber) < 1000 || Integer.parseInt(catalogNumber) > 9999) {
                System.out.println("Invalid course number");
                return null;
            }
            return new String[]{subject, catalogNumber};
        } catch (Exception e) {
            System.out.println("Invalid course name and number.");
            return null;
        }
    }

    public List<Review> getReviews(String classInfo) {
        if (loggedInStudent == null) {
            System.out.println("You must be logged in to see reviews.");
            return null;
        }
        return null;
    }

    public double getAverageRating(Course course) {
        return 0;
    }

    public void logout() {
        loggedInStudent = null;
    }
}
