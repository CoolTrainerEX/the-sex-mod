package com.we1rdoyt.event.player;

import com.we1rdoyt.component.ModDataComponentTypes;
import com.we1rdoyt.item.ModItems;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;

public class ModEvents {
    public static void initialize() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            if (world.isClient || !entity.isLiving() || !itemStack.isOf(ModItems.CONSENT))
                return ActionResult.PASS;

            if (entity.isPlayer()) {
                // Player to player
            } else if (player.isSneaking())
                return ActionResult.SUCCESS.withNewHandStack(ItemUsage.exchangeStack(itemStack, player,
                        new ItemStack(Registries.ITEM.getEntry(ModItems.CONSENT), 1, ComponentChanges.builder()
                                .add(ModDataComponentTypes.TARGET_ENTITY, entity.getUuid()).build())));
            else if (itemStack.contains(ModDataComponentTypes.TARGET_ENTITY)) {
                // Entity to entity
            } else {
                // Player to entity
            }

            itemStack.decrementUnlessCreative(1, player);
            return ActionResult.SUCCESS.withNewHandStack(itemStack);
        });
    }
}
