package edu.virginia.cs.hw7.coursereviews;

import java.sql.*;

public class StudentManager {
    private final DatabaseManager db;

    public StudentManager(DatabaseManager db) {
        this.db = db;
    }

    public void addStudent(Student student) {
        db.connect();
        try {
            PreparedStatement selectStatement = db.connection.prepareStatement(
                    "SELECT * FROM students WHERE NAME = ?");
            selectStatement.setString(1, student.getName());
            ResultSet result = selectStatement.executeQuery();
            if (result.next()) {
                // Student with the same name already exists in students table
                String existingPassword = result.getString("PASSWORD");
                if (!existingPassword.equals(student.getPassword())) {
                    // Update the password of the existing student
                    PreparedStatement updateStatement = db.connection.prepareStatement(
                            "UPDATE students SET PASSWORD = ? WHERE NAME = ?");
                    updateStatement.setString(1, student.getPassword());
                    updateStatement.setString(2, student.getName());
                    updateStatement.executeUpdate();
                    db.connection.commit();
                    System.out.println("Student password updated.");
                } else {
                    System.out.println("Student already exists in the database.");
                }
            } else {
                // Insert a new student into the database
                PreparedStatement insertStatement = db.connection.prepareStatement(
                        "INSERT INTO students (NAME, PASSWORD) VALUES (?, ?)");
                insertStatement.setString(1, student.getName());
                insertStatement.setString(2, student.getPassword());
                insertStatement.executeUpdate();
                db.connection.commit();
                System.out.println("Student added to the database.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }


    public void clearStudents() {
        db.connect();
        try {
            Statement statement = db.connection.createStatement();
            statement.executeUpdate("DELETE FROM students");
            db.connection.commit();
            System.out.println("All students have been deleted from the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.disconnect();
        }
    }
}
