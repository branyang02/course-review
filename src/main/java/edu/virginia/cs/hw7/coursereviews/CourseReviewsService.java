package edu.virginia.cs.hw7.coursereviews;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseReviewsService {
    private final StudentManager studentManager;
    private final CourseManager classManager;
    private final ReviewsManager reviewManager;
    private Student loggedInStudent;
    public CourseReviewsService() {
        DatabaseManager db = new DatabaseManager();
        setupDatabase(db);
        studentManager = new StudentManager(db);
        classManager = new CourseManager(db);
        reviewManager = new ReviewsManager(db);
    }

    private static void setupDatabase(DatabaseManager db) {
        db.connect();
        db.createTables();
        db.populateDatabase();
        db.disconnect();
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
        List<Review> reviews = new ArrayList<>();
        try {
            ResultSet rs = reviewManager.getReviews(course);
            if (rs == null) {
                return null;
            }
            while (rs.next()) {
                String studentID = rs.getString("student_id");
                Student reviewStudent = studentManager.getStudent(studentID);
                if (reviewStudent == null) {
                    System.out.println("Error getting student");
                    return null;
                }
                String courseID = rs.getString("course_id");
                Course reviewCourse = classManager.getCourse(courseID);
                if (reviewCourse == null) {
                    System.out.println("Error getting course");
                    return null;
                }
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                reviews.add(new Review(reviewStudent, reviewCourse, comment, rating));
            }
            return reviews;
        } catch (SQLException e) {
            System.out.println("Error getting reviews");
            return null;
        }
    }

    public double getAverageRating(Course course) {
        try {
            ResultSet rs = reviewManager.getReviews(course);
            if (rs == null) {
                return 0;
            }
            double total = 0;
            double count = 0;
            while (rs.next()) {
                total += rs.getInt("rating");
                count++;
            }
            if (count == 0) {
                return 0;
            }
            return total / count;
        } catch (SQLException e) {
            System.out.println("Error getting reviews");
            return 0;
        }
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
