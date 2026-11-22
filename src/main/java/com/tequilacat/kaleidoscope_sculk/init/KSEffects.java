package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.effect.AbyssEffect;
import com.tequilacat.kaleidoscope_sculk.effect.EchoEffect;
import com.tequilacat.kaleidoscope_sculk.effect.SculkDashEffect;
import com.tequilacat.kaleidoscope_sculk.effect.SonicWaveEffect;
import com.tequilacat.kaleidoscope_sculk.effect.SoulPowerEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KSEffects {
    DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, KaleidoscopeSculk.MODID);

    /** 声波：声波降临，固定15伤害附带目标当前生命的8% */
    DeferredHolder<MobEffect, SonicWaveEffect> SONIC_WAVE = MOB_EFFECTS.register("sonic_wave", () -> new SonicWaveEffect(0x5B6E6A));
    /** 回响：不会被怪物索敌，也不会触发监守者的振动感知 */
    DeferredHolder<MobEffect, EchoEffect> ECHO = MOB_EFFECTS.register("echo", () -> new EchoEffect(0x66CCFF));
    /** 深渊：深处攻击获得额外伤害，饮用深渊药水获得 */
    DeferredHolder<MobEffect, AbyssEffect> ABYSS = MOB_EFFECTS.register("abyss", () -> new AbyssEffect(0x1B0C36));
    /** 幽匿冲刺：在幽匿群系中提升移动速度与攻击速度 */
    DeferredHolder<MobEffect, SculkDashEffect> SCULK_DASH = MOB_EFFECTS.register("sculk_dash", () -> new SculkDashEffect(0x3B5E6A));
    /** 收割之力：攻击伤害提高，数值取决于被收割目标的最大生命值 */
    DeferredHolder<MobEffect, SoulPowerEffect> SOUL_POWER = MOB_EFFECTS.register("soul_power", () -> new SoulPowerEffect(0x7B4FBF));

    static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }
}
