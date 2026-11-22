package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface KSPotions {

    DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, KaleidoscopeSculk.MODID);

    /** 深渊药水：获得 90 秒深渊效果 */
    RegistryObject<Potion> ABYSS_POTION = POTIONS.register("abyss_potion",
            () -> new Potion(new MobEffectInstance(KSEffects.ABYSS.get(), 1800, 0)));

    static void register(IEventBus bus) {
        POTIONS.register(bus);
    }
}
