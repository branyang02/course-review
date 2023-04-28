package edu.virginia.cs.hw7.coursereviewgui;

import edu.virginia.cs.hw7.coursereviews.CourseReviewsService;
import edu.virginia.cs.hw7.coursereviews.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CourseReviewController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;
    private CourseReviewsService courseReviewsService;

    public CourseReviewController() {
        courseReviewsService = new CourseReviewsService();
    }

    public void loginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            courseReviewsService.login(new Student(username, password));
            System.out.println("Login successful!");
            // TODO: go to main menu.
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMessage.setText(e.getMessage());
        }
    }
}
