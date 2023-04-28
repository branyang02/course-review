package edu.virginia.cs.hw7.coursereviewgui;

import edu.virginia.cs.hw7.coursereviews.CourseReviewsService;
import edu.virginia.cs.hw7.coursereviews.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseReviewController {
    public PasswordField confirmPasswordField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;
    private final CourseReviewsService courseReviewsService;

    public CourseReviewController() {
        courseReviewsService = new CourseReviewsService();
    }

    public void loginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            courseReviewsService.login(new Student(username, password));
            System.out.println("Login successful!");
            // TODO: go to main menu with logged in user
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMessage.setText(e.getMessage());
        }
    }

    public void registerButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void registerStudent() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String password2 = confirmPasswordField.getText();

        if (!password.equals(password2)) {
            System.out.println("Passwords do not match.");
            errorMessage.setText("Passwords do not match.");
            return;
        }

        try {
            courseReviewsService.register(new Student(username, password));
            System.out.println("Registration successful!");
            // TODO: Go to main menu with logged in user
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMessage.setText(e.getMessage());
        }
    }
}
