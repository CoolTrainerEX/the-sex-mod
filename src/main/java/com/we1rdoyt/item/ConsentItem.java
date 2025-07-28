package com.we1rdoyt.item;

import java.util.UUID;

import com.we1rdoyt.component.ModDataComponentTypes;

import net.minecraft.component.ComponentChanges;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ConsentItem extends Item {

    public ConsentItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();

        if (world.isClient() || !entity.isLiving() || !stack.isOf(this))
            return ActionResult.PASS;

        if (entity.isPlayer()) {
            stack.decrementUnlessCreative(1, user);
            playerToPlayer(user, (PlayerEntity) entity);
        } else if (user.isSneaking()) {
            ItemStack newStack = ItemUsage.exchangeStack(stack, user, new ItemStack(Registries.ITEM.getEntry(this), 1,
                    ComponentChanges.builder().add(ModDataComponentTypes.TARGET_ENTITY, entity.getUuid()).build()));

            if (!ItemStack.areEqual(stack, newStack))
                user.getInventory().offerOrDrop(newStack);

            return ActionResult.CONSUME.withNewHandStack(newStack);
        } else if (stack.contains(ModDataComponentTypes.TARGET_ENTITY)) {
            UUID uuid = stack.get(ModDataComponentTypes.TARGET_ENTITY);

            if (uuid.equals(entity.getUuid())) {
                user.sendMessage(Text.translatable("item.the-sex-mod.consent.message.same_entity"), true);
                return ActionResult.PASS;
            }

            stack.decrement(1);
            entityToEntity((LivingEntity) world.getEntity(uuid), (LivingEntity) entity);
        } else {
            stack.decrementUnlessCreative(1, user);
            playerToEntity(user, (LivingEntity) entity);
        }

        return ActionResult.SUCCESS.withNewHandStack(stack);
    }

    private static void playerToPlayer(PlayerEntity player, PlayerEntity target) {

    }

    private static void playerToEntity(PlayerEntity player, LivingEntity entity) {

    }

    private static void entityToEntity(LivingEntity entity, LivingEntity target) {

    }
}
