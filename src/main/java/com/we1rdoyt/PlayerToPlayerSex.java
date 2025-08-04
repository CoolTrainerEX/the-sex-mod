package com.we1rdoyt;

import java.util.HashMap;
import java.util.Map;

import com.we1rdoyt.advancement.criterion.ModCriteria;
import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PlayerToPlayerSex implements Sex {
    private final ServerPlayerEntity player, target;
    private final boolean consent;
    private boolean started = false, ended = false, checkOtherPlayerInput = false;
    private int tick = 0, sexBar = 0, sexHealth = MAX_SEX_HEALTH;
    private final Map<ServerPlayerEntity, Boolean> inputs = new HashMap<>();

    public PlayerToPlayerSex(ServerPlayerEntity player, ServerPlayerEntity target) {
        this.player = player;
        this.target = target;

        inputs.put(player, false);
        inputs.put(target, false);

        if (!(consent = target.isSneaking()))
            Sex.entityParticles(target, ParticleTypes.ANGRY_VILLAGER, target.getWorld());

        ModCriteria.SEX.trigger(player, player, target, consent);
    }

    @Override
    public LivingEntity getEntity() {
        return player;
    }

    @Override
    public LivingEntity getTarget() {
        return target;
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

    /**
     * Sets the entity input to true
     */
    public void setEntityInput() {
        inputs.put(player, true);
        player.sendMessage(Text.of("a"));
    }

    /**
     * Sets the target input to true
     */
    public void setTargetInput() {
        inputs.put(target, true);

    }

    @Override
    public void startSex() {
        started = true;
    }

    @Override
    public boolean tick() {
        if (player.isRemoved() || target.isRemoved() || sexBar >= MAX_SEX_BAR || sexHealth <= 0)
            return false;

        tick = (tick + 1) % MAX_TICKS;

        target.copyPositionAndRotation(player);
        player.setPose(EntityPose.SWIMMING);
        target.setPose(EntityPose.SLEEPING);

        PlayerEntity[] players = new PlayerEntity[] { player, target };

        for (PlayerEntity playerEntity : players)
            playerEntity.sendMessage(hudDisplay(), true);

        if (tick == 0)
            fail();
        else
            for (boolean input : inputs.values())
                if (input)
                    success();

        inputs.replaceAll((key, value) -> false);

        return true;
    }

    @Override
    public void success() {
        int bonus = 0;

        for (ServerPlayerEntity playerEntity : new ServerPlayerEntity[] { player, target })
            if (playerEntity.hasStatusEffect(ModStatusEffects.LIBIDO))
                bonus += (playerEntity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 5;

        if (checkOtherPlayerInput)
            tick = 0;

        checkOtherPlayerInput = !checkOtherPlayerInput;

        sexBar += (double) MAX_SEX_BAR_ADD * Math.min(tick + bonus, MAX_TICKS) / MAX_TICKS;
        Sex.entityParticles(player, ParticleTypes.HAPPY_VILLAGER, player.getWorld());
    }

    @Override
    public void fail() {
        sexHealth--;
        checkOtherPlayerInput = false;
        target.damage(target.getWorld(), player.getDamageSources().mobAttack(player), 1);
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

        text.append(Text.translatable("sex.the-sex-mod.spacer"));

        for (int i = 0; i < length; i++)
            text.append(Text.translatable("sex.the-sex-mod.sex_bar_icon")
                    .formatted(i < (double) length * sexBar / MAX_SEX_BAR ? Formatting.BLUE : Formatting.GRAY));

        text.append(Text.translatable("sex.the-sex-mod.spacer"));

        for (int i = 0; i < length; i++)
            text.append(Text.translatable("sex.the-sex-mod.ticks_icon")
                    .formatted(i < (double) length * tick / MAX_TICKS ? Formatting.GREEN : Formatting.GRAY));

        return text;
    }

    @Override
    public void endSex() {
        ServerPlayerEntity[] players = { player, target };

        ended = true;
        player.stopRiding();

        for (ServerPlayerEntity serverPlayer : players)
            serverPlayer.sendMessage(Text.empty(), true);

        breed();
        ModCriteria.SEX_COMPLETE.trigger(player, sexBar >= MAX_SEX_BAR);

        boolean playerHasSTD = player.hasStatusEffect(ModStatusEffects.STD);
        boolean targetHasSTD = target.hasStatusEffect(ModStatusEffects.STD);

        if ((playerHasSTD && targetHasSTD) || (!playerHasSTD && !targetHasSTD))
            return;

        for (int i = 0; i < players.length; i++) {
            ServerPlayerEntity source = players[i];
            ServerPlayerEntity dest = players[1 - i];

            if (source.hasStatusEffect(ModStatusEffects.STD) && !dest.hasStatusEffect(ModStatusEffects.STD)) {
                StatusEffectInstance statusEffectInstance = source.getStatusEffect(ModStatusEffects.STD);

                if (dest.getRandom().nextDouble() < ((STDStatusEffect) statusEffectInstance.getEffectType().value())
                        .getInfectChance() + (statusEffectInstance.getAmplifier() + 1) * 0.25 - Sex.stdResistance(dest))
                    dest.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
        }
    }

    @Override
    public void breed() {
        if (sexBar >= MAX_SEX_BAR
                && player.getRandom().nextDouble() >= Sex.stdResistance(player) + Sex.stdResistance(target))
            return;
    }
}
