package net.acomputerdog.boxle.world.util;

import net.acomputerdog.boxle.world.structure.BlockStorage;

import java.util.List;

/**
 * Thread that scans and compresses block and data arrays within chunks
 */
public class BlockCompressionThread extends Thread {
    private final List<BlockStorage> storages;

    public BlockCompressionThread(List<BlockStorage> storages) {
        super();
        this.storages = storages;
        super.setName("BlockCompressionThread");
        super.setDaemon(true); //JVM can end even if thread is still running
        super.setPriority(4); //slightly low priority to minimize CPU usage
    }

    @Override
    public void run() {
        while (true) {
            for (BlockStorage storage : storages) {
                storage.compressArrays();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
