package com.tequilacat.kaleidoscope_sculk.init;

import com.github.ysbbbbbb.kaleidoscopetavern.fluid.JuiceFluidType;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface KSFluidTypes {

    DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, KaleidoscopeSculk.MODID);

    RegistryObject<JuiceFluidType> SCULK_JUICE_TYPE =
            FLUID_TYPES.register("sculk_juice",
                    () -> new JuiceFluidType(KaleidoscopeSculk.id("sculk_juice"), 0));

    static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }
}
