package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.world.gen.noise.OpenSimplexNoise;

public class SimplexWorldGen extends SimpleWorldGen {
    private final OpenSimplexNoise simplex;

    public SimplexWorldGen(long seed) {
        simplex = new OpenSimplexNoise(seed);
    }

    @Override
    public int getGroundHeight(int x, int y) {
        //Alternate mode: return (int)Math.floor(simplex.eval(x / 20d, y / 20d) * 10d);
        //Alternate mode: return (int)Math.floor(simplex.eval(x / 50d, y / 50d) * 10d);
        return (int) Math.floor(simplex.eval(x / 50d, y / 50d) * 20d);
    }

    public OpenSimplexNoise getSimplex() {
        return simplex;
    }
}
