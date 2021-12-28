import agh.ics.sym.engine.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimulationMapTest {
    @Test
    public void jungleSizeTest () {
        SimulationMap map1 = new SimulationMapBounded(10, 10, 0.01);
        Assertions.assertEquals(map1.jungleLowerLeft, new Vector2d(4, 4));
        Assertions.assertEquals(map1.jungleUpperRight, new Vector2d(4, 4));


        SimulationMap map2 = new SimulationMapBounded(10, 20, 0.01);
        Assertions.assertEquals(map2.jungleLowerLeft, new Vector2d(4, 9));
        Assertions.assertEquals(map2.jungleUpperRight, new Vector2d(4, 10));

        SimulationMap map3 = new SimulationMapBounded(5, 5, 0.16);
        Assertions.assertEquals(map3.jungleLowerLeft, new Vector2d(1, 1));
        Assertions.assertEquals(map3.jungleUpperRight, new Vector2d(2, 2));


        SimulationMap map4 = new SimulationMapBounded(5, 5, 0.64);
        Assertions.assertEquals(map4.jungleLowerLeft, new Vector2d(0, 0));
        Assertions.assertEquals(map4.jungleUpperRight, new Vector2d(3, 3));


        SimulationMap map5 = new SimulationMapBounded(30, 10, 0.20);
        Assertions.assertEquals(map5.jungleLowerLeft, new Vector2d(8, 3));
        Assertions.assertEquals(map5.jungleUpperRight, new Vector2d(20, 6));

        SimulationMap map6 = new SimulationMapBounded(4, 4, 0.25);
        Assertions.assertEquals(map6.jungleLowerLeft, new Vector2d(1, 1));
        Assertions.assertEquals(map6.jungleUpperRight, new Vector2d(2, 2));
        Assertions.assertEquals(map6.jungleWidth, 2);
        Assertions.assertEquals(map6.jungleHeight, 2);
    }
}
