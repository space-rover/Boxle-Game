package net.acomputerdog.boxle.item.type;

import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.item.Item;
import net.acomputerdog.boxle.item.ItemStack;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.World;

/**
 * An item that can receive damage from breaking blocks.
 */
public abstract class ItemTool extends Item {
    /**
     * The maximum damage that this tool can take
     */
    private int maxDamage = 100;

    /**
     * Creates a new Item
     *
     * @param name The name of the item
     */
    protected ItemTool(String name) {
        super(name);
    }

    /**
     * Gets the maximum damage that this tool can accept before breaking
     *
     * @return Return the max damage this tool can accept
     */
    public int getMaxDamage() {
        return maxDamage;
    }

    /**
     * Sets the maximum damage that the tool can accept before breaking
     *
     * @param maxDamage the max damage this tool can accept
     */
    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    /**
     * Called when the tool breaks.
     *
     * @param world     The world the block is in
     * @param block     The type of block being broken
     * @param blockData The data of the block being broken
     * @param location  The location of the block
     * @param item      The itemStack being used to break the block
     */
    public void onToolBreak(World world, Vec3i location, Block block, byte blockData, ItemStack item) {

    }

    @Override
    public void onBreakBlock(World world, Vec3i location, Block block, byte blockData, ItemStack item) {
        item.setDamageValue(item.getDamageValue() + (int) Math.floor(((float) maxDamage * block.getHardness(blockData))));
        if (item.getDamageValue() > maxDamage) {
            this.onToolBreak(world, location, block, blockData, item);
        }
    }
}
