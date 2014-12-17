package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.save.util.RandomAccessBuffer;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Region {
    public static final int REGION_SIZE = 10;
    public static final int REGION_SIZE_BLOCKS = REGION_SIZE * Chunk.CHUNK_SIZE;

    private static final int chunkSize = Chunk.CHUNK_VOLUME + 1;
    private static final int chunkSpaceZ = REGION_SIZE;
    private static final int chunkSpaceX = chunkSpaceZ * chunkSpaceZ;
    private static final int chunkSpaceY = chunkSpaceZ * chunkSpaceZ * chunkSpaceZ;

    private final WorldMetaFile worldFile;
    private final World world;
    private final File file;
    private final Vec3i loc;

    private RandomAccessBuffer rab;

    public Region(WorldMetaFile worldFile, File file, Vec3i loc) {
        this.worldFile = worldFile;
        this.world = worldFile.getWorld();
        this.file = file;
        this.loc = loc;
    }

    public void open() {
        if (file.isFile()) {
            InputStream in = null;
            try {
                rab = new RandomAccessBuffer(in = new GZIPInputStream(new FileInputStream(file)));
                in.close();
            } catch (IOException e) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {}
                }
                throw new RuntimeException("Region file could not be opened!", e);
            }
        } else {
            rab = new RandomAccessBuffer();
        }
    }

    public void save() throws IOException {
        OutputStream out = null;
        try {
            rab.save(out = new GZIPOutputStream(new FileOutputStream(file)));
            out.close();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public boolean hasChunk(Vec3i cLoc) {
        if (cLoc == null) {
            return false;
        }
        long loc = findLoc(cLoc);
        if (loc >= rab.length()) {
            return false;
        }
        long pos = rab.position();
        rab.seek(loc);
        byte flag = rab.readByte();
        rab.seek(pos);
        return flag == 1;
    }

    public void writeChunk(Chunk chunk) throws IOException {
        rab.seek(findLoc(chunk.getLocation()));
        Map<Block, Integer> blockMap = worldFile.getWriteBlockMap();
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    rab.writeInt(blockMap.get(chunk.getBlockAt(x, y, z)));
                }
            }
        }
        chunk.setModifiedFromLoad(false);
    }

    public Chunk readChunk(Vec3i cLoc) throws IOException {
        long loc = findLoc(cLoc);
        rab.seek(loc);
        if (!hasChunk(cLoc)) {
            return null;
        }
        Chunk chunk = new Chunk(world, cLoc);
        Map<Integer, Block> blockMap = worldFile.getReadBlockMap();
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    int val = rab.readInt();
                    chunk.setBlockAt(x, y, z, blockMap.get(val));
                }
            }
        }
        chunk.setModifiedFromLoad(false);
        return chunk;
    }

    private long findLoc(Vec3i cLoc) {
        return (cLoc.x * chunkVolume * chunkVolume) + (cLoc.y * chunkVolume) + (cLoc.z * chunkSize);
    }

    public File getFile() {
        return file;
    }

    public Vec3i getLoc() {
        return loc;
    }

    public World getWorld() {
        return world;
    }
}
