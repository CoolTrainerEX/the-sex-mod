package com.we1rdoyt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
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

	private static final List<Sex> SEX_LIST = new ArrayList<>();

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
					for (ItemStack itemStack : player.getInventory().getMainStacks().stream().filter(isFilledConsent)
							.toList()) {
						Entity entity = player.getWorld().getEntity(itemStack.get(ModDataComponentTypes.TARGET_ENTITY));

						if (entity == null || player.getEyePos().squaredDistanceTo(entity.getPos()) > player
								.getEntityInteractionRange() * player.getEntityInteractionRange()) {
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

		ServerTickEvents.START_WORLD_TICK.register((world) -> {
			for (Sex sex : SEX_LIST) {
				if (!sex.isStarted())
					sex.startSex();
				if (sex.tick()) {
					sex.endSex();
				}
			}

			SEX_LIST.removeIf((sex) -> sex.isEnded());
		});
	}

	/**
	 * Adds to {@link #SEX_LIST} if {@link Sex#getConsent} returns true
	 * 
	 * @param player Player to sex
	 * @param target Target player to sex
	 */
	public static void addSexData(PlayerEntity player, PlayerEntity target) {
		Sex playerToPlayerSex = new PlayerToPlayerSex(player, target);

		if (playerToPlayerSex.getConsent())
			SEX_LIST.add(playerToPlayerSex);
	}

	/**
	 * Adds to {@link #SEX_LIST} if {@link Sex#getConsent} returns true
	 * 
	 * @param player Player to sex
	 * @param entity Entity to sex
	 */
	public static void addSexData(PlayerEntity player, LivingEntity entity) {
		Sex playerToEntitySex = new PlayerToEntitySex(player, entity);

		if (playerToEntitySex.getConsent())
			SEX_LIST.add(playerToEntitySex);
	}

	/**
	 * Adds to {@link #SEX_LIST} if {@link Sex#getConsent} returns true
	 * 
	 * @param entity Entity to sex
	 * @param target Target entity to sex
	 */
	public static void addSexData(LivingEntity entity, LivingEntity target) {
		Sex entityToEntitySex = new EntityToEntitySex(entity, target);

		if (entityToEntitySex.getConsent())
			SEX_LIST.add(entityToEntitySex);
	}

	/**
	 * Checks if {@link #SEX_LIST} contains the entity
	 * 
	 * @param entity Entity to check
	 * @return True if entity is found
	 */
	public static boolean sexDataContains(LivingEntity entity) {
		return SEX_LIST.stream().anyMatch((sex) -> sex.getEntity() == entity || sex.getTarget() == entity);
	}
}