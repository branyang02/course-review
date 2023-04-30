package edu.virginia.cs.hw7.coursereviews;

import edu.virginia.cs.hw7.coursereviewgui.CourseReviewApplication;

public class CommandLineCourseReview {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        if (args.length > 0 && (args[0].equals("-c") || args[0].equals("--command"))) {
            ui.startApplication();
        } else {
            CourseReviewApplication.main(args);
        }
    }
}
