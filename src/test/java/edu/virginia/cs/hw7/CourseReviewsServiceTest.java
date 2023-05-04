package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseReviewsServiceTest {
    private DatabaseManager db;
    private StudentManager studentManager;
    private CourseManager courseManager;
    private ReviewsManager reviewManager;
    private CourseReviewsService courseReviewsService;
    private DatabaseBackup databaseBackup;

    @BeforeEach
    public void setUp() {
        db = new DatabaseManager();
        initializeDatabase(db);

        databaseBackup = new DatabaseBackup();
        databaseBackup.backupDatabase();


        studentManager = new StudentManager(db);
        courseManager = new CourseManager(db);
        reviewManager = new ReviewsManager(db);
        setupDatabase(db);
        courseReviewsService = CourseReviewsService.getInstance();
    }

    @AfterEach
    public void tearDown() {
        databaseBackup.restoreDatabase();
    }


    private void setupDatabase(DatabaseManager db) {
        db.connect();
        db.createTables();
        db.populateDatabase();
        db.disconnect();
    }

    private void initializeDatabase(DatabaseManager db) {
        db.connect();
        db.createTables();
        db.disconnect();
    }

    @Test
    public void testRegister() {
        Student student = new Student("John Doe", "password");
        assertFalse(studentManager.checkStudent(student));
        courseReviewsService.register(student);
        assertTrue(studentManager.checkStudent(student));
        assertEquals(student, courseReviewsService.getLoggedInStudent());
    }

    @Test
    public void testRegisterEmptyUsername() {
        Student student = new Student("", "password");
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.register(student));
    }

    @Test
    public void testRegisterEmptyPassword() {
        Student student = new Student("John Doe", "");
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.register(student));
    }

    @Test
    public void testRegisterUsernameAlreadyExists() {
        Student student = new Student("Alice", "alice1234");
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.register(student));
    }

    @Test
    public void testLogin() {
        Student student = new Student("Alice", "alice123");
        assertTrue(studentManager.checkStudent(student));
        courseReviewsService.login(student);
        assertEquals(student, courseReviewsService.getLoggedInStudent());
    }

    @Test
    public void testLoginInvalidUsername() {
        Student student = new Student("Bobbie", "Bobbie123");
        assertFalse(studentManager.checkStudent(student));
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.login(student));
    }

    @Test
    public void testLogout() {
        Student student = new Student("Alice", "alice123");
        courseReviewsService.login(student);
        assertTrue(studentManager.checkStudent(student));
        assertEquals(student, courseReviewsService.getLoggedInStudent());
        courseReviewsService.logout();
        assertNull(courseReviewsService.getLoggedInStudent());
    }

    @Test
    public void testValidateReview() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 1111);
        Review review = new Review(student, course, "Great course!", 5);
        courseReviewsService.register(student);
        Review validatedReview = courseReviewsService.validateReview(course, review.getComment(), String.valueOf(review.getRating()));
        assertEquals(review.getRating(), validatedReview.getRating());
        assertEquals(review.getComment(), validatedReview.getComment());
        assertEquals(review.getStudent(), validatedReview.getStudent());
        assertEquals(review.getCourse(), validatedReview.getCourse());
    }

    @Test
    public void testValidateReviewInvalidRating() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 1111);
        Review review = new Review(student, course, "Great course!", 5);
        courseReviewsService.register(student);
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.validateReview(course, review.getComment(), "6"));
    }

    @Test
    public void testValidateReviewBadRatingFormat() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 1111);
        Review review = new Review(student, course, "Great course!", 5);
        courseReviewsService.register(student);
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.validateReview(course, review.getComment(), "five"));
    }

    @Test
    public void testSubmitReview() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 1111);
        Review review = new Review(student, course, "Great course!", 5);
        courseReviewsService.register(student);
        courseReviewsService.submitReview(review);
        assertEquals(student, review.getStudent());
        assertEquals(course, review.getCourse());
        assertEquals("Great course!", review.getComment());
        assertEquals(5, review.getRating());
    }

    @Test
    public void testSubmitReviewAlreadySubmitted() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 1111);
        Review review = new Review(student, course, "Great course!", 5);
        courseReviewsService.register(student);
        courseReviewsService.submitReview(review);
        courseReviewsService.logout();
        courseReviewsService.login(student);
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.submitReview(review));
    }

    @Test
    public void testGetReviews() {
        Student student = new Student("Hello", "hello123");
        Student student2 = new Student("Bye", "bye123");
        Student student3 = new Student("Hi", "hi123");
        courseReviewsService.register(student);
        Course course = new Course("CS", 1234);
        Review review = new Review(student, course, "Great course!", 5);
        courseReviewsService.submitReview(review);
        courseReviewsService.logout();
        courseReviewsService.register(student2);
        Review review2 = new Review(student2, course, "I like it", 4);
        courseReviewsService.submitReview(review2);
        courseReviewsService.logout();
        courseReviewsService.register(student3);
        Review review3 = new Review(student3, course, "I don't like it", 2);
        courseReviewsService.submitReview(review3);
        courseReviewsService.logout();

        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        reviews.add(review2);
        reviews.add(review3);
        List<Review> actual = courseReviewsService.getReviews(course);
        for (int i = 0; i < reviews.size(); i++) {
            assertEquals(reviews.get(i).getStudent().getName(), actual.get(i).getStudent().getName());
            assertEquals(reviews.get(i).getCourse().getDepartment(), actual.get(i).getCourse().getDepartment());
            assertEquals(reviews.get(i).getCourse().getCatalogNumber(), actual.get(i).getCourse().getCatalogNumber());
            assertEquals(reviews.get(i).getComment(), actual.get(i).getComment());
            assertEquals(reviews.get(i).getRating(), actual.get(i).getRating());
        }
    }

    @Test
    public void testGetReviewsCourseDoesNotExist() {
        Course course = new Course("CS", 1234);
        assertThrows(RuntimeException.class, () -> courseReviewsService.getReviews(course));
    }

    @Test
    public void testGetReviewsNoReviews() {
        Course course = new Course("CS", 1234);
        courseManager.addCourse(course);
        assertNull(courseReviewsService.getReviews(course));
    }

    @Test
    public void testGetAverageRating() {
        Student student1 = new Student("Hello", "hello123");
        Student student2 = new Student("Bye", "bye123");
        Student student3 = new Student("Hi", "hi123");
        Course course = new Course("CS", 1234);
        Review review1 = new Review(student1, course, "Great course!", 5);
        Review review2 = new Review(student2, course, "I like it", 4);
        Review review3 = new Review(student3, course, "I don't like it", 2);
        studentManager.addStudent(student1);
        studentManager.addStudent(student2);
        studentManager.addStudent(student3);
        courseManager.addCourse(course);
        reviewManager.addReview(review1);
        reviewManager.addReview(review2);
        reviewManager.addReview(review3);
        assertEquals(3.7, courseReviewsService.getAverageRating(course));
    }


    @Test
    public void testGetAverageRatingNullReviews() {
        Course course = new Course("CS", 1234);
        courseManager.addCourse(course);
        assertEquals(0, courseReviewsService.getAverageRating(course));
    }

    @Test
    public void testValidateCourseName() {
        Course course = new Course("CS", 1234);
        assertEquals(course.getDepartment(), courseReviewsService.validateCourseName(course.getDepartment() + " " + String.valueOf(course.getCatalogNumber())).getDepartment());
        assertEquals(course.getCatalogNumber(), courseReviewsService.validateCourseName(course.getDepartment() + " " + String.valueOf(course.getCatalogNumber())).getCatalogNumber());
    }

    @Test
    public void testValidateCourseNameNoSpace() {
        Course course = new Course("CS", 1234);
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.validateCourseName(course.getDepartment() + String.valueOf(course.getCatalogNumber())));
    }

    @Test
    public void testValidateCourseNameInvalidDepartment() {
        Course course = new Course("CS", 1234);
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.validateCourseName("CSCCE 1234"));
    }

    @Test
    public void testValidateCourseNameInvalidCatalogNumber() {
        Course course = new Course("CS", 1234);
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.validateCourseName("CS 123"));
    }

    @Test
    public void testAddCourseCourseAlreadyExist() {
        Course course = new Course("CS", 1234);
        courseManager.addCourse(course);
        assertThrows(IllegalArgumentException.class, () -> courseReviewsService.addCourse(course));
    }
}