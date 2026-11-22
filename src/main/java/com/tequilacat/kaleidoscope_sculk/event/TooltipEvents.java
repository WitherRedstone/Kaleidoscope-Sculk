package com.tequilacat.kaleidoscope_sculk.event;

import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import com.tequilacat.kaleidoscope_sculk.init.KSFoods;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.util.FoodTooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 物品提示事件类。
 * <p>
 * 在物品名之后插入本模组的说明文本：普通物品使用预置的单行提示，
 * 食物物品则追加其食物效果说明。方块形态的菜肴没有食物组件，
 * 改从方块到食物属性的映射中查表获取。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class TooltipEvents {

    /** 描述插入位置，紧跟物品名之后。 */
    private static final int TOOLTIP_INDEX = 1;

    /** 普通物品的单行提示缓存。 */
    private static Map<Item, Component> simpleTooltips;

    /** 方块菜肴到食物属性的映射缓存。 */
    private static Map<Block, FoodProperties> dishFoods;

    private TooltipEvents() {
    }

    /**
     * 处理物品提示事件。
     * <p>
     * 先按需插入普通物品的单行提示，再追加该物品食物属性的效果说明；
     * 两者均无时不改动提示。
     *
     * @param event 物品提示事件
     */
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        int index = TOOLTIP_INDEX;
        Component simpleLine = simpleTooltips().get(stack.getItem());
        if (simpleLine != null) {
            tooltip.add(index++, simpleLine);
        }

        FoodProperties food = resolveFood(stack);
        List<Component> effectLines = new ArrayList<>();
        FoodTooltips.addEffectLines(food, effectLines);
        if (!effectLines.isEmpty()) {
            tooltip.addAll(index, effectLines);
        }
    }

    /**
     * 取物品对应的食物属性。方块形态的菜肴没有食物组件，改从方块查表。
     *
     * @param stack 物品堆
     * @return 食物属性，没有则返回 null
     */
    private static FoodProperties resolveFood(ItemStack stack) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null) {
            return food;
        }
        if (stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof FoodBiteBlock biteBlock) {
            return dishFoods().get(biteBlock);
        }
        return null;
    }

    /**
     * 获取普通物品的单行提示映射。
     * <p>
     * 首次访问时构建并缓存，包含触须、骨片、菌菇等物品的灰色或深灰色说明。
     *
     * @return 物品到提示文本的映射
     */
    private static Map<Item, Component> simpleTooltips() {
        Map<Item, Component> cached = simpleTooltips;
        if (cached == null) {
            cached = Map.ofEntries(
                    simpleTooltip(KSItems.WARDEN_TENDRIL, "warden_tendril", ChatFormatting.GRAY),
                    simpleTooltip(KSItems.ANCIENT_BONE_FRAGMENT, "ancient_bone_fragment", ChatFormatting.DARK_GRAY),
                    simpleTooltip(KSItems.SCULK_FUNGUS, "sculk_fungus", ChatFormatting.GRAY),
                    simpleTooltip(KSItems.ECHO_SEED, "echo_seed", ChatFormatting.GRAY),
                    simpleTooltip(KSItems.SCULK_BRANCH, "sculk_branch", ChatFormatting.GRAY),
                    simpleTooltip(KSItems.SCULK_PINAPPLE, "sculk_pinapple", ChatFormatting.DARK_GRAY),
                    simpleTooltip(KSItems.SOUL, "soul", ChatFormatting.GRAY),
                    simpleTooltip(KSItems.SCULK_PINAPPLE_JELLY, "sculk_pinapple_jelly", ChatFormatting.GRAY),
                    simpleTooltip(KSItems.EROSION_DUMPLING_SKEWER, "erosion_dumpling_skewer", ChatFormatting.GRAY),
                    simpleTooltip(KSItems.SOUL_PANCAKE, "soul_pancake", ChatFormatting.DARK_GRAY)
            );
            simpleTooltips = cached;
        }
        return cached;
    }

    /**
     * 获取方块菜肴到食物属性的映射。
     * <p>
     * 首次访问时构建并缓存，覆盖各类幽匿风味菜肴方块。
     *
     * @return 方块到食物属性的映射
     */
    private static Map<Block, FoodProperties> dishFoods() {
        Map<Block, FoodProperties> cached = dishFoods;
        if (cached == null) {
            cached = Map.of(
                    KSBlocks.ANCIENT_CITY_STYLE_SASHIMI_BLOCK.get(), KSFoods.ANCIENT_CITY_STYLE_SASHIMI_BLOCK_FOOD,
                    KSBlocks.SCULK_STEW_BLOCK.get(), KSFoods.SCULK_STEW_BLOCK_FOOD,
                    KSBlocks.SCULK_CHICKEN_STEW_BLOCK.get(), KSFoods.SCULK_CHICKEN_STEW_BLOCK_FOOD,
                    KSBlocks.SCULK_LAMB_CHOP_BLOCK.get(), KSFoods.SCULK_LAMB_CHOP_BLOCK_FOOD,
                    KSBlocks.SCULK_PORK_RIBS_BLOCK.get(), KSFoods.SCULK_PORK_RIBS_BLOCK_FOOD
            );
            dishFoods = cached;
        }
        return cached;
    }

    /**
     * 构造单行提示条目。
     *
     * @param item  物品供应器
     * @param key   本地化键后缀
     * @param color 文本颜色
     * @return 物品到提示文本的映射条目
     */
    private static Map.Entry<Item, Component> simpleTooltip(Supplier<? extends Item> item, String key, ChatFormatting color) {
        return Map.entry(item.get(),
                Component.translatable("item.kaleidoscope_sculk." + key + ".tooltip").withStyle(color));
    }
}
