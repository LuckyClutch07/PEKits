package net.luckyfeed.pekits.utils;

import cn.nukkit.item.Item;

import static cn.nukkit.item.Item.*;

public enum ArmorType{
    HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);

    private final int slot;

    ArmorType(int slot){
        this.slot = slot;
    }

    /**
     * Attempts to match the ArmorType for the specified ItemStack.
     *
     * @param itemStack The ItemStack to parse the type of.
     * @return The parsed ArmorType. (null if none were found.)
     */
    public final static ArmorType matchType(final Item itemStack){
        if(itemStack == null) { return null; }
        switch (itemStack.getId()){
            case DIAMOND_HELMET:
            case GOLD_HELMET:
            case IRON_HELMET:
            case CHAIN_HELMET:
            case LEATHER_CAP:
            case PUMPKIN:
            case SKULL:
                return HELMET;
            case DIAMOND_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case IRON_CHESTPLATE:
            case CHAIN_CHESTPLATE:
            case LEATHER_TUNIC:
                return CHESTPLATE;
            case DIAMOND_LEGGINGS:
            case GOLD_LEGGINGS:
            case IRON_LEGGINGS:
            case CHAIN_LEGGINGS:
            case LEATHER_PANTS:
                return LEGGINGS;
            case DIAMOND_BOOTS:
            case GOLD_BOOTS:
            case IRON_BOOTS:
            case CHAIN_BOOTS:
            case LEATHER_BOOTS:
                return BOOTS;
            default:
                return null;
        }
    }

    public int getSlot(){
        return slot;
    }
}