package net.diamonddev.sharptone.util;

import net.minecraft.util.Identifier;

public class IdBuilder {

    private final String modid;

    public IdBuilder(String modid) {
        this.modid = modid;
    }

    public Identifier build(String path) {
        return new Identifier(modid, path);
    }
}
