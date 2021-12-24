package agh.ics.sym.engine;

import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class GenoType {
    public static byte possibleGenesNumber = 8;
    public static byte[] possibleGenes = {0, 1, 2, 3, 4, 5, 6, 7};
    public static byte genoTypeLength = 32;
    public final byte[] genoType = new byte[genoTypeLength];
    private final Random rand = new Random();

    public GenoType () {
        for (byte i = 0; i < genoTypeLength; i++) {
            this.genoType[i] = possibleGenes[rand.nextInt(possibleGenesNumber)];
        }
        Arrays.sort(this.genoType);
    }


    public GenoType (GenoType strong, GenoType weak, float ratio) {
        boolean flag = rand.nextBoolean();
        byte toWhichGene = (byte) Math.round(ratio * genoTypeLength);
        byte i;
        if (flag) {
            for (i = 0; i < toWhichGene; i++) {
                this.genoType[i] = strong.genoType[i];
            }
            for (; i < genoTypeLength; i++) {
                this.genoType[i] = weak.genoType[i];
            }
        }
        else {
            for (i = 0; i < genoTypeLength - toWhichGene; i++) {
                this.genoType[i] = weak.genoType[i];
            }
            for (; i < genoTypeLength; i++) {
                this.genoType[i] = strong.genoType[i];
            }
        }
    }


    public byte getRandomGene () {
        return this.genoType[rand.nextInt(genoTypeLength)];
    }

    @Override
    public String toString() {
        return "GenoType{" +
                "genoType=" + Arrays.toString(genoType) +
                '}';
    }
}
