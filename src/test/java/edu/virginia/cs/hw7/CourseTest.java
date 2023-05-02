package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseTest {

    private Course course;

    @BeforeEach
    public void setUp() {
        course = new Course("CS", 101);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("CS", course.getDepartment());
        assertEquals(101, course.getCatalogNumber());
    }

    @Test
    public void testSetters() {
        course.setDepartment("MATH");
        course.setCatalogNumber(201);

        assertEquals("MATH", course.getDepartment());
        assertEquals(201, course.getCatalogNumber());
    }

    @Test
    public void testToString() {
        assertEquals("Course: CS 101", course.toString());

        course.setDepartment("MATH");
        course.setCatalogNumber(201);
        assertEquals("Course: MATH 201", course.toString());
    }
}
