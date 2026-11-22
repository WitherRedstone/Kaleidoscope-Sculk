package com.tequilacat.kaleidoscope_sculk.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.List;

/**
 * 食物效果提示。
 * <p>
 * 把食物属性携带的食用效果整理成提示行，供物品的 appendHoverText 调用。
 */
public final class FoodTooltips {

    /**
     * 把食物携带的效果逐行追加到提示列表。
     *
     * @param food    食物属性，可为空
     * @param tooltip 提示行列表
     */
    public static void addEffectLines(FoodProperties food, List<Component> tooltip) {
        if (food == null) {
            return;
        }
        for (FoodProperties.PossibleEffect possible : food.effects()) {
            MobEffectInstance effect = possible.effect();
            MutableComponent line = effect.getEffect().value().getDisplayName().copy();
            if (effect.getAmplifier() > 0) {
                line.append(" ").append(Component.translatable("potion.potency." + effect.getAmplifier()));
            }
            tooltip.add(line.withStyle(ChatFormatting.BLUE));
        }
    }
}
