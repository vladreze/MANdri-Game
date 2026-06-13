package com.mandri.entities;

public class InventoryLogic {
    Item[] slots = new Item[16];

    public InventoryLogic() {
        this.slots = slots;
    }

    public boolean addItem(Item item) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                slots[i] = item;
                return true;
            }
        }
        return false;
    }

    public Item[] getSlots() {
        return slots;
    }
}
