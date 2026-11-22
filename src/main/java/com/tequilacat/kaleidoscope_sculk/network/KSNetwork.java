package com.tequilacat.kaleidoscope_sculk.network;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Forge 网络通道。
 * <p>
 * 使用 SimpleChannel 注册两个 C2S 数据包：
 * SonicBoomPacket（音波）与 SoulSailAbsorbPacket（灵魂帆收割）。
 */
public final class KSNetwork {

    private static final String PROTOCOL_VERSION = "1";

    /** Forge 网络通道实例。 */
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeSculk.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId;

    private KSNetwork() {
    }

    /**
     * 在主类构造函数或 FMLCommonSetupEvent 中调用一次即可注册所有数据包。
     */
    public static void register() {
        CHANNEL.messageBuilder(SonicBoomPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder((packet, buf) -> { /* 无字段 */ })
                .decoder(buf -> new SonicBoomPacket())
                .consumerMainThread(SonicBoomPacket::handle)
                .add();

        CHANNEL.messageBuilder(SoulSailAbsorbPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder((packet, buf) -> { /* 无字段 */ })
                .decoder(buf -> new SoulSailAbsorbPacket())
                .consumerMainThread(SoulSailAbsorbPacket::handle)
                .add();
    }
}