package net.acomputerdog.boxle.world.gen.structures.types;

import net.acomputerdog.boxle.block.registry.Blocks;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.boxle.world.gen.structures.Structure;

import java.util.Random;

public class StructureTree extends Structure {

    @Override
    public void placeIntoWorld(Chunk chunk, int x, int y, int z) {
        if (chunk.getBlockAt(x, y, z) == Blocks.air) { //don't add in gLoc, because this is accessing through the chunk.
            Vec3i gLoc = CoordConverter.chunkToGlobal(chunk.getLocation());
            World world = chunk.getWorld();
            long seed = world.getGenerator().getSeed();
            Random random = new Random(seed * (x + (y * CHUNK_SIZE) + (z * CHUNK_SIZE * 2)));
            int trunkHeight = random.nextInt(7) + 5;
            int minLeafHeight = random.nextInt(Math.max(trunkHeight - 7, 1)) + 5;
            //int initialLeafWidth = random.nextInt(4) + 1;
            //int finalLeafWidth = random.nextInt(initialLeafWidth - 1) + 1;
            //float leafLayerScale = (initialLeafWidth - finalLeafWidth) / ((trunkHeight + 1) - minLeafHeight);
            int leafRingScale = (int) Math.ceil((trunkHeight - minLeafHeight) / 2f);
            for (int tY = 0; tY <= trunkHeight; tY++) {
                if (world.getBlockAt(gLoc.x + x, gLoc.y + tY + y, gLoc.z + z) != Blocks.air) {
                    break;
                }
                world.setBlockAt(gLoc.x + x, gLoc.y + tY + y, gLoc.z + z, Blocks.wood);
                if (tY >= minLeafHeight) {
                    genLeafRing(world, gLoc.x + x, gLoc.y + tY + y, gLoc.z + z, leafRingScale, false);
                }
                if (tY == trunkHeight) {
                    genLeafRing(world, gLoc.x + x, gLoc.y + tY + y + 1, gLoc.z + z, 1, true);
                }
                /*
                if (tY == minLeafHeight + y) {
                    genLeafRing(world, x, tY, z, initialLeafWidth);
                } else {
                    genLeafRing(world, x, tY, z, (int)Math.floor(initialLeafWidth * leafLayerScale * (tY - y)));
                }
                if (tY == trunkHeight + y) {
                    genLeafRing(world, x, tY + 1, z, finalLeafWidth);
                }
                */
            }
            VecPool.free(gLoc);
        }
    }

    private void genLeafRing(World world, int x, int y, int z, int width, boolean includeCenter) {
        for (int currX = x - width; currX <= x + width; currX++) {
            for (int currZ = z - width; currZ <= z + width; currZ++) {
                if (includeCenter || !(x == currX && z == currZ)) {
                    if (world.getBlockAt(currX, y, currZ) == Blocks.air) {
                        world.setBlockAt(currX, y, currZ, Blocks.leaves);
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
