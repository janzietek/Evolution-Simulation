package agh.ics.sym.gui;

import agh.ics.sym.engine.*;
import com.sun.javafx.geom.Rectangle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;

public class MapVisualizer extends GridPane implements IMapChangeObserver {
    final int gridSize;
    SimulationMap map;
    SimulationEngine engine;
    ImagesManager imagesManager;

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

        imagesManager = new ImagesManager(gridSize);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                double gridX = (int)(e.getX() / gridSize);
                double gridY = (int)(e.getY() / gridSize);

                System.out.println("Mouse X = " + gridX + "  Y = " + gridY);
            }
        };
        addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);


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

                Vector2d currentPosition = new Vector2d(i, j);

                // background
                int backgroundIndex = 1; // savanna
                if(map.isInJungle(currentPosition))
                {
                    backgroundIndex = 0; // jungle
                }
                ImageView imageB = imagesManager.GetImageViewByIndex(backgroundIndex, gridSize-1);
                if(imageB != null)
                {
                    this.add(imageB, i, UR.y - j);
                    GridPane.setHalignment(imageB, HPos.CENTER);
                }

                //Label label;
                if (map.areAnimalsAt(currentPosition)) {

                    ImageView image = imagesManager.GetImageView(map.objectAt(currentPosition), gridSize);
                    if(image != null)
                    {
                        this.add(image, i, UR.y - j);
                        GridPane.setHalignment(image, HPos.CENTER);
                    }
                    /*
                    try {
                        label = new Label(map.objectAt(currentPosition).toString());
                        this.add(label, i, UR.y - j);
                        GridPane.setHalignment(label, HPos.CENTER);
                    }
                    catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    */
                }
                else {
                    if(map.isPlantAt(currentPosition)) {

                        ImageView image = null;
                        if(backgroundIndex == 1) {
                            image = imagesManager.GetImageViewByIndex(3, gridSize);
                        }
                        else {
                            image = imagesManager.GetImageViewByIndex(2, gridSize);
                        }
                        if(image != null) {
                            this.add(image, i, UR.y - j);
                            GridPane.setHalignment(image, HPos.CENTER);
                        }
                    }
                }
            }
        }
    }
}