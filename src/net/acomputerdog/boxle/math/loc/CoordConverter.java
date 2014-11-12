package net.acomputerdog.boxle.math.loc;

import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;

public class CoordConverter {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    public static Vec3i globalToChunk(Vec3i global) {
        global.x = (int) Math.floor((float) global.x / (float) chunkSize);
        global.y = (int) Math.floor((float) global.y / (float) chunkSize);
        global.z = (int) Math.floor((float) global.z / (float) chunkSize);
        return global;
    }

    public static Vec3i chunkToGlobal(Vec3i chunk) {
        chunk.x *= chunkSize;
        chunk.y *= chunkSize;
        chunk.z *= chunkSize;
        return chunk;
    }
}
