package com.tequilacat.kaleidoscope_sculk.network;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.item.SoulSailItem;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 灵魂帆夺魂包。
 * <p>
 * 无字段的心跳包，客户端发来即表示请求执行灵魂帆收割，
 * 服务端据此调用 SoulSailItem.absorb 的实际流程。
 */
public record SoulSailAbsorbPayload() implements CustomPacketPayload {

    /** 包类型标识。 */
    public static final Type<SoulSailAbsorbPayload> TYPE =
            new Type<>(KaleidoscopeSculk.id("soul_sail_absorb"));

    /** 编解码器，包体无字段，编码与解码均为空操作。 */
    public static final StreamCodec<RegistryFriendlyByteBuf, SoulSailAbsorbPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
            },
            buf -> new SoulSailAbsorbPayload()
    );

    /**
     * 获取包类型。
     *
     * @return 包类型标识
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * 服务端处理：执行灵魂帆收割。
     *
     * @param payload 夺魂包
     * @param context 上下文
     */
    public static void handle(SoulSailAbsorbPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                SoulSailItem.absorb(player);
            }
        });
    }
}
