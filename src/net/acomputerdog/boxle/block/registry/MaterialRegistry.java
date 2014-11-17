package net.acomputerdog.boxle.block.registry;

import net.acomputerdog.boxle.block.material.Material;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MaterialRegistry {
    private final Map<String, Material> materialRegistry;

    public MaterialRegistry() {
        materialRegistry = new LinkedHashMap<>();
    }

    public void registerMaterial(Material material) {
        registerMaterial(material.getId(), material);
    }

    public void registerMaterial(String id, Material material) {
        materialRegistry.put(id, material);
    }

    public boolean isMaterialDefined(String id) {
        return materialRegistry.containsKey(id);
    }

    public boolean isMaterialDefined(Material material) {
        return materialRegistry.containsValue(material);
    }

    public Material getMaterial(String id) {
        return materialRegistry.get(id);
    }

    public Collection<Material> getMaterials() {
        return materialRegistry.values();
    }

    public Set<String> getIDs() {
        return materialRegistry.keySet();
    }
}
