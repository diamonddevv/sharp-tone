package net.diamonddev.sharptone.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class SharpToneDamageSource extends EntityDamageSource {

    public SharpToneDamageSource(String name, Entity source) {
        super(name, source);
    }

    @Override
    protected SharpToneDamageSource setBypassesArmor() {
        return (SharpToneDamageSource) super.setBypassesArmor();
    }

    @Override
    protected SharpToneDamageSource setBypassesProtection() {
        return (SharpToneDamageSource) super.setBypassesProtection();
    }

    @Override
    public SharpToneDamageSource setUsesMagic() {
        return (SharpToneDamageSource) super.setUsesMagic();
    }
    public static DamageSource sonicBoomAtk(Entity attacker) {
        return (new SharpToneDamageSource("sonic_boom_atk", attacker)).setBypassesProtection().setUsesMagic();
    }
}
