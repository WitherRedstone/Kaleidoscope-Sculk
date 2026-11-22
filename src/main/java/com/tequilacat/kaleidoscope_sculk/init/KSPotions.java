package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KSPotions {
    DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, KaleidoscopeSculk.MODID);

    /** 深渊药水：获得 90 秒深渊效果 */
    DeferredHolder<Potion, Potion> ABYSS_POTION = POTIONS.register("abyss_potion",
            () -> new Potion(new MobEffectInstance(KSEffects.ABYSS, 1800, 0)));

    static void register(IEventBus bus) {
        POTIONS.register(bus);
    }
}
