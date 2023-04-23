package edu.virginia.cs.hw7.coursereviews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public static final String DB_NAME = "Reviews.sqlite3";
    Connection connection;

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ClassCastException("Database not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConnection() {
        if (connection == null) {
            throw new RuntimeException("Not connected to database");
        }
    }

    private boolean doesTableExist(String tableName) {
        checkConnection();
        try {
            return connection.getMetaData().getTables(null, null, tableName, null).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTables() {
        checkConnection();
        try {
            Statement statement = connection.createStatement();
            String createStudentsTable = "CREATE TABLE IF NOT EXISTS students ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT NOT NULL,"
                    + "PASSWORD TEXT NOT NULL)";
            statement.executeUpdate(createStudentsTable);

            String createCoursesTable = "CREATE TABLE IF NOT EXISTS courses ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "DEPARTMENT TEXT NOT NULL,"
                    + "CATALOG_NUMBER INTEGER NOT NULL)";
            statement.executeUpdate(createCoursesTable);

            String createReviewsTable = "CREATE TABLE IF NOT EXISTS reviews ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "STUDENT_ID INTEGER NOT NULL,"
                    + "COURSE_ID INTEGER NOT NULL,"
                    + "RATING INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),"
                    + "COMMENT TEXT NOT NULL,"
                    + "FOREIGN KEY(STUDENT_ID) REFERENCES students(ID) ON DELETE CASCADE,"
                    + "FOREIGN KEY(COURSE_ID) REFERENCES courses(ID) ON DELETE CASCADE)";
            statement.executeUpdate(createReviewsTable);

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.createTables();
        db.disconnect();
    }
}
