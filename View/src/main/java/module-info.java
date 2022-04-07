module project.view {
    requires javafx.controls;
    requires javafx.fxml;
    opens project.view to javafx.fxml;
    requires Model;
    requires org.jfree.jfreechart;
    requires java.desktop;
    exports project.view;
}