package net.acomputerdog.boxle.save.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RandomAccessBuffer {
    private static final int DEFAULT_CAPACITY = 1;
    private static final int BUFFER_SECTION_SIZE = 256;

    private final List<ByteBuffer> buffers = new ArrayList<>();

    private final ByteBuffer intConvertBuf = ByteBuffer.allocate(4);
    private final ByteBuffer longConvertBuf = ByteBuffer.allocate(8);

    private ByteBuffer activeBuffer;
    private int bufferIndex = 0;
    private int bufferPosition = 0;
    private long capacity = 0;

    public RandomAccessBuffer() {
        this(DEFAULT_CAPACITY);
    }

    public RandomAccessBuffer(long initialSize) {
        if (initialSize < 1) {
            initialSize = 1;
        }
        expand(initialSize);
        seek(0);
    }

    //--------------------------Control Methods-----------ss------------------

    public void save(OutputStream out) throws IOException {
        for (ByteBuffer buffer : buffers) {
            for (int index = 0; index < BUFFER_SECTION_SIZE; index++) {
                out.write(buffer.get(index));
            }
        }
    }

    public void load(InputStream in) throws IOException {
        clear();
        while (in.available() > 0) {
            writeByte(in.read());
        }
        seek(0);
    }

    public void seek(long location) {
        if (location >= capacity) {
            expand(location);
        }
        bufferIndex = (int) Math.floor((double) location / (double) BUFFER_SECTION_SIZE);
        bufferPosition = (int) (location % BUFFER_SECTION_SIZE);
        activeBuffer = buffers.get(bufferIndex);
    }

    public long length() {
        return capacity;
    }

    public long position() {
        return (bufferIndex * BUFFER_SECTION_SIZE) + bufferPosition;
    }

    public void clear() {
        bufferIndex = 0;
        bufferPosition = 0;
        capacity = 0;
        activeBuffer = null;
        buffers.clear();
        expand(1);
        seek(0);
    }

    //--------------------------Read Methods---------------------------

    public byte[] readBytes(int length) {
        return readBytes(new byte[length]);
    }

    public byte[] readBytes(byte[] bytes) {
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = readByte();
        }
        return bytes;
    }

    public List<Byte> readBytes(List<Byte> bytes, int length) {
        int numRead = 0;
        while (numRead < length) {
            bytes.add(readByte());
            numRead++;
        }
        return bytes;
    }

    public byte readByte() {
        byte b = activeBuffer.get(bufferPosition);
        incrementBufferPos();
        return b;
    }

    public int[] readInts(int length) {
        return readInts(new int[length]);
    }

    public int[] readInts(int[] ints) {
        for (int index = 0; index < ints.length; index++) {
            ints[index] = readInt();
        }
        return ints;
    }

    public List<Integer> readInts(List<Integer> ints, int length) {
        int numRead = 0;
        while (numRead < length) {
            ints.add(readInt());
            numRead++;
        }
        return ints;
    }

    public int readInt() {
        intConvertBuf.rewind();
        intConvertBuf.put(readBytes(4));
        intConvertBuf.rewind();
        return intConvertBuf.getInt();
    }


    public short[] readShorts(int length) {
        return readShorts(new short[length]);
    }

    public short[] readShorts(short[] shorts) {
        for (int index = 0; index < shorts.length; index++) {
            shorts[index] = readShort();
        }
        return shorts;
    }

    public List<Short> readShorts(List<Short> shorts, int length) {
        int numRead = 0;
        while (numRead < length) {
            shorts.add(readShort());
            numRead++;
        }
        return shorts;
    }

    public short readShort() {
        return (short) (((readByte() & 0xFF) << 8) | (readByte() & 0xFF));
    }

    public long[] readLongs(int length) {
        return readLongs(new long[length]);
    }

    public long[] readLongs(long[] longs) {
        for (int index = 0; index < longs.length; index++) {
            longs[index] = readLong();
        }
        return longs;
    }

    public List<Long> readLongs(List<Long> longs, int length) {
        int numRead = 0;
        while (numRead < length) {
            longs.add(readLong());
            numRead++;
        }
        return longs;
    }

    public long readLong() {
        longConvertBuf.rewind();
        longConvertBuf.put(readBytes(8));
        longConvertBuf.rewind();
        return longConvertBuf.getLong();
    }

    public char[] readChars(int length) {
        return readChars(new char[length]);
    }

    public char[] readChars(char[] chars) {
        for (int index = 0; index < chars.length; index++) {
            chars[index] = readChar();
        }
        return chars;
    }

    public List<Character> readChars(List<Character> chars, int length) {
        int numRead = 0;
        while (numRead < length) {
            chars.add(readChar());
            numRead++;
        }
        return chars;
    }

    public char readChar() {
        return (char) readShort();
    }

    public boolean[] readBooleans(int length) {
        return readBooleans(new boolean[length]);
    }

    public boolean[] readBooleans(boolean[] bools) {
        for (int index = 0; index < bools.length; index++) {
            bools[index] = readBoolean();
        }
        return bools;
    }

    public List<Boolean> readBooleans(List<Boolean> bools, int length) {
        int numRead = 0;
        while (numRead < length) {
            bools.add(readBoolean());
            numRead++;
        }
        return bools;
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public float[] readFloats(int length) {
        return readFloats(new float[length]);
    }

    public float[] readFloats(float[] floats) {
        for (int index = 0; index < floats.length; index++) {
            floats[index] = readFloat();
        }
        return floats;
    }

    public List<Float> readFloats(List<Float> floats, int length) {
        int numRead = 0;
        while (numRead < length) {
            floats.add(readFloat());
            numRead++;
        }
        return floats;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double[] readDoubles(int length) {
        return readDoubles(new double[length]);
    }

    public double[] readDoubles(double[] doubles) {
        for (int index = 0; index < doubles.length; index++) {
            doubles[index] = readDouble();
        }
        return doubles;
    }

    public List<Double> readDoubles(List<Double> doubles, int length) {
        int numRead = 0;
        while (numRead < length) {
            doubles.add(readDouble());
            numRead++;
        }
        return doubles;
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String[] readStrings(int length) {
        return readStrings(new String[length]);
    }

    public String[] readStrings(String[] strings) {
        for (int index = 0; index < strings.length; index++) {
            strings[index] = readString();
        }
        return strings;
    }

    public List<String> readStrings(List<String> strings, int length) {
        int numRead = 0;
        while (numRead < length) {
            strings.add(readString());
            numRead++;
        }
        return strings;
    }

    public String readString() {
        int length = readInt();
        return new String(readChars(length));
    }

    //-------------Write Values-------------------

    public void writeBytes(List<Byte> bytes) {
        for (byte b : bytes) {
            writeByte(b);
        }
    }

    public void writeBytes(byte[] bytes) {
        for (byte b : bytes) {
            writeByte(b);
        }
    }

    public void writeByte(byte b) {
        activeBuffer.put(bufferPosition, b);
        incrementBufferPos();
    }

    public void writeByte(int b) {
        writeByte((byte) (b % 255));
    }

    public void writeInts(List<Integer> ints) {
        for (int i : ints) {
            writeInt(i);
        }
    }

    public void writeInts(int[] ints) {
        for (int i : ints) {
            writeInt(i);
        }
    }

    public void writeInt(int i) {
        intConvertBuf.rewind();
        intConvertBuf.putInt(i);
        intConvertBuf.rewind();
        writeBytes(intConvertBuf.array());
    }

    public void writeLongs(List<Long> longs) {
        for (long l : longs) {
            writeLong(l);
        }
    }

    public void writeLongs(long[] longs) {
        for (long l : longs) {
            writeLong(l);
        }
    }

    public void writeLong(long l) {
        longConvertBuf.rewind();
        longConvertBuf.putLong(l);
        longConvertBuf.rewind();
        writeBytes(longConvertBuf.array());
    }

    public void writeShorts(List<Integer> shorts) {
        for (Integer s : shorts) {
            writeShort(s);
        }
    }

    public void writeShorts(Integer[] shorts) {
        for (Integer s : shorts) {
            writeShort(s);
        }
    }

    public void writeShort(Integer s) {
        writeByte((byte) (s >> 8));
        writeByte((byte) (s.intValue()));
    }

    public void writeChars(List<Character> chars) {
        for (char c : chars) {
            writeChar(c);
        }
    }

    public void writeChars(char[] chars) {
        for (char c : chars) {
            writeChar(c);
        }
    }

    public void writeChar(char c) {
        writeByte((byte) (c >> 8));
        writeByte((byte) (c));
    }

    public void writeBooleans(List<Boolean> bools) {
        for (boolean b : bools) {
            writeBoolean(b);
        }
    }

    public void writeBooleans(boolean[] bools) {
        for (boolean b : bools) {
            writeBoolean(b);
        }
    }

    public void writeBoolean(boolean b) {
        writeByte(b ? (byte) 1 : 0);
    }

    public void writeFloats(List<Float> floats) {
        for (float f : floats) {
            writeFloat(f);
        }
    }

    public void writeFloats(float[] floats) {
        for (float f : floats) {
            writeFloat(f);
        }
    }

    public void writeFloat(float f) {
        writeInt(Float.floatToIntBits(f));
    }

    public void writeDoubles(List<Double> doubles) {
        for (double d : doubles) {
            writeDouble(d);
        }
    }

    public void writeDoubles(double[] doubles) {
        for (double d : doubles) {
            writeDouble(d);
        }
    }

    public void writeDouble(double d) {
        writeLong(Double.doubleToLongBits(d));
    }

    public void writeStrings(List<String> strings) {
        for (String str : strings) {
            writeString(str);
        }
    }

    public void writeStrings(String[] strings) {
        for (String str : strings) {
            writeString(str);
        }
    }

    public void writeString(String str) {
        char[] chars = str.toCharArray();
        writeInt(chars.length);
        writeChars(chars);
    }

    //---------------Internal Methods------------------------------

    private void incrementBufferPos() {
        bufferPosition++;
        if (bufferPosition >= BUFFER_SECTION_SIZE) {
            seek((bufferIndex * BUFFER_SECTION_SIZE) + bufferPosition);
        }
    }

    private void expand(long size) {
        if (size > capacity) {
            long neededSpace = size - capacity;
            long numBuffers = (long) Math.ceil((double) neededSpace / (double) BUFFER_SECTION_SIZE);
            int index = 0;
            while (index < numBuffers) {
                buffers.add(newByteBuf());
                index++;
            }
            capacity = size;
        }
    }

    private static ByteBuffer newByteBuf() {
        return ByteBuffer.allocateDirect(BUFFER_SECTION_SIZE);
    }

    public static void main(String[] args) throws IOException {
        RandomAccessBuffer buf = new RandomAccessBuffer();
        /*
        byte[] nums = new byte[10000];
        new Random().nextBytes(nums);
        RandomAccessBuffer buf = new RandomAccessBuffer(300);
        buf.writeInt(Integer.MAX_VALUE);
        buf.writeBytes(nums);
        buf.writeString("Test!");
        buf.seek(0);
        if (buf.readInt() != Integer.MAX_VALUE) {
            System.err.println("Invalid value!");
        }
        for (int index = 0; index < nums.length; index++) {
            byte byt = buf.readByte();
            if (nums[index] != byt) {
                System.err.println("Byte at index " + index + " was incorrect!  (Expected " + nums[index] + ", got " + byt + ")");
            }
        }
        if (!buf.readString().equals("Test!")) {
            System.err.println("Invalid value!");
        }
        */
        buf.writeByte(0);
        buf.writeString("Blah Blah Blah");
        buf.writeByte(1);
        buf.writeString("Foo Bar");
        buf.writeByte(2);
        buf.writeString("Fizz Buzz");
        buf.writeByte(3);
        OutputStream out = new FileOutputStream("./test.dat");
        buf.save(out);
        out.close();
        InputStream in = new FileInputStream("./test.dat");
        RandomAccessBuffer buf2 = new RandomAccessBuffer();
        buf2.load(in);
        in.close();
        //buf2.seek(0);
        System.out.println(buf2.readByte());
        System.out.println(buf2.readString());
        System.out.println(buf2.readByte());
        System.out.println(buf2.readString());
        System.out.println(buf2.readByte());
        System.out.println(buf2.readString());
        System.out.println(buf2.readByte());
    }
}
