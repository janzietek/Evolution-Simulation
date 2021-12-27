package agh.ics.sym.engine;


import java.util.*;
import java.lang.Math;

public abstract class SimulationMap implements IPositionChangeObserver{
    public final int savannaWidth;
    public final int savannaHeight;
    public final int jungleWidth;
    public final int jungleHeight;

    public final Vector2d savannaUpperRight;
    public final Vector2d savannaLowerLeft = new Vector2d(0, 0);
    public final Vector2d jungleUpperRight;
    public final Vector2d jungleLowerLeft;


    protected Map<Vector2d,Plant> plants = new HashMap<>();
    protected Map<Vector2d,UnitField> unitFields = new HashMap<>();


    public SimulationMap(int width, int height, double jungleRatio) {
        this.savannaWidth = width;
        this.savannaHeight = height;
        this.savannaUpperRight = new Vector2d(width - 1, height - 1);
        this.jungleWidth = (int) Math.round(Math.sqrt(jungleRatio) * (double) this.savannaWidth);
        this.jungleHeight = (int) Math.round(Math.sqrt(jungleRatio) * (double) this.savannaHeight);

        this.jungleLowerLeft = calculateJungleStartVector();
        this.jungleUpperRight = this.jungleLowerLeft.add(new Vector2d(jungleWidth - 1, jungleHeight - 1));
    }

    public Vector2d calculateJungleStartVector () {
        int x = (this.savannaWidth - this.jungleWidth) / 2;
        int y = (this.savannaHeight - this.jungleHeight) / 2;

        return new Vector2d(x, y);
    }

    public UnitField getUnitField (Vector2d position) {
        if (this.unitFields.get(position) == null) {
            UnitField unitField = new UnitField();
            this.unitFields.put(position, unitField);
        }
        return this.unitFields.get(position);
    }

    public void placeAnimal(Animal animal) {
        UnitField unitField = getUnitField(animal.getPosition());
        unitField.animals.add(animal);
    }

    public void placePlant(Plant plant) {
        this.plants.put(plant.getPosition(), plant);
    }

    public void removeAnimal (Animal animal) {
        this.unitFields.get(animal.getPosition()).animals.remove(animal);
    }

    public void removePlant (Plant plant) {
        this.plants.remove(plant.getPosition());
    }


    public boolean isPlantAt (Vector2d position) {
        return (this.plants.get(position) != null);
    }

    public boolean areAnimalsAt (Vector2d position) {
        if (this.unitFields.get(position) != null) {
            return this.unitFields.get(position).animals.size() != 0;
        }
        return false;
    }

    public IMapElement objectAt (Vector2d position) {
        if (isPlantAt(position))
            return plants.get(position);

        if (areAnimalsAt(position))
            return unitFields.get(position).getTheStrongest().get(0);

        return null;
    }


    @Override
    public void positionChanged(Animal animal, Vector2d newPosition) {
        this.unitFields.get(animal.getPosition()).animals.remove(animal);
        UnitField unitField = getUnitField(newPosition);
        this.unitFields.get(newPosition).animals.add(animal);
    }

    public boolean isInJungle (Vector2d position) {
        return position.isInRectangle(this.jungleLowerLeft, this.jungleUpperRight);
    }

    private ArrayList<Vector2d> getPlantableJungleFields () {
        ArrayList<Vector2d> plantableFields = new ArrayList<>();

        for (int x = this.jungleLowerLeft.x; x <= this.jungleUpperRight.x; x++) {
            for (int y = this.jungleLowerLeft.y; y <= this.jungleUpperRight.y; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!areAnimalsAt(position) && !isPlantAt(position))
                    plantableFields.add(position);
            }
        }
        return plantableFields;
    }

    public Vector2d junglePlantPosition() {
        ArrayList<Vector2d> plantableFields = getPlantableJungleFields();
        if (plantableFields.size() == 0)
            return new Vector2d(-1, -1);
        Random rand = new Random();
        return plantableFields.get(rand.nextInt(plantableFields.size()));
    }

    private ArrayList<Vector2d> getPlantableSavannaFields () {
        ArrayList<Vector2d> plantableFields = new ArrayList<>();

        for (int x = this.savannaLowerLeft.x; x < this.savannaWidth; x++) {
            for (int y = this.savannaLowerLeft.y; y < this.savannaHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!areAnimalsAt(position) && !isPlantAt(position) && !isInJungle(position))
                    plantableFields.add(position);
            }
        }
        return plantableFields;
    }

    public Vector2d savannaPlantPosition () {
        ArrayList<Vector2d> plantableFields = getPlantableSavannaFields();
        if (plantableFields.size() == 0)
            return new Vector2d(-1, -1);
        Random rand = new Random();
        return plantableFields.get(rand.nextInt(plantableFields.size()));
    }

    public abstract Vector2d animalPositionCorrector (Vector2d position, MapDirection direction);

    public ArrayList<Vector2d> getFreePosition () {
        ArrayList<Vector2d> freePositions = new ArrayList<>();

        for (int x = this.savannaLowerLeft.x; x < this.savannaWidth; x++) {
            for (int y = this.savannaLowerLeft.y; y < this.savannaHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!areAnimalsAt(position))
                    freePositions.add(position);
            }
        }
        return freePositions;
    }
}