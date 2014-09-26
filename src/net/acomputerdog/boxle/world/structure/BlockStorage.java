package net.acomputerdog.boxle.world.structure;

import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.world.Chunk;

/**
 * Class used to hold blocks for a Chunk.
 */
public class BlockStorage {
    private static final int[] POWERS_OF_TWO = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};

    private final int maxSize;

    private Block[][][] blockArray = new Block[1][1][1];
    private byte[][][] dataArray = new byte[1][1][1];

    private boolean hasChanged = false;

    public BlockStorage(int maxSize) {
        if (!isPowerOfTwo(maxSize)) {
            throw new IllegalArgumentException("maxSize must be a power of two!");
        }
        this.maxSize = maxSize;
    }

    public BlockStorage() {
        this(Chunk.CHUNK_SIZE);
    }

    public byte getData(int x, int y, int z) {
        verifyBounds(x, y, z);
        int indexX = findIndex(x, dataArray.length);
        int indexY = findIndex(y, dataArray[0].length);
        int indexZ = findIndex(z, dataArray[0][0].length);
        return dataArray[indexX][indexY][indexZ];
    }

    public Block getBlock(int x, int y, int z) {
        verifyBounds(x, y, z);
        int indexX = findIndex(x, blockArray.length);
        int indexY = findIndex(y, blockArray[0].length);
        int indexZ = findIndex(z, blockArray[0][0].length);
        return blockArray[indexX][indexY][indexZ];
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (block == null) throw new IllegalArgumentException("Block cannot be null!");
        verifyBounds(x, y, z);
        expandArrays();
        blockArray[x][y][z] = block;
    }

    public void setData(int x, int y, int z, byte data) {
        verifyBounds(x, y, z);
        expandArrays();
        dataArray[x][y][z] = data;
    }

    private int findIndex(int index, int size) {
        int scale = maxSize / size;
        return index / scale;
    }

    private void verifyBounds(int x, int y, int z) {
        if (x < 0 || x > maxSize || y < 0 || y > maxSize || z < 0 || z > maxSize) {
            throw new IllegalArgumentException("x, y, or z is out of bounds (" + x + ", " + y + ", " + z + ")!");
        }
    }

    /*
    private int findPowerOfTwo(int size) {
        for (int num : POWERS_OF_TWO) {
            if (num > maxSize) {
                throw new RuntimeException("Could not find a power of two smaller than maxSize!");
            }
            if (num >= size) {
                return num;
            }
        }
        throw new RuntimeException("Could not find a power of two smaller than size!");
    }
    */

    public void compressArrays() {
        if (hasChanged) {
            //compression NYI
            hasChanged = false;
        }
    }

    private void expandArrays() {
        hasChanged = true;
        if (blockArray.length < maxSize) {
            Block[][][] newBA = new Block[maxSize][maxSize][maxSize];
            int scaleX = maxSize / blockArray.length;
            int scaleY = maxSize / blockArray[0].length;
            int scaleZ = maxSize / blockArray[0][0].length;
            for (int x = 0; x < maxSize; x++) {
                for (int y = 0; y < maxSize; y++) {
                    for (int z = 0; z < maxSize; z++) {
                        newBA[x][y][z] = blockArray[x / scaleX][y / scaleY][z / scaleZ];
                    }
                }
            }
            blockArray = newBA;
        }
        if (dataArray.length < maxSize) {
            byte[][][] newDA = new byte[maxSize][maxSize][maxSize];
            int scaleX = maxSize / dataArray.length;
            int scaleY = maxSize / dataArray[0].length;
            int scaleZ = maxSize / dataArray[0][0].length;
            for (int x = 0; x < maxSize; x++) {
                for (int y = 0; y < maxSize; y++) {
                    for (int z = 0; z < maxSize; z++) {
                        newDA[x][y][z] = dataArray[x / scaleX][y / scaleY][z / scaleZ];
                    }
                }
            }
            dataArray = newDA;
        }
    }

    /*
    private void resizeBA(int sizeX, int sizeY, int sizeZ) {
        //
        // Incorrect method!
        // Need to actually scale up values!
        //
        Block[][][] newBA = new Block[sizeX][sizeY][sizeZ];
        if (blockArray.length < size) {
            int newScale = size / blockArray.length;
            Block[][][] newBA = new Block[size][blockArray[0].length][blockArray[1].length];
            for (int x = 0; x < blockArray.length; x++) {
                int indexX = x * newScale;
                for (int y = 0; y < blockArray[0].length; y++) {
                    int indexY = y * newScale;
                    for (int z = 0; z < blockArray[1].length; z++) {
                        int indexZ = z * newScale;
                        newBA[x][y][z] = blockArray[x][y][z];
                    }
                }
            }
        } else {

        }

    }
*/

    private static boolean isPowerOfTwo(int size) {
        for (int num : POWERS_OF_TWO) {
            if (num == size) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        BlockStorage storage = new BlockStorage(Chunk.CHUNK_SIZE);
        Block block1 = new Block();
        Block block2 = new Block();
        Block block3 = new Block();
        Block block4 = new Block();
        Block block5 = new Block();
        System.out.println("Initial dimensions: [" + storage.blockArray.length + ", " + storage.blockArray[0].length + ", " + storage.blockArray[0][0].length + "]");
        //storage.setBlock(0,0,0, block1);
        storage.blockArray[0][0][0] = block1;
        storage.setBlock(0, 0, 1, block2);
        storage.setBlock(0, 0, 7, block3);
        storage.setBlock(0, 0, 8, block4);
        storage.setBlock(0, 0, 15, block5);
        System.out.println("New dimensions: [" + storage.blockArray.length + ", " + storage.blockArray[0].length + ", " + storage.blockArray[0][0].length + "]");
        System.out.println("Correct Blocks: " + (storage.getBlock(0, 0, 0) == block1 && storage.getBlock(0, 0, 1) == block2 && storage.getBlock(0, 0, 7) == block3 && storage.getBlock(0, 0, 8) == block4 && storage.getBlock(0, 0, 15) == block5));
    }
}
