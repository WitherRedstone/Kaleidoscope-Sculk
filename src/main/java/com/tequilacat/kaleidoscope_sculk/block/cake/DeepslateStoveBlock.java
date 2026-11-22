package com.tequilacat.kaleidoscope_sculk.block.cake;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * 深板岩灶台。
 * <p>
 * 带水平朝向与点燃状态的灶台方块：点燃时发光并产生火焰、烟雾粒子与噼啪音效，
 * 可用打火石或火焰弹点燃。上方淋雨或上方出现水源时会被浇灭。
 */
public class DeepslateStoveBlock extends HorizontalDirectionalBlock {

    /** 点燃状态属性。 */
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    /** 方块碰撞箱。 */
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    /**
     * 使用给定属性构造深板岩灶台。
     *
     * @param properties 方块属性
     */
    public DeepslateStoveBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false));
    }

    /**
     * 以默认属性构造深板岩灶台。
     * <p>
     * 深板岩材质，需要正确工具采集，点燃时发出亮度 13 的光，
     * 并启用随机刻以处理淋雨浇灭。
     */
    public DeepslateStoveBlock() {
        this(Properties.of()
                .mapColor(MapColor.DEEPSLATE)
                .sound(SoundType.DEEPSLATE)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> state.getValue(LIT) ? 13 : 0)
                .randomTicks()
                .strength(1.5F, 6.0F)
                .noOcclusion());
    }

    /**
     * 获取方块碰撞箱。
     *
     * @param state   方块状态
     * @param level   方块获取器
     * @param pos     方块位置
     * @param context 碰撞上下文
     * @return 碰撞箱
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /**
     * 每刻的客户端动画。
     * <p>
     * 仅点燃时生效：以一定概率播放噼啪音效，持续生成烟雾粒子，
     * 并朝朝向方向生成灵魂火焰粒子。
     *
     * @param state  方块状态
     * @param level  世界
     * @param pos    方块位置
     * @param random 随机源
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        if (random.nextInt(10) == 0) {
            level.playLocalSound(x, y, z, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
                    0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
        }

        level.addParticle(ParticleTypes.SMOKE,
                x + (random.nextDouble() - 0.5) * 0.6,
                pos.getY() + 0.6 + random.nextDouble() * 0.3,
                z + (random.nextDouble() - 0.5) * 0.6,
                0.0, 0.02, 0.0);

        Direction direction = state.getValue(FACING);
        double xOffset = direction.getAxis() == Direction.Axis.X
                ? direction.getStepX() * 0.5 : (random.nextDouble() - 0.5) * 0.8;
        double zOffset = direction.getAxis() == Direction.Axis.Z
                ? direction.getStepZ() * 0.5 : (random.nextDouble() - 0.5) * 0.8;

        level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                pos.getX() + 0.5 + xOffset,
                pos.getY() + 0.6,
                pos.getZ() + 0.5 + zOffset,
                0.0, 0.0, 0.0);
    }

    /**
     * 随机刻更新。
     * <p>
     * 点燃状态下上方淋雨时熄灭并播放灭火音效。
     *
     * @param state  方块状态
     * @param level  服务端世界
     * @param pos    方块位置
     * @param random 随机源
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT) && level.isRainingAt(pos.above())) {
            level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F);
        }
    }

    /**
     * 更新方块状态。
     * <p>
     * 点燃状态下上方出现水源时熄灭并播放灭火音效。
     *
     * @param state         方块状态
     * @param direction     变化方向
     * @param neighborState 相邻方块状态
     * @param level         世界访问器
     * @param pos           方块位置
     * @param neighborPos   相邻方块位置
     * @return 更新后的方块状态
     */
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(LIT) && direction == Direction.UP && level.isWaterAt(neighborPos)
                && level instanceof ServerLevel serverLevel) {
            serverLevel.setBlockAndUpdate(pos, state.setValue(LIT, false));
            serverLevel.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    /**
     * 处理玩家对物品使用方块。
     * <p>
     * 未点燃时可用打火石或火焰弹点燃：火焰弹消耗物品，打火石消耗耐久；
     * 随后播放对应音效并返回成功。
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
        if (!state.getValue(LIT) && (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE))) {
            level.setBlockAndUpdate(pos, state.setValue(LIT, true));

            if (stack.is(Items.FIRE_CHARGE)) {
                level.playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS,
                        1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            } else {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS,
                        1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(
                        hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    /**
     * 放置时朝向玩家所在的水平方向。
     *
     * @param context 放置上下文
     * @return 初始方块状态
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * 创建方块状态定义。
     *
     * @param builder 状态定义构建器
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
    }
}