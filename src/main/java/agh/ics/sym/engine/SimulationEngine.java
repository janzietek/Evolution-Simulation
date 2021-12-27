package agh.ics.sym.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SimulationEngine implements Runnable{
    public final SimulationSettings settings;
    public SimulationStats stats;
    public final SimulationMap map;
    public volatile boolean isRunning = false;
    public int refreshTime;
    public int simulationEra = 0;
    public GenoType dominantGenotype;


    public final LinkedList<Animal> animals = new LinkedList<>();
    public final LinkedList<Animal> lateAnimals = new LinkedList<>();
    public final LinkedList<Plant> plants = new LinkedList<>();

    public final LinkedList<IMapChangeObserver> observers = new LinkedList<>();
    public final Map<GenoType,ArrayList<Animal>> genotypes = new HashMap<>();

    public SimulationEngine(SimulationSettings settings, boolean isBounded) {
        this.settings = settings;
        if (isBounded)
            this.map = new SimulationMapBounded(settings.mapWidth, settings.mapHeight, settings.jungleRatio);
        else
            this.map = new SimulationMapWrapped(settings.mapWidth, settings.mapHeight, settings.jungleRatio);

        this.stats = new SimulationStats(settings);
        this.refreshTime = 200;
        placeAdamsAndEves();
        updateDominantGenotype();
    }


    @Override
    public void run() {
        while (!simulationEnd()) {
            if (isRunning()) {
                try {
                    Thread.sleep(refreshTime);
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
        stats.updatePlants(plants.size());
        removeDeadAnimals();
        moveAnimals();
        eat();
        copulate();
        growPlants();
        updateDominantGenotype();
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
                addAnimalsGenes(animal);
                i--;
            }
        }
    }


    private void removeDeadAnimals() {
        LinkedList<Animal> deadAnimals = new LinkedList<>();
        for (Animal animal : this.animals) {
            if (animal.isDead()) {
                deadAnimals.add(animal);
                stats.updateCausedByDeath(animal);
            }
            else
                animal.getOlder();
        }

        for (Animal animal: deadAnimals) {
            this.map.removeAnimal(animal);
            removeAnimalsGenes(animal);
            lateAnimals.add(animal);
            animals.remove(animal);
        }
    }

    private void moveAnimals() {
        for (Animal animal : this.animals) {
            animal.move();
            animal.modifyEnergy((-1) * settings.moveEnergy);
        }
        stats.updateEnergyCausedByMoving();
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
            stats.updateEnergyCausedByEating();
        }
    }

    private void copulate() {
        for (UnitField unitField: this.map.unitFields.values()) {
            if (unitField.animals.size() > 1) {
                ArrayList<Animal> mates = unitField.selectAnimalsToMate();

                if (mates.get(0).canCopulate() && mates.get(1).canCopulate()) {
                    Animal child = new Animal(mates.get(0), mates.get(1));
                    mates.get(0).addChild(child);
                    mates.get(1).addChild(child);
                    this.map.placeAnimal(child);
                    this.animals.add(child);
                    addAnimalsGenes(child);
                    stats.updateCausedByBirth();
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

    protected void addAnimalsGenes (Animal animal) {
        if (genotypes.get(animal.getGenoType()) == null) {
            ArrayList<Animal> animalsGenes = new ArrayList<>();
            genotypes.put(animal.getGenoType(), animalsGenes);
        }
        genotypes.get(animal.getGenoType()).add(animal);
    }

    protected void removeAnimalsGenes (Animal animal) {
        genotypes.get(animal.getGenoType()).remove(animal);
    }

    protected void updateDominantGenotype () {
        GenoType dominant = this.dominantGenotype;
        int maxNumberOfClones = 0;
        for (ArrayList<Animal> animals: genotypes.values()) {
            if (animals.size() > maxNumberOfClones) {
                maxNumberOfClones = animals.size();
                dominant = animals.get(0).getGenoType();
            }
        }
        this.dominantGenotype = dominant;
    }
}