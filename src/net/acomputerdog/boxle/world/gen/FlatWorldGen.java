package net.acomputerdog.boxle.world.gen;

public class FlatWorldGen extends AbstractWorldGen {

    @Override
    public int getGroundHeight(int x, int y) {
        return 10;
    }

}
