package net.acomputerdog.boxle.world.structure;

import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.world.Chunk;

/**
 * Class used to hold blocks for a Chunk.
 * <p/>
 * Not yet thread-safe!
 */
//todo: make thread-safe
public class BlockStorage {
    /**
     * Array containing powers of two up to 2048
     */
    private static final int[] POWERS_OF_TWO = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
    //stops at 2048 due to maximum reasonable size for a BlockStorage.  Anything over that (or 64, really) would take an extremely long time to set a block or compress arrays.

    /**
     * Maximum size of the arrays in this BlockStorage.  Cannot be greater than 2048.
     */
    private final int maxSize;

    /**
     * Array containing blocks
     */
    private Block[][][] blockArray = new Block[1][1][1];

    /**
     * Array containing block data values
     */
    private byte[][][] dataArray = new byte[1][1][1];

    /**
     * Set to true if block arrays have changed since last compression
     */
    private boolean hasChanged = false;

    /**
     * Creates a new BlockStorage
     *
     * @param maxSize The maximum size of this BlockStorage.  Cannot be greater than 2048.
     */
    public BlockStorage(int maxSize) {
        if (!isPowerOfTwo(maxSize)) throw new IllegalArgumentException("maxSize must be a power of two!");
        this.maxSize = maxSize;
    }

    /**
     * Creates a new BlockStorage with a mazSize of 16
     */
    public BlockStorage() {
        this(Chunk.CHUNK_SIZE);
    }

    /**
     * Gets the data value of a location
     *
     * @param x X-location of block
     * @param y Y-location of block
     * @param z Z-location of block
     * @return return the data value of the block
     */
    public byte getData(int x, int y, int z) {
        verifyBounds(x, y, z); //make sure x, y, and z are within bounds.
        int indexX = findIndex(x, dataArray.length);
        int indexY = findIndex(y, dataArray[0].length);
        int indexZ = findIndex(z, dataArray[0][0].length);
        return dataArray[indexX][indexY][indexZ];
    }

    /**
     * Gets the block at a location
     *
     * @param x X-location of block
     * @param y Y-location of block
     * @param z Z-location of block
     * @return return the block at the location
     */
    public Block getBlock(int x, int y, int z) {
        verifyBounds(x, y, z); //make sure x, y, and z are within bounds.
        int indexX = findIndex(x, blockArray.length);
        int indexY = findIndex(y, blockArray[0].length);
        int indexZ = findIndex(z, blockArray[0][0].length);
        //todo return a default block (air?) if null
        return blockArray[indexX][indexY][indexZ];
    }

    /**
     * Sets a block at a location
     *
     * @param x     X-location of block
     * @param y     Y-location of block
     * @param z     Z-location of block
     * @param block Block to set.  Cannot be null.
     */
    public void setBlock(int x, int y, int z, Block block) {
        if (block == null) throw new IllegalArgumentException("Block cannot be null!");
        verifyBounds(x, y, z);
        expandArrays();
        blockArray[x][y][z] = block;
    }

    /**
     * Sets the data value at a location
     *
     * @param x    X-location of block
     * @param y    Y-location of block
     * @param z    Z-location of block
     * @param data The data value to set
     */
    public void setData(int x, int y, int z, byte data) {
        verifyBounds(x, y, z);
        expandArrays();
        dataArray[x][y][z] = data;
    }

    /**
     * Finds the scaled index of a location.
     *
     * @param index The real (0 - maxSize) index of the location
     * @param size  The size of the array to scale to
     * @return Return the scaled index of the location
     */
    private int findIndex(int index, int size) {
        int scale = maxSize / size; //find the ratio of size to maxSize.  Ex. if maxSize is 16 and size is 8, ratio is two because the array is half of maxSize.
        return index / scale;
    }

    /**
     * Verifies that the x, y, and z locations are within the bounds of this BlockStorage
     *
     * @param x X-location
     * @param y Y-location
     * @param z Z-location
     */
    private void verifyBounds(int x, int y, int z) {
        if (x < 0 || x > maxSize || y < 0 || y > maxSize || z < 0 || z > maxSize) {
            throw new IllegalArgumentException("An index is out of bounds! (" + x + ", " + y + ", " + z + ")!");
        }
    }

    /**
     * Finds a power of two greater than or equal to a number but less than or equal to maxSize
     *
     * @param num The number to find
     * @return return an int that is a power of two greater than or equal to num
     */
    private int findPowerOfTwo(int num) {
        for (int pow : POWERS_OF_TWO) {
            if (pow > maxSize) {
                throw new RuntimeException("Could not find a power of two smaller than maxSize!");
            }
            if (pow >= num) {
                return pow;
            }
        }
        throw new RuntimeException("Could not find a power of two smaller than num!");
    }

    /**
     * Scan and compress the arrays of this BlockStorage.
     * Uses many iterations and manual array copies, so is very slow.
     * Not yet implemented
     */
    public void compressArrays() {
        if (hasChanged) {
            //compression NYI
            hasChanged = false;
        }
    }

    /**
     * Expand the arrays in this BlockStorage to maxSize
     */
    private void expandArrays() {
        hasChanged = true; //mark arrays as having changed
        if (blockArray.length < maxSize) { //skip if array is already fully expanded
            Block[][][] newBA = new Block[maxSize][maxSize][maxSize]; //create new fully expanded array
            int scaleX = maxSize / blockArray.length; //gets ratio of x-size to maxSize.  Reverse of function in findIndex().
            int scaleY = maxSize / blockArray[0].length; //gets ratio of y-size to maxSize.  Reverse of function in findIndex().
            int scaleZ = maxSize / blockArray[0][0].length; //gets ratio of z-size to maxSize.  Reverse of function in findIndex().
            for (int x = 0; x < maxSize; x++) {
                for (int y = 0; y < maxSize; y++) {
                    for (int z = 0; z < maxSize; z++) {
                        newBA[x][y][z] = blockArray[x / scaleX][y / scaleY][z / scaleZ]; //for each [x,y,z], scale [x,y,z] and copy the value.
                    }
                }
            }
            blockArray = newBA; //replace array.  old will be GC'ed
        }
        if (dataArray.length < maxSize) { //skip if array is already fully expanded
            byte[][][] newDA = new byte[maxSize][maxSize][maxSize]; //create new fully expanded array
            int scaleX = maxSize / dataArray.length; //gets ratio of x-size to maxSize.  Reverse of function in findIndex().
            int scaleY = maxSize / dataArray[0].length; //gets ratio of y-size to maxSize.  Reverse of function in findIndex().
            int scaleZ = maxSize / dataArray[0][0].length; //gets ratio of z-size to maxSize.  Reverse of function in findIndex().
            for (int x = 0; x < maxSize; x++) {
                for (int y = 0; y < maxSize; y++) {
                    for (int z = 0; z < maxSize; z++) {
                        newDA[x][y][z] = dataArray[x / scaleX][y / scaleY][z / scaleZ]; //for each [x,y,z], scale [x,y,z] and copy the value.
                    }
                }
            }
            dataArray = newDA; //replace array.  old will be GC'ed
        }
    }

    /**
     * Checks if a number is a power of two
     *
     * @param num The number to check
     * @return return true if the number is a power of two, false otherwise
     */
    private static boolean isPowerOfTwo(int num) {
        for (int pow : POWERS_OF_TWO) {
            if (pow == num) {
                return true;
            }
        }
        return false;
    }
}
