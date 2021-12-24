package agh.ics.sym.engine;
import java.util.Objects;
import java.util.Random;

public class Vector2d implements Comparable<Vector2d> {
    public final int x;
    public final int y;

    public Vector2d (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    boolean precedes(Vector2d other) {
        if (this.x <= other.x && this.y <= other.y) return true;
        else return false;
    }


    boolean follows(Vector2d other) {
        if (this.x >= other.x && this.y >= other.y) return true;
        else return false;
    }

    Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public static Vector2d getRandomVector (Vector2d lowerLeft, Vector2d upperRight) {
        Random rand = new Random();
        int x = rand.nextInt(upperRight.x - lowerLeft.x + 1);
        int y = rand.nextInt(upperRight.y - lowerLeft.y + 1);
        return new Vector2d(x, y);
    }

    public boolean isInRectangle (Vector2d lowerLeft, Vector2d upperRight) {
        return (this.follows(lowerLeft) && this.precedes(upperRight));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    Vector2d opposite () {
        return new Vector2d((-1) * this.x, (-1) * this.y);
    }


    @Override
    public int compareTo(Vector2d other) {
        if (this.y != other.y)
            return this.y - other.y;
        else
            return this.x - other.x;
    }
}