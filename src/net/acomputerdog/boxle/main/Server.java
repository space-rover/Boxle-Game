package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecConverter;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.boxle.world.structure.ChunkTable;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Boxle Server instance
 */
public class Server {
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    private final Set<World> hostedWorlds = new CopyOnWriteArraySet<>();

    private World defaultWorld;

    private final GameConfig config;

    final int renderDistanceH;
    final int renderDistanceV;

    /**
     * Create a new Server instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Server(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
        config = boxle.getGameConfig();
        renderDistanceH = config.renderDistanceHorizontal;
        renderDistanceV = config.renderDistanceVertical;
    }

    /**
     * Initializes this server
     */
    public void init() {
        defaultWorld = new World(boxle, "server_default");
        boxle.getWorlds().addWorld(defaultWorld);
        hostedWorlds.add(defaultWorld);
    }

    /**
     * Ticks this server
     */
    public void tick() {
        unloadExtraChunks();
        loadMissingChunks();
    }

    private void unloadExtraChunks() {
        for (World world : hostedWorlds) {
            Vec3i center = CoordConverter.globalToChunk(VecConverter.vec3iFromVec3f(boxle.getClient().getPlayer().getLocation(), VecPool.createVec3i()));
            ChunkTable chunks = world.getChunks();
            for (Chunk chunk : chunks.getAllChunks()) {
                Vec3i cLoc = chunk.getLocation();
                if (cLoc.x > center.x + renderDistanceH || cLoc.x < center.x - renderDistanceH || cLoc.y > center.y + renderDistanceV || cLoc.y < center.y - renderDistanceV || cLoc.z > center.z + renderDistanceH || cLoc.z < center.z - renderDistanceH) {
                    world.unloadChunk(chunk);
                }
            }
            VecPool.free(center);
        }
    }

    private void loadMissingChunks() {
        int loadedChunks = 0;
        for (World world : hostedWorlds) {
            ChunkTable chunks = world.getChunks();
            Vec3i center = CoordConverter.globalToChunk(VecConverter.vec3iFromVec3f(boxle.getClient().getPlayer().getLocation(), VecPool.createVec3i()));
            //System.out.println(center.asCoords());
            GameConfig config = boxle.getGameConfig();
            for (int y = -renderDistanceV; y <= renderDistanceV; y++) {
                for (int x = -renderDistanceH; x <= renderDistanceH; x++) {
                    for (int z = -renderDistanceH; z <= renderDistanceH; z++) {
                        if (loadedChunks > config.maxLoadedChunksPerTick) {
                            VecPool.free(center);
                            return;
                        }
                        Vec3i newLoc = VecPool.getVec3i(center.x + x, center.y + y, center.z + z);
                        Chunk chunk = chunks.getChunk(newLoc);
                        if (chunk == null) {
                            loadedChunks++;
                            chunk = world.loadOrGenerateChunk(newLoc);
                        }
                        if (chunk.isChanged()) {
                            boxle.getRenderEngine().addChangedChunk(chunk);
                        }
                        VecPool.free(newLoc);
                    }
                }
            }
            VecPool.free(center);
        }

    }

    /**
     * Shuts down this server
     */
    public void shutdown() {

    }

    /**
     * Get the boxle instance of this Server.
     *
     * @return Return the boxle instance of this Server.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    public World getDefaultWorld() {
        return defaultWorld;
    }

    public Set<World> getHostedWorlds() {
        return Collections.unmodifiableSet(hostedWorlds);
    }
}
