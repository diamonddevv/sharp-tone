package net.diamonddev.sharptone;

import net.diamonddev.sharptone.enchantment.ShriekingEnchantment;
import net.diamonddev.sharptone.enchantment.YieldingEnchantment;
import net.diamonddev.sharptone.item.ResonantDaggerItem;
import net.diamonddev.sharptone.util.IdBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharpToneMod implements ModInitializer {

	public static final String modid = "sharptone";
	public static final IdBuilder ID = new IdBuilder(modid);
	public static final Logger LOGGER = LoggerFactory.getLogger("Sharp Tone");

	public static final ResonantDaggerItem RESONANT_DAGGER = new ResonantDaggerItem();
	public static final Item DEEPSLATE_ROD = new Item(new FabricItemSettings());
	public static final Item REINFORCED_HANDLE = new Item(new FabricItemSettings());
	public static final Item ECHOING_BLADE = new Item(new FabricItemSettings());

	public static final YieldingEnchantment YIELDING = new YieldingEnchantment();
	public static final ShriekingEnchantment SHRIEKING = new ShriekingEnchantment();

	public static SoundEvent DISCHARGE;
	public static SoundEvent CHARGE;
	@Override
	public void onInitialize() {
		long s = System.currentTimeMillis();
		//

		Registry.register(Registries.ITEM, ID.build("resonant_dagger"), RESONANT_DAGGER);
		Registry.register(Registries.ITEM, ID.build("deepslate_rod"), DEEPSLATE_ROD);
		Registry.register(Registries.ITEM, ID.build("reinforced_handle"), REINFORCED_HANDLE);
		Registry.register(Registries.ITEM, ID.build("echoing_blade"), ECHOING_BLADE);

		Registry.register(Registries.ENCHANTMENT, ID.build("yielding"), YIELDING);
		Registry.register(Registries.ENCHANTMENT, ID.build("shrieking"), SHRIEKING);

		DISCHARGE = createSound("item.sharptone.resonant_dagger.release_charge");
		CHARGE = createSound("item.sharptone.resonant_dagger.charge");

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			ResonantDaggerItem.appendStacks(entries, Items.TRIDENT);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
			entries.addAfter(Items.DISC_FRAGMENT_5, DEEPSLATE_ROD);
			entries.addAfter(DEEPSLATE_ROD, REINFORCED_HANDLE);
			entries.addAfter(REINFORCED_HANDLE, ECHOING_BLADE);
		});

		//
		long time = System.currentTimeMillis() - s;
		LOGGER.info("Sharp Tone has been initialized. (" + time + " milliseconds)");
	}

	private SoundEvent createSound(String name) {
		Identifier id = ID.build(name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}
}
