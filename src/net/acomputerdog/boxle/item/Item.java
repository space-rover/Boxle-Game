package net.acomputerdog.boxle.item;

import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.World;

/**
 * Items as in the type of item, not an instance of a dropped item in the world.  Similar to Block.
 */
public abstract class Item {
    /**
     * Name of this Item
     */
    private final String name;

    /**
     * Creates a new Item
     *
     * @param name The name of the item
     */
    protected Item(String name) {
        if (name == null) throw new IllegalArgumentException("Item name cannot be null!");
        this.name = name;
    }

    /**
     * Checks if this tool can break a block
     *
     * @param world     The world the block is in
     * @param block     The type of block being broken
     * @param location  The location of the block
     * @param item      The itemStack being used to break the block
     * @return Return true if the block can be broken, false otherwise
     */
    public boolean canBreak(World world, Vec3i location, Block block, ItemStack item) {
        return true;
    }

    /**
     * Checks if this tool can use (right-click) on a block
     *
     * @param world     The world the block is in
     * @param block     The type of block being broken
     * @param location  The location of the block
     * @param item      The itemStack being used to break the block
     * @return Return true if the block can be used, false otherwise
     */
    public boolean canUseBlock(World world, Vec3i location, Block block, ItemStack item) {
        return true;
    }

    /**
     * Called when the item is used to break a block
     *
     * @param world     The world the block is in
     * @param block     The type of block being broken
     * @param location  The location of the block
     * @param item      The itemStack being used to break the block
     */
    public void onBreakBlock(World world, Vec3i location, Block block, ItemStack item) {

    }

    /**
     * Called when the item is used (right-clicked) on a block
     *
     * @param world     The world the block is in
     * @param block     The type of block being broken
     * @param location  The location of the block
     * @param item      The itemStack being used to break the block
     */
    public void onUseBlock(World world, Block block, Vec3i location, ItemStack item) {

    }

    /**
     * Called when the item is right-clicked
     *
     * @param item The itemStack being used to break the block
     */
    public void onUse(ItemStack item) {

    }

    /**
     * Gets the name of this Item
     *
     * @return return the name of this item
     */
    public String getName() {
        return name;
    }
}
