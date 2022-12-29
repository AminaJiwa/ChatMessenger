module chatmessenger {
    requires javafx.controls;
    requires javafx.fxml;

    opens chatmessenger to javafx.fxml;
    exports chatmessenger;
}
