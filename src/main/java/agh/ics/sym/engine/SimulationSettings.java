package agh.ics.sym.engine;

public class SimulationSettings {
    public int mapHeight;
    public int mapWidth;
    public double jungleRatio;
    public int moveEnergy;
    public int plantsEnergy;
    public int animalsStartEnergy;
    public int adamsAndEvesNumber;

    public SimulationSettings() throws IllegalArgumentException{
        this.setMap(10, 10, 0.16);
        this.setSimulationParameters(2, 30, 50, 10);
    }

    public SimulationSettings(int width,
                              int height,
                              double ratio,
                              int startEnergy,
                              int moveEnergy,
                              int plantsEnergy,
                              int aAndE) throws IllegalArgumentException{
        this.setMap(height, width, ratio);
        this.setSimulationParameters(moveEnergy, plantsEnergy, startEnergy, aAndE);
    }

    private void setMap (int height, int width, double jungleRatio) {
        if (height <= 0 || width <= 0)
            throw new IllegalArgumentException("Map parameters have to be > 0");
        if (height == 1 || width == 1)
            throw new IllegalArgumentException("Map cannot contain only one field. That is just a common sense...");
        if (jungleRatio <= 0 || jungleRatio >= 1)
            throw new IllegalArgumentException("Jungle ratio is supposed to be a number in range (0, 1)");

        this.mapHeight = height;
        this.mapWidth = width;
        this.jungleRatio = jungleRatio;
    }

    private void setSimulationParameters (int dailyLostEnergy, int plantsEnergy, int animalsStartEnergy, int adamsAndEvesNumber){
        if (dailyLostEnergy < 1 || plantsEnergy < 1 || animalsStartEnergy < 1 || adamsAndEvesNumber < 1)
            throw new IllegalArgumentException("Simulation settings must all be a positive numbers");

        if (adamsAndEvesNumber > this.mapWidth * this.mapHeight)
            throw new IllegalArgumentException("The number of animals exceeds the number of fields on the map");


        this.adamsAndEvesNumber = adamsAndEvesNumber;
        this.animalsStartEnergy = animalsStartEnergy;
        this.moveEnergy = dailyLostEnergy;
        this.plantsEnergy = plantsEnergy;
    }
}
