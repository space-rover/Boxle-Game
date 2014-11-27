package net.acomputerdog.boxle.render.engine;

import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import jme3tools.optimize.GeometryBatchFactory;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.block.BlockFace;
import net.acomputerdog.boxle.block.block.BlockTex;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;

public class ChunkRenderer {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    public static void buildChunkMesh(Vec3i gLoc, Chunk chunk, Node node) {
        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < chunkSize; y++) {
                for (int z = 0; z < chunkSize; z++) {
                    Block block = chunk.getBlockAt(x, y, z);
                    if (block != null && block.isRenderable()) {
                        BlockTex tex = block.getTextures();
                        if (isTransparent(x + 1, y, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.RIGHT, x, y, z);
                        }
                        if (isTransparent(x - 1, y, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.LEFT, x, y, z);
                        }
                        if (isTransparent(x, y + 1, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.TOP, x, y, z);
                        }
                        if (isTransparent(x, y - 1, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.BOTTOM, x, y, z);
                        }
                        if (isTransparent(x, y, z + 1, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.FRONT, x, y, z);
                        }
                        if (isTransparent(x, y, z - 1, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.BACK, x, y, z);
                        }
                    }
                }
            }
        }
        GeometryBatchFactory.optimize(node);
    }

    private static boolean isTransparent(int x, int y, int z, Chunk chunk) {
        if (x >= chunkSize || x < 0 || y >= chunkSize || y < 0 || z >= chunkSize || z < 0) {
            Chunk newChunk = findNeighborChunk(x, y, z, chunk);
            if (newChunk == null) {
                return true;
            }
            Vec3i blockPos = findLocInNeighbor(x, y, z);
            Block newBlock = newChunk.getBlockAt(blockPos.x, blockPos.y, blockPos.z);
            VecPool.free(blockPos);
            return newBlock == null || newBlock.isTransparent();
        }
        Block block = chunk.getBlockAt(x, y, z);
        return block == null || block.isTransparent();
    }

    private static Chunk findNeighborChunk(int x, int y, int z, Chunk currChunk) {
        Vec3i loc = currChunk.getLocation();
        if (x >= chunkSize) loc.x += 1;
        if (x < 0) loc.x -= 1;
        if (y >= chunkSize) loc.y += 1;
        if (y < 0) loc.y -= 1;
        if (z >= chunkSize) loc.z += 1;
        if (z < 0) loc.z -= 1;
        Chunk newChunk = currChunk.getWorld().getChunks().getChunk(loc);
        VecPool.free(loc);
        return newChunk;
    }

    private static Vec3i findLocInNeighbor(int x, int y, int z) {
        Vec3i vec = VecPool.getVec3i();
        if (x >= chunkSize) {
            vec.x = 0;
        } else if (x < 0) {
            vec.x = chunkSize - 1;
        } else {
            vec.x = x;
        }
        if (y >= chunkSize) {
            vec.y = 0;
        } else if (y < 0) {
            vec.y = chunkSize - 1;
        } else {
            vec.y = y;
        }
        if (z >= chunkSize) {
            vec.z = 0;
        } else if (z < 0) {
            vec.z = chunkSize - 1;
        } else {
            vec.z = z;
        }
        return vec;
    }

    private static void addFace(Node node, BlockTex tex, Vec3i cLoc, BlockFace face, int x, int y, int z) {
        Geometry geom = new Geometry("face@" + x + "_" + y + "_" + z, new Quad(1f, 1f));
        Material mat = tex.getFaceMat(face);
        if (mat.isTransparent()) {
            geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        }
        geom.setMaterial(mat);
        geom.setLocalTranslation(cLoc.x + x + face.xPos + 1, cLoc.y + y + face.yPos, cLoc.z + z + face.zPos + 1);
        geom.rotate(face.xRot, face.yRot, face.zRot);
        geom.updateModelBound();
        node.attachChild(geom);
    }

}
