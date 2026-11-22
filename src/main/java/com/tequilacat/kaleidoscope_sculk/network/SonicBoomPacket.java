package com.tequilacat.kaleidoscope_sculk.network;

import com.tequilacat.kaleidoscope_sculk.effect.SonicWaveEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 音波请求包（客户端 → 服务端）。
 * <p>
 * 无字段心跳包，客户端按键触发即请求释放音波。
 */
public record SonicBoomPacket() {

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SonicWaveEffect.fire(player);
            }
        });
        context.setPacketHandled(true);
    }
}