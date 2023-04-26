package edu.virginia.cs.hw7.coursereviews;

import java.util.Scanner;
public class UserInterface {

    private StudentManager studentManager;
    private CourseManager classManager;
    private ReviewsManager reviewManager;

    public UserInterface() {
        studentManager = new StudentManager("reviews.sqlite3");
        classManager = new CourseManager("reviews.sqlite3");
        reviewManager = new ReviewsManager("reviews.sqlite3");
    }
    public void showLoginActions() {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }

    private void createNewUser() { /* ... */ }
    private void loginUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        if (studentManager.login(username, password)) {
            System.out.println("Login successful!");
            showMainMenu();
        } else {
            System.out.println("Login failed.");
        }
    }
    private void showMainMenu() { /* ... */ }
    private void submitReview() { /* ... */ }
    private String[] getCourseName(String input) { /* ... */
        return new String[0];
    }
    private void seeReviews() { /* ... */ }
    private void printCourseReviews(String subject, String catalogNumber) { /* ... */ }
    private void logout() { /* ... */ }

}
