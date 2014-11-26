package net.acomputerdog.boxle.config;

import net.acomputerdog.core.java.MathUtils;

import java.util.Properties;

public class DataTypeProperties extends Properties {
    public DataTypeProperties() {
        super();
    }

    public DataTypeProperties(Properties defaults) {
        super(defaults);
    }

    public int getIntProperty(String property) {
        return getIntProperty(property, 0);
    }

    public int getIntProperty(String property, int def) {
        String value = super.getProperty(property);
        if (MathUtils.isInteger(value)) {
            return Integer.parseInt(value);
        }
        return def;
    }

    public boolean getBooleanProperty(String property) {
        return getBooleanProperty(property, true);
    }

    public boolean getBooleanProperty(String property, boolean def) {
        String value = super.getProperty(property);
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        return def;
    }

    public float getFloatProperty(String property) {
        return getFloatProperty(property, 0f);
    }

    public float getFloatProperty(String property, float def) {
        String value = super.getProperty(property);
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public byte getByteProperty(String property) {
        return getByteProperty(property, (byte) 0);
    }

    public byte getByteProperty(String property, byte def) {
        String value = super.getProperty(property);
        if (MathUtils.isInteger(value)) {
            return Byte.parseByte(value);
        }
        return def;
    }

    public double getDoubleProperty(String property) {
        return getDoubleProperty(property, 0d);
    }

    public double getDoubleProperty(String property, double def) {
        String value = super.getProperty(property);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public short getShortProperty(String property) {
        return getShortProperty(property, (short) 0);
    }

    public short getShortProperty(String property, short def) {
        String value = super.getProperty(property);
        if (MathUtils.isInteger(value)) {
            return Short.parseShort(value);
        }
        return def;
    }
}
