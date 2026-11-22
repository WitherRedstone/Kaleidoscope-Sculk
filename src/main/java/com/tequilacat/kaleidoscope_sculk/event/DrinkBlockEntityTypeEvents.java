package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 饮品方块实体类型事件类。
 * <p>
 * 向酒馆模组的饮品方块实体类型追加本模组的饮品方块，
 * 使其复用酒馆的饮品方块实体逻辑。若酒馆方块实体类型不存在则不做处理。
 */
@Mod.EventBusSubscriber(modid = KaleidoscopeSculk.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DrinkBlockEntityTypeEvents {


    private static final ResourceLocation TAVERN_DRINK_BE = ResourceLocation.tryBuild("kaleidoscope_tavern", "drink");

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(TAVERN_DRINK_BE).ifPresent(drinkType -> {
                List<Block> blocks = new ArrayList<>();

                blocks.add(KSBlocks.SCULK_BREW_BOTTLE.get());
                blocks.add(KSBlocks.HUADIAO_WINE.get());
                blocks.add(KSBlocks.HONGLAN_WINE.get());

                try {
                    Field validBlocksField = BlockEntityType.class.getDeclaredField("f_58915_");
                    validBlocksField.setAccessible(true);
                    Set<Block> validBlocks = new HashSet<>((Set<Block>) validBlocksField.get(drinkType));
                    validBlocks.addAll(blocks);
                    validBlocksField.set(drinkType, validBlocks);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
