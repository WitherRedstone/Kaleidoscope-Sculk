package com.tequilacat.kaleidoscope_sculk.network;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.effect.SonicWaveEffect;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 声波包。
 * <p>
 * 无字段的心跳包，客户端发来即表示请求释放声波，
 * 服务端据此触发 SonicWaveEffect 的实际效果。
 */
public record SonicBoomPayload() implements CustomPacketPayload {

    /** 包类型标识。 */
    public static final Type<SonicBoomPayload> TYPE =
            new Type<>(KaleidoscopeSculk.id("sonic_boom"));

    /** 编解码器，包体无字段，编码与解码均为空操作。 */
    public static final StreamCodec<RegistryFriendlyByteBuf, SonicBoomPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
            },
            buf -> new SonicBoomPayload()
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
     * 服务端处理：触发声波效果。
     *
     * @param payload 声波包
     * @param context 上下文
     */
    public static void handle(SonicBoomPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                SonicWaveEffect.fire(player);
            }
        });
    }
}
