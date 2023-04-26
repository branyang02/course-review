package edu.virginia.cs.hw7.coursereviews;

import java.util.Scanner;
public class UserInterface {

    private final StudentManager studentManager;
    private final CourseManager classManager;
    private final ReviewsManager reviewManager;

    public UserInterface() {
        DatabaseManager db = new DatabaseManager();
        studentManager = new StudentManager(db);
        classManager = new CourseManager(db);
        reviewManager = new ReviewsManager(db);
    }
    public void showLoginActions() {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }

    private void createNewUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        System.out.println("Enter your password again:");
        String password2 = scanner.nextLine();
        if (!password.equals(password2)) {
            System.out.println("Passwords do not match.");
            return;
        }
        if (studentManager.register(new Student(username, password))) {
            System.out.println("Registration successful!");
            showMainMenu();
        } else {
            System.out.println("Registration failed. There is already a user with that name.");
        }
    }
    private void loginUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        if (studentManager.login(new Student(username, password))) {
            System.out.println("Login successful!");
            showMainMenu();
        } else {
            System.out.println("Login failed. Incorrect username or password.");
        }
    }
    private void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            System.out.println("1. Submit a review");
            System.out.println("2. See reviews for a course");
            System.out.println("3. Logout");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    submitReview();
                    break;
                case "2":
                    seeReviews();
                    break;
                case "3":
                    logout();
                    break label;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }
    }
    private void submitReview() { /* ... */ }
    private String[] getCourseName(String input) { /* ... */
        return new String[0];
    }
    private void seeReviews() { /* ... */ }
    private void printCourseReviews(String subject, String catalogNumber) { /* ... */ }
    private void logout() { /* ... */ }

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            ui.showLoginActions();
            String input = scanner.nextLine();
            if (input.equals("1")) {
                ui.loginUser();
            } else if (input.equals("2")) {
                ui.createNewUser();
            } else if (input.equals("3")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }
}
