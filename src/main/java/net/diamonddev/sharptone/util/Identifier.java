package net.diamonddev.sharptone.util;

import net.diamonddev.sharptone.SharpToneMod;

public class Identifier extends net.minecraft.util.Identifier {
    public Identifier(String path) {
        super(SharpToneMod.modid, path);
    }
}
