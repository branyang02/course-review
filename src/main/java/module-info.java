module edu.virginia.cs.hw7.coursereviewgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens edu.virginia.cs.hw7.coursereviewgui to javafx.fxml;
    exports edu.virginia.cs.hw7.coursereviewgui;
}