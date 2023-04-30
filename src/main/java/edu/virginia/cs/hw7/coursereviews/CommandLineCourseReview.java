package edu.virginia.cs.hw7.coursereviews;

import edu.virginia.cs.hw7.coursereviewgui.CourseReviewApplication;

public class CommandLineCourseReview {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        if (args.length > 0) {
            String flag = args[0];
            if (flag.equals("-g") || flag.equals("--gui")) {
                CourseReviewApplication.main(args);
                return;
            } else if (flag.equals("-c") || flag.equals("--command")) {
                ui.startApplication();
                return;
            }
        }
        System.err.println("Invalid flag. Usage: java -jar CourseReview.jar [-g|--gui|-c|--command]");
    }
}
