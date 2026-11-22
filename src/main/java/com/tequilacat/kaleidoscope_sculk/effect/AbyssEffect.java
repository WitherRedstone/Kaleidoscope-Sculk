package com.tequilacat.kaleidoscope_sculk.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * 深渊：身处地下越深，攻击附带的额外伤害越高。
 */
public class AbyssEffect extends MobEffect {

    public AbyssEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }
}
