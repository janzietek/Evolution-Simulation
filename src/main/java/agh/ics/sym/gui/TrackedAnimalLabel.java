package agh.ics.sym.gui;

import agh.ics.sym.engine.IMapChangeObserver;
import agh.ics.sym.engine.SimulationEngine;
import agh.ics.sym.engine.SimulationMap;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class TrackedAnimalLabel extends Label implements IMapChangeObserver {
    SimulationEngine engine;

    public TrackedAnimalLabel (SimulationEngine engine) {
        this.engine= engine;
        this.setText("Click on map field with animal to display information about it");
    }

    @Override
    public void mapChange() {
        if (engine.getTrackedAnimal() != null){
            Platform.runLater(() -> {
                this.setText(engine.trackedInformation());
            });
        }
    }
}
