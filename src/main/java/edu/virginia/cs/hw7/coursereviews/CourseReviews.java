package edu.virginia.cs.hw7.coursereviews;

public class CourseReviews {
    private final StudentManager studentManager;
    private final CourseManager classManager;
    private final ReviewsManager reviewManager;
    public CourseReviews() {
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
        return true;
    }

    public boolean login(Student student) {
        return !studentManager.checkStudent(student);
    }

    public void submitReview(Review review) {

    }

    public void getReviews(Course course) {

    }

    public double getAverageRating(Course course) {
        return 0;
    }
}
