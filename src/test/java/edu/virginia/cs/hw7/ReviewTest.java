package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.Course;
import edu.virginia.cs.hw7.coursereviews.Review;
import edu.virginia.cs.hw7.coursereviews.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewTest {

    private Review review;
    private Student student;
    private Course course;

    @BeforeEach
    public void setUp() {
        student = new Student("John Doe", "password123");
        course = new Course("CS", 101);
        review = new Review(student, course, "Great course!", 5);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(student, review.getStudent());
        assertEquals(course, review.getCourse());
        assertEquals("Great course!", review.getComment());
        assertEquals(5, review.getRating());
    }

    @Test
    public void testSetters() {
        Student newStudent = new Student("Jane Doe", "password456");
        Course newCourse = new Course("MATH", 201);
        review.setStudent(newStudent);
        review.setCourse(newCourse);
        review.setComment("Challenging but rewarding.");
        review.setRating(4);

        assertEquals(newStudent, review.getStudent());
        assertEquals(newCourse, review.getCourse());
        assertEquals("Challenging but rewarding.", review.getComment());
        assertEquals(4, review.getRating());
    }

    @Test
    public void testToString() {
        assertEquals("Review: Great course!\nRating: 5", review.toString());

        review.setComment("Challenging but rewarding.");
        review.setRating(4);
        assertEquals("Review: Challenging but rewarding.\nRating: 4", review.toString());
    }
}
