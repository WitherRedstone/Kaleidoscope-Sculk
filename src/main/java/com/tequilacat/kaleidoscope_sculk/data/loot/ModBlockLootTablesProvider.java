package com.tequilacat.kaleidoscope_sculk.data.loot;

import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {

    private static final String UNTOUCHED_BITES = "0";

    public ModBlockLootTablesProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        // ==================== 可放置菜肴 ====================
        dish(KSBlocks.SCULK_STEW_BLOCK.get(), KSItems.SCULK_STEW_BLOCK_ITEM.get(), Items.FLOWER_POT);
        dish(KSBlocks.SCULK_CHICKEN_STEW_BLOCK.get(), KSItems.SCULK_CHICKEN_STEW_BLOCK_ITEM.get(), Items.FLOWER_POT);
        dish(KSBlocks.SCULK_PORK_RIBS_BLOCK.get(), KSItems.SCULK_PORK_RIBS_BLOCK_ITEM.get(), Items.BOWL);
        dish(KSBlocks.SCULK_LAMB_CHOP_BLOCK.get(), KSItems.SCULK_LAMB_CHOP.get(), Items.BOWL, KSItems.SCULK_FUNGUS.get());
        dish(KSBlocks.ANCIENT_CITY_STYLE_SASHIMI_BLOCK.get(), KSItems.ANCIENT_CITY_STYLE_SASHIMI.get(), Items.BOWL,
                Items.RED_CANDLE, Items.SCULK_SHRIEKER);
    }

    /**
     * 生成一张菜肴盘的战利品表。
     *
     * @param block     菜肴方块
     * @param dish      整盘菜肴物品
     * @param container 空盘时返还的容器
     * @param extras    吃过之后额外掉落的物品
     */
    private void dish(Block block, Item dish, ItemLike container, ItemLike... extras) {
        LootItemCondition.Builder untouched = untouchedBites(block);
        LootTable.Builder table = LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(ExplosionCondition.survivesExplosion())
                        .when(untouched)
                        .add(LootItem.lootTableItem(dish)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(ExplosionCondition.survivesExplosion())
                        .when(untouched.invert())
                        .add(LootItem.lootTableItem(container)));
        for (ItemLike extra : extras) {
            table.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(ExplosionCondition.survivesExplosion())
                    .when(untouched.invert())
                    .add(LootItem.lootTableItem(extra)));
        }
        table.setRandomSequence(block.getLootTable());
        add(block, table);
    }

    private static LootItemCondition.Builder untouchedBites(Block block) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(((FoodBiteBlock) block).getBites(), UNTOUCHED_BITES));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return List.of(
                KSBlocks.SCULK_STEW_BLOCK.get(),
                KSBlocks.SCULK_CHICKEN_STEW_BLOCK.get(),
                KSBlocks.SCULK_PORK_RIBS_BLOCK.get(),
                KSBlocks.SCULK_LAMB_CHOP_BLOCK.get(),
                KSBlocks.ANCIENT_CITY_STYLE_SASHIMI_BLOCK.get()
        );
    }
}