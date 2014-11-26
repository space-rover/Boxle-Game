package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.gen.structures.Structure;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorldGen implements WorldGen {
    protected static final int chunkSize = Chunk.CHUNK_SIZE;

    protected final long seed;

    protected final List<Structure> decorations = new LinkedList<>();

    protected AbstractWorldGen(long seed) {
        this.seed = seed;
    }

    @Override
    public void generateDecorations(Chunk chunk) {
        if (chunk.isDecorated()) {
            chunk.getWorld().getLogger().logWarning("Already decorated chunk at " + chunk.getLocation());
        } else {
            for (int x = 0; x < chunkSize; x++) {
                for (int y = 0; y < chunkSize; y++) {
                    for (int z = 0; z < chunkSize; z++) {
                        for (Structure structure : decorations) {
                            structure.placeIfPossible(this, chunk, x, y, z);
                        }
                    }
                }
            }
        }
        chunk.markDecorated();
        chunk.setNeedsRebuild(true);
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public void addDecoration(Structure structure) {
        decorations.add(structure);
    }
}
