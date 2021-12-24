package agh.ics.sym.gui;

import agh.ics.sym.engine.SimulationSettings;
import agh.ics.sym.gui.SimulationApp;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ParametersWindow extends GridPane {
    SimulationSettings settings;

    public ParametersWindow() {
        this.setPadding(new Insets(10));
        this.setVgap(8);
        this.setHgap(10);


        Label widthLabel = new Label("Width: ");
        TextField mapWidth = new TextField("10");
        setConstraints(widthLabel, 0, 0);
        setConstraints(mapWidth, 1, 0);

        Label heightLabel = new Label("Height: ");
        TextField mapHeight = new TextField("10");
        setConstraints(heightLabel, 0, 1);
        setConstraints(mapHeight, 1, 1);

        Label ratioLabel = new Label("Jungle ratio: ");
        TextField mapJungleRatio = new TextField("0.16");
        setConstraints(ratioLabel, 0, 2);
        setConstraints(mapJungleRatio, 1, 2);

        Label startEnergyLabel = new Label("Start energy: ");
        TextField animalStartEnergy = new TextField("30");
        setConstraints(startEnergyLabel, 0, 3);
        setConstraints(animalStartEnergy, 1, 3);

        Label moveEnergyLabel = new Label("Move energy: ");
        TextField animalMoveEnergy = new TextField("2");
        setConstraints(moveEnergyLabel, 0, 4);
        setConstraints(animalMoveEnergy, 1, 4);

        Label plantEnergyLabel = new Label("Plant energy: ");
        TextField plantEnergy = new TextField("20");
        setConstraints(plantEnergyLabel, 0, 5);
        setConstraints(plantEnergy, 1, 5);

        Label adamsAndEvesLabel = new Label("Adams and Eves number: ");
        TextField adamsAndEves = new TextField("10");
        setConstraints(adamsAndEvesLabel, 0, 6);
        setConstraints(adamsAndEves, 1, 6);

        Button startButton = new Button("Start simulation");
        setConstraints(startButton, 0, 7);

        startButton.setOnAction(e -> {
            setSettings(mapWidth, mapHeight, mapJungleRatio, animalStartEnergy, animalMoveEnergy, plantEnergy, adamsAndEves);
            Scene scene = new Scene(new SimulationWindow(this.settings));
            SimulationApp.setScene(scene);
        });

        this.getChildren().addAll(
                widthLabel,
                mapWidth,
                heightLabel,
                mapHeight,
                ratioLabel,
                mapJungleRatio,
                startEnergyLabel,
                animalStartEnergy,
                moveEnergyLabel,
                animalMoveEnergy,
                plantEnergyLabel,
                plantEnergy,
                adamsAndEvesLabel,
                adamsAndEves,
                startButton
        );
    }

    private void setSettings (TextField width,
                               TextField height,
                               TextField ratio,
                               TextField startEnergy,
                               TextField moveEnergy,
                               TextField plantEnergy,
                               TextField adamsAndEves) {
        try {
            int wid = Integer.parseInt(width.getText());
            int hei = Integer.parseInt(height.getText());
            double rat = Double.parseDouble(ratio.getText());
            int start = Integer.parseInt(startEnergy.getText());
            int move = Integer.parseInt(moveEnergy.getText());
            int plant = Integer.parseInt(plantEnergy.getText());
            int aAndE = Integer.parseInt(adamsAndEves.getText());

            this.settings = new SimulationSettings(wid, hei, rat, start, move, plant, aAndE);
        }
        catch (IllegalArgumentException ex) {
            System.out.println(ex);
        }
    }
}

