package agh.ics.sym.engine;

public class SimulationMapBounded extends SimulationMap{
    public SimulationMapBounded(int width, int height, double jungleRatio) {
        super(width, height, jungleRatio);
    }

    @Override
    public Vector2d animalPositionCorrector(Vector2d position, MapDirection direction) {
        Vector2d newPosition = position.add(direction.toUnitVector());

        if (newPosition.isInRectangle(this.savannaLowerLeft, this.savannaUpperRight))
            return newPosition;

        return position;
    }
}
