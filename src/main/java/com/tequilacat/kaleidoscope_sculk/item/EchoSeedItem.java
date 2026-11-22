package com.tequilacat.kaleidoscope_sculk.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 回响种子。
 * <p>
 * 只能种在幽匿催发体顶部，种下后在其上方生成对应作物方块，
 * 创造模式下不消耗种子。
 */
public class EchoSeedItem extends Item {

    /** 种下的作物方块。 */
    private final Block cropBlock;

    /**
     * 构造回响种子物品。
     *
     * @param cropBlock  种下的作物方块
     * @param properties 物品属性
     */
    public EchoSeedItem(Block cropBlock, Properties properties) {
        super(properties);
        this.cropBlock = cropBlock;
    }

    /**
     * 对方块使用时尝试种植。
     * <p>
     * 点击位置上方的方块可被替换、且下方是幽匿催发体时才能种植；
     * 服务端放置作物并消耗种子，客户端只返回成功以便播放动画。
     *
     * @param context 使用上下文
     * @return 交互结果
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos abovePos = clickedPos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        BlockState belowState = level.getBlockState(clickedPos);
        if (!aboveState.canBeReplaced() || !belowState.is(Blocks.SCULK_CATALYST)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(abovePos, this.cropBlock.defaultBlockState(), Block.UPDATE_ALL);
            if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
