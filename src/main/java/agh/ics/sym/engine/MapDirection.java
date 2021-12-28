package agh.ics.sym.engine;

import java.util.Random;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTHEAST,
    SOUTHEAST,
    SOUTHWEST,
    NORTHWEST;


    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case WEST -> "W";
            case EAST -> "E";
            case NORTHEAST -> "NE";
            case SOUTHEAST -> "SE";
            case SOUTHWEST -> "SW";
            case NORTHWEST -> "NW";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> MapDirection.NORTHEAST;
            case NORTHEAST -> MapDirection.EAST;
            case EAST -> MapDirection.SOUTHEAST;
            case SOUTHEAST -> MapDirection.SOUTH;
            case SOUTH -> MapDirection.SOUTHWEST;
            case SOUTHWEST -> MapDirection.WEST;
            case WEST -> MapDirection.NORTHWEST;
            case NORTHWEST -> MapDirection.NORTH;
        };
    }

    public MapDirection opposite() {
        return switch (this) {
            case NORTH -> MapDirection.SOUTH;
            case NORTHEAST -> MapDirection.SOUTHWEST;
            case EAST -> MapDirection.WEST;
            case SOUTHEAST -> MapDirection.NORTHWEST;
            case SOUTH -> MapDirection.NORTH;
            case SOUTHWEST -> MapDirection.NORTHEAST;
            case WEST -> MapDirection.EAST;
            case NORTHWEST -> MapDirection.SOUTHEAST;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }

    public static MapDirection getRandomDirection () {
        Random rand = new Random();
        return values()[rand.nextInt(8)];
    }
}