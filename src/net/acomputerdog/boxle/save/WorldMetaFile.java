package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.vec.Vec3f;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.util.BlockMap;
import net.acomputerdog.boxle.world.World;

import java.io.*;

public class WorldMetaFile {
    private final World world;
    private final File file;

    private BlockMap blockMap = new BlockMap();
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

            blockMap.load(in);

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

    }

    public void save() throws IOException {
        DataOutputStream out = null;
        if (playerLoc == null || playerRot == null) {
            playerLoc = Boxle.instance().getClient().getPlayer().getLocation();
            playerRot = Boxle.instance().getClient().getPlayer().getRotation();
        }
        try {
            out = new DataOutputStream(new FileOutputStream(file));

            out.writeFloat(playerLoc.x);
            out.writeFloat(playerLoc.y);
            out.writeFloat(playerLoc.z);
            out.writeFloat(playerRot.x);
            out.writeFloat(playerRot.y);
            out.writeFloat(playerRot.z);
            blockMap.save(out);

            out.close();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public BlockMap getBlockMap() {
        return blockMap;
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
}
