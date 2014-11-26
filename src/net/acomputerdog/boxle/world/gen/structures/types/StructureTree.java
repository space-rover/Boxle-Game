package net.acomputerdog.boxle.world.gen.structures.types;

import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.boxle.world.gen.WorldGen;
import net.acomputerdog.boxle.world.gen.structures.Structure;

import java.util.Random;

public class StructureTree extends Structure {

    @Override
    public void placeIntoWorld(WorldGen gen, Chunk chunk, int x, int y, int z) {
        if (chunk.getBlockAt(x, y, z) == Blocks.air) { //don't add in gLoc, because this is accessing through the chunk.
            Vec3i gLoc = CoordConverter.chunkToGlobal(chunk.getLocation());
            World world = chunk.getWorld();
            long seed = world.getGenerator().getSeed();
            Random random = new Random(seed * (x + (y * CHUNK_SIZE) + (z * CHUNK_SIZE * 2)));
            int trunkHeight = random.nextInt(7) + 5;
            int minLeafHeight = random.nextInt(Math.max(trunkHeight - 5, 1)) + 5;
            int leafRingScale = (int) Math.ceil((trunkHeight - minLeafHeight) / 2f);
            for (int tY = 0; tY <= trunkHeight; tY++) {
                if (world.getBlockAt(gLoc.x + x, gLoc.y + tY + y, gLoc.z + z) != Blocks.air) {
                    break;
                }
                world.setBlockAt(gLoc.x + x, gLoc.y + tY + y, gLoc.z + z, Blocks.wood);
                if (tY >= minLeafHeight && tY < trunkHeight) {
                    genLeafRing(world, gLoc.x + x, gLoc.y + tY + y, gLoc.z + z, leafRingScale, false, true);
                }
                if (tY == trunkHeight) {
                    genLeafRing(world, gLoc.x + x, gLoc.y + tY + y, gLoc.z + z, leafRingScale, true, leafRingScale == 1);
                    genLeafRing(world, gLoc.x + x, gLoc.y + tY + y + 1, gLoc.z + z, 1, true, leafRingScale != 1);
                    world.setBlockAt(gLoc.x + x, gLoc.y + tY + y + 2, gLoc.z + z, Blocks.leaves);
                }
            }
            VecPool.free(gLoc);
        }
    }

    @Override
    public boolean canPlaceAt(WorldGen gen, Chunk chunk, int x, int y, int z) {
        if (x < 0 || x >= CHUNK_SIZE || y < 0 || y >= CHUNK_SIZE || z < 0 || z >= CHUNK_SIZE) {
            return false;
        }
        World world = chunk.getWorld();
        Vec3i gLoc = CoordConverter.chunkToGlobal(chunk.getLocation());
        Vec3i loc = VecPool.getVec3i();
        loc.x = x + gLoc.x;
        loc.y = y + gLoc.y;
        loc.z = z + gLoc.z;
        boolean canPlace = false;
        if (world.getBlockAt(loc) == Blocks.air && !world.getBlockAt(loc.x, loc.y - 1, loc.z).isTransparent()) {
            Random random = new Random(gen.getSeed() * (loc.x + (loc.y * CHUNK_SIZE) + (loc.z * CHUNK_SIZE * 2)));
            int surface = chunk.getGroundHeight(x, z) + 1;
            if (y == surface) {
                float temp = gen.getRainfall(loc);
                if (random.nextFloat() <= gen.getRainfall(loc) && temp >= 0f && temp <= .5f && random.nextInt(100) == 0) {
                    canPlace = true;
                }
            }
        }
        VecPool.free(loc);
        VecPool.free(gLoc);
        return canPlace;
    }

    private void genLeafRing(World world, int x, int y, int z, int width, boolean includeCenter, boolean includeCorners) {
        for (int currX = x - width; currX <= x + width; currX++) {
            for (int currZ = z - width; currZ <= z + width; currZ++) {
                if (includeCorners || !((currX == x + width && currZ == z + width) || (currX == x + width && currZ == z - width) || (currX == x - width && currZ == z + width) || (currX == x - width && currZ == z - width))) {
                    if (includeCenter || !(x == currX && z == currZ)) {
                        if (world.getBlockAt(currX, y, currZ) == Blocks.air) {
                            world.setBlockAt(currX, y, currZ, Blocks.leaves);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getId() {
        return "tree";
    }

    @Override
    public String getDefinition() {
        return "tree";
    }

    @Override
    public String getName() {
        return "Tree";
    }

}
