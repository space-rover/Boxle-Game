package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.util.BlockMap;
import net.acomputerdog.boxle.save.util.RandomAccessBuffer;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Region implements Comparable<Region> {
    public static final int REGION_SIZE = 10;
    public static final int REGION_SIZE_BLOCKS = REGION_SIZE * Chunk.CHUNK_SIZE;

    private static final int chunkSize = Chunk.CHUNK_VOLUME * 4; // * 4
    /*
    private static final int chunkSpaceZ = REGION_SIZE;
    private static final int chunkSpaceX = chunkSpaceZ * chunkSpaceZ;
    private static final int chunkSpaceY = chunkSpaceZ * chunkSpaceZ * chunkSpaceZ;
    */

    private static final int chunkSpaceY = 1;
    private static final int chunkSpaceX = REGION_SIZE;
    private static final int chunkSpaceZ = REGION_SIZE * REGION_SIZE;

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
        //System.out.println("Creating region at " + loc.asCoords());
    }

    public void open() {
        if (file.isFile()) {
            //System.out.println("Loading region " + loc.asCoords());
            InputStream in = null;
            try {
                rab = new RandomAccessBuffer(in = new InflaterInputStream(new FileInputStream(file), new Inflater()));
                //rab = new RandomAccessBuffer(in = new FileInputStream(file));
                in.close();
            } catch (IOException e) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {}
                }
                world.getLogger().logError("Region " + loc.asCoords() + " could not be opened!  The chunks within may be lost!");
                rab = new RandomAccessBuffer();
                //throw new RuntimeException("Region file could not be opened!", e);
            }
        } else {
            rab = new RandomAccessBuffer();
        }
    }

    public void save() throws IOException {
        OutputStream out = null;
        try {
            rab.save(out = new DeflaterOutputStream(new FileOutputStream(file), new Deflater(1)));
            //rab.save(out = new FileOutputStream(file));
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
        //System.out.println("Closing region at " + loc.asCoords());
        rab.reset();
    }

    public boolean hasChunkGlobal(Vec3i cLoc) {
        if (cLoc == null || rab.length() < chunkSize) {
            return false;
        }
        long pos = findLoc(CoordConverter.chunkLocInRegion(cLoc.x), CoordConverter.chunkLocInRegion(cLoc.y), CoordConverter.chunkLocInRegion(cLoc.z));
        return pos < rab.length();
        /*
        if (loc >= rab.length()) {
            return false;
        }
        long pos = rab.position();
        rab.seek(loc);
        byte flag = rab.readByte();
        rab.seek(pos);
        return flag == 1;
        */
    }

    public boolean hasChunk(Vec3i cLoc) {
        if (cLoc == null || rab.length() < chunkSize) {
            return false;
        }
        long pos = findLoc(cLoc);
        return pos < rab.length();
        /*
        if (loc >= rab.length()) {
            return false;
        }
        long pos = rab.position();
        rab.seek(loc);
        byte flag = rab.readByte();
        rab.seek(pos);
        return flag == 1;
        */
    }

    public void writeChunk(Chunk chunk) throws IOException {
        Vec3i rLoc = VecPool.getVec3i(CoordConverter.chunkLocInRegion(chunk.getXLoc()), CoordConverter.chunkLocInRegion(chunk.getYLoc()), CoordConverter.chunkLocInRegion(chunk.getZLoc()));
        verifyChunkLoc(rLoc);
        //System.out.println("Writing chunk at " + chunk.asCoords() + " (" + rLoc.asCoords() + ")");
        //System.out.println("Writing chunk at " + chunk.asCoords());
        rab.seek(findLoc(rLoc));
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

    public Chunk readChunk(int x, int y, int z) throws IOException {
        //Vec3i vec = VecPool.getVec3i(x + (loc.x * REGION_SIZE), y + (loc.y * REGION_SIZE), z + (loc.z * REGION_SIZE));
        Vec3i vec = VecPool.getVec3i(x, y, z);
        Chunk chunk = readChunk(vec);
        VecPool.free(vec);
        return chunk;
    }

    public Chunk readChunk(Vec3i cLoc) throws IOException {
        //Vec3i rLoc = VecPool.getVec3i(cLoc.x + (loc.x * REGION_SIZE), cLoc.y + (loc.y * REGION_SIZE), cLoc.z + (loc.z * REGION_SIZE));
        Vec3i rLoc = VecPool.getVec3i(CoordConverter.chunkLocInRegion(cLoc.x), CoordConverter.chunkLocInRegion(cLoc.y), CoordConverter.chunkLocInRegion(cLoc.z));
        //System.out.println("Reading chunk at " + cLoc.asCoords() + " (" + rLoc.asCoords() + ")");
        verifyChunkLoc(rLoc);
        if (!hasChunk(rLoc)) {
            return null;
        }
        long loc = findLoc(rLoc);
        rab.seek(loc);
        Chunk chunk = new Chunk(world, cLoc);
        BlockMap bm = metaFile.getBlockMap();
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    int val = rab.readInt();
                    Block block = bm.getBlockForId(val);
                    if (block == null) {
                        System.err.println("No block for ID: " + val);
                    }
                    chunk.setBlockAt(x, y, z, block);
                }
            }
        }
        chunk.setModifiedFromLoad(false);
        VecPool.free(rLoc);
        return chunk;
    }

    private long findLoc(Vec3i cLoc) {
        return findLoc(cLoc.x, cLoc.y, cLoc.z);
    }

    private long findLoc(int x, int y, int z) {
        return ((Math.abs(x) * chunkSpaceX) + (Math.abs(y) * chunkSpaceY) + (Math.abs(z) * chunkSpaceZ)) * chunkSize;
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
        /*
        if (
                cLoc.x < loc.x * REGION_SIZE ||
                        cLoc.x >= (loc.x + 1) * REGION_SIZE ||
                        cLoc.y < loc.y * REGION_SIZE ||
                        cLoc.y >= (loc.y + 1) * REGION_SIZE ||
                        cLoc.z < loc.z * REGION_SIZE ||
                        cLoc.z >= (loc.z + 1) * REGION_SIZE
                )
            throw new IllegalArgumentException("Chunk at " + cLoc.asCoords() + " is out of bounds of region at " + loc.asCoords() + "!");
            */
        if (
                cLoc.x < loc.x * REGION_SIZE ||
                        cLoc.x >= 2 * REGION_SIZE ||
                        cLoc.y < loc.y * REGION_SIZE ||
                        cLoc.y >= 2 * REGION_SIZE ||
                        cLoc.z < loc.z * REGION_SIZE ||
                        cLoc.z >= 2 * REGION_SIZE
                )
            throw new IllegalArgumentException("Chunk at " + cLoc.asCoords() + " is out of bounds of region at " + loc.asCoords() + "!");
    }

    @Override
    public int compareTo(Region o) {
        return this.hashCode() - o.hashCode();
    }
}
