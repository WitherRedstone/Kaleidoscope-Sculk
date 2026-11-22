package com.tequilacat.kaleidoscope_sculk.network;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * 网络包注册。
 * <p>
 * 在事件总线上集中注册自定义数据包，目前只有音速冲击与灵魂帆收割两个包，
 * 均由客户端发往服务端。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class KSNetwork {

    /**
     * 注册数据包。
     * <p>
     * 协议版本号为 1，收发双方需保持一致；后续新增数据包在此追加。
     *
     * @param event 数据包注册事件
     */
    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToServer(SonicBoomPayload.TYPE,
                        SonicBoomPayload.STREAM_CODEC,
                        SonicBoomPayload::handle)
                .playToServer(SoulSailAbsorbPayload.TYPE,
                        SoulSailAbsorbPayload.STREAM_CODEC,
                        SoulSailAbsorbPayload::handle);
    }
}
