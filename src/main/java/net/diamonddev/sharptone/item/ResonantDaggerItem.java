package net.diamonddev.sharptone.item;

import net.diamonddev.sharptone.SharpToneMod;
import net.diamonddev.sharptone.util.InstrumentHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
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

import java.util.List;
import java.util.Random;

public class ResonantDaggerItem extends SwordItem implements InstrumentHelper {

    private final Random r = new Random();
    private final String CHARGE_HELPER_KEY = "sharptone.charge_helper";
    private final String CHARGE_TRUE_KEY = "sharptone.charge";
    private final String FULL_CHARGE_KEY = "sharptone.full";

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
                new FabricItemSettings().maxCount(1).group(ItemGroup.COMBAT).maxDamage(1500));
    }

    // NBT Stuff
    private void updateNbt(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        float previous = nbt.getFloat(CHARGE_HELPER_KEY);
        stack.setNbt(null); // clear

        float f = previous + r.nextFloat(0.2f, 1.8f);
        nbt.putFloat(CHARGE_HELPER_KEY, f);
        nbt.putInt(CHARGE_TRUE_KEY, getLastMilestone(f));
        nbt.putBoolean(FULL_CHARGE_KEY, f >= 20.0f);
        stack.setNbt(nbt);
    } // todo: visual indicator of charge that isnt enchantment glint, maybe texture

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
        user.getItemCooldownManager().set(this, 300);

        world.playSound(null, user.getBlockPos(), this.getSoundEvent(), SoundCategory.PLAYERS, this.getVolume(), this.getPitch());
        this.updateNbt(stack); // todo: no this isnt right

        return super.use(world, user, hand);



    }
}
