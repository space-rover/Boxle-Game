package net.acomputerdog.boxle.world.gen.simple;

public class FlatWorldGen extends WorldGenBase {
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
