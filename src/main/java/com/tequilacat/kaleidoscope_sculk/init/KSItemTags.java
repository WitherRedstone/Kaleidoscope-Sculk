package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface KSItemTags {

    /** 可以用于处理幽匿方块与藤蔓的厨刀。 */
    TagKey<Item> KITCHEN_KNIVES = modTag("kitchen_knives");
    /** 锻造模板，用于寂静升级模板的合成。 */
    TagKey<Item> SMITHING_TEMPLATES = modTag("smithing_templates");

    // ==================== 通用标签 ====================
    TagKey<Item> C_DOUGH = commonTag("dough");
    TagKey<Item> C_MUSHROOMS = commonTag("mushrooms");
    TagKey<Item> C_FOODS_BERRY = commonTag("foods/berry");
    TagKey<Item> C_FOODS_COOKED_MEAT = commonTag("foods/cooked_meat");
    TagKey<Item> C_FOODS_RAW_MEAT = commonTag("foods/raw_meat");
    TagKey<Item> C_TOOLS_KNIVES = commonTag("tools/knives");

    // ==================== 农夫乐事 ====================
    TagKey<Item> FARMERS_DELIGHT_KNIVES = externalTag("farmersdelight", "tools/knives");

    // ==================== 森罗物语：料理 ====================
    TagKey<Item> COOKERY_CATERPILLARS = externalTag("kaleidoscope_cookery", "caterpillars");
    TagKey<Item> COOKERY_KITCHEN_KNIFE = externalTag("kaleidoscope_cookery", "kitchen_knife");
    TagKey<Item> COOKERY_MEALS = externalTag("kaleidoscope_cookery", "meals");
    TagKey<Item> COOKERY_TEA_BAGS = externalTag("kaleidoscope_cookery", "tea_bags");
    TagKey<Item> COOKERY_ENCHANTABLE_KNIVES = externalTag("kaleidoscope_cookery", "enchantable/kitchen_knives");
    TagKey<Item> COOKERY_ENCHANTABLE_SICKLES = externalTag("kaleidoscope_cookery", "enchantable/sickles");

    // ==================== 森罗物语：酒馆 ====================
    TagKey<Item> TAVERN_COCKTAIL_BLUE = externalTag("kaleidoscope_tavern", "cocktail_ingredient_blue");
    TagKey<Item> TAVERN_COCKTAIL_GOLD = externalTag("kaleidoscope_tavern", "cocktail_ingredient_gold");
    TagKey<Item> TAVERN_COCKTAIL_RED = externalTag("kaleidoscope_tavern", "cocktail_ingredient_red");

    private static TagKey<Item> modTag(String path) {
        return TagKey.create(Registries.ITEM, KaleidoscopeSculk.id(path));
    }

    private static TagKey<Item> commonTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Item> externalTag(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
