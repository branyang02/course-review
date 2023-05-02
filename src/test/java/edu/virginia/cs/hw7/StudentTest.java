package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTest {

    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student("John Doe", "password123");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("John Doe", student.getName());
        assertEquals("password123", student.getPassword());
    }

    @Test
    public void testSetters() {
        student.setName("Jane Doe");
        student.setPassword("newpassword");

        assertEquals("Jane Doe", student.getName());
        assertEquals("newpassword", student.getPassword());
    }

    @Test
    public void testToString() {
        assertEquals("Student: John Doe", student.toString());

        student.setName("Jane Doe");
        assertEquals("Student: Jane Doe", student.toString());
    }
}
