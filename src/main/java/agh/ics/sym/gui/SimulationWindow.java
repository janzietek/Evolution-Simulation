package agh.ics.sym.gui;

import agh.ics.sym.engine.*;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;


public class SimulationWindow extends BorderPane {
    MapVisualizer map1;
    MapVisualizer map2;
    SimulationEngine engine1;
    SimulationEngine engine2;
    Thread simulation1;
    Thread simulation2;


    public SimulationWindow (SimulationSettings settings, boolean magic1, boolean magic2) {
        try {
            if (magic1)
                this.engine1 = new MagicSimulationEngine(settings, false);
            else
                this.engine1 = new SimulationEngine(settings, false);
            if (magic2)
                this.engine2 = new MagicSimulationEngine(settings, true);
            else
                this.engine2 = new SimulationEngine(settings, true);

            this.map1 = new MapVisualizer(engine1);
            this.map2 = new MapVisualizer(engine2);

            engine1.addObserver(map1);
            engine2.addObserver(map2);

            this.setLeft(map1);
            this.setRight(map2);
            setButtons();

            this.simulation1 = new Thread(engine1);
            this.simulation2 = new Thread(engine2);

            simulation1.start();
            simulation2.start();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        }
    }

    private void setButtons () {
        HBox utilityField = new HBox();
        Button pauseStartButton1 = new Button("Pause");
        Button pauseStartButton2 = new Button("Pause");

        pauseStartButton1.setOnAction(e -> {
            if (this.engine1.isRunning) {
                pauseStartButton1.setText("Start");
            }
            else {
                pauseStartButton1.setText("Pause");
            }
            engine1.changeSimulationStatus();
        });

        pauseStartButton2.setOnAction(e -> {
            if (this.engine2.isRunning) {
                pauseStartButton2.setText("Start");
            }
            else {
                pauseStartButton2.setText("Pause");
            }
            engine2.changeSimulationStatus();
        });

        utilityField.getChildren().addAll(pauseStartButton1, pauseStartButton2);
        this.setCenter(utilityField);
    }
}
