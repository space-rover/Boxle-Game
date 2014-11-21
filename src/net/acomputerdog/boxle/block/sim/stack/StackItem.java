package net.acomputerdog.boxle.block.sim.stack;

public class StackItem {
    public static final String TYPE_INT = "INT";
    public static final String TYPE_BOOLEAN = "BOOL";
    public static final String TYPE_FLOAT = "FLOAT";
    public static final String TYPE_STRING = "STRING";
    public static final String TYPE_NULL = "NULL";
    public static final StackItem NULL = new StackItem(null, "NULL");

    private final Object obj;
    private final String type;

    public StackItem(Object obj, String type) {
        this.obj = obj;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Object getObj() {
        return obj;
    }

    public boolean isType(String type) {
        return type != null && type.equals(this.type);
    }

    public String toString() {
        return type + (obj == null ? "" : "(" + obj.toString() + ")");
    }
}
