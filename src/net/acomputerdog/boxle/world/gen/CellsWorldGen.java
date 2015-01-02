package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.gen.noise.OpenSimplexNoise;

public class CellsWorldGen extends AbstractWorldGen {
    private static final int SPLIT_HEIGHT = 0;
    private static final int SPIRE_HEIGHT_BOOST = 3;
    private static final int MIN_SPIRE_HEIGHT = 4;
    private static final double SPIRE_LOC_SCALE = 8d;
    private static final double SPIRE_COORD_SCALE = 20d;
    private static final double TERRAIN_COORD_SCALE = 20d;
    private static final double WEATHER_COORD_SCALE = 100d;

    private static final int chunkSize = Chunk.CHUNK_SIZE;

    private final OpenSimplexNoise simplex;
    private final long seed;

    public CellsWorldGen(long seed) {
        super(seed);
        this.seed = seed;
        simplex = new OpenSimplexNoise(seed);
    }

    @Override
    public void generateTerrain(Chunk chunk) {
        if (chunk.isGenerated()) {
            chunk.getWorld().getLogger().logWarning("Attempted to generate a chunk twice at " + chunk.asCoords());
        } else {
            Vec3i cLoc = chunk.getLocation();
            int chunkY = cLoc.y * chunkSize;
            if (chunkY + chunkSize < 0) {
                chunk.clear(Blocks.steel);
                return;
            }
            int chunkX = cLoc.x * chunkSize;
            int chunkZ = cLoc.z * chunkSize;
            for (int x = 0; x < chunkSize; x++) {
                for (int z = 0; z < chunkSize; z++) {
                    int height = getGroundHeight(chunkX + x, chunkZ + z);
                    for (int y = 0; y < chunkSize; y++) {
                        int currY = y + chunkY;
                        if (currY > height) {
                            chunk.setBlockAt(x, y, z, Blocks.air);
                        } else if (currY == height) {
                            chunk.setBlockAt(x, y, z, Blocks.grassySteel);
                        } else {
                            chunk.setBlockAt(x, y, z, Blocks.steel);
                        }
                    }
                }
            }
        }
        chunk.markGenerated();
        chunk.setNeedsRebuild(true);
        chunk.setModifiedFromLoad(true);
    }

    public int getGroundHeight(int x, int y) {
        int simplexHeight = (int) Math.floor(simplex.eval(x / TERRAIN_COORD_SCALE, y / TERRAIN_COORD_SCALE) * TERRAIN_COORD_SCALE);
        int spireHeight = getSpireHeight(x, y);
        return simplexHeight >= spireHeight && simplexHeight > MIN_SPIRE_HEIGHT ? spireHeight : SPLIT_HEIGHT;
    }

    private int getSpireHeight(int x, int y) {
        x = (int) Math.floor(x / SPIRE_LOC_SCALE);
        y = (int) Math.floor(y / SPIRE_LOC_SCALE);
        return Math.max((int) Math.floor(simplex.eval(x / SPIRE_COORD_SCALE, y / SPIRE_COORD_SCALE) * SPIRE_COORD_SCALE) + SPIRE_HEIGHT_BOOST, MIN_SPIRE_HEIGHT);
    }

    @Override
    public float getRainfall(Vec3i loc) {
        return (float) simplex.eval(loc.x / WEATHER_COORD_SCALE, loc.z / WEATHER_COORD_SCALE);
    }

    @Override
    public float getTemperature(Vec3i loc) {
        return (float) simplex.eval(loc.x / WEATHER_COORD_SCALE, loc.z / WEATHER_COORD_SCALE);
    }

    @Override
    public long getSeed() {
        return seed;
    }
}
