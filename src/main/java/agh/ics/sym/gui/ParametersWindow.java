package agh.ics.sym.gui;

import agh.ics.sym.engine.SimulationSettings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
        TextField mapWidth = new TextField("15");
        setConstraints(widthLabel, 0, 0);
        setConstraints(mapWidth, 1, 0);

        Label heightLabel = new Label("Height: ");
        TextField mapHeight = new TextField("15");
        setConstraints(heightLabel, 0, 1);
        setConstraints(mapHeight, 1, 1);

        Label ratioLabel = new Label("Jungle ratio: ");
        TextField mapJungleRatio = new TextField("0.17");
        setConstraints(ratioLabel, 0, 2);
        setConstraints(mapJungleRatio, 1, 2);

        Label startEnergyLabel = new Label("Start energy: ");
        TextField animalStartEnergy = new TextField("100");
        setConstraints(startEnergyLabel, 0, 3);
        setConstraints(animalStartEnergy, 1, 3);

        Label moveEnergyLabel = new Label("Move energy: ");
        TextField animalMoveEnergy = new TextField("3");
        setConstraints(moveEnergyLabel, 0, 4);
        setConstraints(animalMoveEnergy, 1, 4);

        Label plantEnergyLabel = new Label("Plant energy: ");
        TextField plantEnergy = new TextField("100");
        setConstraints(plantEnergyLabel, 0, 5);
        setConstraints(plantEnergy, 1, 5);

        Label adamsAndEvesLabel = new Label("Adams and Eves number: ");
        TextField adamsAndEves = new TextField("30");
        setConstraints(adamsAndEvesLabel, 0, 6);
        setConstraints(adamsAndEves, 1, 6);

        Label refreshTimeLabel = new Label("Map refresh time: ");
        TextField mapRefreshTime = new TextField("300");
        setConstraints(refreshTimeLabel, 0, 7);
        setConstraints(mapRefreshTime, 1, 7);

        Button startButton = new Button("Start simulation");
        setConstraints(startButton, 0, 9);

        CheckBox magic1 = new CheckBox("Make left map magic");
        CheckBox magic2 = new CheckBox("Make right map magic");
        setConstraints(magic1, 0, 8);
        setConstraints(magic2, 1, 8);

        startButton.setOnAction(e -> {
            try {
                setSettings(mapWidth, mapHeight, mapJungleRatio, animalStartEnergy, animalMoveEnergy, plantEnergy, adamsAndEves, mapRefreshTime);
                Scene scene = new Scene(new SimulationWindow(this.settings, magic1.isSelected(), magic2.isSelected()));
                SimulationApp.setScene(scene);
                SimulationApp.window.setFullScreen(true);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex);
            }
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
                refreshTimeLabel,
                mapRefreshTime,
                magic1,
                magic2,
                startButton
        );
    }

    private void setSettings (TextField width,
                               TextField height,
                               TextField ratio,
                               TextField startEnergy,
                               TextField moveEnergy,
                               TextField plantEnergy,
                               TextField adamsAndEves,
                              TextField mapRefreshTime) {
        try {
            int wid = Integer.parseInt(width.getText());
            int hei = Integer.parseInt(height.getText());
            double rat = Double.parseDouble(ratio.getText());
            int start = Integer.parseInt(startEnergy.getText());
            int move = Integer.parseInt(moveEnergy.getText());
            int plant = Integer.parseInt(plantEnergy.getText());
            int aAndE = Integer.parseInt(adamsAndEves.getText());
            int ref = Integer.parseInt(mapRefreshTime.getText());

            this.settings = new SimulationSettings(wid, hei, rat, start, move, plant, aAndE, ref);
        }
        catch (IllegalArgumentException ex) {
            System.out.println(ex);
        }
    }

}

