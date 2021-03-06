package net.acomputerdog.boxle.main;

import com.jme3.scene.Node;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.entity.Entity;
import net.acomputerdog.boxle.entity.types.EntityPlayer;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.spiral.Spiral2i;
import net.acomputerdog.boxle.math.vec.Vec2i;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecConverter;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.engine.ChunkRenderer;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.render.util.ChunkNode;
import net.acomputerdog.boxle.save.SaveManager;
import net.acomputerdog.boxle.save.world.files.Region;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.boxle.world.structure.ChunkTable;
import net.acomputerdog.core.logger.CLogger;

import java.io.IOException;
import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Boxle Server instance
 */
public class Server {
    private static final int chunkSize = Chunk.CHUNK_SIZE;
    private static final int regionSize = Region.REGION_SIZE;
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    private final Set<World> hostedWorlds = new CopyOnWriteArraySet<>();

    private World defaultWorld;

    private final GameConfig config;

    final int renderDistanceH;
    final int renderDistanceV;

    private final Set<Chunk> rebuildChunks = new ConcurrentSkipListSet<>();

    private int numChunks = 0;
    private int numUnload = 0;

    private Vec3i lastPlayerCLoc;
    private Spiral2i spiral;
    private final Vec2i spiralLoc;

    private final CLogger logger;

