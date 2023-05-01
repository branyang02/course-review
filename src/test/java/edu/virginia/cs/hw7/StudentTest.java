package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.Student;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class StudentTest {

    @Test
    public void testGetName() {
        String name = "John Doe";
        String password = "password";
        Student student = new Student(name, password);
        assertEquals(name, student.getName());
    }

    @Test
    public void testGetPassword() {
        String name = "John Doe";
        String password = "password";
        Student student = new Student(name, password);
        assertEquals(password, student.getPassword());
    }

    @Test
    public void testSetName() {
        String name = "John Doe";
        String password = "password";
        Student student = new Student(name, password);
        String newName = "Jane Doe";
        student.setName(newName);
        assertEquals(newName, student.getName());
    }

    @Test
    public void testSetPassword() {
        String name = "John Doe";
        String password = "password";
        Student student = new Student(name, password);
        String newPassword = "newpassword";
        student.setPassword(newPassword);
        assertEquals(newPassword, student.getPassword());
    }

    @Test
    public void testToString() {
        String name = "John Doe";
        String password = "password";
        Student student = new Student(name, password);
        assertEquals("Student: " + name, student.toString());
    }
}
