package com.tequilacat.kaleidoscope_sculk.util;

import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Supplier;

/**
 * 效果配对类。
 * <p>
 * 将状态效果实例的供应器与触发概率配对，供食物属性构建等场景延迟获取效果并指定生效概率。
 */
public class EffectPair {

    /** 状态效果实例的供应器，用于延迟加载效果。 */
    private final Supplier<MobEffectInstance> effect;

    /** 效果触发概率，范围通常为 0.0 到 1.0。 */
    private final float probability;

    private EffectPair(Supplier<MobEffectInstance> effect, float probability) {
        this.effect = effect;
        this.probability = probability;
    }

    /**
     * 工厂方法，创建效果与概率的配对对象。
     *
     * @param effect      状态效果实例的供应器
     * @param probability 效果触发概率
     * @return 新建的效果配对
     */
    public static EffectPair of(Supplier<MobEffectInstance> effect, float probability) {
        return new EffectPair(effect, probability);
    }

    /**
     * 取得状态效果实例的供应器。
     *
     * @return 效果供应器
     */
    public Supplier<MobEffectInstance> getEffect() {
        return this.effect;
    }

    /**
     * 取得效果触发概率。
     *
     * @return 概率值
     */
    public float getProbability() {
        return this.probability;
    }
}
