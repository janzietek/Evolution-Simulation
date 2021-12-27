package agh.ics.sym.gui;

import agh.ics.sym.engine.IMapChangeObserver;
import agh.ics.sym.engine.SimulationEngine;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

public class SimulationCharts extends VBox implements IMapChangeObserver {
    private final SimulationEngine engine;
    final int chartHeight;
    final int chartWidth;

    private XYChart.Series<Number, Number> plantsSeries;
    private XYChart.Series<Number, Number> lifespanSeries;
    private XYChart.Series<Number, Number> animalsSeries;
    private XYChart.Series<Number, Number> childrenSeries;
    private XYChart.Series<Number, Number> energySeries;


    public SimulationCharts (SimulationEngine engine) {
        this.engine = engine;
        this.engine.addObserver(this);
        this.chartHeight = 150;
        this.chartWidth = 400;

        createPlantsChart();
        createAnimalsChart();
        createEnergyChart();
        createLifespanChart();
        createChildrenChart();
    }

    public void createPlantsChart () {
        this.plantsSeries  = new XYChart.Series<>(
                FXCollections.observableArrayList()
        );
        plantsSeries.getData().add(new XYChart.Data<>(0, 0));
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Simulation era");
        xAxis.setAnimated(false);
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        xAxis.setManaged(true);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of plants");
        yAxis.setAnimated(false);

        LineChart<Number, Number> plantsChart = new LineChart<>(xAxis, yAxis);
        plantsChart.setMaxHeight(chartHeight);
        plantsChart.setMaxHeight(chartWidth);
        plantsChart.getData().add(plantsSeries);
        plantsChart.setCreateSymbols(false);
        plantsChart.setAnimated(false);
        plantsChart.setTitle("Plants");
        this.getChildren().add(plantsChart);
    }

    public void createLifespanChart () {
        this.lifespanSeries  = new XYChart.Series<>(
                FXCollections.observableArrayList()
        );
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Simulation era");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Lifespan");
        xAxis.setAnimated(false);
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        LineChart<Number, Number> lifespanChart = new LineChart<>(xAxis, yAxis);

        lifespanChart.setMaxHeight(chartHeight);
        lifespanChart.setMaxHeight(chartWidth);
        lifespanChart.getData().add(lifespanSeries);
        lifespanChart.setAnimated(false);
        lifespanChart.setCreateSymbols(false);
        lifespanChart.setTitle("Average lifespan");
        this.getChildren().add(lifespanChart);
    }

    public void createAnimalsChart () {
        this.animalsSeries  = new XYChart.Series<>(
                FXCollections.observableArrayList()
        );
        animalsSeries.getData().add(new XYChart.Data<>(0, this.engine.settings.adamsAndEvesNumber));
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Simulation era");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of animals");
        xAxis.setAnimated(false);
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        LineChart<Number, Number> animalsChart = new LineChart<>(xAxis, yAxis);
        animalsChart.setMaxHeight(chartHeight);
        animalsChart.setMaxHeight(chartWidth);
        animalsChart.getData().add(animalsSeries);
        animalsChart.setCreateSymbols(false);
        animalsChart.setAnimated(false);
        animalsChart.setTitle("Alive animals");
        this.getChildren().add(animalsChart);
    }
    public void createEnergyChart () {
        energySeries  = new XYChart.Series<>(
                FXCollections.observableArrayList()
        );
        energySeries.getData().add(new XYChart.Data<>(0, this.engine.settings.animalsStartEnergy));
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Simulation era");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Energy");
        xAxis.setAnimated(false);
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        LineChart<Number, Number> energyChart = new LineChart<>(xAxis, yAxis);
        energyChart.setMaxHeight(chartHeight);
        energyChart.setMaxHeight(chartWidth);
        energyChart.getData().add(energySeries);
        energyChart.setCreateSymbols(false);
        energyChart.setAnimated(false);
        energyChart.setTitle("Average energy");
        this.getChildren().add(energyChart);
    }
    public void createChildrenChart () {
        childrenSeries  = new XYChart.Series<>(
                FXCollections.observableArrayList()
        );
        childrenSeries.getData().add(new XYChart.Data<>(0, 0));
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Simulation era");
        NumberAxis yAxis = new NumberAxis(0, 5, 1);
        yAxis.setLabel("Number of children");
        xAxis.setAnimated(false);
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        LineChart<Number, Number> childrenChart = new LineChart<>(xAxis, yAxis);
        childrenChart.setMaxHeight(chartHeight);
        childrenChart.setMaxHeight(chartWidth);
        childrenChart.getData().add(childrenSeries);
        childrenChart.setCreateSymbols(false);
        childrenChart.setAnimated(false);
        this.getChildren().add(childrenChart);
        childrenChart.setTitle("Average fertility");
    }
    @Override
    public void mapChange() {
        Platform.runLater(() -> {
            if (plantsSeries.getData().size() > 20)
                plantsSeries.getData().remove(0);
            plantsSeries.getData().add(new XYChart.Data<>(this.engine.simulationEra, this.engine.stats.plantsNumber));

            if (animalsSeries.getData().size() > 20)
                animalsSeries.getData().remove(0);
            animalsSeries.getData().add(new XYChart.Data<>(this.engine.simulationEra, this.engine.stats.animalsNumber));

            if (energySeries.getData().size() > 20)
                energySeries.getData().remove(0);
            energySeries.getData().add(new XYChart.Data<>(this.engine.simulationEra, this.engine.stats.getAverageEnergy()));

            if (lifespanSeries.getData().size() > 20)
                lifespanSeries.getData().remove(0);
            if (this.engine.stats.getAverageLifespan() > 0.0) {
                lifespanSeries.getData().add(new XYChart.Data<>(this.engine.simulationEra, this.engine.stats.getAverageLifespan()));
            }

            if (childrenSeries.getData().size() > 20)
                childrenSeries.getData().remove(0);
            childrenSeries.getData().add(new XYChart.Data<>(this.engine.simulationEra, this.engine.stats.getAverageFertility()));
        });
    }
}
