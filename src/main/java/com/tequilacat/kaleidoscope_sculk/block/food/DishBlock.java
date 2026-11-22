package com.tequilacat.kaleidoscope_sculk.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * 盘装食物方块。
 * <p>
 * 继承厨师模组的可多次分食食物方块，在吃完最后一口时移除方块并返还容器：
 * 按掉落表弹出额外掉落物（跳过容器本身），再弹出容器物品；
 * 若容器不是碗，还会额外弹出一个空碗。
 */
public class DishBlock extends FoodBiteBlock {

    /** 食物吃完后返还的容器物品。 */
    private final ItemLike container;

    /**
     * 构造盘装食物方块。
     *
     * @param foodProperties 食物属性
     * @param maxBites       最多可吃的口数
     * @param container      吃完后返还的容器
     */
    public DishBlock(FoodProperties foodProperties, int maxBites, ItemLike container) {
        super(foodProperties, maxBites, null);
        this.container = container;
    }

    /**
     * 处理进食。
     * <p>
     * 先执行父类进食逻辑；若本次吃下的是最后一口，则移除方块、
     * 按掉落表弹出额外掉落物并返还容器，容器不是碗时再补一个空碗。
     *
     * @param level  世界
     * @param pos    方块位置
     * @param state  方块状态
     * @param player 玩家
     * @return 交互结果
     */
    @Override
    protected InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        InteractionResult result = super.eat(level, pos, state, player);
        if (!result.consumesAction() || !(level instanceof ServerLevel serverLevel)) {
            return result;
        }

        BlockState currentState = serverLevel.getBlockState(pos);
        if (!currentState.is(this) || currentState.getValue(getBites()) < getMaxBites()) {
            return result;
        }

        serverLevel.destroyBlock(pos, false);
        ItemStack containerStack = new ItemStack(container);
        for (ItemStack drop : rollDrops(serverLevel, pos, currentState, player)) {
            if (ItemStack.isSameItem(drop, containerStack)) {
                continue;
            }
            Block.popResource(serverLevel, pos, drop);
        }
        Block.popResource(serverLevel, pos, containerStack);
        if (!containerStack.is(Items.BOWL)) {
            Block.popResource(serverLevel, pos, new ItemStack(Items.BOWL));
        }
        return result;
    }

    /**
     * 按方块掉落表生成额外掉落物。
     * <p>
     * 掉落表为空时返回空列表；否则以方块位置、方块状态与玩家等参数
     * 构造掉落上下文并随机产出物品。
     *
     * @param level  服务端世界
     * @param pos    方块位置
     * @param state  方块状态
     * @param player 玩家
     * @return 掉落物列表
     */
    private List<ItemStack> rollDrops(ServerLevel level, BlockPos pos, BlockState state, Player player) {
        ResourceLocation lootTable = getLootTable();
        if (lootTable.equals(BuiltInLootTables.EMPTY)) {
            return List.of();
        }

        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, null)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .create(LootContextParamSets.BLOCK);

        LootTable table = level.getServer().getLootData().getLootTable(lootTable);
        if (table == LootTable.EMPTY) {
            return List.of();
        }
        return table.getRandomItems(params);
    }
}