package com.tequilacat.kaleidoscope_sculk.network;

import com.tequilacat.kaleidoscope_sculk.item.SoulSailItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 灵魂帆收割请求包（客户端 → 服务端）。
 * <p>
 * 无字段心跳包，客户端按键触发即请求执行灵魂帆收割。
 */
public record SoulSailAbsorbPacket() {

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SoulSailItem.absorb(player);
            }
        });
        context.setPacketHandled(true);
    }
}