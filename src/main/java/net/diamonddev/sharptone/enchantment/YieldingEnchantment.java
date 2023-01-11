package net.diamonddev.sharptone.enchantment;

import net.diamonddev.sharptone.asm.EnchantTarget;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class YieldingEnchantment extends Enchantment {
    public YieldingEnchantment() {
        super(Rarity.RARE, EnchantTarget.DAGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
