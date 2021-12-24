package agh.ics.sym.engine;

//import agh.ics.sym.gui.SimulationApp;
import javafx.application.Application;

import java.util.SortedSet;
import java.util.TreeSet;

public class World {
    public static void main (String[] args) throws InterruptedException {
        SimulationSettings settings = new SimulationSettings();

        SimulationMap map = new SimulationMapBounded(10, 20, 0.01);
        System.out.println(map.jungleUpperRight);
        System.out.println(map.jungleLowerLeft);
    }
}
