package agh.ics.sym.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SimulationApp extends Application{
    static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setOnCloseRequest(e -> window.close());
        Scene scene = new Scene(new ParametersWindow());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void setScene(Scene scene) {
        window.setScene(scene);
    }

}
