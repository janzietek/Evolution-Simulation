package agh.ics.sym.gui;

import agh.ics.sym.engine.*;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;


public class SimulationWindow extends BorderPane{
    MapVisualizer map1;
    MapVisualizer map2;
    SimulationEngine engine1;
    SimulationEngine engine2;
    Thread simulation1;
    Thread simulation2;
    Button pauseStartButton1;
    Button pauseStartButton2;
    DominantGenotypeLabel dominant1;
    DominantGenotypeLabel dominant2;


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
            this.dominant1 = new DominantGenotypeLabel(engine1);
            this.dominant2 = new DominantGenotypeLabel(engine2);

            engine1.addObserver(map1);
            engine2.addObserver(map2);
            engine1.addObserver(dominant1);
            engine2.addObserver(dominant2);

            this.pauseStartButton1 = new Button("Click to start simulation");
            this.pauseStartButton2 = new Button("Click to start simulation");

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

            VBox left = new VBox();
            left.getChildren().addAll(pauseStartButton1, map1, dominant1);
            this.setLeft(left);

            VBox right = new VBox();
            right.getChildren().addAll(pauseStartButton2, map2, dominant2);
            this.setRight(right);

            VBox center = new VBox();

            HBox charts = new HBox();
            charts.getChildren().addAll(new SimulationCharts(engine1), new SimulationCharts(engine2));
            center.getChildren().add(charts);
            this.setCenter(center);

            this.simulation1 = new Thread(engine1);
            this.simulation2 = new Thread(engine2);

            simulation1.start();
            simulation2.start();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        }
    }

}
