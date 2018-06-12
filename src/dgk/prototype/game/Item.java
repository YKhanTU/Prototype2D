package dgk.prototype.game;

import java.io.Serializable;

public abstract class Item implements Serializable {

    /**
     * The displayable name of the item.
     */
    private String name;

    /**
     * The rarity of the item, which is influenced by an RNG.
     * If an item is not influenced by this, then it simply is Rarity.NONE.
     */
    private Rarity rarity;

    /**
     * The price of the item. This is heavily influenced by RNG and the Economy.
     */
    private int price;

    /**
     * If true, the item has a stack size > 1. Otherwise, it's stack size is 1 and cannot be stacked.
     */
    private boolean isStackable;
    private int stackSize;
    private int stackLimit;

    public Item(String name, Rarity rarity, boolean isStackable) {
        this(name, rarity, isStackable, 1, 1);
    }

    public Item(String name, Rarity rarity, boolean isStackable, int stackSize, int stackLimit) {
        this.name = name;
        this.rarity = rarity;
        this.price = -1;
        this.isStackable = isStackable;

        if(isStackable) {
            this.stackSize = stackSize;
            this.stackLimit = stackLimit;
        }else{
            this.stackSize = 1;
            this.stackLimit = 1;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public void setStackable(boolean stackable) {
        isStackable = stackable;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public int getStackLimit() {
        return stackLimit;
    }

    public void setStackLimit(int stackLimit) {
        this.stackLimit = stackLimit;
    }

    /**
     * This determines whether or not the Item is tradeable.
     * If the 'price' variable is set to -1, then it is not tradeable.
     * @return True if price does not equal -1, otherwise False.
     */
    public boolean isTradeable() {
        return price != -1;
    }
}