    private final RenderEngine engine;

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
        logger = new CLogger("Server", false, true);
        engine = boxle.getRenderEngine();
        spiralLoc = VecPool.getVec2i(0, 0);
    }

    /**
     * Initializes this server
     */
    public void init() {
        try {
            defaultWorld = SaveManager.loadOrCreateWorldSave(config.worldName).createWorld();
        } catch (IOException e) {
            logger.logWarning("Exception loading world!  A new one will be created!", e);
            defaultWorld = SaveManager.createWorldSave(config.worldName).createWorld(); //not the same as above, this forces it to create a new world where the above tries to load the old
        }
        boxle.getWorlds().addWorld(defaultWorld);
        hostedWorlds.add(defaultWorld);
    }

    /**
     * Ticks this server
     */
    public void tick() {

        decorateChunks();
        long oldTime = System.currentTimeMillis();
        numChunks = 0;
        numUnload = 0;
        //todo iterate through worlds and add parameter to methods
        rebuildNeighborChunks();
        rebuildChangedChunks();
        unloadExtraChunks();
        unloadExtraRegions();
        if (config.outputRenderDebugInfo && numChunks > 0) {
            long newTime = System.currentTimeMillis();
            logger.logDetail("Built " + numChunks + " chunk meshes and unloaded " + numUnload + " chunks in " + ((newTime - oldTime) / 1000f) + " seconds.");
        }

        for (World world : hostedWorlds) {
            for (Entity entity : world.getEntities()) {
                entity.onTick();
            }
        }
    }

    private void unloadExtraRegions() {
        for (World world : hostedWorlds) {
            Vec3i center = CoordConverter.globalToRegion(VecConverter.floorVec3iFromVec3f(boxle.getClient().getPlayer().getLocation(), VecPool.createVec3i()));
            Set<Region> regions = world.getRegionSet();
            for (Region region : regions) {
                Vec3i rLoc = region.getLoc();
                if (rLoc.x > center.x + CoordConverter.regionLocOfChunkCeil(renderDistanceH) || rLoc.x < center.x - CoordConverter.regionLocOfChunkCeil(renderDistanceH) || rLoc.y > center.y + CoordConverter.regionLocOfChunkCeil(renderDistanceV) || rLoc.y < center.y - CoordConverter.regionLocOfChunkCeil(renderDistanceV) || rLoc.z > center.z + CoordConverter.regionLocOfChunkCeil(renderDistanceH) || rLoc.z < center.z - CoordConverter.regionLocOfChunkCeil(renderDistanceH)) {
                    System.out.println("Unloading region at " + rLoc.asCoords());
                    world.removeRegion(region);
                    SaveManager.unloadRegion(region);
                }
                VecPool.free(rLoc);
            }
            VecPool.free(center);
        }
        /*
        Vec3i endLoc = VecPool.getVec3i();
        Vec3i renderAreaStart = VecPool.getVec3i();
        Vec3i renderAreaEnd = VecPool.getVec3i();
        Vec3i center = VecPool.createVec3i();
        AABBI renderArea = new AABBI();
        AABBI regionArea = new AABBI();
        for (World world : hostedWorlds) {
            for (Region region : world.getRegionSet()) {
                Vec3i startLoc = region.getLoc();
                startLoc.x *= regionSize;
                startLoc.y *= regionSize;
                startLoc.z *= regionSize;
                endLoc.x = startLoc.x + regionSize;
                endLoc.x = startLoc.x + regionSize;
                endLoc.x = startLoc.x + regionSize;
                CoordConverter.globalToChunk(VecConverter.floorVec3iFromVec3f(boxle.getClient().getPlayer().getLocation(), center));
                renderAreaStart.x = center.x + config.renderDistanceHorizontal;
                renderAreaStart.y = center.y + config.renderDistanceVertical;
                renderAreaStart.z = center.z + config.renderDistanceHorizontal;
                renderAreaEnd.x = center.x - config.renderDistanceHorizontal;
                renderAreaEnd.y = center.y - config.renderDistanceVertical;
                renderAreaEnd.z = center.z - config.renderDistanceHorizontal;
                renderArea.setCorners(renderAreaStart, renderAreaEnd);
                regionArea.setCorners(startLoc, endLoc);
                if (!regionArea.collidesWith(renderArea)) {
                    SaveManager.unloadRegion(region);
                }
                VecPool.free(startLoc);
            }
        }
        VecPool.free(center);
        VecPool.free(renderAreaEnd);
        VecPool.free(renderAreaStart);
        VecPool.free(endLoc);
        */
    }

    private void decorateChunks() {
        //there has to be a better way to check neighbors...
        for (World world : hostedWorlds) {
            Queue<Chunk> decorateChunks = world.getDecorateChunks();
            for (Chunk chunk : decorateChunks) {
                Vec3i nLoc = chunk.getLocation();
                nLoc.x++;
                ChunkTable chunks = world.getChunks();
                if (chunks.getChunk(nLoc) != null) {
                    nLoc.x -= 2;
                    if (chunks.getChunk(nLoc) != null) {
                        nLoc.x++;
                        nLoc.y++;
                        if (chunks.getChunk(nLoc) != null) {
                            nLoc.y -= 2;
                            if (chunks.getChunk(nLoc) != null) {
                                nLoc.y++;
                                nLoc.z++;
                                if (chunks.getChunk(nLoc) != null) {
                                    nLoc.z -= 2;
                                    if (chunks.getChunk(nLoc) != null) {
                                        decorateChunks.remove(chunk);
                                        world.getGenerator().generateDecorations(chunk);
                                        chunk.setModifiedFromLoad(true);
                                        chunk.markDecorated();
                                        chunk.setNeedsRebuild(true);
                                        chunk.setModifiedFromLoad(true);
                                        SaveManager.saveChunkDelayed(chunk);
                                    }
                                }
                            }
                        }
                    }
                }
                VecPool.free(nLoc);
            }
        }
    }

    private void unloadExtraChunks() {
        for (World world : hostedWorlds) {
            Vec3i center = CoordConverter.globalToChunk(VecConverter.floorVec3iFromVec3f(boxle.getClient().getPlayer().getLocation(), VecPool.createVec3i()));
            ChunkTable chunks = world.getChunks();
            for (Chunk chunk : chunks.getAllChunks()) {
                Vec3i cLoc = chunk.getLocation();
                if (cLoc.x > center.x + renderDistanceH || cLoc.x < center.x - renderDistanceH || cLoc.y > center.y + renderDistanceV || cLoc.y < center.y - renderDistanceV || cLoc.z > center.z + renderDistanceH || cLoc.z < center.z - renderDistanceH) {
                    numUnload++;
                    rebuildChunks.remove(chunk);
                    world.unloadChunk(chunk);
                }
                VecPool.free(cLoc);
            }
            VecPool.free(center);
        }
    }

    private void rebuildNeighborChunks() {
        for (Chunk chunk : rebuildChunks) {
            rebuildChunks.remove(chunk);
            numChunks++;
            Vec3i cLoc = chunk.getLocation();
            ChunkNode node = new ChunkNode("chunk@" + cLoc.asCoords());
            VecPool.free(cLoc);
            buildChunk(chunk, node, false);
            ChunkNode oldNode = chunk.getChunkNode();
            if (oldNode.getParent() != null) {
                engine.removeNode(oldNode);
            }
            chunk.setChunkNode(node);
            engine.addNode(node);
            if (numChunks > config.maxLoadedChunksPerTick) {
                return;
            }
        }
    }

    private void rebuildChangedChunks() {
        EntityPlayer player = boxle.getClient().getPlayer();
        World world = player.getWorld();
        ChunkTable chunks = world.getChunks();
        Vec3i center = CoordConverter.globalToChunk(VecConverter.floorVec3iFromVec3f(player.getLocation(), VecPool.createVec3i()));
        if (!center.equals(lastPlayerCLoc)) {
            VecPool.free(lastPlayerCLoc);
            lastPlayerCLoc = center;
            spiral = new Spiral2i(VecPool.getVec2i(center.x, center.z));
        }
        GameConfig config = boxle.getGameConfig();
        while (numChunks < config.maxLoadedChunksPerTick) {
            spiral.next(spiralLoc);
            int sX = spiralLoc.x;
            int sZ = spiralLoc.y;
            if (Math.abs(sX - center.x) >= renderDistanceH || Math.abs(sZ - center.z) >= renderDistanceH) {
                if (Math.abs(sX - center.x) >= renderDistanceH && Math.abs(sZ - center.z) >= renderDistanceH) {
                    spiral = new Spiral2i(VecPool.getVec2i(center.x, center.z));
                    VecPool.free(lastPlayerCLoc);
                    lastPlayerCLoc = center;
                }
                break;
            }
            for (int y = renderDistanceV; y >= -renderDistanceV; y--) {
                Vec3i newLoc = VecPool.getVec3i(sX, center.y + y, sZ);
                Chunk chunk = chunks.getChunk(newLoc);
                if (chunk == null) {
                    chunk = SaveManager.loadOrGenerateChunk(world, newLoc);
                    //chunk = world.loadOrGenerateChunk(newLoc);
                }
                if (chunk != null && chunk.needsRebuild()) { //if null chunk has not been loaded yet
                    numChunks++;
                    rebuildChunks.remove(chunk); //make sure the chunk is not rendered twice
                    ChunkNode node = new ChunkNode("chunk@" + newLoc.asCoords());
                    buildChunk(chunk, node, true);
                    ChunkNode oldNode = chunk.getChunkNode();
                    if (oldNode.getParent() != null) {
                        engine.removeNode(chunk.getChunkNode());
                    }
                    chunk.setChunkNode(node);
                    engine.addNode(node);
                    VecPool.free(newLoc);
                }
            }
        }
    }


    public void buildChunk(Chunk chunk, Node node, boolean notifyNeighbors) {
        chunk.setNeedsRebuild(false);

        Vec3i cLoc = chunk.getLocation();
        Vec3i gLoc = CoordConverter.chunkToGlobal(cLoc.duplicate());
        ChunkRenderer.buildChunkMesh(gLoc, chunk, node);

        if (notifyNeighbors) {
            if (config.notifyNeighborsMode >= 0) {
                ChunkTable chunks = chunk.getWorld().getChunks();
                //immediate neighbors
                notifyNeighbor(cLoc, 1, 0, 0, chunks);
                notifyNeighbor(cLoc, -1, 0, 0, chunks);
                notifyNeighbor(cLoc, 0, 1, 0, chunks);
                notifyNeighbor(cLoc, 0, -1, 0, chunks);
                notifyNeighbor(cLoc, 0, 0, 1, chunks);
                notifyNeighbor(cLoc, 0, 0, -1, chunks);
                if (config.notifyNeighborsMode >= 1) {
                    //edges
                    notifyNeighbor(cLoc, 1, 1, 0, chunks);
                    notifyNeighbor(cLoc, 1, -1, 0, chunks);
                    notifyNeighbor(cLoc, -1, 1, 0, chunks);
                    notifyNeighbor(cLoc, -1, -1, 0, chunks);
                    notifyNeighbor(cLoc, 1, 0, 1, chunks);
                    notifyNeighbor(cLoc, 1, 0, -1, chunks);
                    notifyNeighbor(cLoc, -1, 0, 1, chunks);
                    notifyNeighbor(cLoc, 1, 0, -1, chunks);
                    notifyNeighbor(cLoc, 0, 1, 1, chunks);
                    notifyNeighbor(cLoc, 0, -1, 1, chunks);
                    notifyNeighbor(cLoc, 0, 1, -1, chunks);
                    notifyNeighbor(cLoc, 0, -1, -1, chunks);
                    if (config.notifyNeighborsMode >= 2) {
                        //corners
                        notifyNeighbor(cLoc, 1, 1, 1, chunks);
                        notifyNeighbor(cLoc, 1, 1, -1, chunks);
                        notifyNeighbor(cLoc, -1, 1, 1, chunks);
                        notifyNeighbor(cLoc, -1, 1, -1, chunks);
                        notifyNeighbor(cLoc, 1, -1, 1, chunks);
                        notifyNeighbor(cLoc, 1, -1, -1, chunks);
                        notifyNeighbor(cLoc, -1, -1, 1, chunks);
                        notifyNeighbor(cLoc, -1, -1, -1, chunks);
                        if (config.notifyNeighborsMode >= 3) {
                            //2nd layer immediates
                            notifyNeighbor(cLoc, 2, 0, 0, chunks);
                            notifyNeighbor(cLoc, -2, 0, 0, chunks);
                            notifyNeighbor(cLoc, 0, 2, 0, chunks);
                            notifyNeighbor(cLoc, 0, -2, 0, chunks);
                            notifyNeighbor(cLoc, 0, 0, 2, chunks);
                            notifyNeighbor(cLoc, 0, 0, -2, chunks);
                        }
                    }
                }
            }
        }
        VecPool.free(cLoc);
        VecPool.free(gLoc);
    }

    private void notifyNeighbor(Vec3i cLoc, int x, int y, int z, ChunkTable chunks) {
        Chunk nChunk = chunks.getChunk(x + cLoc.x, y + cLoc.y, z + cLoc.z);
        if (nChunk != null && !rebuildChunks.contains(nChunk)) {
            rebuildChunks.add(nChunk);
            nChunk.setNeedsRebuild(false);
        }
    }

    /**
     * Shuts down this server
     */
    public void shutdown() {
        for (World world : hostedWorlds) {
            try {
                SaveManager.saveWorld(world);
            } catch (IOException e) {
                logger.logError("Unable to save world \"" + world.getName() + "\"", e);
            }
        }
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
