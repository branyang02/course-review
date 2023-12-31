package edu.virginia.cs.hw7.coursereviews;

import java.util.List;
import java.util.Scanner;
public class UserInterface {
    private final CourseReviewsService courseReviewsService;
    private final Scanner scanner;

    public UserInterface() {
        courseReviewsService = CourseReviewsService.getInstance();
        scanner = new Scanner(System.in);
    }

    public void startApplication() {
        while (true) {
            showLoginActions();
            String input = scanner.nextLine();
            switch (input) {
                case "1" -> loginUser();
                case "2" -> createNewUser();
                case "3" -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void showLoginActions() {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }

    private void createNewUser() {
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

        try {
            courseReviewsService.register(new Student(username, password));
            System.out.println("Registration successful!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loginUser() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        try {
            courseReviewsService.login(new Student(username, password));
            System.out.println("Login successful!");
            showMainMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("1. Submit a review");
            System.out.println("2. See reviews for a course");
            System.out.println("3. Logout");
            String input = scanner.nextLine();
            switch (input) {
                case "1" -> submitReview();
                case "2" -> seeReviews();
                case "3" -> {
                    logout();
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private void submitReview() {
        try {
            System.out.println("Enter the course name and number:");
            String classInfo = scanner.nextLine().trim();
            Course course = courseReviewsService.validateCourseName(classInfo);

            System.out.println("Enter your review:");
            String comment = scanner.nextLine();

            System.out.println("Enter your rating (1-5):");
            String rating = scanner.nextLine();

            Review review = courseReviewsService.validateReview(course, comment, rating);
            courseReviewsService.submitReview(review);
            System.out.println("Review submitted!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void seeReviews() {
        try {
        System.out.println("Enter the course name and number:");
        String className = scanner.nextLine().trim();
        Course course = courseReviewsService.validateCourseName(className);

        if (course == null) {
            System.out.println("Invalid course name.");
            return;
        }

        List<Review> reviews = courseReviewsService.getReviews(course);
        if (reviews.isEmpty()) {
            System.out.println("No reviews found.");
            return;
        }

        System.out.println("Reviews for " + course + ":");
        for (Review review : reviews) {
            System.out.println(review.toString());
        }

        System.out.println("Course Average " + courseReviewsService.getAverageRating(course) + "/5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void logout() {
        System.out.println("Logging out...");
        courseReviewsService.logout();
        startApplication();
    }


}
