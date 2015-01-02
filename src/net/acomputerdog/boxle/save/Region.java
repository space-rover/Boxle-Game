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
    private static final int CHUNK_MARKER = 0x11111111;

    public static final int REGION_SIZE = 10;
    public static final int REGION_SIZE_BLOCKS = REGION_SIZE * Chunk.CHUNK_SIZE;

    private static final int chunkSize = (Chunk.CHUNK_VOLUME * 4) + 4; // +4 for chunk flag
    /*
    private static final int chunkSpaceZ = REGION_SIZE;
    private static final int chunkSpaceX = chunkSpaceZ * chunkSpaceZ;
    private static final int chunkSpaceY = chunkSpaceZ * chunkSpaceZ * chunkSpaceZ;
    */

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
        //System.out.println("Creating region at " + loc.asCoords());
    }

    public void open() {
        if (file.isFile()) {
            System.out.println("Loading region " + loc.asCoords());
            InputStream in = null;
            try {
                rab = new RandomAccessBuffer(in = new InflaterInputStream(new FileInputStream(file), new Inflater()));
                //rab = new RandomAccessBuffer(in = new FileInputStream(file));
                in.close();
                //System.out.println("Loaded region from disk.  Total size is " + rab.length() + " bytes.");
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
            System.err.println("Creating empty region!");
            rab = new RandomAccessBuffer();
        }
        rab.seek(0);
        //for (int count = 0; count < 100; count++) {
        //    System.out.println(rab.readInt());
        //}
        //rab.seek(0);
    }

    public void save() throws IOException {
        System.out.println("Saving region at " + loc.asCoords());
        OutputStream out = null;
        try {
            rab.save(out = new DeflaterOutputStream(new FileOutputStream(file), new Deflater(1)));
            //rab.save(out = new FileOutputStream(file));
            out.flush();
            out.close();
            //System.out.println("Saved region to disk.  Total size " + rab.length() + " bytes.");
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
        rab.clear();
    }

    public boolean hasChunkGlobal(Vec3i cLoc) {
        return !(cLoc == null || rab.length() < chunkSize) && hasChunkAt(findChunkLoc(CoordConverter.chunkLocInRegion(cLoc.x), CoordConverter.chunkLocInRegion(cLoc.y), CoordConverter.chunkLocInRegion(cLoc.z)));
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
        return cLoc != null && rab.length() >= chunkSize && hasChunkAt(findChunkLoc(cLoc));

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

    private boolean hasChunkAt(long pos) {
        //System.out.println("Looking for chunk at " + pos);
        if (pos + chunkSize >= rab.length()) return false;
        rab.seek(pos);
        //System.out.println(rab.readByte());
        //System.out.println(rab.readByte());
        //System.out.println(rab.readByte());
        //System.out.println(rab.readByte());
        //rab.seek(pos);
        int val = rab.readInt();
        //System.out.println("Chunk marker byte is " + val);
        return val == CHUNK_MARKER;
    }

    public void writeChunk(Chunk chunk) throws IOException {
        Vec3i rLoc = VecPool.getVec3i(CoordConverter.chunkLocInRegion(chunk.getXLoc()), CoordConverter.chunkLocInRegion(chunk.getYLoc()), CoordConverter.chunkLocInRegion(chunk.getZLoc()));
        verifyChunkLoc(rLoc);
        System.out.println("Writing chunk at " + chunk.asCoords() + " (" + rLoc.asCoords() + ")");
        //System.out.println("Writing chunk at " + chunk.asCoords());
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

    /*
    public Chunk readChunk(int x, int y, int z) throws IOException {
        //Vec3i vec = VecPool.getVec3i(x + (loc.x * REGION_SIZE), y + (loc.y * REGION_SIZE), z + (loc.z * REGION_SIZE));
        Vec3i vec = VecPool.getVec3i(x, y, z);
        Chunk chunk = readChunk(vec);
        VecPool.free(vec);
        return chunk;
    }
*/

    public Chunk readChunk(Vec3i cLoc) throws IOException {
        //Vec3i rLoc = VecPool.getVec3i(cLoc.x + (loc.x * REGION_SIZE), cLoc.y + (loc.y * REGION_SIZE), cLoc.z + (loc.z * REGION_SIZE));
        Vec3i rLoc = VecPool.getVec3i(CoordConverter.chunkLocInRegion(cLoc.x), CoordConverter.chunkLocInRegion(cLoc.y), CoordConverter.chunkLocInRegion(cLoc.z));
        System.out.println("Reading chunk at " + cLoc.asCoords() + " (" + rLoc.asCoords() + "/" + findChunkLoc(rLoc) + ")");
        verifyChunkLoc(rLoc);
        if (!hasChunk(rLoc)) {
            return null;
        }
        long loc = findChunkLoc(rLoc);
        rab.seek(loc);
        if (rab.readInt() != CHUNK_MARKER) {
            System.err.println("Reading chunk from invalid area!");
        }
        long offset = 4;
        Chunk chunk = new Chunk(world, cLoc);
        BlockMap bm = metaFile.getBlockMap();
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    long pos = rab.position();
                    if (pos != loc + offset) {
                        System.err.println("RAB position moved!  Expected " + (loc + offset) + ", got " + pos);
                    }
                    int val = rab.readInt();
                    offset += 4;
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

    private long findChunkLoc(Vec3i cLoc) {
        return findChunkLoc(cLoc.x, cLoc.y, cLoc.z);
    }

    private long findChunkLoc(int x, int y, int z) {
        //System.out.println("Chunk offset for " + x +"," + y + "," + z +": " + (((x * chunkSpaceX) + (y * chunkSpaceY) + (z * chunkSpaceZ)) * chunkSize));
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
        /*
        if (
                cLoc.x < loc.x * REGION_SIZE ||
                        cLoc.x >= 2 * REGION_SIZE ||
                        cLoc.y < loc.y * REGION_SIZE ||
                        cLoc.y >= 2 * REGION_SIZE ||
                        cLoc.z < loc.z * REGION_SIZE ||
                        cLoc.z >= 2 * REGION_SIZE
                )
            throw new IllegalArgumentException("Chunk at " + cLoc.asCoords() + " is out of bounds of region at " + loc.asCoords() + "!");
            */
        if (
                cLoc.x < 0 ||
                        cLoc.x >= REGION_SIZE ||
                        cLoc.y < 0 ||
                        cLoc.y >= REGION_SIZE ||
                        cLoc.z < 0 ||
                        cLoc.z >= REGION_SIZE
                )
            throw new IllegalArgumentException("Chunk at " + cLoc.asCoords() + " is out of bounds of region at " + loc.asCoords() + "!");
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
