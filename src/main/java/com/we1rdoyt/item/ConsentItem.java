package com.we1rdoyt.item;

import java.util.UUID;

import com.we1rdoyt.TheSexMod;
import com.we1rdoyt.component.ModDataComponentTypes;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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

        if (world.isClient() || user.getItemCooldownManager().isCoolingDown(stack) || !entity.isMobOrPlayer()
                || !entity.isLiving() || TheSexMod.sexDataContains(entity))
            return ActionResult.PASS;

        UUID uuid;

        if (entity.isPlayer())
            TheSexMod.addSexData((ServerPlayerEntity) user, (ServerPlayerEntity) entity);
        else if (user.isSneaking()) {
            stack.set(ModDataComponentTypes.TARGET_ENTITY, entity.getUuid());
            user.setStackInHand(hand, stack);
            return ActionResult.SUCCESS.withNewHandStack(stack);
        } else if (stack.contains(ModDataComponentTypes.TARGET_ENTITY)
                && !(uuid = stack.get(ModDataComponentTypes.TARGET_ENTITY)).equals(entity.getUuid()))
            TheSexMod.addSexData((ServerPlayerEntity) user, (MobEntity) world.getEntity(uuid), (MobEntity) entity);
        else
            TheSexMod.addSexData((ServerPlayerEntity) user, (MobEntity) entity);

        stack.decrementUnlessCreative(1, user);
        stack.getOrDefault(DataComponentTypes.USE_COOLDOWN, new UseCooldownComponent(10)).set(stack, user);
        return ActionResult.SUCCESS.withNewHandStack(stack);
    }
}
