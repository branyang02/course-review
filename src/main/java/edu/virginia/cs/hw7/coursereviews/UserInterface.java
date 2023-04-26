package edu.virginia.cs.hw7.coursereviews;

import java.util.Scanner;
public class UserInterface {
    public final CourseReviews courseReviews;
    public UserInterface() {
        courseReviews = new CourseReviews();
    }
    private void showLoginActions() {
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
        if (courseReviews.register(new Student(username, password))) {
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
        if (courseReviews.login(new Student(username, password))) {
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
    private void submitReview() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the course name and number:");
        String className = scanner.nextLine().trim();
        String[] courseInfo = getCourseInfo(className);
        System.out.println("Enter your review:");
        String review = scanner.nextLine();
        System.out.println("Enter your rating (1-5):");
        int rating = scanner.nextInt();
//        if (reviewManager.submitReview(new Review(new Student(), new Course(courseInfo[0], courseInfo[1]), review, rating))) {
//            System.out.println("Review submitted successfully!");
//        } else {
//            System.out.println("Review submission failed. The course does not exist.");
//        }
    }
    private String[] getCourseInfo(String input) {
        try {
            String[] courseInfo = input.split(" ");
            String subject = courseInfo[0];
            if (subject.length() < 2 || subject.length() > 4) {
                System.out.println("Invalid course name");
                return null;
            }
            String catalogNumber = courseInfo[1];
            return new String[]{subject, catalogNumber};
        } catch (Exception e) {
            System.out.println("Invalid course name and number.");
            return null;
        }
    }
    private void seeReviews() { /* ... */ }
    private void printCourseReviews(String subject, String catalogNumber) { /* ... */ }
    private void logout() {
        System.out.println("Logging out...");
        startApplication();
    }

    public static void startApplication() {
        UserInterface ui = new UserInterface();
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            ui.showLoginActions();
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    ui.loginUser();
                    break;
                case "2":
                    ui.createNewUser();
                    break;
                case "3":
                    break label;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        startApplication();
    }
}
