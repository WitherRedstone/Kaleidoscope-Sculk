package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 饮品方块实体类型事件类。
 * <p>
 * 向酒馆模组的饮品方块实体类型追加本模组的饮品方块，
 * 使其复用酒馆的饮品方块实体逻辑。若酒馆方块实体类型不存在则不做处理。
 */
public final class DrinkBlockEntityTypeEvents {

    /** 酒馆模组饮品方块实体的注册名。 */
    private static final ResourceLocation TAVERN_DRINK_BE = ResourceLocation.tryBuild("kaleidoscope_tavern", "drink");

    private DrinkBlockEntityTypeEvents() {
    }

    /**
     * 处理方块实体类型追加方块事件。
     * <p>
     * 找到酒馆饮品方块实体类型后，把幽匿酿瓶、花雕酒与红蓝酒追加到其中。
     *
     * @param event 方块实体类型追加方块事件
     */
    public static void onBlockEntityTypeAddBlocks(BlockEntityTypeAddBlocksEvent event) {
        BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(TAVERN_DRINK_BE).ifPresent(drinkType -> {
            List<Block> blocks = new ArrayList<>();
            blocks.add(KSBlocks.SCULK_BREW_BOTTLE.get());
            blocks.add(KSBlocks.HUADIAO_WINE.get());
            blocks.add(KSBlocks.HONGLAN_WINE.get());
            event.modify(drinkType, blocks.toArray(new Block[0]));
        });
    }
}