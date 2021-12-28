
import agh.ics.sym.engine.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AnimalTest {
    @Test
    public void genotypeTest () {
        SimulationMap map = new SimulationMapWrapped(5, 5, 0.36);
        Animal animal1 = new Animal(map, new Vector2d(2, 2), 30, 1);
        Animal animal2 = new Animal(map, new Vector2d(2, 2), 30, 1);

        Animal child = new Animal(animal1, animal2);

        GenoType genotype1 = animal1.getGenoType();
        GenoType genotype2 = animal2.getGenoType();

        byte[] childGenotype1 = new byte[32]; // 1st possibility
        byte[] childGenotype2 = new byte[32]; // 2nd possibility

        int i;
        for (i = 0; i < 16; i++) {
             childGenotype1[i] = genotype1.genoType[i];
            childGenotype2[i] = genotype2.genoType[i];
        }
        for (; i < 32; i++) {
            childGenotype1[i] = genotype2.genoType[i];
            childGenotype2[i] = genotype1.genoType[i];
        }

        System.out.println(child.getGenoType());
        System.out.println(Arrays.toString(childGenotype1));
        System.out.println(Arrays.toString(childGenotype2));


        Assertions.assertTrue(Arrays.equals(child.getGenoType().genoType, childGenotype1) || Arrays.equals(child.getGenoType().genoType, childGenotype2));
    }

}