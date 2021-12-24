package agh.ics.sym.engine;

import com.sun.source.tree.BreakTree;

import java.util.*;

public class UnitField {
    protected ArrayList<Animal> animals = new ArrayList<>();


    public ArrayList<Animal> getTheStrongest () {
        ArrayList<Animal> theStrongest = new ArrayList<>();

        int maxEnergy = Integer.MIN_VALUE;

        for (Animal animal: animals) {
            if (animal.energy > maxEnergy) {
                maxEnergy = animal.energy;
            }
        }
        for (Animal animal: animals) {
            if (animal.energy == maxEnergy)
                theStrongest.add(animal);
        }

        return theStrongest;
    }

    public ArrayList<Animal> selectAnimalsToMate() {
        ArrayList<Animal> mates = new ArrayList<>();
        ArrayList<Animal> theStrongest = this.getTheStrongest();

        if (theStrongest.size() == 1) {
            mates.add(theStrongest.get(0));
            ArrayList<Animal> possibleMates = new ArrayList<>();

            int maxEnergy = Integer.MIN_VALUE;

            for (Animal animal: animals) {
                if (animal.energy > maxEnergy && animal.energy != mates.get(0).energy) {
                    maxEnergy = animal.energy;
                }
            }
            for (Animal animal: animals) {
                if (animal.energy == maxEnergy)
                    possibleMates.add(animal);
            }

            Collections.shuffle(possibleMates);

            mates.add(possibleMates.get(0));
        }
        else if (theStrongest.size() > 1) {
            Collections.shuffle(theStrongest);

            mates.add(theStrongest.get(0));
            mates.add(theStrongest.get(1));
        }
        return mates;
    }

    @Override
    public String toString() {
        return animals.toString();
    }
}
