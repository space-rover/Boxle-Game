package net.acomputerdog.boxle.save.world.files;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.util.BlockMap;
import net.acomputerdog.boxle.save.util.RandomAccessBuffer;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.logger.CLogger;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

//todo: Add isModifiedFromLoad
public class Region implements Comparable<Region> {
    private static final CLogger logger = new CLogger("Region_IO", false, true);

    private static final int CHUNK_MARKER = 0x11111111;

    public static final int REGION_SIZE = 10;
    public static final int REGION_SIZE_BLOCKS = REGION_SIZE * Chunk.CHUNK_SIZE;

    private static final int chunkSize = (Chunk.CHUNK_VOLUME * 4) + 4; // +4 for chunk flag

    private static final int chunkSpaceY = REGION_SIZE * REGION_SIZE;
    private static final int chunkSpaceX = REGION_SIZE;
    private static final int chunkSpaceZ = 1;

    private final WorldMetaFile metaFile;
    private final World world;
    private final File file;
    private final Vec3i loc;

    private RandomAccessBuffer rab;

    public Region(WorldMetaFile metaFile, File file, Vec3i loc) {
        this.metaFile = metaFile;
        this.world = metaFile.getWorld();
        this.file = file;
        this.loc = loc;
    }

    public void open() {
        if (file.isFile()) {
            InputStream in = null;
            try {
                rab = new RandomAccessBuffer(in = new InflaterInputStream(new FileInputStream(file), new Inflater()));
                in.close();
            } catch (IOException e) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {}
                }
                logger.logError("Region at " + world.getName() + "/" + loc.asCoords() + " could not be opened!  The chunks within may be lost!");
                rab = new RandomAccessBuffer();
            }
        } else {
            rab = new RandomAccessBuffer();
        }
    }

    public void save() throws IOException {
        OutputStream out = null;
        try {
            rab.save(out = new DeflaterOutputStream(new FileOutputStream(file), new Deflater(1)));
            out.flush();
            out.close();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public void close() {
        rab.clear();
    }

    public boolean hasChunkGlobal(Vec3i cLoc) {
        return !(cLoc == null || rab.length() < chunkSize) && hasChunkAt(findChunkLoc(CoordConverter.chunkLocInRegion(cLoc.x), CoordConverter.chunkLocInRegion(cLoc.y), CoordConverter.chunkLocInRegion(cLoc.z)));
    }

    public boolean hasChunk(Vec3i cLoc) {
        return cLoc != null && rab.length() >= chunkSize && hasChunkAt(findChunkLoc(cLoc));
    }

    private boolean hasChunkAt(long pos) {
        if (pos + chunkSize >= rab.length()) return false;
        rab.seek(pos);
        return rab.readInt() == CHUNK_MARKER;
    }

    public void writeChunk(Chunk chunk) throws IOException {
        Vec3i rLoc = VecPool.getVec3i(CoordConverter.chunkLocInRegion(chunk.getXLoc()), CoordConverter.chunkLocInRegion(chunk.getYLoc()), CoordConverter.chunkLocInRegion(chunk.getZLoc()));
        verifyChunkLoc(rLoc);
        rab.seek(findChunkLoc(rLoc));
        rab.writeInt(CHUNK_MARKER);
        BlockMap bm = metaFile.getBlockMap();
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    rab.writeInt(bm.getIdForBlock(chunk.getBlockAt(x, y, z)));
                }
            }
        }
        chunk.setModifiedFromLoad(false);
        VecPool.free(rLoc);
    }

    public Chunk readChunk(Vec3i cLoc) throws IOException {
        Vec3i rLoc = VecPool.getVec3i(CoordConverter.chunkLocInRegion(cLoc.x), CoordConverter.chunkLocInRegion(cLoc.y), CoordConverter.chunkLocInRegion(cLoc.z));
        verifyChunkLoc(rLoc);
        if (!hasChunk(rLoc)) {
            return null;
        }
        long loc = findChunkLoc(rLoc);
        rab.seek(loc);
        if (rab.readInt() != CHUNK_MARKER) {
            logger.logError("Reading chunk from invalid area!");
            logger.logError("This should not happen, please report this error!");
        }
        long offset = 4;
        Chunk chunk = new Chunk(world, cLoc);
        BlockMap bm = metaFile.getBlockMap();
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    long pos = rab.position();
                    if (pos != loc + offset) {
                        logger.logError("RAB position moved!  Expected " + (loc + offset) + ", got " + pos + "!");
                        logger.logError("This should not happen, please report this error!");
                    }
                    int val = rab.readInt();
                    offset += 4;
                    Block block = bm.getBlockForId(val);
                    if (block == null) {
                        logger.logWarning("No block for ID: " + val + "!  Air block will be loaded instead!");
                        logger.logError("This should not happen, please report this error!");
                        block = Blocks.air;
                    }
                    chunk.setBlockAt(x, y, z, block);
                }
            }
        }
        chunk.setModifiedFromLoad(false);
        VecPool.free(rLoc);
        return chunk;
    }

    private long findChunkLoc(Vec3i cLoc) {
        return findChunkLoc(cLoc.x, cLoc.y, cLoc.z);
    }

    private long findChunkLoc(int x, int y, int z) {
        return ((x * chunkSpaceX) + (y * chunkSpaceY) + (z * chunkSpaceZ)) * chunkSize;
    }

    public File getFile() {
        return file;
    }

    public Vec3i getLoc() {
        return loc.copy();
    }

    public World getWorld() {
        return world;
    }

    private void verifyChunkLoc(Vec3i cLoc) {
        if (cLoc.x < 0 || cLoc.x >= REGION_SIZE || cLoc.y < 0 || cLoc.y >= REGION_SIZE || cLoc.z < 0 || cLoc.z >= REGION_SIZE) {
            throw new IllegalArgumentException("Chunk at " + cLoc.asCoords() + " is out of bounds of region at " + loc.asCoords() + "!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Region)) return false;

        Region region = (Region) o;

        return loc.equals(region.loc) && world.equals(region.world);

    }

    @Override
    public int hashCode() {
        int result = world.hashCode();
        result = 31 * result + loc.hashCode();
        return result;
    }

    @Override
    public int compareTo(Region o) {
        return this.hashCode() - o.hashCode();
    }

    @Override
    public String toString() {
        return "Region{" +
                "loc=" + loc +
                ", world=" + world +
                '}';
    }
}
