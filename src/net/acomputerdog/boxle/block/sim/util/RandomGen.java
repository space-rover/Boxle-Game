package net.acomputerdog.boxle.block.sim.util;

import java.util.Random;

public class RandomGen {
    private static final Random random = new Random("RandomGen".hashCode() + System.currentTimeMillis());
    private static final char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ".toCharArray();

    public static int nextInt() {
        return random.nextInt();
    }

    public static String nextString() {
        return String.valueOf(nextInt());
        /*
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        ArrayUtils.limBytes(bytes, (byte)52);
        return createString(bytes);
        */
    }

    private static String createString(byte[] bytes) {
        char[] chars = new char[bytes.length];
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = (byte) (bytes[index] % chars.length);
        }
        return new String(chars);
    }
}
