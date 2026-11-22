package com.tequilacat.kaleidoscope_sculk.util;

import net.minecraft.world.food.FoodProperties;

/**
 * 食物属性构建工具类。
 * <p>
 * 提供便捷方法创建食物属性，统一处理饱和度倍率换算，
 * 并支持附带效果、始终可食用与使用后返还容器等配置。
 */
public class FoodBuilderHelper {

    /**
     * 创建食物属性构建器。
     *
     * @param nutrition  营养值
     * @param saturation 饱和度修饰符
     * @return 食物属性构建器
     */
    public static FoodProperties.Builder food(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturation);
    }

    /**
     * 创建基础食物属性。
     *
     * @param nutrition  营养值
     * @param saturation 饱和度修饰符
     * @return 食物属性
     */
    public static FoodProperties simple(int nutrition, float saturation) {
        return food(nutrition, saturation).build();
    }

    /**
     * 创建附带效果的食物属性。
     *
     * @param nutrition  营养值
     * @param saturation 饱和度修饰符
     * @param effects    附带效果
     * @return 食物属性
     */
    public static FoodProperties simple(int nutrition, float saturation, EffectPair... effects) {
        FoodProperties.Builder builder = food(nutrition, saturation);
        return applyEffects(builder, effects).build();
    }

    /**
     * 创建始终可食用的食物属性。
     *
     * @param nutrition  营养值
     * @param saturation 饱和度修饰符
     * @return 食物属性
     */
    public static FoodProperties alwaysEdible(int nutrition, float saturation) {
        return food(nutrition, saturation).alwaysEat().build();
    }

    /**
     * 创建始终可食用且附带效果的食物属性。
     *
     * @param nutrition  营养值
     * @param saturation 饱和度修饰符
     * @param effects    附带效果
     * @return 食物属性
     */
    public static FoodProperties alwaysEdible(int nutrition, float saturation, EffectPair... effects) {
        FoodProperties.Builder builder = food(nutrition, saturation).alwaysEat();
        return applyEffects(builder, effects).build();
    }

    /**
     * 为构建器附加全部效果。
     *
     * @param builder 食物属性构建器
     * @param effects 附带效果
     * @return 原构建器
     */
    private static FoodProperties.Builder applyEffects(FoodProperties.Builder builder, EffectPair... effects) {
        for (EffectPair effect : effects) {
            builder.effect(effect.getEffect(), effect.getProbability());
        }
        return builder;
    }
}