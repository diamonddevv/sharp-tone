package net.diamonddev.sharptone.util;

import net.minecraft.sound.SoundEvent;

public interface InstrumentHelper {
    SoundEvent getSoundEvent();
    float getPitch();
    float getVolume();
}
