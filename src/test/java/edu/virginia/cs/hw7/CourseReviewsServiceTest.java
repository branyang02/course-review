package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseReviewsServiceTest {
    private DatabaseManager db;
    private StudentManager studentManager;
    private CourseManager courseManager;
    private ReviewsManager reviewManager;
    private CourseReviewsService courseReviewsService;

    @BeforeEach
    public void setUp() {
        db = new DatabaseManager();
        studentManager = new StudentManager(db);
        courseManager = new CourseManager(db);
        reviewManager = new ReviewsManager(db);
        setupDatabase(db);
        courseReviewsService = CourseReviewsService.getInstance();
    }

    private void setupDatabase(DatabaseManager db) {
        db.connect();
        db.createTables();
        db.populateDatabase();
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
    public void testLogin() {
        Student student = new Student("Alice", "alice123");
        assertTrue(studentManager.checkStudent(student));
        courseReviewsService.login(student);
        assertEquals(student, courseReviewsService.getLoggedInStudent());
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

    // TODO: always reset database after testing
    // TODO: add other functions for 100% coverage
}