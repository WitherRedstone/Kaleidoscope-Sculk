package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public interface KSFluids {

    DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, KaleidoscopeSculk.MODID);

    RegistryObject<FlowingFluid> SCULK_JUICE = FLUIDS.register("sculk_juice", () -> new ForgeFlowingFluid.Source(properties()));
    RegistryObject<FlowingFluid> SCULK_JUICE_FLOWING = FLUIDS.register("sculk_juice_flowing", () -> new ForgeFlowingFluid.Flowing(properties()));

    private static ForgeFlowingFluid.Properties properties() {
        return new ForgeFlowingFluid.Properties(
                KSFluidTypes.SCULK_JUICE_TYPE,
                SCULK_JUICE,
                SCULK_JUICE_FLOWING)
                .bucket(KSItems.SCULK_JUICE_BUCKET);
    }

    static void register(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
