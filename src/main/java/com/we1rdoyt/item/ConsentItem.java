package com.we1rdoyt.item;

import java.util.UUID;

import com.we1rdoyt.component.ModDataComponentTypes;
import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.component.ComponentChanges;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
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

        if (world.isClient() || !entity.isLiving() || entity.getCommandTags().contains("sex"))
            return ActionResult.PASS;

        if (entity.isPlayer()) {
            stack.decrementUnlessCreative(1, user);
            playerToPlayer(user, (PlayerEntity) entity, (ServerWorld) world);
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
            entityToEntity((LivingEntity) world.getEntity(uuid), (LivingEntity) entity, (ServerWorld) world);
        } else {
            stack.decrementUnlessCreative(1, user);
            playerToEntity(user, (LivingEntity) entity, (ServerWorld) world);
        }

        return ActionResult.SUCCESS.withNewHandStack(stack);
    }

    private static void playerToPlayer(PlayerEntity player, PlayerEntity target, ServerWorld world) {
        if (!getPlayerConsent(target)) {
            noConsentParticles(target, world);
            return;
        }

        startSex(player, target);
        endSex(player, target);
    }

    private static void playerToEntity(PlayerEntity player, LivingEntity entity, ServerWorld world) {
        if (!getEntityConsent(entity)) {
            noConsentParticles(entity, world);
            return;
        }

        startSex(player, entity);
        endSex(player, entity);
    }

    private static void entityToEntity(LivingEntity entity, LivingEntity target, ServerWorld world) {
        if (!getEntityConsent(entity)) {
            noConsentParticles(entity, world);
            return;
        }

        if (!getEntityConsent(target)) {
            noConsentParticles(target, world);
            return;
        }

        startSex(entity, target);
        endSex(entity, target);
    }

    private static boolean getPlayerConsent(PlayerEntity target) {
        return true;
    }

    private static boolean getEntityConsent(LivingEntity entity) {
        double chance = 0.5;

        if (entity.hasStatusEffect(ModStatusEffects.LIBIDO))
            chance += (entity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 0.25;

        return entity.getRandom().nextDouble() < chance;
    }

    private static void noConsentParticles(LivingEntity entity, ServerWorld world) {
        world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, entity.getParticleX(0.5), entity.getBodyY(1),
                entity.getParticleZ(0.5), 5, 0.25, 0.25, 0.25, 1);

    }

    private static boolean startSex(LivingEntity entity, LivingEntity target) {
        entity.addCommandTag("sex");
        target.addCommandTag("sex");
        return entity.startRiding(target, true);
    }

    private static void endSex(LivingEntity entity, LivingEntity target) {
        entity.removeCommandTag("sex");
        target.removeCommandTag("sex");
        entity.stopRiding();

        if (entity.hasStatusEffect(ModStatusEffects.STD)) {
            StatusEffectInstance statusEffectInstance = entity.getStatusEffect(ModStatusEffects.STD);

            if (target.getRandom().nextDouble() < ((STDStatusEffect) statusEffectInstance.getEffectType())
                    .getInfectChance() + (statusEffectInstance.getAmplifier() + 1) * 0.25)
                target.addStatusEffect(statusEffectInstance);
        }
    }
}
