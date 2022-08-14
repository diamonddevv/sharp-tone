package net.diamonddev.sharptone.mixin;

import net.diamonddev.sharptone.item.ResonantDaggerItem;
import net.diamonddev.sharptone.util.SharpToneDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }



    @Inject(method = "onKilledBy",at =  @At("HEAD"))
    private void sharptone$increaseResonance(LivingEntity adversary, CallbackInfo ci) {
        if (adversary != null) {
            ItemStack stack = adversary.getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof ResonantDaggerItem resonantDaggerItem) {
                resonantDaggerItem.updateNbt(stack, adversary);
            }
        }
    }

    @Inject(method = "modifyAppliedDamage",at =  @At("HEAD"), cancellable = true)
    private void sharptone$preventSelfSonicDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (Objects.equals(source.name, "sonic_boom_atk")) {
            if (source.getSource() == this) {
                cir.setReturnValue(0.0f);
            }
        }
    }
}
