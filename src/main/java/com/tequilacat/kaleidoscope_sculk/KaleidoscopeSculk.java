package com.tequilacat.kaleidoscope_sculk;

import com.mojang.logging.LogUtils;
import com.tequilacat.kaleidoscope_sculk.config.KSServerConfig;
import com.tequilacat.kaleidoscope_sculk.event.DrinkBlockEntityTypeEvents;
import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import com.tequilacat.kaleidoscope_sculk.init.KSCreativeModeTabs;
import com.tequilacat.kaleidoscope_sculk.init.KSDataComponents;
import com.tequilacat.kaleidoscope_sculk.init.KSDamageTypes;
import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import com.tequilacat.kaleidoscope_sculk.init.KSEntities;
import com.tequilacat.kaleidoscope_sculk.init.KSFluidTypes;
import com.tequilacat.kaleidoscope_sculk.init.KSFluids;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.init.KSPotions;
import com.tequilacat.kaleidoscope_sculk.init.KSTeacups;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(KaleidoscopeSculk.MODID)
public class KaleidoscopeSculk {
    public static final String MODID = "kaleidoscope_sculk";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeSculk(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, KSServerConfig.SPEC);

        KSBlocks.register(modEventBus);
        KSItems.register(modEventBus);
        KSCreativeModeTabs.register(modEventBus);
        KSEffects.register(modEventBus);
        KSEntities.register(modEventBus);
        KSPotions.register(modEventBus);
        KSFluidTypes.register(modEventBus);
        KSFluids.register(modEventBus);
        KSDataComponents.register(modEventBus);
        KSDamageTypes.register(modEventBus);
        KSTeacups.init();

        modEventBus.addListener(DrinkBlockEntityTypeEvents::onBlockEntityTypeAddBlocks);
        modEventBus.addListener(KSCreativeModeTabs::onBuildCreativeTabContents);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}
