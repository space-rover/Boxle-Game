package net.acomputerdog.boxle.save.util;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.block.Blocks;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockMap {
    private final Map<Integer, Block> readBlockMap;
    private final Map<Block, Integer> writeBlockMap;

    private int nextId = 0;

    public BlockMap() {
        readBlockMap = new HashMap<>();
        writeBlockMap = new HashMap<>();
    }

    public int getIdForBlock(Block block) {
        Integer id = writeBlockMap.get(block);
        if (id == null) {
            id = nextId;
            writeBlockMap.put(block, id);
            readBlockMap.put(id, block);
            nextId++;
        }
        return id;
    }

    public Block getBlockForId(int id) {
        return readBlockMap.get(id);
    }

    public void load(DataInput in) throws IOException {
        readBlockMap.clear();
        writeBlockMap.clear();
        int numIds = in.readInt();
        for (int id = 0; id < numIds; id++) {
            Block block = Blocks.BLOCKS.getFromDef(in.readUTF());
            readBlockMap.put(id, block);
            writeBlockMap.put(block, id);
        }
        nextId = numIds;
    }

    public void save(DataOutput out) throws IOException {
        int numIds = readBlockMap.size();
        out.writeInt(numIds);
        for (int id = 0; id < numIds; id++) {
            out.writeUTF(readBlockMap.get(id).getDefinition());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockMap)) return false;

        BlockMap blockMap = (BlockMap) o;

        return readBlockMap.equals(blockMap.readBlockMap);
    }

    @Override
    public int hashCode() {
        return readBlockMap.hashCode();
    }

    @Override
    public String toString() {
        return "BlockMap{" +
                "readBlockMap=" + readBlockMap +
                ", writeBlockMap=" + writeBlockMap +
                '}';
    }
}
