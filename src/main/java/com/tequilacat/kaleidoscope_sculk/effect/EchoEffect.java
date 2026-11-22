package com.tequilacat.kaleidoscope_sculk.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * 回响：不会被怪物索敌，也不会触发监守者的振动感知。
 */
public class EchoEffect extends MobEffect {

    public EchoEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }
}
