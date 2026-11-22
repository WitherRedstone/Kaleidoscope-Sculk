package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.util.EffectPair;
import com.tequilacat.kaleidoscope_sculk.util.FoodBuilderHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects.MUSTARD;
import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects.VIGOR;
import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects.WARMTH;

public interface KSFoods {

    // ==================== 作物与材料 ====================
    /** 幽匿菠萝 */
    FoodProperties SCULK_PINAPPLE_FOOD = FoodBuilderHelper.simple(4, 0.3F);
    /** 监守者触须 */
    FoodProperties WARDEN_TENDRIL_FOOD = FoodBuilderHelper.alwaysEdible(2, 0.2F);
    /** 幽匿菌 */
    FoodProperties SCULK_FUNGUS_FOOD = FoodBuilderHelper.alwaysEdible(2, 0.2F);
    /** 诡异肉 */
    FoodProperties EERIE_MEAT_FOOD = FoodBuilderHelper.simple(3, 0.3F,
            EffectPair.of(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(MobEffects.DARKNESS, 200), 1.0F));
    /** 熟幽寂肉 */
    FoodProperties COOKED_EERIE_MEAT_FOOD = FoodBuilderHelper.simple(8, 0.8F);
    /** 幽匿果肉 */
    FoodProperties SCULK_FLESH_FOOD = FoodBuilderHelper.simple(4, 0.8F,
            EffectPair.of(() -> new MobEffectInstance(MobEffects.DARKNESS, 200), 1.0F));
    /** 幽匿毛虫 */
    FoodProperties SCULK_CATERPILLAR_FOOD = FoodBuilderHelper.alwaysEdible(18, 0.2F,
            EffectPair.of(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(MobEffects.DARKNESS, 600), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 3600), 1.0F));


    // ==================== 普通食物 ====================
    /** 苞果冻 */
    FoodProperties SCULK_PINAPPLE_JELLY_FOOD = FoodBuilderHelper.alwaysEdible(9, 0.65F,
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SONIC_WAVE, 1200), 1.0F));
    /** 侵蚀饺子串 */
    FoodProperties EROSION_DUMPLING_SKEWER_FOOD = FoodBuilderHelper.simple(14, 0.6F);
    /** 灵魂薄饼 */
    FoodProperties SOUL_PANCAKE_FOOD = FoodBuilderHelper.alwaysEdible(8, 0.625F,
            EffectPair.of(() -> new MobEffectInstance(MobEffects.REGENERATION, 100), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200), 1.0F));
    /** 幽匿风味月饼 */
    FoodProperties SCULK_MOONCAKE_FOOD = FoodBuilderHelper.alwaysEdible(14, 1.2F,
            EffectPair.of(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 2), 1.0F));
    /** 远古骨脆片 */
    FoodProperties ANCIENT_BRITTLE_BONE_FRAGMENTS_FOOD = FoodBuilderHelper.alwaysEdible(6, 0.8F,
            EffectPair.of(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 1), 1.0F));


    // ==================== 碗装食物 ====================
    /** 煮幽匿菠萝 */
    FoodProperties BOIL_SCULK_PINAPPLE_FOOD = FoodBuilderHelper.alwaysEdible(6, 0.667F,
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 3600), 1.0F));
    /** 炒监守者触须 */
    FoodProperties COOKED_WARDEN_TENDRIL_BOWL_FOOD = FoodBuilderHelper.alwaysEdible(8, 0.667F,
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SONIC_WAVE, 6000, 2), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 3600), 1.0F));
    /** 幽匿炖大骨 */
    FoodProperties PORK_ANCIENT_BONE_SOUP_FOOD = FoodBuilderHelper.alwaysEdible(6, 0.667F,
            EffectPair.of(() -> new MobEffectInstance(VIGOR, 6000), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SONIC_WAVE, 6000), 1.0F));
    /** 幽匿真菌汤 */
    FoodProperties SCULK_FUNGUS_SOUP_FOOD = FoodBuilderHelper.alwaysEdible(5, 0.3F,
            EffectPair.of(() -> new MobEffectInstance(MobEffects.DARKNESS, 600), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 1.0F));


    // ==================== 可放置的菜肴 ====================
    /** 古城风味刺身：4 口，整盘 16 营养 */
    FoodProperties ANCIENT_CITY_STYLE_SASHIMI_BLOCK_FOOD = FoodBuilderHelper.alwaysEdible(4, 0.6F,
            EffectPair.of(() -> new MobEffectInstance(MUSTARD, 6000), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 3600), 1.0F));
    FoodProperties ANCIENT_CITY_STYLE_SASHIMI_ITEM_FOOD = FoodBuilderHelper.alwaysEdible(16, 2.4F,
            EffectPair.of(() -> new MobEffectInstance(MUSTARD, 6000), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 3600), 1.0F));

    /** 幽匿烩菜：3 口，整盘 12 营养 */
    FoodProperties SCULK_STEW_BLOCK_FOOD = FoodBuilderHelper.alwaysEdible(4, 0.6F,
            EffectPair.of(() -> new MobEffectInstance(WARMTH, 6000), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 2400), 1.0F));
    FoodProperties SCULK_STEW_ITEM_FOOD = FoodBuilderHelper.alwaysEdible(12, 1.8F,
            EffectPair.of(() -> new MobEffectInstance(WARMTH, 6000), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 2400), 1.0F));

    /** 幽匿炖鸡煲：4 口，整盘 20 营养 */
    FoodProperties SCULK_CHICKEN_STEW_BLOCK_FOOD = FoodBuilderHelper.alwaysEdible(5, 0.6F,
            EffectPair.of(() -> new MobEffectInstance(WARMTH, 2400), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 3600), 1.0F));
    FoodProperties SCULK_CHICKEN_STEW_ITEM_FOOD = FoodBuilderHelper.alwaysEdible(20, 2.4F,
            EffectPair.of(() -> new MobEffectInstance(WARMTH, 2400), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.ECHO, 3600), 1.0F));

    /** 幽匿羊排：3 口，整盘 12 营养 */
    FoodProperties SCULK_LAMB_CHOP_BLOCK_FOOD = FoodBuilderHelper.alwaysEdible(4, 0.6F,
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SCULK_DASH, 6000), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SONIC_WAVE, 2400, 2), 1.0F));
    FoodProperties SCULK_LAMB_CHOP_ITEM_FOOD = FoodBuilderHelper.alwaysEdible(12, 1.8F,
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SCULK_DASH, 6000), 1.0F),
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SONIC_WAVE, 2400, 2), 1.0F));

    /** 幽匿排骨：4 口，整盘 16 营养 */
    FoodProperties SCULK_PORK_RIBS_BLOCK_FOOD = FoodBuilderHelper.alwaysEdible(4, 0.6F,
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SONIC_WAVE, 3600, 4), 1.0F));
    FoodProperties SCULK_PORK_RIBS_ITEM_FOOD = FoodBuilderHelper.alwaysEdible(16, 2.4F,
            EffectPair.of(() -> new MobEffectInstance(KSEffects.SONIC_WAVE, 3600, 4), 1.0F));
}
