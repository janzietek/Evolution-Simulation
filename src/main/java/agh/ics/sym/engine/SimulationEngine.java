package agh.ics.sym.engine;

import java.util.ArrayList;
import java.util.LinkedList;

public class SimulationEngine implements Runnable{
    private final SimulationSettings settings;
    public final SimulationMap map;
    int simulationEra = 0;
    public final LinkedList<Animal> animals = new LinkedList<>();
    public final LinkedList<Plant> plants = new LinkedList<>();
    public volatile boolean isRunning = true;
    public final LinkedList<IMapChangeObserver> observers = new LinkedList<>();

    public SimulationEngine(SimulationSettings settings, boolean isBounded) {
        this.settings = settings;
        if (isBounded)
            this.map = new SimulationMapBounded(settings.mapWidth, settings.mapHeight, settings.jungleRatio);
        else
            this.map = new SimulationMapWrapped(settings.mapWidth, settings.mapHeight, settings.jungleRatio);

        placeAdamsAndEves();
    }


    @Override
    public void run() {
        while (!simulationEnd()) {
            if (isRunning()) {

                try {
                    Thread.sleep(1000);
                    this.singleEra();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (IMapChangeObserver observer: observers)
                {
                    observer.mapChange();
                }
            }
        }
    }


    public void addObserver (IMapChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver (IMapChangeObserver observer) {
        this.observers.remove(observer);
    }

    public synchronized void changeSimulationStatus () {
        this.isRunning = !this.isRunning;
    }
    
    private synchronized boolean isRunning () {
        return this.isRunning;
    }

    public void singleEra() {
        removeDeadAnimals();
        moveAnimals();
        eat();
        copulate();
        growPlants();
        this.simulationEra = simulationEra + 1;
    }


    public boolean simulationEnd () {
        if (animals.size() == 0 && simulationEra > 0) {
            changeSimulationStatus();
            return true;
        }
        return false;
    }

    private void placeAdamsAndEves() {
        int i = settings.adamsAndEvesNumber;
        while (i > 0) {
            Vector2d position = Vector2d.getRandomVector(map.savannaLowerLeft, map.savannaUpperRight);
            if (!this.map.areAnimalsAt(position)) {
                Animal animal;
                animal = new Animal(this.map, position, this.settings.animalsStartEnergy);
                animals.add(animal);
                this.map.placeAnimal(animal);
                i--;
            }
        }
    }


    private void removeDeadAnimals() {
        LinkedList<Animal> deadAnimals = new LinkedList<>();

        for (Animal animal : this.animals) {
            if (animal.isDead()) {
                deadAnimals.add(animal);
            }
        }

        for (Animal animal: deadAnimals) {
            this.map.removeAnimal(animal);
            animals.remove(animal);
        }
    }

    private void moveAnimals() {
        for (Animal animal : this.animals) {
            animal.move();
            animal.modifyEnergy((-1) * settings.moveEnergy);
        }
    }

    private void eat() {
        LinkedList<Plant> plantsToRemove = new LinkedList<>();

        for (Plant plant : this.plants) {
            if (this.map.areAnimalsAt(plant.getPosition())) {
                plantsToRemove.add(plant);

                ArrayList<Animal> theStrongest = this.map.unitFields.get(plant.getPosition()).getTheStrongest();

                for (Animal animal : theStrongest)
                    animal.modifyEnergy(this.settings.plantsEnergy / theStrongest.size());
            }
        }
        for (Plant plant: plantsToRemove){
            this.map.removePlant(plant);
            this.plants.remove(plant);
        }
    }

    private void copulate() {
        for (UnitField unitField: this.map.unitFields.values()) {
            if (unitField.animals.size() > 1) {
                ArrayList<Animal> mates = unitField.selectAnimalsToMate();

                if (mates.get(0).canCopulate() && mates.get(1).canCopulate()) {
                    Animal child = new Animal(mates.get(0), mates.get(1));
                    this.map.placeAnimal(child);
                    this.animals.add(child);
                }
            }
        }
    }

    private void growPlants () {
        Vector2d savannaPlantPosition = this.map.savannaPlantPosition();
        Vector2d junglePlantPosition = this.map.junglePlantPosition();

        if (!savannaPlantPosition.equals(new Vector2d(-1, -1))) {
            Plant savannaPlant = new Plant(savannaPlantPosition);
            this.map.placePlant(savannaPlant);
            this.plants.add(savannaPlant);
        }

        if (!junglePlantPosition.equals(new Vector2d(-1, -1))) {
            Plant junglePlant = new Plant(junglePlantPosition);
            this.map.placePlant(junglePlant);
            this.plants.add(junglePlant);
        }
    }

}

