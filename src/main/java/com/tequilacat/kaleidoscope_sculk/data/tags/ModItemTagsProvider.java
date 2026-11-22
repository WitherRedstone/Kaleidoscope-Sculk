package com.tequilacat.kaleidoscope_sculk.data.tags;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItemTags;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, KaleidoscopeSculk.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // ==================== 通用标签 ====================
        tag(KSItemTags.C_DOUGH)
                .add(KSItems.SCULK_DOUGH.get());
        tag(KSItemTags.C_MUSHROOMS)
                .add(KSItems.SCULK_FUNGUS.get());
        tag(KSItemTags.C_FOODS_BERRY)
                .add(KSItems.SCULK_PINAPPLE.get());
        tag(KSItemTags.C_FOODS_COOKED_MEAT)
                .add(KSItems.COOKED_EERIE_MEAT.get());
        tag(KSItemTags.C_FOODS_RAW_MEAT)
                .add(KSItems.WARDEN_TENDRIL.get())
                .add(KSItems.EERIE_MEAT.get());
        tag(KSItemTags.C_TOOLS_KNIVES)
                .add(KSItems.EROSION_KITCHEN_KNIFE.get())
                .add(KSItems.SILENT_KITCHEN_KNIFE.get());

        // ==================== 农夫乐事 ====================
        tag(KSItemTags.FARMERS_DELIGHT_KNIVES)
                .add(KSItems.EROSION_KITCHEN_KNIFE.get())
                .add(KSItems.SILENT_KITCHEN_KNIFE.get());

        // ==================== 森罗物语：料理 ====================
        tag(KSItemTags.COOKERY_CATERPILLARS)
                .add(KSItems.SCULK_CATERPILLAR.get());
        tag(KSItemTags.COOKERY_KITCHEN_KNIFE)
                .add(KSItems.EROSION_KITCHEN_KNIFE.get())
                .add(KSItems.SILENT_KITCHEN_KNIFE.get());
        tag(KSItemTags.COOKERY_MEALS)
                .add(KSItems.SCULK_CATERPILLAR.get());
        tag(KSItemTags.COOKERY_TEA_BAGS)
                .add(KSItems.ECHO_PUER_TEA_BAG.get());
        tag(KSItemTags.COOKERY_ENCHANTABLE_KNIVES)
                .add(KSItems.EROSION_KITCHEN_KNIFE.get())
                .add(KSItems.SILENT_KITCHEN_KNIFE.get());
        tag(KSItemTags.COOKERY_ENCHANTABLE_SICKLES)
                .add(KSItems.SCULK_BONE_SICKLE.get());

        // ==================== 森罗物语：酒馆 ====================
        tag(KSItemTags.TAVERN_COCKTAIL_BLUE)
                .add(KSItems.SCULK_BREW.get());
        tag(KSItemTags.TAVERN_COCKTAIL_GOLD)
                .add(KSItems.HONGLAN_WINE.get());
        tag(KSItemTags.TAVERN_COCKTAIL_RED)
                .add(KSItems.HUADIAO_WINE.get());

        // ==================== 本模组 ====================
        tag(KSItemTags.KITCHEN_KNIVES)
                .add(KSItems.EROSION_KITCHEN_KNIFE.get())
                .add(KSItems.SILENT_KITCHEN_KNIFE.get())
                .addTag(KSItemTags.COOKERY_KITCHEN_KNIFE)
                .addTag(KSItemTags.FARMERS_DELIGHT_KNIVES);
        tag(KSItemTags.SMITHING_TEMPLATES)
                .add(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                .add(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)
                .add(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);

        // ==================== 原版 ====================
        tag(ItemTags.FOX_FOOD)
                .add(KSItems.SCULK_PINAPPLE.get());
        tag(ItemTags.SWORDS)
                .add(KSItems.EROSION_KITCHEN_KNIFE.get())
                .add(KSItems.SILENT_KITCHEN_KNIFE.get())
                .add(KSItems.SCULK_BONE_SICKLE.get());
    }
}
