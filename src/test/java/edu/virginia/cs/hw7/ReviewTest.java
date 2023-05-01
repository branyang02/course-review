package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.Course;
import edu.virginia.cs.hw7.coursereviews.Review;
import edu.virginia.cs.hw7.coursereviews.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReviewTest {

    @Test
    public void testGetStudent() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        Assertions.assertEquals(student, review.getStudent());
    }

    @Test
    public void testGetCourse() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        Assertions.assertEquals(course, review.getCourse());
    }

    @Test
    public void testGetComment() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        Assertions.assertEquals(comment, review.getComment());
    }

    @Test
    public void testGetRating() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        Assertions.assertEquals(rating, review.getRating());
    }

    @Test
    public void testSetStudent() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        Student newStudent = new Student("Jane Doe", "newpassword");
        review.setStudent(newStudent);
        Assertions.assertEquals(newStudent, review.getStudent());
    }

    @Test
    public void testSetCourse() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        Course newCourse = new Course("ECE", 1500);
        review.setCourse(newCourse);
        Assertions.assertEquals(newCourse, review.getCourse());
    }

    @Test
    public void testSetComment() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        String newComment = "This course was amazing!";
        review.setComment(newComment);
        Assertions.assertEquals(newComment, review.getComment());
    }

    @Test
    public void testSetRating() {
        Student student = new Student("John Doe", "password");
        Course course = new Course("CS", 2150);
        String comment = "This course was great!";
        int rating = 5;
        Review review = new Review(student, course, comment, rating);
        int newRating = 4;
        review.setRating(newRating);
        Assertions.assertEquals(newRating, review.getRating());
    }
}