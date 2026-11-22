package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KSFluids {

    DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, KaleidoscopeSculk.MODID);

    DeferredHolder<Fluid, FlowingFluid> SCULK_JUICE = FLUIDS.register("sculk_juice", () -> new BaseFlowingFluid.Source(properties()));
    DeferredHolder<Fluid, FlowingFluid> SCULK_JUICE_FLOWING = FLUIDS.register("sculk_juice_flowing", () -> new BaseFlowingFluid.Flowing(properties()));

    private static BaseFlowingFluid.Properties properties() {
        return new BaseFlowingFluid.Properties(
                KSFluidTypes.SCULK_JUICE_TYPE,
                SCULK_JUICE,
                SCULK_JUICE_FLOWING)
                .bucket(KSItems.SCULK_JUICE_BUCKET);
    }

    static void register(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
