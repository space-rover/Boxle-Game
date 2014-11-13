package net.acomputerdog.boxle.world.gen;

public class FlatWorldGen extends SimpleWorldGen {
    private final int height;

    public FlatWorldGen(int height) {
        this.height = height;
    }

    public FlatWorldGen() {
        this(10);
    }

    @Override
    public int getGroundHeight(int x, int y) {
        return height;
    }

}
