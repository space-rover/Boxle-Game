package net.acomputerdog.boxle.block;

import net.acomputerdog.boxle.block.types.nonsolid.BlockAir;
import net.acomputerdog.boxle.block.types.solid.BlockDirt;
import net.acomputerdog.boxle.block.types.solid.BlockStone;
import net.acomputerdog.core.logger.CLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry of Blocks.
 */
public class Blocks {

    public static final BlockAir air = new BlockAir();
    public static final BlockDirt dirt = new BlockDirt();
    public static final BlockStone stone = new BlockStone();

    /**
     * Logger for Blocks
     */
    private static final CLogger logger = new CLogger("BlockRegistry", true, true);

    /**
     * Map of block names to instances
     */
    private static final Map<String, Block> blockMap = new ConcurrentHashMap<>();

    /**
     * Registers a block into the registry
     *
     * @param block The block to register. Cannot be null.
     */
    public static void registerBlock(Block block) {
        if (block == null) throw new IllegalArgumentException("Cannot register a null block!");
        if (blockMap.put(block.getName(), block) != null) {
            logger.logWarning("Registering duplicate block: " + block.getName());
        }
    }

    /**
     * Gets a block by it's name.
     *
     * @param name The name of the block.
     * @return Return the block with the given name, or null if none is defined.
     */
    public static Block getBlock(String name) {
        if (name == null) {
            return null; //OK to return null, since a non-existent key will return null anyway.
        }
        return blockMap.get(name);
    }
}