package com.mandri.entities;

public class InventoryLogic {
    Item[] slots = new Item[16];

    private int activeRow = 0;
    private int selectedCol = 0;

    public InventoryLogic() {

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

    public boolean consumeItem(String itemName) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && slots[i].getName().equals(itemName)) {
                slots[i] = null;
                return true;
            }
        }
        return false;
    }

    public Item[] getSlots() {
        return slots;
    }

    public void selectColumn(int col) {
        if (col >= 0 && col < 4) {
            selectedCol = col;
        }
    }

    public void nextRow() {
        activeRow++;
        if (activeRow > 3) {
            activeRow = 0;
        }
    }

    public void prevRow() {
        activeRow--;
        if (activeRow < 0) {
            activeRow = 3;
        }
    }

    public int getGlobalSelectedIndex() {
        return (activeRow * 4) + selectedCol;
    }

    public int getActiveRow() {
        return activeRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public Item getSelectedItem() {
        return slots[getGlobalSelectedIndex()];
    }

    public boolean hasItem(String itemName) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && slots[i].getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
}
