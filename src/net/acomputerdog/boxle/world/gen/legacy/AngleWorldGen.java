package net.acomputerdog.boxle.world.gen.legacy;

public class AngleWorldGen extends WorldGenBase {

    @Override
    public int getGroundHeight(int x, int y) {
        if (x >= 0) {
            return x / 2;
        } else {
            return y / 2;
        }
    }
}
