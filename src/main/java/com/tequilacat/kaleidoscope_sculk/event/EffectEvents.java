package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.effect.SculkDashEffect;
import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

/**
 * 状态效果事件类。
 * <p>
 * 处理与幽匿冲刺、声波效果相关的生命周期：
 * 幽匿冲刺被移除或到期时清理其附加的属性修饰符；
 * 声波效果再次获得时与已有实例合并为更高等级与更长时长，而非重复添加。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class EffectEvents {

    private EffectEvents() {
    }

    /**
     * 效果被移除时调用。
     * <p>
     * 若移除的是幽匿冲刺，则清理其附加的属性修饰符。
     *
     * @param event 效果移除事件
     */
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffect().is(KSEffects.SCULK_DASH.getKey())) {
            SculkDashEffect.clearModifiers(event.getEntity());
        }
    }

    /**
     * 效果到期时调用。
     * <p>
     * 若到期的是幽匿冲刺，则清理其附加的属性修饰符。
     *
     * @param event 效果到期事件
     */
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        MobEffectInstance instance = event.getEffectInstance();
        if (instance != null && instance.getEffect().is(KSEffects.SCULK_DASH.getKey())) {
            SculkDashEffect.clearModifiers(event.getEntity());
        }
    }

    /**
     * 效果生效判定时调用。
     * <p>
     * 仅服务端对玩家的声波效果处理：已有声波效果时取消本次添加，
     * 改为以两者中较高的等级（上限 20）与较长的时长合并为新实例。
     *
     * @param event 效果生效判定事件
     */
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        MobEffectInstance newEffect = event.getEffectInstance();
        if (!newEffect.getEffect().is(KSEffects.SONIC_WAVE.getKey())) {
            return;
        }

        MobEffectInstance existing = player.getEffect(KSEffects.SONIC_WAVE);
        if (existing == null) {
            return;
        }

        int amplifier = Math.clamp(newEffect.getAmplifier(), existing.getAmplifier(), 20);
        int duration = Math.max(existing.getDuration(), newEffect.getDuration());

        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        player.removeEffect(KSEffects.SONIC_WAVE);
        player.addEffect(new MobEffectInstance(KSEffects.SONIC_WAVE, duration, amplifier));
    }
}
