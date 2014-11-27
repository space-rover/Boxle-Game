package net.acomputerdog.boxle.block.block;

import net.acomputerdog.boxle.block.util.Identifiable;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.aabb.AABBF;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.logger.CLogger;

/**
 * Represents a type of Block.
 */
public class Block implements Identifiable {
    public static final CLogger LOGGER = new CLogger("Block", false, true);

    /**
     * Name of this block.  Used internally to identify it.
     */
    private final String name;

    private final String id;

    private final Boxle boxle;

    private boolean isBreakable = true;
    private float resistance = 1.0f;
    private float explosionResistance = 1.0f;
    private float strength = 100f;
    private float hardness = .1f;
    private boolean isCollidable = true;
    private boolean isTransparent = false;
    private byte lightReduction = (byte) 255;
    private byte lightOutput = 0;
    private boolean renderable = true;
    private AABBF bounds = new AABBF(0f, 0f, 0f, 1f, 1f, 1f);

    private BlockTex tex;

    /**
     * Creates a new Block
     *
     * @param name  The name of this block.
     * @param id
     */
    public Block(String id, String name) {
        this.id = id;
        if (name == null) throw new IllegalArgumentException("Block name cannot be null!");
        this.name = name;
        boxle = Boxle.instance();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDefinition() {
        return id;
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
        return isBreakable;
    }

    /**
     * Gets the resistance to damage of this block.  The value is multiplied to incoming damage.
     *
     * @return Return a float representing the resistance of the block
     */
    public float getResistance() {
        return resistance;
    }

    /**
     * Gets the resistance to an explosion of the block.  The value is multiplied to incoming explosion damage.
     *
     * @return Return a float representing the explosion resistance of the block
     */
    public float getExplosionResistance() {
        return explosionResistance;
    }

    /**
     * Gets the "strength" of a block.  Is used as a base "hit point" value when being damaged.
     *
     * @return Return a float of the strength of the block
     */
    public float getStrength() {
        return strength;
    }

    /**
     * Gets the damage factor done to tools breaking this block.  Value is multiplied in to scale damage.
     *
     * @return Return a float of the hardness of this block
     */
    public float getHardness() {
        return hardness;
    }

    /**
     * Checks if the block can be collided with
     *
     * @return Return true if the block can be collided with, false otherwise.
     */
    public boolean isCollidable() {
        return isCollidable;
    }

    /**
     * Checks if this block is clear
     *
     * @return Return true if the block is clear
     */
    public boolean isTransparent() {
        return isTransparent;
    }

    /**
     * Gets the light reduction of light passing through this block.
     *
     * @return return a byte of the reduction of light passing through this block
     */
    public byte getLightReduction() {
        return lightReduction;
    }

    /**
     * Gets the light level emitted by this block
     *
     * @return Return a byte of the amount of light emitted by this block.
     */
    public byte getLightOutput() {
        return lightOutput;
    }

    public boolean isRenderable() {
        return renderable;
    }

    public AABBF getBlockBounds() {
        return bounds;
    }

    public BlockTex getTextures() {
        if (tex == null && isRenderable()) {
            tex = new BlockTex(this);
            tex.loadAllDefault();
        }
        return tex;
    }

    public void setBreakable(boolean isBreakable) {
        this.isBreakable = isBreakable;
    }

    public void setResistance(float resistance) {
        this.resistance = resistance;
    }

    public void setExplosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    public void setCollidable(boolean collidable) {
        this.isCollidable = collidable;
    }

    public void setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    public void setLightReduction(byte lightReduction) {
        this.lightReduction = lightReduction;
    }

    public void setLightOutput(byte lightOutput) {
        this.lightOutput = lightOutput;
    }

    public void setRenderable(boolean renderable) {
        this.renderable = renderable;
    }

    public void setBounds(AABBF bounds) {
        this.bounds = bounds;
    }

    public void setTextures(BlockTex tex) {
        this.tex = tex;
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

    public Boxle getBoxle() {
        return boxle;
    }
}
