package edu.virginia.cs.hw7.coursereviews;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewsManager {
    private final DatabaseManager db;

    public ReviewsManager(DatabaseManager db) {
        this.db = db;
    }

    public void addReview(Review review) {
        int studentID = getStudentID(review.getStudent());
        int courseID = getCourseID(review.getCourse());
        db.connect();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "INSERT INTO reviews (student_id, course_id, rating, comment) VALUES (?, ?, ?, ?)");
            statement.setInt(1, studentID);
            statement.setInt(2, courseID);
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.executeUpdate();
            db.connection.commit();
            System.out.println("Review added to the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    public void removeReview(Review review) {
        int studentID = getStudentID(review.getStudent());
        int courseID = getCourseID(review.getCourse());
        db.connect();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "DELETE FROM reviews WHERE student_id = ? AND course_id = ?");
            statement.setInt(1, studentID);
            statement.setInt(2, courseID);
            statement.executeUpdate();
            db.connection.commit();
            System.out.println("Review removed from the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    public void clearReviews() {
        db.connect();
        try {
            Statement statement = db.connection.createStatement();
            statement.executeUpdate("DELETE FROM reviews");
            db.connection.commit();
            System.out.println("All reviews have been deleted from the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    private int getStudentID(Student student) {
        db.connect();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "SELECT ID FROM students WHERE NAME = ?");
            statement.setString(1, student.getName());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("ID");
            } else {
                throw new RuntimeException("Student not found: " + student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    private int getCourseID(Course course) {
        db.connect();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "SELECT ID FROM courses WHERE DEPARTMENT = ? AND CATALOG_NUMBER = ?");
            statement.setString(1, course.getDepartment());
            statement.setInt(2, course.getCatalogNumber());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("ID");
            } else {
                throw new RuntimeException("Course not found: " + course);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    public ResultSet getReviews(Course course) {
        db.connect();
        List<Review> reviews = new ArrayList<>();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "SELECT * FROM reviews WHERE course_id = ?");
            statement.setInt(1, getCourseID(course));
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }

    public boolean checkReview(Review review) {
        db.connect();
        try {
            PreparedStatement statement = db.connection.prepareStatement(
                    "SELECT * FROM reviews WHERE student_id = ? AND course_id = ?");
            statement.setInt(1, getStudentID(review.getStudent()));
            statement.setInt(2, getCourseID(review.getCourse()));
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }
}