package com.tequilacat.kaleidoscope_sculk.data;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopetavern.datagen.builder.BarrelBuilder;
import com.github.ysbbbbbb.kaleidoscopetavern.datagen.builder.PressingTubBuilder;
import com.github.ysbbbbbb.kaleidoscopetavern.init.ModFluids;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSFluids;
import com.tequilacat.kaleidoscope_sculk.init.KSItemTags;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.init.KSTeacups;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider {

    public ModRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        craftingRecipes(output);
        cookingRecipes(output);
        smithingRecipes(output);
        barrelRecipes(output);
        pressingTubRecipes(output);
        choppingBoardRecipes(output);
        millstoneRecipes(output);
        potRecipes(output);
        stockpotRecipes(output);
        teapotRecipes(output);
    }

    /** 工作台 */
    private void craftingRecipes(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, KSItems.ANCIENT_CITY_STYLE_SASHIMI.get())
                .requires(ModItems.SASHIMI.get(), 4)
                .requires(Items.RED_CANDLE)
                .requires(Items.SCULK_SHRIEKER)
                .requires(Items.BOWL)
                .unlockedBy(getHasName(Items.TROPICAL_FISH), has(Items.TROPICAL_FISH))
                .unlockedBy(getHasName(Items.RED_CANDLE),  has(Items.RED_CANDLE))
                .unlockedBy(getHasName(Items.SCULK_SHRIEKER), has(Items.SCULK_SHRIEKER))
                .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
                .save(output, KaleidoscopeSculk.id("ancient_city_style_sashimi"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, KSItems.DEEPSLATE_CAKE_SLICE.get())
                .pattern(" S ")
                .pattern(" D ")
                .pattern(" P ")
                .define('S', Items.SUGAR)
                .define('D', KSItems.SCULK_DOUGH.get())
                .define('P', Items.DEEPSLATE_BRICK_SLAB)
                .unlockedBy(getHasName(Items.SUGAR), has(Items.SUGAR))
                .unlockedBy(getHasName(KSItems.SCULK_DOUGH), has(KSItems.SCULK_DOUGH.get()))
                .unlockedBy(getHasName(Items.DEEPSLATE_BRICK_SLAB), has(Items.DEEPSLATE_BRICK_SLAB))
                .save(output, KaleidoscopeSculk.id("deepslate_cake_slice"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, KSItems.DEEPSLATE_STOVE.get())
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SSS")
                .define('S', Items.DEEPSLATE)
                .define('C', Items.SOUL_CAMPFIRE)
                .unlockedBy(getHasName(Items.DEEPSLATE), has(Items.DEEPSLATE))
                .unlockedBy(getHasName(Items.SOUL_CAMPFIRE), has(Items.SOUL_CAMPFIRE))
                .save(output, KaleidoscopeSculk.id("deepslate_stove"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, KSItems.ECHO_PUER_TEA_BAG.get())
                .pattern("LLL")
                .pattern("LCL")
                .pattern("LLL")
                .define('L', ModItems.DRIED_TEA_LEAVES.get())
                .define('C', KSItems.SCULK_DUST.get())
                .unlockedBy(getHasName(ModItems.DRIED_TEA_LEAVES), has(ModItems.DRIED_TEA_LEAVES.get()))
                .unlockedBy(getHasName(KSItems.SCULK_DUST), has(KSItems.SCULK_DUST.get()))
                .save(output, KaleidoscopeSculk.id("echo_puer_tea_bag"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ECHO_SHARD)
                .pattern("EEE")
                .pattern("EAE")
                .pattern("EEE")
                .define('E', KSItems.ECHO_SEED.get())
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy(getHasName(KSItems.ECHO_SEED), has(KSItems.ECHO_SEED.get()))
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(output, KaleidoscopeSculk.id("echo_shard"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, KSItems.EROSION_KITCHEN_KNIFE.get())
                .pattern("IB ")
                .pattern("IC ")
                .pattern("   ")
                .define('I', Items.IRON_INGOT)
                .define('B', Items.SCULK_CATALYST)
                .define('C', KSItems.SCULK_BRANCH.get())
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.SCULK_CATALYST), has(Items.SCULK_CATALYST))
                .unlockedBy(getHasName(KSItems.SCULK_BRANCH), has(KSItems.SCULK_BRANCH.get()))
                .save(output, KaleidoscopeSculk.id("erosion_knife"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, KSItems.SCULK_BONE_SICKLE.get())
                .pattern("BBF")
                .pattern(" TB")
                .pattern("T  ")
                .define('B', KSItems.ANCIENT_BONE_FRAGMENT.get())
                .define('F', KSItems.SCULK_FUNGUS.get())
                .define('T', KSItems.WARDEN_TENDRIL.get())
                .unlockedBy(getHasName(KSItems.ANCIENT_BONE_FRAGMENT), has(KSItems.ANCIENT_BONE_FRAGMENT.get()))
                .unlockedBy(getHasName(KSItems.SCULK_FUNGUS), has(KSItems.SCULK_FUNGUS.get()))
                .unlockedBy(getHasName(KSItems.WARDEN_TENDRIL), has(KSItems.WARDEN_TENDRIL.get()))
                .save(output, KaleidoscopeSculk.id("sculk_bone_sickle"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, KSItems.SCULK_DOUGH.get())
                .requires(Items.WATER_BUCKET)
                .requires(KSItems.SCULK_DUST.get())
                .requires(KSItems.SOUL.get())
                .unlockedBy(getHasName(KSItems.SCULK_DUST), has(KSItems.SCULK_DUST.get()))
                .unlockedBy(getHasName(KSItems.SOUL), has(KSItems.SOUL.get()))
                .save(output, KaleidoscopeSculk.id("sculk_dough"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE.get())
                .pattern("BEB")
                .pattern("BTB")
                .pattern("BBB")
                .define('B', KSItems.WARDEN_TENDRIL.get())
                .define('E', Items.ECHO_SHARD)
                .define('T', KSItemTags.SMITHING_TEMPLATES)
                .unlockedBy(getHasName(KSItems.WARDEN_TENDRIL), has(KSItems.WARDEN_TENDRIL.get()))
                .unlockedBy(getHasName(KSItems.ANCIENT_BONE_FRAGMENT), has(KSItems.ANCIENT_BONE_FRAGMENT.get()))
                .save(output, KaleidoscopeSculk.id("silent_upgrade_smithing_template"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, KSItems.SOUL.get())
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .unlockedBy(getHasName(Items.SOUL_SAND), has(Items.SOUL_SAND))
                .unlockedBy(getHasName(Items.SOUL_SOIL), has(Items.SOUL_SOIL))
                .save(output, KaleidoscopeSculk.id("soul_from_soul_sand_or_soil"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, KSItems.SOUL_SAIL.get())
                .pattern("BAB")
                .pattern("CDC")
                .pattern(" D ")
                .define('B', ItemTags.WOOL)
                .define('A', KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE.get())
                .define('C', KSItems.ANCIENT_BONE_FRAGMENT.get())
                .define('D', ItemTags.WOODEN_FENCES)
                .unlockedBy(getHasName(KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE), has(KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE.get()))
                .unlockedBy(getHasName(KSItems.ANCIENT_BONE_FRAGMENT), has(KSItems.ANCIENT_BONE_FRAGMENT.get()))
                .save(output, KaleidoscopeSculk.id("soul_sail"));
    }

    /** 营火 */
    private void cookingRecipes(RecipeOutput output) {
        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(KSItems.EERIE_MEAT.get()), RecipeCategory.FOOD,
                        KSItems.COOKED_EERIE_MEAT.get(), 0.35F, 600)
                .unlockedBy(getHasName(KSItems.EERIE_MEAT), has(KSItems.EERIE_MEAT.get()))
                .save(output, KaleidoscopeSculk.id("cooked_eerie_meat_campfire"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(KSItems.EERIE_MEAT.get()), RecipeCategory.FOOD,
                        KSItems.COOKED_EERIE_MEAT.get(), 0.35F, 200)
                .unlockedBy(getHasName(KSItems.EERIE_MEAT), has(KSItems.EERIE_MEAT.get()))
                .save(output, KaleidoscopeSculk.id("cooked_eerie_meat_smelting"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(KSItems.EERIE_MEAT.get()), RecipeCategory.FOOD,
                        KSItems.COOKED_EERIE_MEAT.get(), 0.35F, 100)
                .unlockedBy(getHasName(KSItems.EERIE_MEAT), has(KSItems.EERIE_MEAT.get()))
                .save(output, KaleidoscopeSculk.id("cooked_eerie_meat_smoking"));
    }

    /** 锻造台 */
    private void smithingRecipes(RecipeOutput output) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(KSItems.EROSION_KITCHEN_KNIFE.get()),
                        Ingredient.of(Items.SCULK_SHRIEKER),
                        RecipeCategory.TOOLS,
                        KSItems.SILENT_KITCHEN_KNIFE.get())
                .unlocks(getHasName(KSItems.EROSION_KITCHEN_KNIFE), has(KSItems.EROSION_KITCHEN_KNIFE.get()))
                .unlocks(getHasName(Items.SCULK_SHRIEKER), has(Items.SCULK_SHRIEKER))
                .unlocks(getHasName(KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE), has(KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(output, KaleidoscopeSculk.id("silent_knife_smithing"));
    }

    /** 森罗物语：酒馆-酒桶 */
    private void barrelRecipes(RecipeOutput output) {
        BarrelBuilder.builder()
                .addIngredient(Items.GLOWSTONE_DUST)
                .addIngredient(ItemTags.FLOWERS)
                .setFluid(KSFluids.SCULK_JUICE.get())
                .setCarrier(com.github.ysbbbbbb.kaleidoscopetavern.init.ModItems.EMPTY_BOTTLE.get())
                .setResult(KSItems.HONGLAN_WINE.get())
                .save(output);
        BarrelBuilder.builder()
                .addIngredient(ModItems.RICE_SEED.get())
                .addIngredient(KSItems.SCULK_DUST.get())
                .setFluid(ModFluids.GOLD_GRAPE_JUICE.get())
                .setCarrier(com.github.ysbbbbbb.kaleidoscopetavern.init.ModItems.EMPTY_BOTTLE.get())
                .setResult(KSItems.HUADIAO_WINE.get())
                .save(output);
        BarrelBuilder.builder()
                .addIngredient(KSItems.SCULK_FUNGUS.get())
                .addIngredient(Items.WHEAT)
                .setFluid(KSFluids.SCULK_JUICE.get())
                .setCarrier(com.github.ysbbbbbb.kaleidoscopetavern.init.ModItems.EMPTY_BOTTLE.get())
                .setResult(KSItems.SCULK_BREW.get())
                .save(output);
    }

    /** 森罗物语：酒馆-果盆 */
    private void pressingTubRecipes(RecipeOutput output) {
        PressingTubBuilder.builder()
                .setIngredient(KSItems.SCULK_FLESH.get())
                .setFluid(KSFluids.SCULK_JUICE.get())
                .save(output);
    }

    /** 森罗物语：厨房-砧板 */
    private void choppingBoardRecipes(RecipeOutput output) {
        ChoppingBoardBuilder.builder()
                .setIngredient(KSItems.SCULK_PINAPPLE.get())
                .setResult(KSItems.SCULK_FLESH.get(), 3)
                .setCutCount(3)
                .setModelId(KaleidoscopeSculk.id("sculk_flesh"))
                .save(output);
        ChoppingBoardBuilder.builder()
                .setIngredient(Items.SCULK)
                .setResult(KSItems.SCULK_FUNGUS.get(), 4)
                .setCutCount(4)
                .setModelId(KaleidoscopeSculk.id("sculk_fungus"))
                .save(output);
    }

    /** 森罗物语：厨房-石磨 */
    private void millstoneRecipes(RecipeOutput output) {
        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.ECHO_SHARD)
                .setResult(KSItems.ECHO_SEED.get(), 2)
                .save(output);
        MillstoneRecipeBuilder.builder()
                .setIngredient(KSItems.SCULK_BRANCH.get())
                .setResult(KSItems.SCULK_DUST.get(), 2)
                .save(output);
    }

    /** 森罗物语：厨房-炒锅 */
    private void potRecipes(RecipeOutput output) {
        PotRecipeBuilder.builder()
                .setBowlCarrier()
                .addInput(
                        KSItems.ANCIENT_BONE_FRAGMENT.get(), KSItems.ANCIENT_BONE_FRAGMENT.get(),
                        KSItems.ANCIENT_BONE_FRAGMENT.get(), KSItems.ANCIENT_BONE_FRAGMENT.get())
                .setStirFryCount(6)
                .setResult(KSItems.ANCIENT_BRITTLE_BONE_FRAGMENTS.get())
                .save(output);
        PotRecipeBuilder.builder()
                .setBowlCarrier()
                .addInput(KSItems.WARDEN_TENDRIL.get(), KSItems.WARDEN_TENDRIL.get())
                .setStirFryCount(6)
                .setResult(KSItems.COOKED_WARDEN_TENDRIL_BOWL.get())
                .save(output);
        PotRecipeBuilder.builder()
                .setCarrier(Items.STICK)
                .addInput(ModItems.QINGTUAN.get(), ModItems.QINGTUAN.get(), KSItems.SCULK_PINAPPLE.get())
                .setStirFryCount(3)
                .setResult(KSItems.EROSION_DUMPLING_SKEWER.get())
                .save(output);

        PotRecipeBuilder.builder()
                .setBowlCarrier()
                .addInput(KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get(),
                        TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON)
                .setStirFryCount(3)
                .setResult(KSItems.SCULK_LAMB_CHOP.get())
                .save(output);
        PotRecipeBuilder.builder()
                .addInput(
                        ResourceLocation.fromNamespaceAndPath("kaleidoscope_chinesefood", "raw_mooncake"),
                        KSItems.WARDEN_TENDRIL.get(), KSItems.WARDEN_TENDRIL.get(), KSItems.WARDEN_TENDRIL.get())
                .setStirFryCount(5)
                .setResult(KSItems.SCULK_MOONCAKE.get())
                .save(output.withConditions(new ModLoadedCondition("kaleidoscope_chinesefood")),"sculk_mooncake_chinesefood");
        PotRecipeBuilder.builder()
                .addInput(
                        KSItems.SCULK_DOUGH.get(), KSItems.WARDEN_TENDRIL.get(),
                        KSItems.WARDEN_TENDRIL.get(), KSItems.WARDEN_TENDRIL.get())
                .setStirFryCount(5)
                .setResult(KSItems.SCULK_MOONCAKE.get())
                .save(output.withConditions(new NotCondition(new ModLoadedCondition("kaleidoscope_chinesefood"))),"sculk_mooncake");
        PotRecipeBuilder.builder()
                .addInput(KSItems.SCULK_FLESH.get(), KSItems.SCULK_FLESH.get(), Items.SUGAR, Items.SUGAR, Items.SLIME_BALL)
                .setStirFryCount(4)
                .setResult(KSItems.SCULK_PINAPPLE_JELLY.get())
                .save(output);
        PotRecipeBuilder.builder()
                .setBowlCarrier()
                .addInput(
                        KSItems.EERIE_MEAT.get(), KSItems.EERIE_MEAT.get(), KSItems.EERIE_MEAT.get(),
                        KSItems.ANCIENT_BONE_FRAGMENT.get(), KSItems.ANCIENT_BONE_FRAGMENT.get(),
                        KSItems.ANCIENT_BONE_FRAGMENT.get(), KSItems.ANCIENT_BONE_FRAGMENT.get())
                .setStirFryCount(6)
                .setResult(KSItems.SCULK_PORK_RIBS_BLOCK_ITEM.get())
                .save(output);
        PotRecipeBuilder.builder()
                .addInput(Items.SUGAR, KSItems.SCULK_DOUGH.get())
                .setStirFryCount(4)
                .setResult(KSItems.SOUL_PANCAKE.get())
                .save(output);
    }

    /** 森罗物语：厨房-汤锅 */
    private void stockpotRecipes(RecipeOutput output) {
        StockpotRecipeBuilder.builder()
                .addInput(
                        KSItems.SCULK_FLESH.get(), KSItems.SCULK_FLESH.get()
                )
                .setCarrier(Items.BOWL)
                .setResult(KSItems.BOIL_SCULK_PINAPPLE.get(), 1)
                .save(output);
        StockpotRecipeBuilder.builder()
                .addInput(
                        KSItems.ANCIENT_BONE_FRAGMENT.get(), KSItems.ANCIENT_BONE_FRAGMENT.get(),
                        KSItems.ANCIENT_BONE_FRAGMENT.get()
                )
                .setCarrier(Items.BOWL)
                .setResult(KSItems.PORK_ANCIENT_BONE_SOUP.get(), 1)
                .save(output);
        StockpotRecipeBuilder.builder()
                .addInput(
                        Items.COOKED_CHICKEN, Items.RED_MUSHROOM,
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_DUST.get()
                )
                .setCarrier(Items.BOWL)
                .setResult(KSItems.SCULK_CHICKEN_STEW_BLOCK_ITEM.get(), 1)
                .save(output);
        StockpotRecipeBuilder.builder()
                .addInput(
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get()
                )
                .setSoupBase(BuiltInRegistries.FLUID.getKey(Fluids.WATER))
                .setResult(KSItems.SCULK_FUNGUS_SOUP.get(), 1)
                .save(output, "sculk_fungus_soup_1");
        StockpotRecipeBuilder.builder()
                .addInput(
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get(),
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get()
                )
                .setSoupBase(BuiltInRegistries.FLUID.getKey(Fluids.WATER))
                .setResult(KSItems.SCULK_FUNGUS_SOUP.get(), 2)
                .save(output, "sculk_fungus_soup_2");
        StockpotRecipeBuilder.builder()
                .addInput(
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get(),
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get(),
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get()
                )
                .setSoupBase(BuiltInRegistries.FLUID.getKey(Fluids.WATER))
                .setResult(KSItems.SCULK_FUNGUS_SOUP.get(), 3)
                .save(output, "sculk_fungus_soup_3");
        StockpotRecipeBuilder.builder()
                .addInput(
                        KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get(), KSItems.SCULK_FUNGUS.get(),
                        TagCommon.VEGETABLES, TagCommon.VEGETABLES, TagCommon.RAW_MEATS
                )
                .setCarrier(Items.FLOWER_POT)
                .setResult(KSItems.SCULK_STEW_BLOCK_ITEM.get(), 3)
                .save(output);
    }

    /** 森罗物语：厨房-茶壶 */
    private void teapotRecipes(RecipeOutput output) {
        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(KSItems.ECHO_PUER_TEA_BAG.get())
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(KSTeacups.ECHO_PUER))
                .setTime(2400)
                .save(output);
    }
}
