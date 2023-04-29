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
    public TextField courseNameField;
    public TextField reviewField;
    public TextField ratingField;
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

    public void loginButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            courseReviewsService.login(new Student(username, password));
            System.out.println("Login successful!");

            loadNewScene("MainMenu.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMessage.setText(e.getMessage());
        }
    }

    public void registerButtonClicked(ActionEvent event) {
        try {
            loadNewScene("Register.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void registerStudent(ActionEvent event) {
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

            loadNewScene("MainMenu.fxml", event);
        } catch (IOException e) {
           e.printStackTrace();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            errorMessage.setText(e.getMessage());
        }
    }

    private void loadNewScene(String name, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void goSubmitAReview(ActionEvent actionEvent) {
        try {
            loadNewScene("SubmitReview.fxml", actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAReview(ActionEvent actionEvent) {
        try {
            loadNewScene("CheckReviews.fxml", actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut(ActionEvent actionEvent) {
        try {
            loadNewScene("Login.fxml", actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackToLogin(ActionEvent event) {
        try {
            loadNewScene("Login.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackToMainMenu(ActionEvent event) {
        try {
            loadNewScene("MainMenu.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void submitAReview(ActionEvent event) {
    }

    public void seeReviews(ActionEvent event) {
    }
}
