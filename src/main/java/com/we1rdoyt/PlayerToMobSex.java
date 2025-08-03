package com.we1rdoyt;

import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PlayerToMobSex implements Sex {
    private final ServerPlayerEntity player;
    private final MobEntity mob;
    private final boolean consent;
    private boolean started = false, ended = false;
    private int tick = 0, sexBar = 0, sexHealth = MAX_SEX_HEALTH;

    public PlayerToMobSex(ServerPlayerEntity player, MobEntity mob) {
        this.player = player;
        this.mob = mob;

        double chance = 0.5;

        if (mob.hasStatusEffect(ModStatusEffects.LIBIDO))
            chance += (mob.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 0.25;

        if (!(consent = mob.getRandom().nextDouble() < chance))
            Sex.entityParticles(mob, ParticleTypes.ANGRY_VILLAGER, (ServerWorld) mob.getWorld());
    }

    @Override
    public LivingEntity getEntity() {
        return player;
    }

    @Override
    public LivingEntity getTarget() {
        return mob;
    }

    @Override
    public boolean getConsent() {
        return consent;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void startSex() {
        player.startRiding(mob, true);
        started = true;
    }

    @Override
    public boolean tick() {
        if (player.isRemoved() || mob.isRemoved() || !mob.hasPassenger(player) || sexBar >= MAX_SEX_BAR
                || sexHealth <= 0)
            return false;

        tick = (tick + 1) % MAX_TICKS;

        player.sendMessage(hudDisplay(), true);

        if (tick == 0)
            fail();
        else if (player.isJumping())
            success();

        return true;
    }

    @Override
    public void success() {
        int bonus = 0;

        for (LivingEntity livingEntity : new LivingEntity[] { player, mob })
            if (livingEntity.hasStatusEffect(ModStatusEffects.LIBIDO))
                bonus += (livingEntity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 5;

        sexBar += (double) MAX_SEX_BAR_ADD * Math.min(tick + bonus, MAX_TICKS) / MAX_TICKS;
        tick = 0;
        Sex.entityParticles(player, ParticleTypes.HAPPY_VILLAGER, player.getWorld());
        mob.playAmbientSound();
    }

    @Override
    public void fail() {
        sexHealth--;
        mob.damage((ServerWorld) mob.getWorld(), player.getDamageSources().mobAttack(player), 1);
        Sex.entityParticles(player, ParticleTypes.ANGRY_VILLAGER, player.getWorld());
    }

    /**
     * Text to display to the player
     * 
     * @return Text to display
     */
    private Text hudDisplay() {
        int length = 10;
        MutableText text = Text.empty();

        for (int i = 0; i < MAX_SEX_HEALTH; i++)
            text.append(Text.translatable("sex.the-sex-mod.sex_health_icon")
                    .formatted(i < sexHealth ? Formatting.RED : Formatting.GRAY));

        text.append("    ");

        for (int i = 0; i < length; i++)
            text.append(Text.translatable("sex.the-sex-mod.sex_bar_icon")
                    .formatted(i < (double) length * sexBar / MAX_SEX_BAR ? Formatting.BLUE : Formatting.GRAY));

        text.append("    ");

        for (int i = 0; i < length; i++)
            text.append(Text.translatable("sex.the-sex-mod.ticks_icon")
                    .formatted(i < (double) length * tick / MAX_TICKS ? Formatting.GREEN : Formatting.GRAY));

        return text;
    }

    @Override
    public void endSex() {
        LivingEntity[] entities = { player, mob };

        ended = true;
        player.stopRiding();
        player.sendMessage(Text.empty(), true);

        boolean playerHasSTD = player.hasStatusEffect(ModStatusEffects.STD);
        boolean entityHasSTD = mob.hasStatusEffect(ModStatusEffects.STD);

        if ((playerHasSTD && entityHasSTD) || (!playerHasSTD && !entityHasSTD))
            return;

        for (int i = 0; i < entities.length; i++) {
            LivingEntity source = entities[i];
            LivingEntity dest = entities[1 - i];

            if (source.hasStatusEffect(ModStatusEffects.STD) && !dest.hasStatusEffect(ModStatusEffects.STD)) {
                StatusEffectInstance statusEffectInstance = source.getStatusEffect(ModStatusEffects.STD);

                if (dest.getRandom().nextDouble() < ((STDStatusEffect) statusEffectInstance.getEffectType().value())
                        .getInfectChance() + (statusEffectInstance.getAmplifier() + 1) * 0.25)
                    dest.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
        }
    }
}
