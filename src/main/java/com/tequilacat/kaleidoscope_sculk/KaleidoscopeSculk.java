package com.tequilacat.kaleidoscope_sculk;

import com.mojang.logging.LogUtils;
import com.tequilacat.kaleidoscope_sculk.config.KSServerConfig;
import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import com.tequilacat.kaleidoscope_sculk.init.KSCreativeModeTabs;
import com.tequilacat.kaleidoscope_sculk.init.KSDamageTypes;
import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import com.tequilacat.kaleidoscope_sculk.init.KSEntities;
import com.tequilacat.kaleidoscope_sculk.init.KSFluidTypes;
import com.tequilacat.kaleidoscope_sculk.init.KSFluids;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.init.KSPotions;
import com.tequilacat.kaleidoscope_sculk.init.KSTeacups;
import com.tequilacat.kaleidoscope_sculk.network.KSNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;   // ← 新增
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(KaleidoscopeSculk.MODID)
public class KaleidoscopeSculk {
    public static final String MODID = "kaleidoscope_sculk";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeSculk(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        context.registerConfig(ModConfig.Type.COMMON, KSServerConfig.SPEC);

        KSBlocks.register(modEventBus);
        KSItems.register(modEventBus);
        KSCreativeModeTabs.register(modEventBus);
        KSEffects.register(modEventBus);
        KSEntities.register(modEventBus);
        KSPotions.register(modEventBus);
        KSFluidTypes.register(modEventBus);
        KSFluids.register(modEventBus);
        KSDamageTypes.register(modEventBus);

        KSTeacups.init();
        KSNetwork.register();

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.put(KSItems.ECHO_SEED.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(KSItems.SCULK_PINAPPLE.get(), 0.65F);
        });
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}
