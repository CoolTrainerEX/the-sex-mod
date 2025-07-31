package com.we1rdoyt.item;

import java.util.UUID;

import com.we1rdoyt.TheSexMod;
import com.we1rdoyt.component.ModDataComponentTypes;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

        if (world.isClient() || user.getItemCooldownManager().isCoolingDown(stack) || !entity.isLiving()
                || TheSexMod.sexDataContains(entity))
            return ActionResult.PASS;

        if (entity.isPlayer())
            TheSexMod.addSexData(user, (PlayerEntity) entity);
        else if (user.isSneaking()) {
            stack.set(ModDataComponentTypes.TARGET_ENTITY, entity.getUuid());
            return ActionResult.SUCCESS;
        } else if (stack.contains(ModDataComponentTypes.TARGET_ENTITY)) {
            UUID uuid = stack.get(ModDataComponentTypes.TARGET_ENTITY);

            if (uuid.equals(entity.getUuid())) {
                user.sendMessage(Text.translatable("item.the-sex-mod.consent.message.same_entity"), true);
                return ActionResult.PASS;
            }

            TheSexMod.addSexData((LivingEntity) world.getEntity(uuid), entity);
        } else
            TheSexMod.addSexData(user, entity);

        stack.decrementUnlessCreative(1, user);
        stack.getOrDefault(DataComponentTypes.USE_COOLDOWN, new UseCooldownComponent(10)).set(stack, user);
        return ActionResult.SUCCESS.withNewHandStack(stack);
    }
}
