package agh.ics.sym.engine;

public interface IMapElement {

    Vector2d getPosition();

    String toString();

    boolean isAt(Vector2d position);
}