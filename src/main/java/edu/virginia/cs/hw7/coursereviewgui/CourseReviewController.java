package edu.virginia.cs.hw7.coursereviewgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CourseReviewController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}