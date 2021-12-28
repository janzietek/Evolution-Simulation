package agh.ics.sym.engine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SimulationEngine implements Runnable{
    public final SimulationSettings settings;
    public SimulationStats stats;
    public final SimulationMap map;
    public int simulationEra = 0;

    public volatile boolean isRunning = false;
    public boolean simulationEnd = false;
    public GenoType dominantGenotype;
    File statsFile;

    private Animal trackedAnimal = null;
    private final LinkedList<Animal> trackedAnimalChildren = new LinkedList<>();
    private final LinkedList<Animal> trackedAnimalDescendants = new LinkedList<>();

    public final LinkedList<Animal> animals = new LinkedList<>();
    public final LinkedList<Plant> plants = new LinkedList<>();

    public final LinkedList<IMapChangeObserver> observers = new LinkedList<>();
    public final Map<GenoType,ArrayList<Animal>> genotypes = new HashMap<>();



    public SimulationEngine(SimulationSettings settings, boolean isBounded) {
        this.settings = settings;
        if (isBounded) {
            this.map = new SimulationMapBounded(settings.mapWidth, settings.mapHeight, settings.jungleRatio);
            try {
                Files.deleteIfExists(Paths.get("boundedSimulationStats.csv"));
                this.statsFile = new File("boundedSimulationStats.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            this.map = new SimulationMapWrapped(settings.mapWidth, settings.mapHeight, settings.jungleRatio);
            try {
                Files.deleteIfExists(Paths.get("wrappedSimulationStats.csv"));
                this.statsFile = new File("wrappedSimulationStats.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.stats = new SimulationStats(settings);

        placeAdamsAndEves();
        updateDominantGenotype();
    }


    @Override
    public void run() {
        while (!simulationEnd) {
            if (isRunning()) {
                try {
                    Thread.sleep(settings.refreshTime);
                    this.singleEra();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (IMapChangeObserver observer: observers)
                {
                    observer.mapChange();
                }
                setSimulationEnd();
            }
        }
    }

    private void saveStatsToFile () {
        try {
            FileWriter fw = new FileWriter(statsFile.getName(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(stats.toString());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addObserver (IMapChangeObserver observer) {
        this.observers.add(observer);
    }

    public synchronized void changeSimulationStatus () {
        this.isRunning = !this.isRunning;
    }
    
    private synchronized boolean isRunning () {
        return this.isRunning;
    }

    public void singleEra() {
        saveStatsToFile();
        stats.updatePlants(plants.size());
        removeDeadAnimals();
        moveAnimals();
        eat();
        copulate();
        growPlants();
        updateDominantGenotype();
        this.simulationEra = simulationEra + 1;
    }


    public void setSimulationEnd() {
        if (animals.size() == 0 && simulationEra > 0) {
            changeSimulationStatus();
            this.simulationEnd = true;
        }
        this.simulationEnd = false;
    }

    private void placeAdamsAndEves() {
        int i = settings.adamsAndEvesNumber;
        while (i > 0) {
            Vector2d position = Vector2d.getRandomVector(map.savannaLowerLeft, map.savannaUpperRight);
            if (!this.map.areAnimalsAt(position)) {
                Animal animal;
                animal = new Animal(this.map, position, this.settings.animalsStartEnergy, this.simulationEra);
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
                    if (mates.get(0).equals(trackedAnimal) || mates.get(1).equals(trackedAnimal)) {
                        this.trackedAnimalDescendants.add(child);
                        this.trackedAnimalChildren.add(child);
                    }
                    if (trackedAnimalDescendants.contains(mates.get(0)) || trackedAnimalDescendants.contains(mates.get(1))) {
                        this.trackedAnimalDescendants.add(child);
                    }
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


    public boolean setTrackedAnimal (Vector2d position) {
        UnitField unitField = this.map.getUnitField(position);
        System.out.println(unitField.toString());
        if (unitField.animals.size() > 0) {
            this.trackedAnimal = unitField.getTheStrongest().get(0);
            this.trackedAnimalDescendants.clear();
            this.trackedAnimalChildren.clear();
            return true;
        }
        return false;
    }

    public Animal getTrackedAnimal () {
        return this.trackedAnimal;
    }


    public String trackedInformation() {
        if (trackedAnimal.energy > 0) {
            return "Tracked animal genotype: " + trackedAnimal.getGenoType().toString() + "\n"
                    + "Number of children: " + trackedAnimalChildren.size() + "\n"
                    + "Number of descendants: " + trackedAnimalDescendants.size();
        }
        else {
            return "Tracked animal genotype: " + trackedAnimal.getGenoType().toString() + "\n"
                    + "Number of children: " + trackedAnimalChildren.size() + "\n"
                    + "Number of descendants: " + trackedAnimalDescendants.size() + "\n"
                    + "Regretfully, this animal died in " + (trackedAnimal.birthDate + trackedAnimal.age) + " era of simulation";
        }
    }
}