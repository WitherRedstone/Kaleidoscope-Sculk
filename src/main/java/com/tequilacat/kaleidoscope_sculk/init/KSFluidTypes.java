package com.tequilacat.kaleidoscope_sculk.init;

import com.github.ysbbbbbb.kaleidoscopetavern.fluid.JuiceFluidType;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public interface KSFluidTypes {

    DeferredRegister<net.neoforged.neoforge.fluids.FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, KaleidoscopeSculk.MODID);

    DeferredHolder<net.neoforged.neoforge.fluids.FluidType, JuiceFluidType> SCULK_JUICE_TYPE =
            FLUID_TYPES.register("sculk_juice", () -> new JuiceFluidType(KaleidoscopeSculk.id("sculk_juice"), 0));

    static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }
}
