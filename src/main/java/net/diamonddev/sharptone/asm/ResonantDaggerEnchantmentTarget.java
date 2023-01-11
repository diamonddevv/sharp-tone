package net.diamonddev.sharptone.asm;

import net.diamonddev.sharptone.item.ResonantDaggerItem;
import net.diamonddev.sharptone.mixin.EnchantmentTargetMixin;
import net.minecraft.item.Item;

public class ResonantDaggerEnchantmentTarget extends EnchantmentTargetMixin {
    @Override
    public boolean isAcceptableItem(Item item) {
        return item instanceof ResonantDaggerItem;
    }
}
