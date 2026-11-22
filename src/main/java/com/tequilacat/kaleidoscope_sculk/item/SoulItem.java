package com.tequilacat.kaleidoscope_sculk.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 灵魂。
 * <p>
 * 对方块使用时注入灵魂：沙子变成灵魂沙，泥土类方块变成灵魂土，
 * 火焰变成灵魂火，其余方块不响应。转化成功消耗一个物品，创造模式除外。
 */
public class SoulItem extends Item {

    /**
     * 构造灵魂物品。
     *
     * @param properties 物品属性
     */
    public SoulItem(Properties properties) {
        super(properties);
    }

    /**
     * 对方块使用时尝试注入灵魂。
     * <p>
     * 按目标方块选择转化结果，无法转化时返回 PASS；
     * 服务端替换方块、播放音效并消耗物品，客户端只返回成功以便播放动画。
     *
     * @param context 使用上下文
     * @return 交互结果
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockState newState = null;
        if (state.is(Blocks.SAND)) {
            newState = Blocks.SOUL_SAND.defaultBlockState();
        } else if (state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.ROOTED_DIRT)) {
            newState = Blocks.SOUL_SOIL.defaultBlockState();
        } else if (state.is(Blocks.FIRE)) {
            newState = Blocks.SOUL_FIRE.defaultBlockState();
        }
        if (newState == null) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(pos, newState, Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }
}