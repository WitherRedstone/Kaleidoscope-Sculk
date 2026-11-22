package com.tequilacat.kaleidoscope_sculk.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.List;
import java.util.function.Supplier;

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
        for (Pair<MobEffectInstance, Float> possible : food.getEffects()) {
            MobEffectInstance effect = possible.getFirst();
            MutableComponent line = effect.getEffect().getDisplayName().copy();
            if (effect.getAmplifier() > 0) {
                line.append(" ").append(Component.translatable("potion.potency." + effect.getAmplifier()));
            }
            tooltip.add(line.withStyle(ChatFormatting.BLUE));
        }
    }
}