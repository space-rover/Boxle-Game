package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.math.vec.Vec3f;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.World;

import java.io.*;
import java.util.*;

public class WorldMetaFile {
    private final World world;
    private final File file;

    private Map<Integer, Block> readBlockMap = new HashMap<>();
    private Map<Block, Integer> writeBlockMap = new HashMap<>();
    private Vec3f playerLoc;
    private Vec3f playerRot;

    public WorldMetaFile(World world, File file) {
        this.world = world;
        this.file = file;
    }

    public void load() throws IOException {
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(file));

            playerLoc = VecPool.getVec3f(in.readFloat(), in.readFloat(), in.readFloat());
            playerRot = VecPool.getVec3f(in.readFloat(), in.readFloat(), in.readFloat());
            readBlockMap(in);

            reverseReadMap();
            in.close();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public void create() {
        readBlockMap = new LinkedHashMap<>();
        writeBlockMap = new LinkedHashMap<>();
    }

    public void save() throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));

            out.writeFloat(playerLoc.x);
            out.writeFloat(playerLoc.y);
            out.writeFloat(playerLoc.z);
            out.writeFloat(playerRot.x);
            out.writeFloat(playerRot.y);
            out.writeFloat(playerRot.z);
            writeBlockMap(out);

            out.close();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public Map<Integer, Block> getReadBlockMap() {
        return readBlockMap;
    }

    public Map<Block, Integer> getWriteBlockMap() {
        return writeBlockMap;
    }

    public World getWorld() {
        return world;
    }

    public Vec3f getPlayerLoc() {
        return playerLoc;
    }

    public Vec3f getPlayerRot() {
        return playerRot;
    }

    private void writeBlockMap(DataOutput out) throws IOException {
        Collection<Block> blocks = readBlockMap.values();
        out.writeInt(blocks.size());
        for (Block block : blocks) {
            out.writeUTF(block.getDefinition());
        }
    }

    private void reverseReadMap() {
        writeBlockMap = new HashMap<>();
        Set<Integer> shorts = readBlockMap.keySet();
        for (int id : shorts) {
            writeBlockMap.put(readBlockMap.get(id), id);
        }
    }

    private void readBlockMap(DataInput in) throws IOException {
        readBlockMap = new LinkedHashMap<>();
        int numIds = in.readInt();
        for (int id = 0; id < numIds; id++) {
            readBlockMap.put(id, Blocks.BLOCKS.getFromDef(in.readUTF()));
        }
    }
}
