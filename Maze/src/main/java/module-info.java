module com.musatov.maze.model {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens com.musatov.maze to javafx.fxml, javafx.graphics;
    opens com.musatov.maze.view to javafx.fxml;
    exports com.musatov.maze.model;
}