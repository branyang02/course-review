package edu.virginia.cs.hw7.coursereviews;

import java.util.List;

public class CourseReviewsService {
    private final StudentManager studentManager;
    private final CourseManager classManager;
    private final ReviewsManager reviewManager;
    private Student loggedInStudent;
    public CourseReviewsService() {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.createTables();
        db.populateDatabase();
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

    public void submitReview(Review review) {
        if (loggedInStudent == null) {
            System.out.println("You must be logged in to submit a review.");
            return;
        }
        if (reviewManager.checkReview(review)) {
            System.out.println("You have already submitted a review for this course.");
            return;
        }
        reviewManager.addReview(review);
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

    public List<Review> getReviews(Course course) {
        if (loggedInStudent == null) {
            System.out.println("You must be logged in to see reviews.");
            return null;
        }
        return getReviews(course);
    }

    public double getAverageRating(Course course) {
        return 0;
    }

    public void logout() {
        loggedInStudent = null;
    }

    public Student getLoggedInStudent() {
        return loggedInStudent;
    }

    public Course validateCourseName(String courseName) {
        String[] courseInfo = getCourseInfo(courseName);
        if (courseInfo == null) {
            return null;
        }
        String subject = courseInfo[0];
        if (subject.length() < 2 || subject.length() > 4 || !subject.matches("[A-Z]+")) {
            return null;
        }
        String catalogNumber = courseInfo[1];
        if (catalogNumber.length() != 4 || !catalogNumber.matches("\\d{4}")) {
            return null;
        }
        return new Course(subject, Integer.parseInt(catalogNumber));
    }

}
