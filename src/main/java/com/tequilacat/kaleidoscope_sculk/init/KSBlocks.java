package com.tequilacat.kaleidoscope_sculk.init;

import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.DrinkBlock;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.block.cake.DeepslateCakeBlock;
import com.tequilacat.kaleidoscope_sculk.block.cake.DeepslateStoveBlock;
import com.tequilacat.kaleidoscope_sculk.block.crop.EchoCropBlock;
import com.tequilacat.kaleidoscope_sculk.block.crop.EchoCropTopBlock;
import com.tequilacat.kaleidoscope_sculk.block.food.DishBlock;
import com.tequilacat.kaleidoscope_sculk.util.DrinkShapes;
import com.tequilacat.kaleidoscope_sculk.util.FoodShapes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KSBlocks {
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeSculk.MODID);


    // ==================== 方块 ====================
    /** 深板岩炉灶 */
    DeferredBlock<DeepslateStoveBlock> DEEPSLATE_STOVE = BLOCKS.register("deepslate_stove", () -> new DeepslateStoveBlock());


    // ==================== 作物 ====================
    /** 回响作物 */
    DeferredBlock<EchoCropBlock> ECHO_CROP = BLOCKS.register("echo_crop",
            () -> new EchoCropBlock(Block.Properties.of().noCollission().randomTicks().instabreak().noOcclusion()));
    /** 回响作物顶部 */
    DeferredBlock<EchoCropTopBlock> ECHO_CROP_TOP = BLOCKS.register("echo_crop_top",
            () -> new EchoCropTopBlock(Block.Properties.of().noCollission().randomTicks().instabreak().noOcclusion()));


    // ==================== 整盘食物 ====================
    /** 深板岩蛋糕 */
    DeferredBlock<DeepslateCakeBlock> DEEPSLATE_CAKE = BLOCKS.register("deepslate_cake",
            () -> new DeepslateCakeBlock(Block.Properties.of().sound(SoundType.DEEPSLATE).strength(0.5F).noOcclusion().instabreak()));
    /** 幽匿烩菜 */
    DeferredBlock<FoodBiteBlock> SCULK_STEW_BLOCK = BLOCKS.register("sculk_stew_block",
            () -> new DishBlock(KSFoods.SCULK_STEW_BLOCK_FOOD, 3, Items.FLOWER_POT).setAABB(FoodShapes.MEDIUM_WITH_MAT));
    /** 幽匿排骨 */
    DeferredBlock<FoodBiteBlock> SCULK_PORK_RIBS_BLOCK = BLOCKS.register("sculk_pork_ribs_block",
            () -> new DishBlock(KSFoods.SCULK_PORK_RIBS_BLOCK_FOOD, 4, Items.BOWL).setAABB(FoodShapes.PLATE));
    /** 幽匿炖鸡煲 */
    DeferredBlock<FoodBiteBlock> SCULK_CHICKEN_STEW_BLOCK = BLOCKS.register("sculk_chicken_stew_block",
            () -> new DishBlock(KSFoods.SCULK_CHICKEN_STEW_BLOCK_FOOD, 4, Items.FLOWER_POT).setAABB(FoodShapes.MEDIUM_WITH_MAT));
    /** 古城风味刺身 */
    DeferredBlock<FoodBiteBlock> ANCIENT_CITY_STYLE_SASHIMI_BLOCK = BLOCKS.register("ancient_city_style_sashimi",
            () -> new DishBlock(KSFoods.ANCIENT_CITY_STYLE_SASHIMI_BLOCK_FOOD, 4, Items.BOWL).setAABB(FoodShapes.PLATE));
    /** 幽匿羊排 */
    DeferredBlock<FoodBiteBlock> SCULK_LAMB_CHOP_BLOCK = BLOCKS.register("sculk_lamb_chop_block",
            () -> new DishBlock(KSFoods.SCULK_LAMB_CHOP_BLOCK_FOOD, 3, Items.BOWL).setAABB(FoodShapes.PLATE));


    // ==================== 酒水 ====================
    /** 幽匿菠萝啤 */
    DeferredBlock<DrinkBlock> SCULK_BREW_BOTTLE = BLOCKS.register("sculk_brew_bottle",
            () -> new DrinkBlock(4, DrinkShapes.sculkBrew()));
    /** 花雕酒 */
    DeferredBlock<DrinkBlock> HUADIAO_WINE = BLOCKS.register("huadiao_wine",
            () -> new DrinkBlock(3, DrinkShapes.huadiaoWine()));
    /** 红兰酒 */
    DeferredBlock<DrinkBlock> HONGLAN_WINE = BLOCKS.register("honglan_wine",
            () -> new DrinkBlock(3, DrinkShapes.honglanWine()));


    static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
