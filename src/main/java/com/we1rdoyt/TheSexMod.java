package com.we1rdoyt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.we1rdoyt.component.ModDataComponentTypes;
import com.we1rdoyt.item.ModItemGroups;
import com.we1rdoyt.item.ModItems;
import com.we1rdoyt.potion.ModPotions;

public class TheSexMod implements ModInitializer {
	public static final String MOD_ID = "the-sex-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ModPotions.initialize();
		ModItems.initialize();
		ModItemGroups.initialize();
		ModDataComponentTypes.initialize();

		ServerTickEvents.START_SERVER_TICK.register(client -> {
			Predicate<ItemStack> isFilledConsent = itemStack -> itemStack.isOf(ModItems.CONSENT)
					&& itemStack.contains(ModDataComponentTypes.TARGET_ENTITY);

			for (ServerPlayerEntity player : client.getPlayerManager().getPlayerList())
				if (player.getInventory().contains(isFilledConsent))
					for (ItemStack itemStack : player.getInventory().getMainStacks()) {
						Entity entity = player.getWorld().getEntity(itemStack.get(ModDataComponentTypes.TARGET_ENTITY));

						if (isFilledConsent.test(itemStack)
								&& (entity == null || player.getEyePos().squaredDistanceTo(entity.getPos()) > player
										.getEntityInteractionRange() * player.getEntityInteractionRange())) {
							player.getInventory().removeOne(itemStack);
							itemStack.remove(ModDataComponentTypes.TARGET_ENTITY);
							player.getInventory().offerOrDrop(itemStack);
							player.sendMessage(entity == null
									? Text.translatable("item.the-sex-mod.consent.message.entity_does_not_exist")
									: Text.translatable("item.the-sex-mod.consent.message.out_of_range",
											entity.getName()),
									true);
						}
					}
		});
	}
}