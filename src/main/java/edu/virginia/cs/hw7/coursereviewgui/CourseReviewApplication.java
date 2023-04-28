package edu.virginia.cs.hw7.coursereviewgui;

import edu.virginia.cs.hw7.coursereviews.CommandLineCourseReview;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseReviewApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Course Review");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        if (args.length > 0) {
            String arg = args[0].toLowerCase();
            if (arg.equals("--command") || arg.equals("-c")) {
                CommandLineCourseReview.main(args);
                return;
            }
        }

        launch();
    }
}