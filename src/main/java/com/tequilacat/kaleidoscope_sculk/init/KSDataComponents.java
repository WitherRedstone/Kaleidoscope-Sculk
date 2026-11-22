package com.tequilacat.kaleidoscope_sculk.init;

import com.mojang.serialization.Codec;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KSDataComponents {
    DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, KaleidoscopeSculk.MODID);

    /** 灵魂帆的收割冷却结束时间，冷却绑定在物品本身而非玩家。 */
    DeferredHolder<DataComponentType<?>, DataComponentType<Long>> SOUL_SAIL_COOLDOWN =
            DATA_COMPONENTS.register("soul_sail_cooldown",
                    () -> DataComponentType.<Long>builder()
                            .persistent(Codec.LONG)
                            .networkSynchronized(ByteBufCodecs.VAR_LONG)
                            .build());

    static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
