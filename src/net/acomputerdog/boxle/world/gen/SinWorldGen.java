package net.acomputerdog.boxle.world.gen;

public class SinWorldGen extends SimpleWorldGen {

    @Override
    public int getGroundHeight(int x, int y) {
        return (int) Math.floor(Math.sin(x / 10d) * 5d + Math.sin(y / 10d) * 5d);
    }
}
