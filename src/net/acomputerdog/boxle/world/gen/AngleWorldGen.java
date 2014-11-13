package net.acomputerdog.boxle.world.gen;

public class AngleWorldGen extends SimpleWorldGen {

    @Override
    public int getGroundHeight(int x, int y) {
        if (x >= 0) {
            return x / 2;
        } else {
            return y / 2;
        }
    }
}
