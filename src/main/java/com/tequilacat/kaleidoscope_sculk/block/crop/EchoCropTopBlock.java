package com.tequilacat.kaleidoscope_sculk.block.crop;

import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * 回响作物（上半部分）。
 * <p>
 * 没有碰撞体积且不可见，只跟随下半部分存在；收获后让下半部分回退到第三生长阶段。
 * 若下方不存在回响作物，则放置或相邻方块变化时自动销毁。
 */
public class EchoCropTopBlock extends Block {

    /** 生长阶段属性。 */
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);

    /** 成熟阶段。 */
    private static final int MATURE_AGE = 7;

    /** 收获后下半部分回退到的阶段。 */
    private static final int HARVEST_RESET_AGE = 3;

    /** 选择形状。 */
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    /** 空碰撞形状。 */
    private static final VoxelShape EMPTY_SHAPE = Shapes.empty();

    /**
     * 构造回响作物上半部分。
     *
     * @param properties 方块属性
     */
    public EchoCropTopBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    /**
     * 获取选择形状。
     *
     * @param state   方块状态
     * @param level   方块获取器
     * @param pos     方块位置
     * @param context 碰撞上下文
     * @return 选择形状
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /**
     * 获取碰撞形状。
     *
     * @param state   方块状态
     * @param level   方块获取器
     * @param pos     方块位置
     * @param context 碰撞上下文
     * @return 空碰撞形状
     */
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                        CollisionContext context) {
        return EMPTY_SHAPE;
    }

    /**
     * 获取渲染形状。
     *
     * @param state 方块状态
     * @return 不可见渲染形状
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    /**
     * 获取创造模式中键拾取的物品。
     *
     * @param state  方块状态
     * @param target 命中结果
     * @param level  世界读取器
     * @param pos    方块位置
     * @param player 玩家
     * @return 幽匿菠萝物品
     */
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos,
                                       Player player) {
        return new ItemStack(KSItems.SCULK_PINAPPLE.get());
    }

    /**
     * 创建方块状态定义。
     *
     * @param builder 状态定义构建器
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    /**
     * 获取方块掉落物。
     * <p>
     * 仅在成熟时掉落幽匿菠萝，未成熟时无掉落。
     *
     * @param state   方块状态
     * @param builder 掉落参数构建器
     * @return 掉落物列表
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (state.getValue(AGE) >= MATURE_AGE) {
            return List.of(new ItemStack(KSItems.SCULK_PINAPPLE.get()));
        }
        return List.of();
    }

    /**
     * 玩家破坏方块时调用。
     * <p>
     * 成熟时让下方回响作物回退到第三生长阶段。
     *
     * @param level       世界
     * @param player      玩家
     * @param pos         方块位置
     * @param state       方块状态
     * @param blockEntity 方块实体
     * @param tool        使用的工具
     */
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide && state.getValue(AGE) >= MATURE_AGE) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            if (belowState.getBlock() instanceof EchoCropBlock) {
                level.setBlock(belowPos, belowState.setValue(EchoCropBlock.AGE, HARVEST_RESET_AGE), Block.UPDATE_ALL);
            }
        }
    }

    /**
     * 判断下方是否存在回响作物。
     *
     * @param level 方块获取器
     * @param pos   方块位置
     * @return 下方为回响作物时返回 true
     */
    private static boolean hasCropBelow(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() instanceof EchoCropBlock;
    }

    /**
     * 放置时检查下方作物。
     * <p>
     * 下方不存在回响作物时立即销毁自身。
     *
     * @param state    方块状态
     * @param level    世界
     * @param pos      方块位置
     * @param oldState 旧方块状态
     * @param isMoving 是否正在移动
     */
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!hasCropBelow(level, pos)) {
            level.destroyBlock(pos, false);
        }
    }

    /**
     * 相邻方块变化时检查下方作物。
     * <p>
     * 下方不存在回响作物时销毁自身。
     *
     * @param state         方块状态
     * @param level         世界
     * @param pos           方块位置
     * @param neighborBlock 相邻方块
     * @param neighborPos   相邻方块位置
     * @param isMoving      是否正在移动
     */
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                BlockPos neighborPos, boolean isMoving) {
        if (!hasCropBelow(level, pos)) {
            level.destroyBlock(pos, false);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
    }
}