package com.tequilacat.kaleidoscope_sculk.block.cake;

import com.tequilacat.kaleidoscope_sculk.init.KSDamageTypes;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 深板岩蛋糕。
 * <p>
 * 一种可多次分食的蛋糕方块，共可咬 4 口：空手右键直接吃下并恢复饥饿值，
 * 手持物品右键则额外造成一次伤害并给予生命恢复效果；
 * 手持对应蛋糕片可放回一口。吃掉最后一口后方块被移除，
 * 破坏时按剩余可吃次数掉落蛋糕片，比较器输出剩余口数。
 */
public class DeepslateCakeBlock extends Block {

    /** 方块碰撞箱。 */
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);

    /** 已咬口数属性。 */
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 4);

    /** 最多可咬的口数。 */
    public static final int MAX_BITES = 4;
    /** 每口恢复的饥饿值。 */
    private static final int NUTRITION = 4;
    /** 每口恢复的饱和度。 */
    private static final float SATURATION = 0.2F;
    /** 手持物品进食时造成的伤害。 */
    private static final float BITE_DAMAGE = 2.0F;

    /**
     * 构造深板岩蛋糕方块。
     *
     * @param properties 方块属性
     */
    public DeepslateCakeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
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
     * 创建方块状态定义。
     *
     * @param builder 状态定义构建器
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    /**
     * 获取放置时的方块状态。
     * <p>
     * 下方需要可支撑的面，否则无法放置。
     *
     * @param context 放置上下文
     * @return 放置后的方块状态，无法放置时返回 null
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (!level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
            return null;
        }
        return this.defaultBlockState().setValue(BITES, 0);
    }

    /**
     * 处理玩家对物品使用方块。
     * <p>
     * 手持蛋糕片时放回一口（已咬过且未吃光才有效）；
     * 否则走手持物品进食逻辑。
     *
     * @param stack     手持物品堆叠
     * @param state     方块状态
     * @param level     世界
     * @param pos       方块位置
     * @param player    玩家
     * @param hand      交互手
     * @param hitResult 命中结果
     * @return 物品交互结果
     */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        // 手持蛋糕片可以放回一口。
        if (stack.is(KSItems.DEEPSLATE_CAKE_SLICE.get())) {
            int currentBites = state.getValue(BITES);
            if (currentBites <= 0 || currentBites >= MAX_BITES) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(BITES, Math.max(0, currentBites - 1)), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        return this.eatWithItem(state, level, pos, player);
    }

    /**
     * 处理空手使用方块。
     * <p>
     * 直接吃下一口。
     *
     * @param state     方块状态
     * @param level     世界
     * @param pos       方块位置
     * @param player    玩家
     * @param hitResult 命中结果
     * @return 交互结果
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        return this.eat(state, level, pos, player);
    }

    /**
     * 空手吃下一口。
     * <p>
     * 已吃光或玩家无法进食时跳过；服务端恢复饥饿值、播放进食音效并推进咬数。
     *
     * @param state  方块状态
     * @param level  世界
     * @param pos    方块位置
     * @param player 玩家
     * @return 交互结果
     */
    private InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player) {
        int currentBites = state.getValue(BITES);
        if (currentBites >= MAX_BITES || !player.canEat(true)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        player.getFoodData().eat(NUTRITION, SATURATION);
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS,
                1.0F, level.random.nextFloat() * 0.1F + 0.9F);
        this.advanceBite(state, level, pos, player, currentBites);
        return InteractionResult.SUCCESS;
    }

    /**
     * 手持物品吃下一口。
     * <p>
     * 已吃光或玩家无法进食时跳过；服务端恢复饥饿值、造成伤害、
     * 施加生命恢复效果并推进咬数。
     *
     * @param state  方块状态
     * @param level  世界
     * @param pos    方块位置
     * @param player 玩家
     * @return 物品交互结果
     */
    private ItemInteractionResult eatWithItem(BlockState state, Level level, BlockPos pos, Player player) {
        int currentBites = state.getValue(BITES);
        if (currentBites >= MAX_BITES || !player.canEat(true)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        player.getFoodData().eat(NUTRITION, SATURATION);
        this.hurtOnBite(level, player);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0));

        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS,
                1.0F, level.random.nextFloat() * 0.1F + 0.9F);
        this.advanceBite(state, level, pos, player, currentBites);
        return ItemInteractionResult.SUCCESS;
    }

    /**
     * 推进咬数。
     * <p>
     * 达到最大咬数时移除方块，否则更新方块状态。
     *
     * @param state        方块状态
     * @param level        世界
     * @param pos          方块位置
     * @param player       玩家
     * @param currentBites 当前咬数
     */
    private void advanceBite(BlockState state, Level level, BlockPos pos, Player player, int currentBites) {
        int newBites = currentBites + 1;
        if (newBites >= MAX_BITES) {
            level.destroyBlock(pos, false, player);
        } else {
            level.setBlock(pos, state.setValue(BITES, newBites), Block.UPDATE_ALL);
        }
    }

    /**
     * 手持物品进食时对玩家造成伤害。
     * <p>
     * 若伤害会致死则使用自定义伤害类型使其死亡，否则直接扣减生命并播放受伤音效。
     *
     * @param level  世界
     * @param player 玩家
     */
    private void hurtOnBite(Level level, Player player) {
        float newHealth = player.getHealth() - BITE_DAMAGE;
        if (newHealth <= 0.0F) {
            DamageSource source = new DamageSource(
                    level.registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(KSDamageTypes.DEEPSLATE_CAKE_SLICE),
                    player
            );
            player.hurt(source, BITE_DAMAGE);
        } else {
            player.setHealth(newHealth);
            player.playSound(SoundEvents.PLAYER_HURT, 1.0F, 1.0F);
        }
    }

    /**
     * 获取方块掉落物。
     * <p>
     * 按剩余可吃次数掉落对应数量的深板岩蛋糕片。
     *
     * @param state   方块状态
     * @param builder 掉落参数构建器
     * @return 掉落物列表
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        int remaining = MAX_BITES - state.getValue(BITES);
        if (remaining > 0) {
            drops.add(new ItemStack(KSItems.DEEPSLATE_CAKE_SLICE.get(), remaining));
        }
        return drops;
    }

    /**
     * 判断是否具有比较器输出。
     *
     * @param state 方块状态
     * @return 恒为 true
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /**
     * 获取比较器输出信号。
     * <p>
     * 输出剩余可吃次数。
     *
     * @param state 方块状态
     * @param level 世界
     * @param pos   方块位置
     * @return 比较器信号强度
     */
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return MAX_BITES - state.getValue(BITES);
    }
}
