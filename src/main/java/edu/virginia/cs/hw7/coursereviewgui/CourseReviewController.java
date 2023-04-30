package edu.virginia.cs.hw7.coursereviewgui;

import edu.virginia.cs.hw7.coursereviews.Course;
import edu.virginia.cs.hw7.coursereviews.CourseReviewsService;
import edu.virginia.cs.hw7.coursereviews.Review;
import edu.virginia.cs.hw7.coursereviews.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CourseReviewController {
    public PasswordField confirmPasswordField;
    @FXML
    public TextField courseNameField;
    @FXML
    public TextField reviewField;
    @FXML
    public TextField ratingField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;
    private final CourseReviewsService courseReviewsService;
    @FXML
    public VBox reviewsContainer;

    public CourseReviewController() {
        courseReviewsService = CourseReviewsService.getInstance();
    }

    public void loginButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Student currentStudent = new Student(username, password);
            courseReviewsService.login(currentStudent);

            loadNewScene("MainMenu.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
            errorMessage.setText("Passwords do not match.");
            return;
        }

        try {
            courseReviewsService.register(new Student(username, password));

            loadNewScene("MainMenu.fxml", event);
        } catch (IOException e) {
           e.printStackTrace();
        }catch (Exception e) {
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
        String courseName = courseNameField.getText();
        String comment = reviewField.getText();
        String rating = ratingField.getText();
        Course course;
        Review review;

        try {
            course = courseReviewsService.validateCourseName(courseName);
        } catch (IllegalArgumentException e) {
            errorMessage.setText(e.getMessage());
            return;
        }
        try {
            review = courseReviewsService.validateReview(course, comment, rating);
        } catch (IllegalArgumentException e) {
            errorMessage.setText(e.getMessage());
            return;
        }

        try {
            courseReviewsService.submitReview(review);

            loadNewScene("MainMenu.fxml", event);
        } catch (IllegalArgumentException e) {
            errorMessage.setText(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seeReviews(ActionEvent event) {
        String courseName = courseNameField.getText();
        Course course;
        Label label;
        reviewsContainer = new VBox();

        try {
            course = courseReviewsService.validateCourseName(courseName);
        } catch (IllegalArgumentException e) {
            errorMessage.setText(e.getMessage());
            return;
        }
        List<Review> reviews = courseReviewsService.getReviews(course);
        if (reviews.isEmpty()) {
            errorMessage.setText("No reviews found");
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowReviews.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reviewsContainer = loader.getRoot();
        int count = 0;
        double rating = 0.0;
        for (Review review : reviews) {
            count++;
            label = new Label();
            label.setText(count + ". " + review.getComment() + " " + review.getRating() + "/5");
            label.setFont(Font.font(24));
            label.setPadding(new Insets(5));
            label.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 10px;");
            reviewsContainer.getChildren().add(label);
            rating += review.getRating();
        }
        label = new Label("Course Average: " + rating/count + "/5");
        label.setFont(Font.font(24));
        label.setPadding(new Insets(5));
        label.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10px;");
        reviewsContainer.getChildren().add(label);

    }
}
