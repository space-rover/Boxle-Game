package net.acomputerdog.boxle.math.loc;

import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.save.Region;
import net.acomputerdog.boxle.world.Chunk;

//todo move to another package
public class CoordConverter {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    public static Vec3i globalToChunk(Vec3i global) {
        global.x = (int) Math.floor((float) global.x / (float) chunkSize);
        global.y = (int) Math.floor((float) global.y / (float) chunkSize);
        global.z = (int) Math.floor((float) global.z / (float) chunkSize);
        return global;
    }

    public static Vec3i globalToBlock(Vec3i global) {
        global.x = globalToBlock(global.x);
        global.y = globalToBlock(global.y);
        global.z = globalToBlock(global.z);
        return global;
    }

    public static int globalToBlock(int global) {
        if (global >= 0) {
            return global % chunkSize;
        } else {
            return chunkSize - 1 + ((global + 1) % chunkSize);
        }

    }

    public static Vec3i chunkToGlobal(Vec3i chunk) {
        chunk.x *= chunkSize;
        chunk.y *= chunkSize;
        chunk.z *= chunkSize;
        return chunk;
    }

    public static int chunkLocInRegion(int loc) {
        return Math.abs(loc) % Region.REGION_SIZE;
    }

    public static int regionLocOfChunk(int loc) {
        return (int) Math.floor((float) loc / (float) Region.REGION_SIZE);
    }

    public static void main(String[] args) {
        for (int x = -35; x <= 20; x++) {
            System.out.println(x + " -> " + globalToBlock(x));
        }
    }
}
