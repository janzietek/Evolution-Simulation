package agh.ics.sym.engine;

import java.util.ArrayList;
import java.util.Random;

public class MagicSimulationEngine extends SimulationEngine{
    public int magicUses = 0;

    public MagicSimulationEngine(SimulationSettings settings, IMapChangeObserver observer) {
        super(settings);
    }

    @Override
    public void singleEra() {
        if (this.magicUses < 3 && animals.size() == 5)
            this.putMagicInAction();
        super.singleEra();
    }

    public void putMagicInAction () {
        ArrayList<Animal> magicCopies = new ArrayList<>();
        ArrayList<Vector2d> positions = map.getFreePosition();
        Random rand = new Random();
        Vector2d position;

        for (Animal animal:animals) {
            if (!positions.isEmpty()) {
                position = positions.get(rand.nextInt(positions.size()));
                Animal magicAnimal = new Animal(animal, position);
                magicCopies.add(magicAnimal);
                positions.remove(position);
                this.map.placeAnimal(magicAnimal);
            }
        }
        this.animals.addAll(magicCopies);
    }
}
