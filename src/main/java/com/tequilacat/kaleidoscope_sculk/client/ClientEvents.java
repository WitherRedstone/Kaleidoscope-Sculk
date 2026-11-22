package com.tequilacat.kaleidoscope_sculk.client;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.network.SonicBoomPayload;
import com.tequilacat.kaleidoscope_sculk.network.SoulSailAbsorbPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * 客户端事件类。
 * <p>
 * 在客户端每刻检测音波与灵魂帆按键的按下瞬间，
 * 首次按下时向服务端发送对应的请求数据包；
 * 通过记录上一刻的按下状态实现边沿触发，避免按住时重复发送。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID, value = Dist.CLIENT)
public final class ClientEvents {

    /** 音波按键上一刻是否按下。 */
    private static boolean sonicBoomPressed;

    /** 灵魂帆按键上一刻是否按下。 */
    private static boolean soulSailPressed;

    private ClientEvents() {
    }

    /**
     * 客户端每刻更新。
     * <p>
     * 玩家不存在时跳过；否则分别检测两个按键并发送对应请求。
     *
     * @param event 客户端每刻更新事件
     */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        sonicBoomPressed = sendOnPress(KSKeys.SONIC_BOOM_KEY, sonicBoomPressed, SonicBoomPayload::new);
        soulSailPressed = sendOnPress(KSKeys.SOUL_SAIL_KEY, soulSailPressed, SoulSailAbsorbPayload::new);
    }

    /**
     * 按键边沿触发发送。
     * <p>
     * 仅在按键从未按下变为按下时向服务端发送一次数据包，
     * 并返回当前按下状态供下一刻比较。
     *
     * @param key         按键映射
     * @param lastPressed 上一刻的按下状态
     * @param packet      待发送的数据包供应器
     * @return 当前按下状态
     */
    private static boolean sendOnPress(KeyMapping key, boolean lastPressed,
                                       Supplier<? extends CustomPacketPayload> packet) {
        boolean pressed = key.isDown();
        if (pressed && !lastPressed) {
            PacketDistributor.sendToServer(packet.get());
        }
        return pressed;
    }
}
