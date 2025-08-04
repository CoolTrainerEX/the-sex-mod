package com.we1rdoyt.advancement.criterion;

import com.we1rdoyt.TheSexMod;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModCriteria extends Criteria {
    public static final SexCriterion SEX = register("sex", new SexCriterion());
    public static final SexCompleteCriterion SEX_COMPLETE = register("sex_complete", new SexCompleteCriterion());

    public static void init() {
    }

    public static <T extends Criterion<?>> T register(String id, T criterion) {
        return Registry.register(Registries.CRITERION, Identifier.of(TheSexMod.MOD_ID, id), criterion);
    }
}
