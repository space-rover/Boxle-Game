package net.acomputerdog.boxle.world.gen.structures;

import net.acomputerdog.boxle.block.dynamic.Identifiable;
import net.acomputerdog.boxle.world.Chunk;

public abstract class Structure implements Identifiable {
    protected static final int CHUNK_SIZE = Chunk.CHUNK_SIZE;

    public abstract void placeIntoWorld(Chunk chunk, int x, int y, int z);

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        return obj instanceof Structure && getId().equals(((Structure) obj).getId());
    }

    @Override
    public String toString() {
        return getName();
    }
}
