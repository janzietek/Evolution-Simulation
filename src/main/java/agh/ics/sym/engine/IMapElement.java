package agh.ics.sym.engine;

public interface IMapElement {

    public Vector2d getPosition ();

    public String toString ();

    public boolean isAt (Vector2d position);
}