package edu.virginia.cs.hw7.coursereviews;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.createTables();
        db.populateDatabase();
        db.disconnect();
    }
}
