package net.acomputerdog.boxle.world.util;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.util.ThreadUtils;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;

/**
 * Thread that scans and compresses block and data arrays within chunks.
 */
public class BlockCompressionThread extends Thread {
    /**
     * Boxle instance
     */
    private final Boxle boxle;

    public BlockCompressionThread(Boxle boxle) {
        super();
        this.boxle = boxle;
        super.setName("BlockCompressionThread");
        super.setDaemon(true); //JVM can end even if thread is still running
        super.setPriority(4); //slightly low priority to minimize CPU usage
    }

    @Override
    public void run() {
        while (boxle.canRun()) {
            for (World world : boxle.getWorlds().getWorlds()) {
                for (Chunk chunk : world.getChunks().getAllChunks()) {
                    chunk.getBlocks().compressArrays();
                }
            }
            ThreadUtils.sleep(1); //limit to 1000 TPS, to lower CPU usage
        }
    }
}
