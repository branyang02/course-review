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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    @FXML
    private Label feedbackMessage;
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
        ScrollPane scrollPane = new ScrollPane();
        VBox scrollContent = new VBox();

        try {
            course = courseReviewsService.validateCourseName(courseName);
        } catch (IllegalArgumentException e) {
            errorMessage.setText(e.getMessage());
            return;
        }

        List<Review> reviews;

        try {
            reviews = courseReviewsService.getReviews(course);
        } catch (RuntimeException e) {
            errorMessage.setText("No reviews found");
            return;
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
        for (Review review : reviews) {
            count++;
            label = new Label();
            label.setText(count + ". " + review.getComment() + " " + review.getRating() + "/5");
            label.setFont(Font.font(18));
            label.setPadding(new Insets(5));
//            label.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 10px;");
            label.setStyle("-fx-text-fill: black");
            label.setWrapText(true);
            scrollContent.getChildren().add(label);
        }
        scrollContent.setAlignment(Pos.CENTER);
        scrollContent.setSpacing(10);

        scrollPane.setContent(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(800,600);
        reviewsContainer.getChildren().add(scrollPane);

        label = new Label("Course Average: " + courseReviewsService.getAverageRating(course) + "/5");
        label.setFont(Font.font(24));
        label.setPadding(new Insets(5));
//        label.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10px;");
        label.setStyle("-fx-text-fill: black");
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(label, createStarContainer(course));

        reviewsContainer.getChildren().add(hbox);

        reviewsContainer.setPadding(new Insets(0, 0, 20, 0));
    }

    private HBox createStarContainer(Course course) {
        HBox starContainer = new HBox();
        starContainer.setSpacing(3);
        starContainer.setAlignment(Pos.CENTER);
        ImageView star1 = new ImageView();
        star1.setId("star1");
        star1.setFitHeight(30);
        star1.setFitWidth(30);
        star1.setImage(new Image(Objects.requireNonNull(getClass().getResource("empty-star.png")).toString()));

        ImageView star2 = new ImageView();
        star2.setId("star2");
        star2.setFitHeight(30);
        star2.setFitWidth(30);
        star2.setImage(new Image(Objects.requireNonNull(getClass().getResource("empty-star.png")).toString()));

        ImageView star3 = new ImageView();
        star3.setId("star3");
        star3.setFitHeight(30);
        star3.setFitWidth(30);
        star3.setImage(new Image(Objects.requireNonNull(getClass().getResource("empty-star.png")).toString()));

        ImageView star4 = new ImageView();
        star4.setId("star4");
        star4.setFitHeight(30);
        star4.setFitWidth(30);
        star4.setImage(new Image(Objects.requireNonNull(getClass().getResource("empty-star.png")).toString()));

        ImageView star5 = new ImageView();
        star5.setId("star5");
        star5.setFitHeight(30);
        star5.setFitWidth(30);
        star5.setImage(new Image(Objects.requireNonNull(getClass().getResource("empty-star.png")).toString()));

        starContainer.getChildren().addAll(star1, star2, star3, star4, star5);

        double rating = courseReviewsService.getAverageRating(course);

        if (rating >= 0.5) {
            star1.setImage(new Image(Objects.requireNonNull(getClass().getResource("half-star.png")).toString()));
        }
        if (rating >= 1) {
            star1.setImage(new Image(Objects.requireNonNull(getClass().getResource("full-star.png")).toString()));
        }
        if (rating >= 1.5) {
            star2.setImage(new Image(Objects.requireNonNull(getClass().getResource("half-star.png")).toString()));
        }
        if (rating >= 2) {
            star2.setImage(new Image(Objects.requireNonNull(getClass().getResource("full-star.png")).toString()));
        }
        if (rating >= 2.5) {
            star3.setImage(new Image(Objects.requireNonNull(getClass().getResource("half-star.png")).toString()));
        }
        if (rating >= 3) {
            star3.setImage(new Image(Objects.requireNonNull(getClass().getResource("full-star.png")).toString()));
        }
        if (rating >= 3.5) {
            star4.setImage(new Image(Objects.requireNonNull(getClass().getResource("half-star.png")).toString()));
        }
        if (rating >= 4) {
            star4.setImage(new Image(Objects.requireNonNull(getClass().getResource("full-star.png")).toString()));
        }
        if (rating >= 4.5) {
            star5.setImage(new Image(Objects.requireNonNull(getClass().getResource("half-star.png")).toString()));
        }
        if (rating >= 5) {
            star5.setImage(new Image(Objects.requireNonNull(getClass().getResource("full-star.png")).toString()));
        }


        return starContainer;
    }


}
