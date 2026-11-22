package com.tequilacat.kaleidoscope_sculk.block.crop;

import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * 回响作物（下半部分）。
 * <p>
 * 只能种在幽匿催发体上，成熟后长出上半部分；破坏或收获时同步处理上半部分。
 * 成熟后可空手收获种子与幽匿菠萝并重置为幼苗，也可用骨粉催熟；
 * 失去下方催发体时整株一并销毁。
 */
public class EchoCropBlock extends CropBlock {

    /** 生长阶段属性。 */
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);

    /** 成熟阶段。 */
    private static final int MATURE_AGE = 7;

    /** 收获后重置的阶段。 */
    private static final int RESET_AGE = 0;

    /** 按阶段划分的碰撞箱。 */
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    };

    /**
     * 构造回响作物方块。
     *
     * @param properties 方块属性
     */
    public EchoCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    /**
     * 获取生长阶段属性。
     *
     * @return 生长阶段属性
     */
    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    /**
     * 获取最大生长阶段。
     *
     * @return 最大生长阶段
     */
    @Override
    public int getMaxAge() {
        return MATURE_AGE;
    }

    /**
     * 获取基础种子物品。
     *
     * @return 回响种子
     */
    @Override
    protected ItemLike getBaseSeedId() {
        return KSItems.ECHO_SEED.get();
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
     * 获取方块碰撞箱。
     * <p>
     * 按当前生长阶段返回对应高度的碰撞箱。
     *
     * @param state   方块状态
     * @param level   方块获取器
     * @param pos     方块位置
     * @param context 碰撞上下文
     * @return 碰撞箱
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    /**
     * 判断可否种植在指定方块上。
     *
     * @param state 下方方块状态
     * @param level 方块获取器
     * @param pos   方块位置
     * @return 下方为幽匿催发体时返回 true
     */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.SCULK_CATALYST);
    }

    /**
     * 判断下方是否为有效土壤。
     *
     * @param level 方块获取器
     * @param pos   方块位置
     * @return 下方为幽匿催发体时返回 true
     */
    private static boolean isOnValidSoil(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(Blocks.SCULK_CATALYST);
    }

    /**
     * 若上方存在上半部分则将其摧毁。
     *
     * @param level 世界
     * @param pos   下半部分位置
     */
    private static void destroyTopIfPresent(Level level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        if (level.getBlockState(abovePos).getBlock() instanceof EchoCropTopBlock) {
            level.destroyBlock(abovePos, false);
        }
    }

    /**
     * 判断方块能否存活。
     *
     * @param state 方块状态
     * @param level 世界读取器
     * @param pos   方块位置
     * @return 下方为有效土壤时返回 true
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return isOnValidSoil(level, pos);
    }

    /**
     * 相邻方块变化时检查土壤。
     * <p>
     * 失去有效土壤时销毁整株作物。
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
        if (!isOnValidSoil(level, pos)) {
            destroyPlant(level, pos, state);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
    }

    /**
     * 随机刻生长。
     * <p>
     * 失去有效土壤时销毁整株；未成熟时按生长速度概率提升阶段，
     * 达到成熟阶段后长出上半部分。
     *
     * @param state  方块状态
     * @param level  服务端世界
     * @param pos    方块位置
     * @param random 随机源
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!isOnValidSoil(level, pos)) {
            destroyPlant(level, pos, state);
            return;
        }

        int currentAge = state.getValue(AGE);
        if (currentAge < MATURE_AGE) {
            float growthChance = getGrowthSpeed(state.getBlock(), level, pos);
            if (random.nextInt((int) (25.0F / growthChance) + 1) == 0) {
                int newAge = currentAge + 1;
                level.setBlock(pos, state.setValue(AGE, newAge), 2);

                if (newAge >= MATURE_AGE) {
                    createTopBlock(level, pos);
                }
            }
        }
    }

    /**
     * 在方块上方生成成熟的作物上半部分。
     *
     * @param level 服务端世界
     * @param pos   下半部分位置
     */
    private void createTopBlock(ServerLevel level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        if (level.isEmptyBlock(abovePos)) {
            level.setBlock(abovePos, KSBlocks.ECHO_CROP_TOP.get().defaultBlockState()
                    .setValue(EchoCropTopBlock.AGE, MATURE_AGE), Block.UPDATE_ALL);
        }
    }

    /**
     * 销毁整株作物。
     *
     * @param level 世界
     * @param pos   下半部分位置
     * @param state 方块状态
     */
    private void destroyPlant(Level level, BlockPos pos, BlockState state) {
        destroyTopIfPresent(level, pos);
        level.destroyBlock(pos, true);
    }

    /**
     * 处理玩家对物品使用方块。
     * <p>
     * 未成熟时使用骨粉可将阶段随机提升 1~3 级，达到成熟后长出上半部分；
     * 成熟时不再接受骨粉。
     *
     * @param state     方块状态
     * @param level     世界
     * @param pos       方块位置
     * @param player    玩家
     * @param hand      交互手
     * @param hit 命中结果
     * @return 物品交互结果
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);

        // 手持骨粉 → 催熟（成熟后不再接受骨粉，PASS 让 super 处理）
        if (!stack.isEmpty() && stack.is(Items.BONE_MEAL)) {
            int currentAge = state.getValue(AGE);
            if (currentAge >= MATURE_AGE) {
                return InteractionResult.PASS;
            }

            if (!level.isClientSide) {
                ServerLevel serverLevel = (ServerLevel) level;
                RandomSource random = serverLevel.getRandom();

                int newAge = Math.min(currentAge + random.nextInt(3) + 1, MATURE_AGE);
                serverLevel.setBlock(pos, state.setValue(AGE, newAge), Block.UPDATE_ALL);
                serverLevel.levelEvent(2005, pos, 0);

                if (newAge >= MATURE_AGE) {
                    createTopBlock(serverLevel, pos);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // 空手（或其他物品）→ 采摘
        int age = state.getValue(AGE);
        if (age < MATURE_AGE) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            int seedCount = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(KSItems.ECHO_SEED.get(), seedCount));
            popResource(level, pos, new ItemStack(KSItems.SCULK_PINAPPLE.get()));

            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
                    1.0F, 0.8F + level.random.nextFloat() * 0.4F);

            destroyTopIfPresent(level, pos);
            level.setBlock(pos, state.setValue(AGE, RESET_AGE), Block.UPDATE_ALL);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * 获取方块掉落物。
     * <p>
     * 仅在成熟时掉落种子与幽匿菠萝，未成熟时无掉落。
     *
     * @param state   方块状态
     * @param builder 掉落参数构建器
     * @return 掉落物列表
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (state.getValue(AGE) >= MATURE_AGE) {
            return matureDrops(builder.getLevel());
        }
        return List.of();
    }

    /**
     * 玩家破坏方块时调用。
     * <p>
     * 在默认破坏处理后同步摧毁上方上半部分。
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
        destroyTopIfPresent(level, pos);
    }

    /**
     * 方块被移除时调用。
     * <p>
     * 若替换后的方块不是自身，则摧毁上方上半部分。
     *
     * @param state    方块状态
     * @param level    世界
     * @param pos      方块位置
     * @param newState 新方块状态
     * @param isMoving 是否正在移动
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(this)) {
            destroyTopIfPresent(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    /**
     * 生成成熟作物的掉落物。
     *
     * @param level 世界
     * @return 种子与幽匿菠萝的掉落物列表
     */
    private static List<ItemStack> matureDrops(Level level) {
        int seedCount = 1 + level.random.nextInt(2);
        return List.of(
                new ItemStack(KSItems.ECHO_SEED.get(), seedCount),
                new ItemStack(KSItems.SCULK_PINAPPLE.get())
        );
    }
}