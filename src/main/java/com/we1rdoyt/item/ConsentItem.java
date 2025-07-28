package com.we1rdoyt.item;

import java.util.function.Consumer;

import com.we1rdoyt.component.ModDataComponentTypes;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

public class ConsentItem extends Item {
    public ConsentItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent,
            Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.of(stack.get(ModDataComponentTypes.TARGET_ENTITY)));
    }
}
