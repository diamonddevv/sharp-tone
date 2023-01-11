package net.diamonddev.sharptone.item;

import net.diamonddev.sharptone.SharpToneMod;
import net.diamonddev.sharptone.util.EnchantmentUtil;
import net.diamonddev.sharptone.util.InstrumentHelper;
import net.diamonddev.sharptone.util.SonicBoomAttack;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ResonantDaggerItem extends SwordItem implements InstrumentHelper {

    private final Random r = new Random();
    private static final String CHARGE_HELPER_KEY = "sharptone.charge_helper";
    private static final String CHARGE_TRUE_KEY = "sharptone.charge";
    private static final String FULL_CHARGE_KEY = "sharptone.full";

    private enum ResonateCharge {

        SILENT_ECHO(0),
        ECHOING_WHISPER(1),
        REVERBERATING_CALL(2),
        VIBRATING_SCREAM(3),
        RESONATE_VOICES(4);

        private final int level;
        ResonateCharge(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public String getTranslationKey() {
            return "text.sharptone.charge." + this.getLevel();
        }

        public static ResonateCharge getCharge(int l) {
            for (ResonateCharge c : ResonateCharge.values()) {
                if (c.getLevel() == l) {
                    return c;
                }
            }
            return l >= 4 ? RESONATE_VOICES : SILENT_ECHO;
        }
    }

    public int getLastMilestone(float f) {
        /*
        Milestones are multiples of 5:

        M0 = 0-4
        M1 = 5-9;
        M2 = 10-14
        M3 = 15-19
        M4 = 20
        */
        return (int) ((Math.floor(f / 5) * 5) / 5);
    }


    public ResonantDaggerItem() {
        super(new ResonantToolMaterial(), 1, 4,
                new FabricItemSettings().maxCount(1).maxDamage(900));
    }

    // NBT Stuff
    public void updateNbt(ItemStack stack, Entity entity, LivingEntity killedEntity) {
        NbtCompound nbt = stack.getOrCreateNbt();
        float previous = nbt.getFloat(CHARGE_HELPER_KEY);
        int previousMilestone = nbt.getInt(CHARGE_TRUE_KEY);

        stack.setNbt(null); // clear

        int charge = killedEntity.getXpToDrop() * (EnchantmentUtil.getYieldingLevel(stack) + 1); // Add Yielding Bonus
        float f = previous <= 20.0f ? previous + (charge * r.nextFloat(0.1f, 0.8f)) : 20.0f;

        if (previousMilestone < getLastMilestone(f)) {
            entity.getWorld().playSound(null, entity.getBlockPos(), SharpToneMod.CHARGE, SoundCategory.PLAYERS, this.getVolume(), this.getPitch());
        }

        nbt.putFloat(CHARGE_HELPER_KEY, f);
        nbt.putInt(CHARGE_TRUE_KEY, getLastMilestone(f));
        nbt.putBoolean(FULL_CHARGE_KEY, f >= 20.0f);
        stack.setNbt(nbt);
    }

    public static void fixNBT(ItemStack stack) {
        float helperCharge = stack.getOrCreateNbt().getFloat(CHARGE_HELPER_KEY);
        int trueCharge;
        boolean fullCharge;

        // Assume helper charge is correct

        // Fix True-Charge
        trueCharge = (int) Math.floor(helperCharge / 5);

        // Fix Full-Charge
        fullCharge = trueCharge == 4;

        // Apply
        stack.getOrCreateNbt().putFloat(CHARGE_HELPER_KEY, helperCharge);
        stack.getOrCreateNbt().putInt(CHARGE_TRUE_KEY, trueCharge);
        stack.getOrCreateNbt().putBoolean(FULL_CHARGE_KEY, fullCharge);
    }

    public static void setHelperValue(ItemStack stack, float helper) {
        stack.getOrCreateNbt().putFloat(CHARGE_HELPER_KEY, helper);
    }

    public void resetChargeNBT(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean(FULL_CHARGE_KEY, false);
        nbt.putFloat(CHARGE_HELPER_KEY, 0.0f);
        nbt.putInt(CHARGE_TRUE_KEY, 0);
        stack.setNbt(nbt);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt()) {
            int charge = stack.getOrCreateNbt().getInt(CHARGE_TRUE_KEY);
            ResonateCharge resonateCharge = ResonateCharge.getCharge(charge);

            MutableText text = Text.translatable("text.sharptone.charge.prefix");
            text.append(" ").append(Text.translatable(resonateCharge.getTranslationKey()));

            tooltip.add(text);
        }
    }

    // Sound Stuff
    @Override
    public SoundEvent getSoundEvent() {
        return SharpToneMod.DISCHARGE;
    }

    @Override
    public float getPitch() {
        return r.nextFloat(0.1f, 2.0f);
    }

    @Override
    public float getVolume() {
        return r.nextFloat(5.0f, 15.0f);
    }

    // Usage
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 160);
        stack.damage(10, net.minecraft.util.math.random.Random.create(), null);
        world.playSound(null, user.getBlockPos(), this.getSoundEvent(), SoundCategory.PLAYERS, this.getVolume(), this.getPitch());
        if (!world.isClient) {
            SonicBoomAttack.create
                    (
                            user,
                            user.getCommandSource().getWorld(),
                            (int) stack.getOrCreateNbt().getFloat(CHARGE_HELPER_KEY) * (stack.getOrCreateNbt().getBoolean(FULL_CHARGE_KEY) ? 18 : 8),
                            (stack.getOrCreateNbt().getInt(CHARGE_TRUE_KEY) + 1) * (EnchantmentUtil.getShriekingLevel(stack) * 2)
                    );

            this.resetChargeNBT(stack);
        }

        return super.use(world, user, hand);

    }


    public static void appendStacks(FabricItemGroupEntries group, Item after) {
        Collection<ItemStack> daggers = new ArrayList<>();
        for (ResonateCharge chargeLevel : ResonateCharge.values()) {
            int charge = chargeLevel.getLevel();
            ItemStack stack = new ItemStack(SharpToneMod.RESONANT_DAGGER);
            setHelperValue(stack, charge * 5);
            fixNBT(stack);

            daggers.add(stack);
        }
        group.addAfter(after, daggers);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.setNbt(null);
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean(FULL_CHARGE_KEY, false);
        nbt.putFloat(CHARGE_HELPER_KEY, 0.0f);
        nbt.putInt(CHARGE_TRUE_KEY, 0);
        stack.setNbt(nbt);
        return stack;
    }
}
