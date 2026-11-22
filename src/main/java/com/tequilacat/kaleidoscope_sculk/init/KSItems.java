package com.tequilacat.kaleidoscope_sculk.init;

import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodOnlyItem;
import com.github.ysbbbbbb.kaleidoscopetavern.item.DrinkBlockItem;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.item.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface KSItems {
    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KaleidoscopeSculk.MODID);


    // ==================== 方块 ====================
    /** 深板岩炉灶 */
    RegistryObject<BlockItem> DEEPSLATE_STOVE = ITEMS.register("deepslate_stove",
            () -> new BlockItem(KSBlocks.DEEPSLATE_STOVE.get(), new Item.Properties()));


    // ==================== 材料 ====================
    /** 监守者触须 */
    RegistryObject<Item> WARDEN_TENDRIL = ITEMS.register("warden_tendril",
            () -> new Item(new Item.Properties().food(KSFoods.WARDEN_TENDRIL_FOOD)));
    /** 远古骨片 */
    RegistryObject<AncientBoneFragmentItem> ANCIENT_BONE_FRAGMENT = ITEMS.register("ancient_bone_fragment",
            () -> new AncientBoneFragmentItem(new Item.Properties().stacksTo(16).fireResistant()));
    /** 幽匿菌 */
    RegistryObject<Item> SCULK_FUNGUS = ITEMS.register("sculk_fungus",
            () -> new Item(new Item.Properties().food(KSFoods.SCULK_FUNGUS_FOOD)));
    /** 幽匿菠萝 */
    RegistryObject<Item> SCULK_PINAPPLE = ITEMS.register("sculk_pinapple",
            () -> new Item(new Item.Properties().food(KSFoods.SCULK_PINAPPLE_FOOD)));
    /** 煮幽匿菠萝 */
    RegistryObject<BowlFoodOnlyItem> BOIL_SCULK_PINAPPLE = ITEMS.register("boil_sculk_pinapple",
            () -> new BowlFoodOnlyItem(KSFoods.BOIL_SCULK_PINAPPLE_FOOD));
    /** 幽匿毛虫 */
    RegistryObject<Item> SCULK_CATERPILLAR = ITEMS.register("sculk_caterpillar",
            () -> new Item(new Item.Properties().food(KSFoods.SCULK_CATERPILLAR_FOOD)));
    /** 灵魂 */
    RegistryObject<SoulItem> SOUL = ITEMS.register("soul",
            () -> new SoulItem(new Item.Properties().stacksTo(64)));
    /** 幽匿树枝 */
    RegistryObject<Item> SCULK_BRANCH = ITEMS.register("sculk_branch",
            () -> new Item(new Item.Properties().stacksTo(64)));
    /** 诡异肉 */
    RegistryObject<Item> EERIE_MEAT = ITEMS.register("eerie_meat",
            () -> new Item(new Item.Properties().food(KSFoods.EERIE_MEAT_FOOD)));
    /** 幽匿粉尘 */
    RegistryObject<Item> SCULK_DUST = ITEMS.register("sculk_dust", () -> new Item(new Item.Properties()));
    /** 幽匿面团 */
    RegistryObject<Item> SCULK_DOUGH = ITEMS.register("sculk_dough", () -> new Item(new Item.Properties()));
    /** 无声升级锻造模板 */
    RegistryObject<Item> SILENT_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("silent_upgrade_smithing_template",
            () -> new Item(new Item.Properties()));
    /** 幽匿汁桶 */
    RegistryObject<BucketItem> SCULK_JUICE_BUCKET = ITEMS.register("sculk_juice_bucket",
            () -> new BucketItem(KSFluids.SCULK_JUICE.get(),
                    new Item.Properties().stacksTo(16).craftRemainder(Items.BUCKET)));


    // ==================== 工具 ====================
    /** 侵蚀厨刀 */
    RegistryObject<ErosionKnifeItem> EROSION_KITCHEN_KNIFE = ITEMS.register("erosion_knife", ErosionKnifeItem::new);
    /** 无声厨刀 */
    RegistryObject<SilentKnifeItem> SILENT_KITCHEN_KNIFE = ITEMS.register("silent_kitchen_knife", SilentKnifeItem::new);
    /** 幽匿骨镰 */
    RegistryObject<SculkBoneSickleItem> SCULK_BONE_SICKLE = ITEMS.register("sculk_bone_sickle", SculkBoneSickleItem::new);


    // ==================== 作物 ====================
    /** 回声种子 */
    RegistryObject<EchoSeedItem> ECHO_SEED = ITEMS.register("echo_seed",
            () -> new EchoSeedItem(KSBlocks.ECHO_CROP.get(), new Item.Properties()));


    // ==================== 食物 ====================
    /** 幽匿果肉 */
    RegistryObject<Item> SCULK_FLESH = ITEMS.register("sculk_flesh",
            () -> new Item(new Item.Properties().food(KSFoods.SCULK_FLESH_FOOD)));
    /** 幽匿真菌汤 */
    RegistryObject<BowlFoodOnlyItem> SCULK_FUNGUS_SOUP = ITEMS.register("sculk_fungus_soup",
            () -> new BowlFoodOnlyItem(KSFoods.SCULK_FUNGUS_SOUP_FOOD));
    /** 炒监守者触须 */
    RegistryObject<BowlFoodOnlyItem> COOKED_WARDEN_TENDRIL_BOWL = ITEMS.register("cooked_warden_tendril_bowl",
            () -> new BowlFoodOnlyItem(KSFoods.COOKED_WARDEN_TENDRIL_BOWL_FOOD));
    /** 远古骨脆片 */
    RegistryObject<Item> ANCIENT_BRITTLE_BONE_FRAGMENTS = ITEMS.register("ancient_brittle_bone_fragments",
            () -> new Item(new Item.Properties().food(KSFoods.ANCIENT_BRITTLE_BONE_FRAGMENTS_FOOD)));
    /** 幽匿炖大骨 */
    RegistryObject<BowlFoodOnlyItem> PORK_ANCIENT_BONE_SOUP = ITEMS.register("pork_ancient_bone_soup",
            () -> new BowlFoodOnlyItem(KSFoods.PORK_ANCIENT_BONE_SOUP_FOOD));
    /** 灵魂薄饼 */
    RegistryObject<Item> SOUL_PANCAKE = ITEMS.register("soul_pancake",
            () -> new Item(new Item.Properties().food(KSFoods.SOUL_PANCAKE_FOOD)));
    /** 幽匿风味月饼 */
    RegistryObject<Item> SCULK_MOONCAKE = ITEMS.register("sculk_mooncake",
            () -> new Item(new Item.Properties().food(KSFoods.SCULK_MOONCAKE_FOOD)));
    /** 苞果冻 */
    RegistryObject<Item> SCULK_PINAPPLE_JELLY = ITEMS.register("sculk_pinapple_jelly",
            () -> new Item(new Item.Properties().food(KSFoods.SCULK_PINAPPLE_JELLY_FOOD)));
    /** 侵蚀饺子串 */
    RegistryObject<Item> EROSION_DUMPLING_SKEWER = ITEMS.register("erosion_dumpling_skewer",
            () -> new Item(new Item.Properties().food(KSFoods.EROSION_DUMPLING_SKEWER_FOOD)));
    /** 熟幽寂肉 */
    RegistryObject<Item> COOKED_EERIE_MEAT = ITEMS.register("cooked_eerie_meat",
            () -> new Item(new Item.Properties().food(KSFoods.COOKED_EERIE_MEAT_FOOD)));
    /** 深板岩蛋糕切片 */
    RegistryObject<DeepslateCakeSliceItem> DEEPSLATE_CAKE_SLICE = ITEMS.register("deepslate_cake_slice",
            () -> new DeepslateCakeSliceItem(new Item.Properties()));


    // ==================== 装备 ====================
    /** 灵魂帆 */
    RegistryObject<SoulSailItem> SOUL_SAIL = ITEMS.register("soul_sail",
            () -> new SoulSailItem(new Item.Properties().stacksTo(1)));


    // ==================== 整盘食物 ====================
    /** 深板岩蛋糕 */
    RegistryObject<BlockItem> DEEPSLATE_CAKE = ITEMS.register("deepslate_cake",
            () -> new BlockItem(KSBlocks.DEEPSLATE_CAKE.get(), new Item.Properties()));
    /** 幽匿烩菜 */
    RegistryObject<BowlFoodBlockItem> SCULK_STEW_BLOCK_ITEM = registerBowlFood("sculk_stew_block",
            KSBlocks.SCULK_STEW_BLOCK, KSFoods.SCULK_STEW_ITEM_FOOD, Items.FLOWER_POT);
    /** 幽匿排骨 */
    RegistryObject<BowlFoodBlockItem> SCULK_PORK_RIBS_BLOCK_ITEM = registerBowlFood("sculk_pork_ribs_block",
            KSBlocks.SCULK_PORK_RIBS_BLOCK, KSFoods.SCULK_PORK_RIBS_ITEM_FOOD, Items.BOWL);
    /** 幽匿炖鸡煲 */
    RegistryObject<BowlFoodBlockItem> SCULK_CHICKEN_STEW_BLOCK_ITEM = registerBowlFood("sculk_chicken_stew_block",
            KSBlocks.SCULK_CHICKEN_STEW_BLOCK, KSFoods.SCULK_CHICKEN_STEW_ITEM_FOOD, Items.FLOWER_POT);
    /** 古城风味刺身 */
    RegistryObject<BowlFoodBlockItem> ANCIENT_CITY_STYLE_SASHIMI = registerBowlFood("ancient_city_style_sashimi",
            KSBlocks.ANCIENT_CITY_STYLE_SASHIMI_BLOCK, KSFoods.ANCIENT_CITY_STYLE_SASHIMI_ITEM_FOOD, Items.BOWL);
    /** 幽匿羊排 */
    RegistryObject<BowlFoodBlockItem> SCULK_LAMB_CHOP = registerBowlFood("sculk_lamb_chop",
            KSBlocks.SCULK_LAMB_CHOP_BLOCK, KSFoods.SCULK_LAMB_CHOP_ITEM_FOOD, Items.BOWL);


    // ==================== 饮品 ====================
    /** 幽匿普洱茶叶 */
    RegistryObject<Item> ECHO_PUER_TEA_BAG = ITEMS.register("echo_puer_tea_bag",
            () -> new Item(new Item.Properties()));
    /** 花雕酒 */
    RegistryObject<Item> HUADIAO_WINE = ITEMS.register("huadiao_wine",
            () -> new DrinkBlockItem(KSBlocks.HUADIAO_WINE.get()));
    /** 红兰酒 */
    RegistryObject<Item> HONGLAN_WINE = ITEMS.register("honglan_wine",
            () -> new DrinkBlockItem(KSBlocks.HONGLAN_WINE.get()));
    /** 幽匿菠萝啤 */
    RegistryObject<Item> SCULK_BREW = ITEMS.register("sculk_brew",
            () -> new DrinkBlockItem(KSBlocks.SCULK_BREW_BOTTLE.get()));



    /**
     * 注册整盘菜肴物品。
     *
     * @param name      注册名
     * @param block     对应的菜肴方块
     * @param food      整盘食物属性
     * @param container 食用后返还的容器
     * @return 整盘菜肴物品
     */
    private static RegistryObject<BowlFoodBlockItem> registerBowlFood(String name, RegistryObject<? extends Block> block, FoodProperties food, Item container) {
        return ITEMS.register(name, () -> new DishItem(block.get(), food, container));
    }

    static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
