package edu.virginia.cs.hw7.coursereviews;

import java.sql.*;

public class CourseManager {
    private final DatabaseManager db;

    public CourseManager(DatabaseManager db) {
        this.db = db;
    }

    public void addCourse(Course course) {
        db.connect();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "SELECT * FROM courses WHERE department = ? AND catalog_number = ?");
            statement.setString(1, course.getDepartment());
            statement.setInt(2, course.getCatalogNumber());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Course already exists in the database.");
            } else {
                statement = db.connection.prepareStatement(
                        "INSERT INTO courses (department, catalog_number) VALUES (?, ?)");
                statement.setString(1, course.getDepartment());
                statement.setInt(2, course.getCatalogNumber());
                statement.executeUpdate();
                db.connection.commit();
                System.out.println("Course added to the database.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    public void clearCourses() {
        db.connect();
        try {
            Statement statement = db.connection.createStatement();
            statement.executeUpdate("DELETE FROM courses");
            db.connection.commit();
            System.out.println("All courses have been deleted from the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    public Course getCourse(String courseID) {
        db.connect();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "SELECT * FROM courses WHERE id = ?");
            statement.setString(1, courseID);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Course(result.getString("department"), result.getInt("catalog_number"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }
}
