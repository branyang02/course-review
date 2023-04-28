package edu.virginia.cs.hw7.coursereviews;

import java.util.List;

public class CourseReviewsService {
    private final StudentManager studentManager;
    private final CourseManager courseManager;
    private final ReviewsManager reviewManager;
    private Student loggedInStudent;
    public CourseReviewsService() {
        DatabaseManager db = new DatabaseManager();
//        setupDatabase(db);
        studentManager = new StudentManager(db);
        courseManager = new CourseManager(db);
        reviewManager = new ReviewsManager(db);
    }

    private static void setupDatabase(DatabaseManager db) {
        db.connect();
        db.createTables();
        db.populateDatabase();
        db.disconnect();
    }

    public void register(Student student) {
        if (student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
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
        if (!courseManager.checkCourse(review.getCourse())) {
            addCourse(review.getCourse());
        }
        if (reviewManager.checkReview(review)) {
            throw new IllegalArgumentException("You have already submitted a review for this course.");
        }
        reviewManager.addReview(review);
    }

    public List<Review> getReviews(Course course) {
        List<Review> reviews = reviewManager.getReviews(course);
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }
        return reviews;
    }

    public double getAverageRating(Course course) {
        List<Review> reviews = reviewManager.getReviews(course);
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        double total = 0;
        double count = 0;
        for (Review review : reviews) {
            total += review.getRating();
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return total / count;
    }

    public Course validateCourseName(String courseName) {
        String[] courseInfo = courseName.split(" ");
        if (courseInfo.length != 2) {
            throw new IllegalArgumentException("Invalid course name.");
        }
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
        if (courseManager.checkCourse(course)) {
            throw new IllegalArgumentException("Course already exists.");
        }
        courseManager.addCourse(course);
    }
}
