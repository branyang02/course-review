package edu.virginia.cs.hw7;

import edu.virginia.cs.hw7.coursereviews.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CourseTest {

    @Test
    public void testGetDepartment() {
        String department = "CS";
        int catalogNumber = 2150;
        Course course = new Course(department, catalogNumber);
        Assertions.assertEquals(department, course.getDepartment());
    }

    @Test
    public void testGetCatalogNumber() {
        String department = "CS";
        int catalogNumber = 2150;
        Course course = new Course(department, catalogNumber);
        Assertions.assertEquals(catalogNumber, course.getCatalogNumber());
    }

    @Test
    public void testSetDepartment() {
        String department = "CS";
        int catalogNumber = 2150;
        Course course = new Course(department, catalogNumber);
        String newDepartment = "ECE";
        course.setDepartment(newDepartment);
        Assertions.assertEquals(newDepartment, course.getDepartment());
    }

    @Test
    public void testSetCatalogNumber() {
        String department = "CS";
        int catalogNumber = 2150;
        Course course = new Course(department, catalogNumber);
        int newCatalogNumber = 2160;
        course.setCatalogNumber(newCatalogNumber);
        Assertions.assertEquals(newCatalogNumber, course.getCatalogNumber());
    }

    @Test
    public void testToString() {
        String department = "CS";
        int catalogNumber = 2150;
        Course course = new Course(department, catalogNumber);
        Assertions.assertEquals("Course: " + department + " " + catalogNumber, course.toString());
    }
}
