package dgk.prototype.game;

import java.util.Iterator;

public class Inventory {

    private int inventorySize;
    private boolean isLimited;

    /**
     * The weapon slot. TODO: Change this so that the Person class has the weapon slot, not the Inventory.
     * The ammunition slot is one of multiple slots that are in the storage.
     */
    private Item weaponSlot;
    private Item[] storage;

    /**
     * The Inventory consists of items that are held by a Person.
     * @param inventorySize represents the size of the inventory array (0 -> inventorySize - 1)
     * @param isLimited represents whether or not the inventory is limited by the item stack limit.
     *                  This is commonly used for vendors, traders, and so on.
     */
    public Inventory(int inventorySize, boolean isLimited) {
        this.inventorySize = inventorySize;
        this.isLimited = isLimited;

        this.weaponSlot = null;
        this.storage = new Item[inventorySize];
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public Item getWeaponSlot() {
        return weaponSlot;
    }

    public Item[] getStorage() {
        return storage;
    }

    /**
     * Adds an items to the storage. If the item is the same as another and is stackable, then we will stack them. If the stack limit is exceeded, we will make a new stack.
     * @param add The item that will be added to the storage.
     */
    public void addItem(Item add) {
        return;
    }

    public boolean isFull() {
        for(int i = 0; i < inventorySize; i++) {
            if(storage[i] == null) {
                return false;
            }
        }

        return true;
    }

    public void clear() {
        for(int i = 0; i < inventorySize; i++) {
            storage[i] = null;
        }
    }
}
