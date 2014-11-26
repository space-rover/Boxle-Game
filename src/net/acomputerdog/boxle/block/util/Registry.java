package net.acomputerdog.boxle.block.util;

import java.util.*;

public class Registry<T extends Identifiable> {
    private final Map<String, T> defMap;
    private final Map<String, T> idMap;

    public Registry() {
        defMap = new LinkedHashMap<>();
        idMap = new LinkedHashMap<>();
    }

    public <T2 extends T> T2 register(T2 item) {
        defMap.put(item.getDefinition(), item);
        idMap.put(item.getId(), item);
        return item;
    }

    public boolean isDefined(String def) {
        return defMap.containsKey(def);
    }

    public boolean isDefined(T item) {
        return defMap.containsValue(item);
    }

    public T getFromDef(String def) {
        return defMap.get(def);
    }

    public T getFromId(String id) {
        return idMap.get(id);
    }

    public Collection<T> getItems() {
        return defMap.values();
    }

    public Set<String> getDefinitions() {
        return Collections.unmodifiableSet(defMap.keySet());
    }

    public Set<String> getIds() {
        return Collections.unmodifiableSet(idMap.keySet());
    }
}
