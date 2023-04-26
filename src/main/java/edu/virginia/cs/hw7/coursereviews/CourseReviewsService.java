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

    public void register(Student student) {
        if (studentManager.checkStudent(student)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        studentManager.addStudent(student);
        this.loggedInStudent = student;
    }

    public void login(Student student) {
        if (!studentManager.verifyStudent(student)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        this.loggedInStudent = student;
    }

    public void logout() {
        loggedInStudent = null;
    }

    public Student getLoggedInStudent() {
        return loggedInStudent;
    }
    public Review validateReview(Course course, String comment, String rating) {
        int intRating;
        try {
            intRating = Integer.parseInt(rating);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating. Please enter an integer.");
        }
        if (intRating < 1 || intRating > 5) {
            throw new IllegalArgumentException("Invalid rating. Please enter a number between 1 and 5.");
        }
        return new Review(getLoggedInStudent(), course, comment, intRating);
    }

    public void submitReview(Review review) {
        if (!classManager.checkCourse(review.getCourse())) {
            addCourse(review.getCourse());
        }
        if (reviewManager.checkReview(review)) {
            throw new IllegalArgumentException("You have already submitted a review for this course.");
        }
        reviewManager.addReview(review);
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
                    throw new RuntimeException("Error getting student");
                }
                String courseID = rs.getString("course_id");
                Course reviewCourse = classManager.getCourse(courseID);
                if (reviewCourse == null) {
                    throw new RuntimeException("Error getting course");
                }
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                reviews.add(new Review(reviewStudent, reviewCourse, comment, rating));
            }
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting reviews");
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
            throw new RuntimeException("Error getting average rating");
        }
    }

    public Course validateCourseName(String courseName) {
        String[] courseInfo = courseName.split(" ");
        String subject = courseInfo[0];
        if (subject.length() < 2 || subject.length() > 4 || !subject.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Invalid course name.");
        }
        String catalogNumber = courseInfo[1];
        if (catalogNumber.length() != 4 || !catalogNumber.matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid course number.");
        }
        return new Course(subject, Integer.parseInt(catalogNumber));
    }

    public void addCourse(Course course) {
        if (classManager.checkCourse(course)) {
            throw new IllegalArgumentException("Course already exists.");
        }
        classManager.addCourse(course);
    }
}
