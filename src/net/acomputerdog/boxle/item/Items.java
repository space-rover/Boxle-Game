package net.acomputerdog.boxle.item;

import net.acomputerdog.core.logger.CLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry of Items
 */
public class Items {

    /**
     * Logger for Items
     */
    private static final CLogger logger = new CLogger("ItemRegistry", true, true);

    /**
     * Map of item names to instances
     */
    private static final Map<String, Item> itemMap = new ConcurrentHashMap<>();

    /**
     * Registers a item into the registry
     *
     * @param item The item to register. Cannot be null.
     */
    public static void registerItem(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot register a null item!");
        if (itemMap.put(item.getName(), item) != null) {
            logger.logWarning("Registering duplicate item: " + item.getName());
        }
    }

    /**
     * Gets an item by it's name.
     *
     * @param name The name of the item.
     * @return Return the item with the given name, or null if none is defined.
     */
    public static Item getItem(String name) {
        if (name == null) {
            return null; //OK to return null, since a non-existent key will return null anyway.
        }
        return itemMap.get(name);
    }
}
