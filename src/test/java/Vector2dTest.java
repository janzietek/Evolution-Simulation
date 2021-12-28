import agh.ics.sym.engine.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector2dTest {
    @Test
    public void equalsTest () {
        Assertions.assertEquals(new Vector2d(10, 10), new Vector2d(10, 10));
        Assertions.assertNotEquals(new Vector2d(10, 10), new Vector2d(10, 11));
    }

    @Test
    public void toStringTest () {
        Assertions.assertEquals("(10,10)",new Vector2d(10, 10).toString());
    }

    @Test
    public void precedesTest () {
        Assertions.assertTrue(new Vector2d(10, 10).precedes(new Vector2d(101, 101)));
        Assertions.assertFalse(new Vector2d(10, 10).precedes(new Vector2d(9, 11)));
    }

    @Test
    public void followsTest () {
        Assertions.assertTrue(new Vector2d(100, 100).follows(new Vector2d(11, 11)));
        Assertions.assertFalse(new Vector2d(10, 10).follows(new Vector2d(9, 11)));
    }

    @Test
    public void addTest () {
        Assertions.assertEquals(new Vector2d(20, 20), (new Vector2d(9, 11)).add(new Vector2d(11, 9)));
    }

    @Test
    public void oppositeTest () {
        Assertions.assertEquals(new Vector2d(-10, -10), new Vector2d(10, 10).opposite());
    }

    @Test
    public void isInRectangleTest () {
        Vector2d vector2d = new Vector2d(2, 2);
        Assertions.assertTrue(vector2d.isInRectangle(new Vector2d(0, 0), new Vector2d(2, 2)));
        Assertions.assertTrue(vector2d.isInRectangle(new Vector2d(1, 1), new Vector2d(23, 3)));
        Assertions.assertFalse(vector2d.isInRectangle(new Vector2d(3, 2), new Vector2d(23, 3)));
    }
}