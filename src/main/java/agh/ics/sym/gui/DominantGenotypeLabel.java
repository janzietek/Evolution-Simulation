package agh.ics.sym.gui;

import agh.ics.sym.engine.IMapChangeObserver;
import agh.ics.sym.engine.SimulationEngine;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class DominantGenotypeLabel extends Label implements IMapChangeObserver {
    SimulationEngine engine;
    public DominantGenotypeLabel(SimulationEngine engine) {
        this.engine = engine;
        this.setText("Dominant genotype: " + engine.dominantGenotype.toString());
    }

    @Override
    public void mapChange() {
        Platform.runLater(() -> this.setText("Dominant genotype: " + engine.dominantGenotype.toString()));
    }
}
