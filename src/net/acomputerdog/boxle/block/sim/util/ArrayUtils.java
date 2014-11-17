package net.acomputerdog.boxle.block.sim.util;

public class ArrayUtils {
    public static void limBytes(byte[] bytes, byte lim) {
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = limit(bytes[index], lim);
        }
    }

    private static byte limit(byte b, byte limit) {
        return (byte) (b % limit);
    }
}
