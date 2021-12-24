package agh.ics.sym.gui;

import agh.ics.sym.engine.*;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class MapVisualizer extends GridPane implements IMapChangeObserver {
    final int gridSize;
    SimulationMap map;
    SimulationEngine engine;

    public MapVisualizer (SimulationEngine engine) {
        this.engine = engine;
        this.map = engine.map;
        this.gridSize = (int) 600 / Integer.max(map.savannaWidth, map.savannaHeight);
        for (int i = 0; i < map.savannaHeight; i++) {
            this.getRowConstraints().add(new RowConstraints(gridSize));
        }
        for (int i = 0; i < map.savannaWidth; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(gridSize));
        }
        drawGridPane();
    }


    @Override
    public void mapChange() {
        Platform.runLater(() -> {
            this.setGridLinesVisible(false);
            this.getChildren().clear();
            drawGridPane();
        });
    }

    private void drawGridPane() {
        this.setGridLinesVisible(true);

        Vector2d LL = map.savannaLowerLeft;
        Vector2d UR = map.savannaUpperRight;

        int i, j;

        for (i = LL.x; i <= UR.x; i++) {
            for (j = UR.y; j >= LL.y; j--) {
                Label label;
                Vector2d currentPosition = new Vector2d(i, j);
                if (map.areAnimalsAt(currentPosition)) {
                    try {
                        label = new Label(map.objectAt(currentPosition).toString());
                        this.add(label, i, UR.y - j);
                        GridPane.setHalignment(label, HPos.CENTER);
                    }
                    catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    if(map.isPlantAt(currentPosition)) {
                        try {
                            label = new Label(map.objectAt(currentPosition).toString());
                            this.add(label, i, UR.y - j);
                            GridPane.setHalignment(label, HPos.CENTER);
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
