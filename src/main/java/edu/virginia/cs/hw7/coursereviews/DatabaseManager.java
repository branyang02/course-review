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

    public void populateDatabase() {
        DatabaseManager db = new DatabaseManager();
        db.connect();

        CourseManager courseManager = new CourseManager(db);
        StudentManager studentManager = new StudentManager(db);
        ReviewsManager reviewsManager = new ReviewsManager(db);

        // Clear database
        reviewsManager.clearReviews();
        studentManager.clearStudents();
        courseManager.clearCourses();

        // Add courses
        Course cs2100 = new Course("CS", 2100);
        Course cs2110 = new Course("CS", 2110);
        Course cs2150 = new Course("CS", 2150);
        courseManager.addCourse(cs2100);
        courseManager.addCourse(cs2110);
        courseManager.addCourse(cs2150);

        // Add students
        Student alice = new Student("Alice", "alice123");
        Student bob = new Student("Bob", "bob456");
        Student charlie = new Student("Charlie", "charlie789");
        studentManager.addStudent(alice);
        studentManager.addStudent(bob);
        studentManager.addStudent(charlie);

        // Add reviews
        Review review1 = new Review(alice, cs2100, "Great course, highly recommend!", 5);
        Review review2 = new Review(bob, cs2100, "Not too bad, but not great either.", 3);
        Review review3 = new Review(charlie, cs2100, "Terrible course, do not take!", 1);
        Review review4 = new Review(alice, cs2110, "Very challenging, but learned a lot.", 4);
        Review review5 = new Review(bob, cs2110, "Lectures were boring, but assignments were interesting.", 3);
        Review review6 = new Review(charlie, cs2150, "This course was a complete waste of time.", 1);

        reviewsManager.addReview(review1);
        reviewsManager.addReview(review2);
        reviewsManager.addReview(review3);
        reviewsManager.addReview(review4);
        reviewsManager.addReview(review5);
        reviewsManager.addReview(review6);

        db.disconnect();

    }
}
