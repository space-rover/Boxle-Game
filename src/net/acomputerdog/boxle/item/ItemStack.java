package net.acomputerdog.boxle.item;

/**
 * Represents an instance of an Item with additional data
 */
public class ItemStack {
    /**
     * Type of item
     */
    private Item itemType;

    /**
     * Item data value
     */
    private int dataValue;

    /**
     * Item damage value
     */
    private int damageValue;

    /**
     * Amount of items in this ItemStack
     */
    private int amount;

    /**
     * Creates a new ItemStack
     *
     * @param itemType    The type of Item
     * @param amount      The amount of the item
     * @param dataValue   The data value of the item
     * @param damageValue The damage value of the item
     */
    public ItemStack(Item itemType, int amount, int dataValue, int damageValue) {
        if (itemType == null) throw new IllegalArgumentException("Item type cannot be null!");
        this.itemType = itemType;
        this.dataValue = dataValue;
        this.damageValue = damageValue;
        this.amount = amount;
    }

    /**
     * Creates a new ItemStack
     *
     * @param itemType  The type of Item
     * @param amount    The amount of the item
     * @param dataValue The data value of the item
     */
    public ItemStack(Item itemType, int amount, int dataValue) {
        this(itemType, amount, dataValue, 0);
    }

    /**
     * Creates a new ItemStack
     *
     * @param itemType The type of Item
     * @param amount   The amount of the item
     */
    public ItemStack(Item itemType, int amount) {
        this(itemType, amount, 0);
    }

    /**
     * Creates a new ItemStack
     *
     * @param itemType The type of Item
     */
    public ItemStack(Item itemType) {
        this(itemType, 1);
    }

    /**
     * Gets the item type of this ItemStack
     *
     * @return Return the item type of this item stack
     */
    public Item getItem() {
        return itemType;
    }

    /**
     * Sets the item type of this ItemStack
     *
     * @param itemType The type of this ItemStack
     */
    public void setItem(Item itemType) {
        if (itemType == null) throw new IllegalArgumentException("Item type cannot be null!");
        this.itemType = itemType;
    }

    /**
     * Gets the data value of the Item
     *
     * @return an int of the data value of this item
     */
    public int getDataValue() {
        return dataValue;
    }

    /**
     * Sets the data value of the item
     *
     * @param dataValue an int of the data value of this item
     */
    public void setDataValue(int dataValue) {
        this.dataValue = dataValue;
    }

    /**
     * Gets the damage on this Item
     *
     * @return return the damage on the item
     */
    public int getDamageValue() {
        return damageValue;
    }

    /**
     * Sets the damage on this item
     *
     * @param damageValue The damage on the item
     */
    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    /**
     * Gets the amount of items in this stack
     *
     * @return Return the amount of items in this itemStack
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of items in this item stack
     *
     * @param amount The amount of the item
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStack)) return false;

        ItemStack itemStack = (ItemStack) o;

        return amount == itemStack.amount && damageValue == itemStack.damageValue && dataValue == itemStack.dataValue && itemType.equals(itemStack.itemType);

    }

    @Override
    public int hashCode() {
        int result = itemType.hashCode();
        result = 31 * result + dataValue;
        result = 31 * result + damageValue;
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "itemType=" + itemType +
                ", dataValue=" + dataValue +
                ", damageValue=" + damageValue +
                ", amount=" + amount +
                '}';
    }
}
