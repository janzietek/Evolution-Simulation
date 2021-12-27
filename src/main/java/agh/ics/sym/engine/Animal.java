package agh.ics.sym.engine;

import java.lang.Math;
import java.util.LinkedList;

public class Animal implements IMapElement, Comparable<Animal>{
    private final GenoType genoType;
    public final int startEnergy;

    private MapDirection orientation;
    public int energy;
    public int age = 0;
    private Vector2d position;
    private final SimulationMap map;
    public LinkedList<Animal> children = new LinkedList<>();

    // constructor for magically created animals
    public Animal (SimulationMap map, Vector2d initialPosition, int startEnergy) {
        this.position = initialPosition;
        this.map = map;
        this.orientation = MapDirection.getRandomDirection();
        this.genoType = new GenoType();
        this.startEnergy = startEnergy;
        this.energy = startEnergy;
    }

    // constructor for traditionally created animals, assuming father.energy >= mother.energy
    public Animal (Animal father, Animal mother) {
        this.position = father.position;
        this.map = father.map;
        this.orientation = MapDirection.getRandomDirection();
        float energyRatio = (float) father.energy / (father.energy + mother.energy);
        this.genoType = new GenoType(father.getGenoType(), mother.getGenoType(), energyRatio);
        this.startEnergy = copulatingEnergy(father, mother);
        this.energy = this.startEnergy;
    }

    // constructor for magic copies
    public Animal (Animal original, Vector2d position) {
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.startEnergy = original.startEnergy;
        this.energy = original.startEnergy;
        this.map = original.map;
        this.genoType = original.genoType;
    }

    public void addChild(Animal child) {
        this.children.add(child);
    }

    public MapDirection getOrientation () {
        return this.orientation;
    }
    public GenoType getGenoType () {
        return this.genoType;
    }
    public Vector2d getPosition () {
        return this.position;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public boolean isDead () {
        return this.energy <= 0;
    }

    public boolean canCopulate () {
        return this.energy > 0.5 * this.startEnergy;
    }


    public void modifyEnergy (int energyModification) {
        this.energy = this.energy + energyModification;
    }

    private int copulatingEnergy (Animal father, Animal mother) {
        int fathersPleasure = (int) Math.round(0.25 * father.energy);
        int mothersEffort = (int) Math.round(0.25 * mother.energy);

        int usedEnergy = mothersEffort + fathersPleasure;

        mother.modifyEnergy((-1) * mothersEffort);
        father.modifyEnergy((-1) * fathersPleasure);

        return usedEnergy;
    }


    private void basicMoves (MoveOptions options) {
        Vector2d newPosition;
        switch (options) {
            case TURNRIGHT -> setOrientation(this.orientation.next());
            case MOVEFORWARD -> {
                newPosition = map.animalPositionCorrector(this.position, this.orientation);
                this.map.positionChanged(this, newPosition);
                setPosition(newPosition);
            }
            case MOVEBACKWARD -> {
                newPosition = map.animalPositionCorrector(this.position, this.orientation.opposite());
                this.map.positionChanged(this, newPosition);
                setPosition(newPosition);
            }
        }
    }

    public void move() {
        byte gene = genoType.getRandomGene();

        if (gene == 0) {
            this.basicMoves(MoveOptions.MOVEFORWARD);
        }
        else if (gene == 4) {
            this.basicMoves(MoveOptions.MOVEBACKWARD);
        }
        else {
            while (gene > 0) {
                basicMoves(MoveOptions.TURNRIGHT);
                gene = (byte) (gene - 1);
            }
        }
    }

    public void getOlder () {
        this.age = this.age + 1;
    }

    public boolean isAt (Vector2d position) {
        return position.equals(this.position);
    }


    public String toString() {
        return this.orientation.toString() + this.energy;
    }


    @Override
    public int compareTo(Animal A) {
        return A.energy - this.energy;
    }
}
