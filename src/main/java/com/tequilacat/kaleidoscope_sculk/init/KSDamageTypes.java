package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public interface KSDamageTypes {
    DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, KaleidoscopeSculk.MODID);

    /** 食用深板岩蛋糕受到的伤害 */
    ResourceKey<DamageType> DEEPSLATE_CAKE_SLICE = ResourceKey.create(Registries.DAMAGE_TYPE, KaleidoscopeSculk.id("deepslate_cake_slice"));

    static void register(IEventBus bus) {
        DAMAGE_TYPES.register(bus);
    }
}
