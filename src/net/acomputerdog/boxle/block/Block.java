package net.acomputerdog.boxle.block;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.World;

/**
 * Represents a type of Block.
 * TODO: remove block data
 */
public abstract class Block {
    /**
     * Name of this block.  Used internally to identify it.
     */
    public final String name;

    private final Boxle boxle;

    private BlockTex tex;

    /**
     * Creates a new Block
     *
     * @param name  The name of this block.
     * @param boxle
     */
    protected Block(String name, Boxle boxle) {
        this.boxle = boxle;
        if (name == null) throw new IllegalArgumentException("Block name cannot be null!");
        this.name = name;
        Blocks.registerBlock(this);
    }

    /**
     * Gets the name of this block
     *
     * @return return the name of this block
     */
    public final String getName() {
        return name;
    }

    /**
     * Checks if the block can be placed at the specified location
     *
     * @param world    The world the block will be placed in
     * @param location The location to be placed at
     * @return Return true if the block can be placed, false otherwise.
     */
    public boolean canPlaceAt(World world, Vec3i location) {
        return true;
    }

    /**
     * Called whenever the block is updated, such as from a neighboring block change.
     *
     * @param world    The world the block is in
     * @param location The location of the block
     */
    public void onUpdate(World world, Vec3i location) {

    }

    /**
     * Called if the block has requested to relieve tick updates.
     *
     * @param world    The world the block is in
     * @param location The location of the block
     */
    public void onTick(World world, Vec3i location) {

    }

    /**
     * Checks if the block can be destroyed
     *
     * @return Return true if the block can be destroyed.
     */
    public boolean isBreakable() {
        return true;
    }

    /**
     * Gets the resistance to damage of this block.  The value is multiplied to incoming damage.
     *
     * @return Return a float representing the resistance of the block
     */
    public float getResistance() {
        return 1.0f;
    }

    /**
     * Gets the resistance to an explosion of the block.  The value is multiplied to incoming explosion damage.
     *
     * @return Return a float representing the explosion resistance of the block
     */
    public float getExplosionResistance() {
        return 1.0f;
    }

    /**
     * Gets the "strength" of a block.  Is used as a base "hit point" value when being damaged.
     *
     * @return Return a float of the strength of the block
     */
    public float getStrength() {
        return 100f;
    }

    /**
     * Gets the damage factor done to tools breaking this block.  Value is multiplied in to scale damage.
     *
     * @return Return a float of the hardness of this block
     */
    public float getHardness() {
        return .1f;
    }

    /**
     * Checks if the block can be collided with
     *
     * @return Return true if the block can be collided with, false otherwise.
     */
    public boolean blocksMovement() {
        return true;
    }

    /**
     * Checks if this block is clear
     *
     * @return Return true if the block is clear
     */
    public boolean isTransparent() {
        return false;
    }

    /**
     * Gets the light reduction of light passing through this block.
     *
     * @return return a byte of the reduction of light passing through this block
     */
    public byte getLightReduction() {
        return (byte) 255;
    }

    /**
     * Gets the light level emitted by this block
     *
     * @return Return a byte of the amount of light emitted by this block.
     */
    public byte getLightOutput() {
        return 0;
    }

    public boolean isRenderable() {
        return true;
    }

    public BlockTex getTextures() {
        if (tex == null && isRenderable()) {
            tex = new BlockTex(this);
            tex.loadAllDefault();
        }
        return tex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;

        return name.equals(block.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Block{" +
                "name='" + name +
                '}';
    }
}
