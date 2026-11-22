package com.tequilacat.kaleidoscope_sculk.init;

import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodOnlyItem;
import com.github.ysbbbbbb.kaleidoscopetavern.item.DrinkBlockItem;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.item.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public interface KSItems {
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeSculk.MODID);


    // ==================== 方块 ====================
    /** 深板岩炉灶 */
    DeferredItem<BlockItem> DEEPSLATE_STOVE = ITEMS.registerItem("deepslate_stove",
            properties -> new BlockItem(KSBlocks.DEEPSLATE_STOVE.get(), properties), new Item.Properties());


    // ==================== 材料 ====================
    /** 监守者触须 */
    DeferredItem<Item> WARDEN_TENDRIL = ITEMS.registerItem("warden_tendril", Item::new, new Item.Properties().food(KSFoods.WARDEN_TENDRIL_FOOD));
    /** 远古骨片 */
    DeferredItem<AncientBoneFragmentItem> ANCIENT_BONE_FRAGMENT = ITEMS.registerItem("ancient_bone_fragment",
            AncientBoneFragmentItem::new, new Item.Properties().stacksTo(16).fireResistant());
    /** 幽匿菌 */
    DeferredItem<Item> SCULK_FUNGUS = ITEMS.registerItem("sculk_fungus", Item::new, new Item.Properties().food(KSFoods.SCULK_FUNGUS_FOOD));
    /** 幽匿菠萝 */
    DeferredItem<Item> SCULK_PINAPPLE = ITEMS.registerItem("sculk_pinapple", Item::new, new Item.Properties().food(KSFoods.SCULK_PINAPPLE_FOOD));
    /** 煮幽匿菠萝 */
    DeferredItem<BowlFoodOnlyItem> BOIL_SCULK_PINAPPLE = ITEMS.register("boil_sculk_pinapple", () -> new BowlFoodOnlyItem(KSFoods.BOIL_SCULK_PINAPPLE_FOOD));
    /** 幽匿毛虫 */
    DeferredItem<Item> SCULK_CATERPILLAR = ITEMS.registerItem("sculk_caterpillar", Item::new, new Item.Properties().food(KSFoods.SCULK_CATERPILLAR_FOOD));
    /** 灵魂 */
    DeferredItem<SoulItem> SOUL = ITEMS.registerItem("soul", SoulItem::new, new Item.Properties().stacksTo(64));
    /** 幽匿树枝 */
    DeferredItem<Item> SCULK_BRANCH = ITEMS.registerItem("sculk_branch", Item::new, new Item.Properties().stacksTo(64));
    /** 诡异肉 */
    DeferredItem<Item> EERIE_MEAT = ITEMS.registerItem("eerie_meat", Item::new, new Item.Properties().food(KSFoods.EERIE_MEAT_FOOD));
    /** 幽匿粉尘 */
    DeferredItem<Item> SCULK_DUST = ITEMS.registerItem("sculk_dust", Item::new, new Item.Properties());
    /** 幽匿面团 */
    DeferredItem<Item> SCULK_DOUGH = ITEMS.registerItem("sculk_dough", Item::new, new Item.Properties());
    /** 无声升级锻造模板 */
    DeferredItem<Item> SILENT_UPGRADE_SMITHING_TEMPLATE = ITEMS.registerItem("silent_upgrade_smithing_template", Item::new, new Item.Properties());
    /** 幽匿汁桶 */
    DeferredItem<BucketItem> SCULK_JUICE_BUCKET = ITEMS.register("sculk_juice_bucket",
            () -> new BucketItem(KSFluids.SCULK_JUICE.get(), new Item.Properties().stacksTo(16).craftRemainder(Items.BUCKET)));


    // ==================== 工具 ====================
    /** 侵蚀厨刀 */
    DeferredItem<ErosionKnifeItem> EROSION_KITCHEN_KNIFE = ITEMS.register("erosion_knife", ErosionKnifeItem::new);
    /** 无声厨刀 */
    DeferredItem<SilentKnifeItem> SILENT_KITCHEN_KNIFE = ITEMS.register("silent_kitchen_knife", SilentKnifeItem::new);
    /** 幽匿骨镰 */
    DeferredItem<SculkBoneSickleItem> SCULK_BONE_SICKLE = ITEMS.register("sculk_bone_sickle", SculkBoneSickleItem::new);


    // ==================== 作物 ====================
    /** 回声种子 */
    DeferredItem<EchoSeedItem> ECHO_SEED = ITEMS.registerItem("echo_seed",
            properties -> new EchoSeedItem(KSBlocks.ECHO_CROP.get(), properties), new Item.Properties());


    // ==================== 食物 ====================
    /** 幽匿果肉 */
    DeferredItem<Item> SCULK_FLESH = ITEMS.registerItem("sculk_flesh", Item::new, new Item.Properties().food(KSFoods.SCULK_FLESH_FOOD));
    /** 幽匿真菌汤 */
    DeferredItem<BowlFoodOnlyItem> SCULK_FUNGUS_SOUP = ITEMS.register("sculk_fungus_soup", () -> new BowlFoodOnlyItem(KSFoods.SCULK_FUNGUS_SOUP_FOOD));
    /** 炒监守者触须 */
    DeferredItem<BowlFoodOnlyItem> COOKED_WARDEN_TENDRIL_BOWL = ITEMS.register("cooked_warden_tendril_bowl", () ->
            new BowlFoodOnlyItem(KSFoods.COOKED_WARDEN_TENDRIL_BOWL_FOOD));
    /** 远古骨脆片 */
    DeferredItem<Item> ANCIENT_BRITTLE_BONE_FRAGMENTS = ITEMS.registerItem("ancient_brittle_bone_fragments", Item::new,
            new Item.Properties().food(KSFoods.ANCIENT_BRITTLE_BONE_FRAGMENTS_FOOD));
    /** 幽匿炖大骨 */
    DeferredItem<BowlFoodOnlyItem> PORK_ANCIENT_BONE_SOUP = ITEMS.register("pork_ancient_bone_soup", () -> new BowlFoodOnlyItem(KSFoods.PORK_ANCIENT_BONE_SOUP_FOOD));
    /** 灵魂薄饼 */
    DeferredItem<Item> SOUL_PANCAKE = ITEMS.registerItem("soul_pancake", Item::new, new Item.Properties().food(KSFoods.SOUL_PANCAKE_FOOD));
    /** 幽匿风味月饼 */
    DeferredItem<Item> SCULK_MOONCAKE = ITEMS.registerItem("sculk_mooncake", Item::new, new Item.Properties().food(KSFoods.SCULK_MOONCAKE_FOOD));
    /** 苞果冻 */
    DeferredItem<Item> SCULK_PINAPPLE_JELLY = ITEMS.registerItem("sculk_pinapple_jelly", Item::new, new Item.Properties().food(KSFoods.SCULK_PINAPPLE_JELLY_FOOD));
    /** 侵蚀饺子串 */
    DeferredItem<Item> EROSION_DUMPLING_SKEWER = ITEMS.registerItem("erosion_dumpling_skewer", Item::new, new Item.Properties().food(KSFoods.EROSION_DUMPLING_SKEWER_FOOD));
    /** 熟幽寂肉 */
    DeferredItem<Item> COOKED_EERIE_MEAT = ITEMS.registerItem("cooked_eerie_meat", Item::new, new Item.Properties().food(KSFoods.COOKED_EERIE_MEAT_FOOD));
    /** 深板岩蛋糕切片 */
    DeferredItem<DeepslateCakeSliceItem> DEEPSLATE_CAKE_SLICE = ITEMS.registerItem("deepslate_cake_slice", DeepslateCakeSliceItem::new, new Item.Properties());


    // ==================== 装备 ====================
    /** 灵魂帆 */
    DeferredItem<SoulSailItem> SOUL_SAIL = ITEMS.register("soul_sail", () -> new SoulSailItem(soulSailProperties()));


    // ==================== 整盘食物 ====================
    /** 深板岩蛋糕 */
    DeferredItem<BlockItem> DEEPSLATE_CAKE = ITEMS.registerItem("deepslate_cake",
            properties -> new BlockItem(KSBlocks.DEEPSLATE_CAKE.get(), properties), new Item.Properties());
    /** 幽匿烩菜 */
    DeferredItem<BowlFoodBlockItem> SCULK_STEW_BLOCK_ITEM = registerBowlFood("sculk_stew_block",
            KSBlocks.SCULK_STEW_BLOCK, KSFoods.SCULK_STEW_ITEM_FOOD, Items.FLOWER_POT);
    /** 幽匿排骨 */
    DeferredItem<BowlFoodBlockItem> SCULK_PORK_RIBS_BLOCK_ITEM = registerBowlFood("sculk_pork_ribs_block",
            KSBlocks.SCULK_PORK_RIBS_BLOCK, KSFoods.SCULK_PORK_RIBS_ITEM_FOOD, Items.BOWL);
    /** 幽匿炖鸡煲 */
    DeferredItem<BowlFoodBlockItem> SCULK_CHICKEN_STEW_BLOCK_ITEM = registerBowlFood("sculk_chicken_stew_block",
            KSBlocks.SCULK_CHICKEN_STEW_BLOCK, KSFoods.SCULK_CHICKEN_STEW_ITEM_FOOD, Items.FLOWER_POT);
    /** 古城风味刺身 */
    DeferredItem<BowlFoodBlockItem> ANCIENT_CITY_STYLE_SASHIMI = registerBowlFood("ancient_city_style_sashimi",
            KSBlocks.ANCIENT_CITY_STYLE_SASHIMI_BLOCK, KSFoods.ANCIENT_CITY_STYLE_SASHIMI_ITEM_FOOD, Items.BOWL);
    /** 幽匿羊排 */
    DeferredItem<BowlFoodBlockItem> SCULK_LAMB_CHOP = registerBowlFood("sculk_lamb_chop",
            KSBlocks.SCULK_LAMB_CHOP_BLOCK, KSFoods.SCULK_LAMB_CHOP_ITEM_FOOD, Items.BOWL);


    // ==================== 饮品 ====================
    /** 幽匿普洱茶叶 */
    DeferredItem<Item> ECHO_PUER_TEA_BAG = ITEMS.registerItem("echo_puer_tea_bag", Item::new, new Item.Properties());
    /** 花雕酒 */
    DeferredItem<Item> HUADIAO_WINE = ITEMS.register("huadiao_wine", () -> new DrinkBlockItem(KSBlocks.HUADIAO_WINE.get()));
    /** 红兰酒 */
    DeferredItem<Item> HONGLAN_WINE = ITEMS.register("honglan_wine", () -> new DrinkBlockItem(KSBlocks.HONGLAN_WINE.get()));
    /** 幽匿菠萝啤 */
    DeferredItem<Item> SCULK_BREW = ITEMS.register("sculk_brew", () -> new DrinkBlockItem(KSBlocks.SCULK_BREW_BOTTLE.get()));



    /**
     * 灵魂帆：4 点护甲、无耐久、不可损坏，佩戴在头盔栏。
     */
    private static Item.Properties soulSailProperties() {
        return new Item.Properties().stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR, new AttributeModifier(KaleidoscopeSculk.id("soul_sail_armor"), 4.0D, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD).build());
    }

    /**
     * 注册整盘菜肴物品。
     *
     * @param name      注册名
     * @param block     对应的菜肴方块
     * @param food      整盘食物属性
     * @param container 食用后返还的容器
     * @return 整盘菜肴物品
     */
    private static DeferredItem<BowlFoodBlockItem> registerBowlFood(String name, DeferredBlock<? extends Block> block, FoodProperties food, Item container) {
        return ITEMS.register(name, () -> new DishItem(block.get(), food, container));
    }

    static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
