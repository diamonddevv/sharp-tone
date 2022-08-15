package net.diamonddev.sharptone;

import net.diamonddev.sharptone.item.ResonantDaggerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.diamonddev.sharptone.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharpToneMod implements ModInitializer {

	public static final String modid = "sharptone";
	public static final Logger LOGGER = LoggerFactory.getLogger("Sharp Tone");

	public static final ResonantDaggerItem RESONANT_DAGGER = new ResonantDaggerItem();
	public static final Item DEEPSLATE_ROD = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item REINFORCED_HANDLE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item ECHOING_BLADE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static SoundEvent DISCHARGE;
	@Override
	public void onInitialize() {
		long s = System.currentTimeMillis();
		//

		Registry.register(Registry.ITEM, new Identifier("deepslate_rod"), DEEPSLATE_ROD);
		Registry.register(Registry.ITEM, new Identifier("resonant_dagger"), RESONANT_DAGGER);
		Registry.register(Registry.ITEM, new Identifier("reinforced_handle"), REINFORCED_HANDLE);
		Registry.register(Registry.ITEM, new Identifier("echoing_blade"), ECHOING_BLADE);

		DISCHARGE = createSound("item.sharptone.resonant_dagger.release_charge");

		//
		long time = System.currentTimeMillis() - s;
		LOGGER.info("Sharp Tone has been initialized. (" + time + " milliseconds)");
	}

	private SoundEvent createSound(String name) {
		Identifier id = new Identifier(name);
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
	}
}
