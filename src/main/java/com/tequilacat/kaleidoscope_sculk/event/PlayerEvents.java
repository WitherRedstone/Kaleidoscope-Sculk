package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.effect.SonicWaveEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * 玩家事件类。
 * <p>
 * 服务端每刻检测玩家主手物品是否发生变化：
 * 一旦变化即记录新的主手物品，并重置声波的连射计数，
 * 避免切换物品后延续旧的连射进度。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class PlayerEvents {

    /** 持久化数据中记录上次主手物品的键。 */
    private static final String LAST_MAIN_HAND_KEY = "kaleidoscope_sculk:sonic_last_main_hand";

    private PlayerEvents() {
    }

    /**
     * 玩家每刻更新。
     * <p>
     * 仅服务端处理；当主手物品与上次记录不同时更新记录并重置连射计数。
     *
     * @param event 玩家每刻更新事件
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        String held = BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).toString();
        String last = player.getPersistentData().getString(LAST_MAIN_HAND_KEY);
        if (!held.equals(last)) {
            player.getPersistentData().putString(LAST_MAIN_HAND_KEY, held);
            SonicWaveEffect.resetStreak(player);
        }
    }
}
