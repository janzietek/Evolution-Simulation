package agh.ics.sym.engine;


public class SimulationStats {
    public int plantsNumber;
    public int animalsNumber;
    public int deadAnimalsNumber;
    public int totalEnergy;
    public int totalDeadAge;
    public int totalChildren;

    SimulationSettings settings;

    public SimulationStats (SimulationSettings settings) {
        this.settings = settings;
        this.animalsNumber = settings.adamsAndEvesNumber;
        this.totalEnergy = settings.animalsStartEnergy * settings.adamsAndEvesNumber;
        this.totalChildren = 0;
        this.totalDeadAge = 0;
        this.deadAnimalsNumber = 0;
    }

    public void updatePlants (int plantsNumber) {
        this.plantsNumber = plantsNumber;
    }

    public void updateCausedByDeath(Animal animal) {
        this.totalDeadAge = this.totalDeadAge + animal.age;
        this.totalChildren = this.totalChildren - animal.children.size();
        this.totalEnergy = this.totalEnergy + (-1) * animal.energy;

        this.animalsNumber = this.animalsNumber - 1;
        this.deadAnimalsNumber = this.deadAnimalsNumber + 1;
    }

    public void updateEnergyCausedByEating () {
        this.totalEnergy = this.totalEnergy + this.settings.plantsEnergy;
    }

    public void updateEnergyCausedByMoving () {
        this.totalEnergy = this.totalEnergy - this.animalsNumber * this.settings.moveEnergy;
    }


    public void updateCausedByBirth () {
        this.totalChildren = this.totalChildren + 2;
        this.animalsNumber = this.animalsNumber + 1;
    }

    public void updateCausedByMagic (int animalStartEnergy) {
        this.animalsNumber = this.animalsNumber + 1;
        this.totalEnergy = this.totalEnergy + animalStartEnergy;
    }

    public double getAverageEnergy () {
        return (double) totalEnergy / animalsNumber;
    }

    public double getAverageLifespan () {
        if (deadAnimalsNumber > 0)
            return (double) totalDeadAge / deadAnimalsNumber;
        else
            return 0.0;
    }

    public double getAverageFertility () {
        return (double) totalChildren / animalsNumber;
    }

    @Override
    public String toString() {
        return "SimulationStats{" +
                "plantsNumber=" + plantsNumber +
                ", animalsNumber=" + animalsNumber +
                ", deadAnimalsNumber=" + deadAnimalsNumber +
                ", totalEnergy=" + totalEnergy +
                ", totalDeadAge=" + totalDeadAge +
                ", totalChildren=" + totalChildren +
                '}';
    }
}
