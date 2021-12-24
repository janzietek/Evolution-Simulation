package agh.ics.sym.engine;

public class Plant implements IMapElement{
    private final Vector2d position;

    public Plant (Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition () {
        return this.position;
    }

    @Override
    public String toString () {
        return "*" + this.position + "*";
    }

    public boolean isAt (Vector2d position) {
        if (position.equals(this.position)) return true;
        else return false;
    }
}
