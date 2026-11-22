package com.tequilacat.kaleidoscope_sculk.init;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KSCreativeModeTabs {

    DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeSculk.MODID);

    DeferredHolder<CreativeModeTab, CreativeModeTab> SCULK_TAB =
            CREATIVE_MODE_TABS.register("kaleidoscope_sculk_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(KSItems.COOKED_WARDEN_TENDRIL_BOWL.get()))
                    .title(Component.translatable("itemGroup." + KaleidoscopeSculk.MODID))
                    .displayItems((parameters, output) -> addItems(output))
                    .build());

    ResourceKey<CreativeModeTab> COOKERY_MAIN_TAB =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("kaleidoscope_cookery", "cookery_main"));
    ResourceKey<CreativeModeTab> COOKERY_FOOD_TAB =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("kaleidoscope_cookery", "cookery_food"));

    static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }

    static void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(COOKERY_MAIN_TAB) && !event.getTabKey().equals(COOKERY_FOOD_TAB)) {
            return;
        }

        Item teacup = TeacupRegistry.getItem(KSTeacups.ECHO_PUER);
        if (teacup != Items.AIR) {
            event.remove(new ItemStack(teacup), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private static void addItems(CreativeModeTab.Output output) {
        // ==================== 方块 ====================
        output.accept(KSItems.DEEPSLATE_STOVE.get());

        // ==================== 材料 ====================
        output.accept(KSItems.WARDEN_TENDRIL.get());
        output.accept(KSItems.ANCIENT_BONE_FRAGMENT.get());
        output.accept(KSItems.SCULK_FUNGUS.get());
        output.accept(KSItems.SCULK_PINAPPLE.get());
        output.accept(KSItems.BOIL_SCULK_PINAPPLE.get());
        output.accept(KSItems.SCULK_CATERPILLAR.get());
        output.accept(KSItems.SOUL.get());
        output.accept(KSItems.SCULK_BRANCH.get());
        output.accept(KSItems.EERIE_MEAT.get());
        output.accept(KSItems.SCULK_DUST.get());
        output.accept(KSItems.SCULK_DOUGH.get());
        output.accept(KSItems.SILENT_UPGRADE_SMITHING_TEMPLATE.get());
        output.accept(KSItems.SCULK_JUICE_BUCKET.get());

        // ==================== 工具 ====================
        output.accept(KSItems.EROSION_KITCHEN_KNIFE.get());
        output.accept(KSItems.SILENT_KITCHEN_KNIFE.get());
        output.accept(KSItems.SCULK_BONE_SICKLE.get());

        // ==================== 作物 ====================
        output.accept(KSItems.ECHO_SEED.get());

        // ==================== 食物 ====================
        output.accept(KSItems.SCULK_FLESH.get());
        output.accept(KSItems.SCULK_FUNGUS_SOUP.get());
        output.accept(KSItems.COOKED_WARDEN_TENDRIL_BOWL.get());
        output.accept(KSItems.ANCIENT_BRITTLE_BONE_FRAGMENTS.get());
        output.accept(KSItems.PORK_ANCIENT_BONE_SOUP.get());
        output.accept(KSItems.SOUL_PANCAKE.get());
        output.accept(KSItems.SCULK_MOONCAKE.get());
        output.accept(KSItems.SCULK_PINAPPLE_JELLY.get());
        output.accept(KSItems.EROSION_DUMPLING_SKEWER.get());
        output.accept(KSItems.COOKED_EERIE_MEAT.get());
        output.accept(KSItems.DEEPSLATE_CAKE_SLICE.get());

        // ==================== 装备 ====================
        output.accept(KSItems.SOUL_SAIL.get());

        // ==================== 整盘食物 ====================
        output.accept(KSItems.DEEPSLATE_CAKE.get());
        output.accept(KSItems.SCULK_STEW_BLOCK_ITEM.get());
        output.accept(KSItems.SCULK_PORK_RIBS_BLOCK_ITEM.get());
        output.accept(KSItems.SCULK_CHICKEN_STEW_BLOCK_ITEM.get());
        output.accept(KSItems.ANCIENT_CITY_STYLE_SASHIMI.get());
        output.accept(KSItems.SCULK_LAMB_CHOP.get());

        // ==================== 饮品 ====================
        Item teacup = TeacupRegistry.getItem(KSTeacups.ECHO_PUER);
        if (teacup != Items.AIR) {
            output.accept(new ItemStack(teacup));
        }
        output.accept(KSItems.ECHO_PUER_TEA_BAG.get());
        output.accept(BottleBlockItem.getMaxLevelDrink(KSItems.HUADIAO_WINE));
        output.accept(BottleBlockItem.getMaxLevelDrink(KSItems.HONGLAN_WINE));
        output.accept(BottleBlockItem.getMaxLevelDrink(KSItems.SCULK_BREW));
    }
}
