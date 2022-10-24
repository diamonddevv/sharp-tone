package net.diamonddev.sharptone.util;

import net.diamonddev.sharptone.SharpToneMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class EnchantmentUtil {
    public static boolean hasEnchantment(ItemStack stack, Enchantment enchantment) {
        return EnchantmentHelper.getLevel(enchantment, stack) > 0;
    }

    public static int getYieldingLevel(ItemStack stack) {
        if (hasEnchantment(stack, SharpToneMod.YIELDING)) {
            return EnchantmentHelper.getLevel(SharpToneMod.YIELDING, stack);
        }
        return 0;
    }

    public static int getShriekingLevel(ItemStack stack) {
        if (hasEnchantment(stack, SharpToneMod.SHRIEKING)) {
            return EnchantmentHelper.getLevel(SharpToneMod.SHRIEKING, stack);
        }
        return 0;
    }
}
