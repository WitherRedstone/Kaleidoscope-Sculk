package com.tequilacat.kaleidoscope_sculk.init;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

public class KSTeacups {

    /** 幽匿回响茶 */
    public static ResourceLocation ECHO_PUER;

    /**
     * 注册茶饮数据，必须早于创造模式标签页构建。
     */
    public static void init() {
        TeacupRegistry registry = new TeacupRegistry();

        ECHO_PUER = registry.registerTeacupData(KaleidoscopeSculk.id("echo_puer"),
                TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(KSEffects.ECHO, 8 * 60 * 20)));
    }
}
