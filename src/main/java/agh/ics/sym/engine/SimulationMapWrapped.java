package agh.ics.sym.engine;

public class SimulationMapWrapped extends SimulationMap{

    public SimulationMapWrapped(int width, int height, double jungleRatio) {
        super(width, height, jungleRatio);
    }

    @Override
    public Vector2d animalPositionCorrector(Vector2d position, MapDirection direction) {
        Vector2d newPosition = position.add(direction.toUnitVector());

        if (newPosition.isInRectangle(this.savannaLowerLeft, this.savannaUpperRight))
            return position;
        else {
            int x = newPosition.x;
            int y = newPosition.y;

            if (position.x > this.savannaUpperRight.x)
                x = 0;
            else if (position.x < this.savannaLowerLeft.x)
                x = this.savannaUpperRight.x;

            if (position.y > this.savannaUpperRight.y)
                y = 0;
            else if (position.y < this.savannaLowerLeft.y)
                y = this.savannaUpperRight.y;

            return new Vector2d(x, y);
        }
    }
}
