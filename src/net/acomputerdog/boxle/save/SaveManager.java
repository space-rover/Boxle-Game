package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.world.WorldSave;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.java.Patterns;
import net.acomputerdog.core.logger.CLogger;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SaveManager {
    public static final CLogger LOGGER = new CLogger("World_Saves", false, true);

    private static final String UNDERSCORE = Patterns.quote("_");

    private static final File worldsDir = createWorldsDir();

    public static boolean worldExists(String name) {
        return new File(worldsDir, name).isDirectory();
    }

    private static final Map<String, WorldSave> saveMap = new HashMap<>();

    public static WorldSave createWorld(String name) {
        WorldSave save = saveMap.get(name);
        if (save == null) {
            save = new WorldSave(name);
            save.createWorld();
        }
        return save;
    }

    public static void saveWorld(World world) throws IOException {
        saveMap.get(world.getName()).save();
    }

    public static boolean hasChunk(World world, int x, int y, int z) {
        return new File(worldsDir, "/" + world.getName() + "/chunks/" + x + "_" + y + "_" + z + ".chunk").isFile();
    }

    public static void saveChunkDelayed(Chunk chunk) {
        IOThread.getThread(chunk.getWorld()).addSave(chunk);
    }

    public static void saveChunk(Chunk chunk) throws IOException {
        if (chunk.isModifiedFromLoad()) {
            chunk.setModifiedFromLoad(false);
            File chunkFile = getChunkFile(chunk);
            DataOutputStream out = null;
            try {
                out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(chunkFile)));
                Map<Block, Short> blockMap = writeBlockMap(out, chunk);
                for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                    for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                        for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                            out.writeShort(blockMap.get(chunk.getBlockAt(x, y, z)));
                        }
                    }
                }
                chunk.setModifiedFromLoad(false);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {}
                }
            }
        }
    }

    private static Map<Block, Short> writeBlockMap(DataOutput out, Chunk chunk) throws IOException {
        Map<Block, Short> blockMap = new LinkedHashMap<>();
        short nextId = 0;
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    Block block = chunk.getBlockAt(x, y, z);
                    if (!blockMap.containsKey(block)) {
                        blockMap.put(block, nextId);
                        nextId++;
                    }
                }
            }
        }
        Set<Block> blocks = blockMap.keySet();
        out.writeShort(blocks.size());
        for (Block block : blocks) {
            out.writeUTF(block.getDefinition());
        }
        return blockMap;
    }

    public static World loadWorld(String name) throws IOException {
        World world = new World(Boxle.instance(), name);
        File worldDir = getWorldDir(name);
        if (!worldDir.isDirectory()) {
            throw new FileNotFoundException("World \"" + name + "\" does not exist!");
        }
        File chunksDir = new File(worldDir, "/chunks/");
        if (!chunksDir.isDirectory()) {
            throw new FileNotFoundException("World is missing chunks directory!");
        }
        File metaFile = new File(worldDir, "meta.dat");
        if (!metaFile.isFile()) {
            throw new FileNotFoundException("Word is missing meta.dat file!");
        }

        return world;
    }

    public static void loadChunkDelayed(World world, Vec3i loc) {
        IOThread.getThread(world).addLoad(loc);
    }

    public static Chunk loadChunk(World world, Vec3i loc) throws IOException {
        return loadChunk(world, new File(getWorldDir(world.getName()), "/chunks/" + loc.x + "_" + loc.y + "_" + loc.z + ".chunk"));
    }

    public static Chunk loadChunk(World world, File file) throws IOException {
        String fileName = file.getName();
        String[] nameParts = fileName.substring(0, fileName.length() - 6).split(UNDERSCORE);
        if (nameParts.length < 3) {
            throw new IllegalArgumentException("Passed file is not a valid chunk!");
        }
        Chunk chunk;
        try {
            chunk = new Chunk(world, VecPool.getVec3i(Integer.parseInt(nameParts[0]), Integer.parseInt(nameParts[1]), Integer.parseInt(nameParts[2])));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Passed file is not a valid chunk!", e);
        }
        DataInputStream in = null;
        try {
            in = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
            readChunk(chunk, in);
            return chunk;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public static void readChunk(Chunk chunk, DataInput in) throws IOException {
        /*Map<Short, Block> blockMap = readBlockMap(in);
        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    short val = in.readShort();
                    chunk.setBlockAt(x, y, z, blockMap.get(val));
                }
            }
        }
        chunk.setModifiedFromLoad(false);
        */
    }

    public static File getRegionFile(String world, int x, int y, int z) {
        File dir = new File(getWorldDir(world), "/regions/");
        if (!(dir.isDirectory() || dir.mkdirs())) {
            LOGGER.logWarning("Unable to create regions directory for world " + world);
        }
        return new File(dir, "/" + x + "_" + y + "_" + z + ".region");
    }

    private static File createWorldsDir() {
        File worlds = new File("./worlds/");
        if (!(worlds.isDirectory() || worlds.mkdirs())) {
            LOGGER.logWarning("Unable to create saves directory!");
        }
        return worlds;
    }

    public static File getWorldDir(String name) {
        File dir = new File(worldsDir, "/" + name + "/");
        if (!(dir.isDirectory() || dir.mkdirs())) {
            LOGGER.logWarning("Unable to create world directory for world " + name);
        }
        return dir;
    }

    private static File getChunkFile(Chunk chunk) {
        File dir = new File(getWorldDir(chunk.getWorld().getName()), "/chunks/");
        if (!(dir.isDirectory() || dir.mkdirs())) {
            LOGGER.logWarning("Unable to create world directory for chunk at " + chunk.asCoords());
        }
        return new File(dir, "/" + chunk.getXLoc() + "_" + chunk.getYLoc() + "_" + chunk.getZLoc() + ".chunk");
    }
}
