module se233.project2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.logging;
    requires org.apache.logging.log4j;

    opens se233.project2 to javafx.fxml;
    exports se233.project2;
    exports se233.project2.Boss;
    opens se233.project2.Boss to javafx.fxml;
    exports se233.project2.Player;
    opens se233.project2.Player to javafx.fxml;
    exports se233.project2.Enemy;
    opens se233.project2.Enemy to javafx.fxml;
    exports se233.project2.Character;
    opens se233.project2.Character to javafx.fxml;
}