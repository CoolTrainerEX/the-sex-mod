package com.we1rdoyt;

import com.we1rdoyt.component.ModDataComponentTypes;
import com.we1rdoyt.item.ModItems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TheSexModClient implements ClientModInitializer {
	private static boolean jump = false;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as
		// rendering.

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			boolean newJump = client.player != null && client.options.jumpKey.isPressed();

			if (!jump && newJump)
				ClientPlayNetworking.send(client.player.hasVehicle() ? new RidingPlayerJump() : new PlayerJump());

			jump = newJump;
		});

		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
			if (!stack.isOf(ModItems.CONSENT) || !stack.contains(ModDataComponentTypes.TARGET_ENTITY))
				return;

			lines.add(Text.translatable("item.the-sex-mod.consent.tooltip", MinecraftClient.getInstance().world
					.getEntity(stack.get(ModDataComponentTypes.TARGET_ENTITY)).getName()));
		});
	}
}