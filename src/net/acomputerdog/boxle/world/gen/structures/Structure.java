package net.acomputerdog.boxle.world.gen.structures;

import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.gen.WorldGen;
import net.acomputerdog.core.identity.Identifiable;

public abstract class Structure implements Identifiable {
    protected static final int CHUNK_SIZE = Chunk.CHUNK_SIZE;

    public abstract void placeIntoWorld(WorldGen gen, Chunk chunk, int x, int y, int z);

    public abstract boolean canPlaceAt(WorldGen gen, Chunk chunk, int x, int y, int z);

    public void placeIfPossible(WorldGen gen, Chunk chunk, int x, int y, int z) {
        if (canPlaceAt(gen, chunk, x, y, z)) {
            placeIntoWorld(gen, chunk, x, y, z);
        }
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj == this || obj instanceof Structure && getId().equals(((Structure) obj).getId()));
    }

    @Override
    public String toString() {
        return getName();
    }
}
