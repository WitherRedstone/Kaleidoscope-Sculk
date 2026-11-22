package com.tequilacat.kaleidoscope_sculk.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.SickleHarvestEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.TeaTreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEnchantments;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SickleItem;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

/**
 * 幽匿骨镰。
 * <p>
 * 继承外部模组的镰刀物品，以点击位置为中心进行范围收割，
 * 收割半径随横扫附魔等级扩大，耐久消耗与冷却随等级提高。
 */
public class SculkBoneSickleItem extends SickleItem {

    /** 镰刀材质：石质挖掘等级，耐久 3000，修复材料为远古骨碎片。 */
    private static final Tier SCULK_BONE_SICKLE_TIER = new ForgeTier(
            1, 3000, 5.0F, 0.0F, 12,
            BlockTags.NEEDS_IRON_TOOL,
            () -> Ingredient.of(KSItems.ANCIENT_BONE_FRAGMENT.get())
    );

    /** 基础收割半径（水平方向）。 */
    private static final int HARVEST_RADIUS_XZ = 4;
    /** 收割高度（点击位置向上的格数）。 */
    private static final int HARVEST_HEIGHT = 3;
    /** 横扫附魔的生效上限。 */
    private static final int MAX_SWEEP_LEVEL = 3;

    /**
     * 构造幽匿骨镰。
     * <p>
     * 耐久 3000，攻击属性按剑的公式以镰刀材质计算：
     * 基础伤害加成 6、攻击速度修正 -1.6。
     */
    public SculkBoneSickleItem() {
        super(SCULK_BONE_SICKLE_TIER, 6, -1.6F, new Item.Properties().durability(3000));
    }

    /**
     * 对方块使用时进行范围收割。
     * <p>
     * 以点击位置为中心，按收割半径与高度逐格尝试收割；收割完成后播放横扫音效
     * 并触发横扫动作，按横扫等级消耗耐久并进入冷却。客户端直接返回成功以便播放动画。
     *
     * @param context 使用上下文
     * @return 交互结果
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return super.useOn(context);
        }

        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clickedPos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        int sweepLevel = Math.min(this.sweepLevel(level, stack), MAX_SWEEP_LEVEL);
        int radiusXZ = HARVEST_RADIUS_XZ + sweepLevel;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int x = -radiusXZ; x <= radiusXZ; x++) {
            for (int y = 0; y <= HARVEST_HEIGHT; y++) {
                for (int z = -radiusXZ; z <= radiusXZ; z++) {
                    mutablePos.set(clickedPos.getX() + x, clickedPos.getY() + y, clickedPos.getZ() + z);
                    this.harvest(level, player, stack, mutablePos);
                }
            }
        }

        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
        player.sweepAttack();
        stack.hurtAndBreak(durabilityCost(sweepLevel), player, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        player.getCooldowns().addCooldown(this, 7);
        return InteractionResult.SUCCESS;
    }

    /**
     * 获取本次收割的耐久消耗。
     *
     * @param sweepLevel 横扫等级
     * @return 消耗的耐久点数
     */
    private static int durabilityCost(int sweepLevel) {
        return switch (sweepLevel) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 8;
            default -> 1;
        };
    }

    /**
     * 读取物品上的横扫附魔等级。
     *
     * @param level 所在世界
     * @param stack 手持物品
     * @return 横扫附魔等级，无附魔时为 0
     */
    private int sweepLevel(Level level, ItemStack stack) {
        return stack.getEnchantmentLevel(ModEnchantments.SWEEP.get());
    }

    /**
     * 尝试收割单个方块。
     * <p>
     * 空气、不可交互或命中黑名单的方块直接跳过；收割前先发出镰刀收割事件，
     * 事件被取消时按事件结果决定是否消耗耐久。作物收割后重置为最小年龄并保留含水状态。
     *
     * @param level  所在世界
     * @param player 玩家
     * @param stack  手持物品
     * @param pos    目标方块
     * @return 是否收割成功
     */
    private boolean harvest(Level level, Player player, ItemStack stack, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.isAir() || !level.mayInteract(player, pos)
                || blockState.is(TagMod.SICKLE_HARVEST_BLACKLIST)) {
            return false;
        }

        Block block = blockState.getBlock();
        BlockPos targetPos = pos.immutable();

        SickleHarvestEvent event = new SickleHarvestEvent(player, stack, targetPos, blockState);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return event.isCostDurability();
        }

        if (block instanceof TeaTreeBlock teaTreeBlock) {
            if (!teaTreeBlock.isMaxAge(blockState)) {
                return false;
            }
            teaTreeBlock.playerDestroy(level, player, targetPos, blockState, null, ItemStack.EMPTY);
            level.setBlock(targetPos, teaTreeBlock.getStateForAge(0), Block.UPDATE_ALL);
            level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, targetPos, Block.getId(blockState));
            return true;
        }

        if (block instanceof CropBlock cropBlock) {
            if (block instanceof RiceCropBlock) {
                int offset = blockState.getValue(RiceCropBlock.LOCATION);
                targetPos = targetPos.below(offset);
                blockState = level.getBlockState(targetPos);
            }
            if (!cropBlock.isMaxAge(blockState)) {
                return false;
            }
            cropBlock.playerDestroy(level, player, targetPos, blockState, null, ItemStack.EMPTY);
            BlockState stateForAge = cropBlock.getStateForAge(0);
            BooleanProperty waterlogged = BlockStateProperties.WATERLOGGED;
            if (stateForAge.hasProperty(waterlogged)) {
                stateForAge = stateForAge.setValue(waterlogged, blockState.getValue(waterlogged));
            }
            level.setBlock(targetPos, stateForAge, Block.UPDATE_ALL);
            level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, targetPos, Block.getId(blockState));
            return true;
        }

        if (block instanceof BushBlock) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.gameMode.destroyBlock(targetPos);
                level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, targetPos, Block.getId(blockState));
                return true;
            }
        }

        return false;
    }

    /**
     * 追加提示文本。
     * <p>
     * 显示工具提示中的说明行。
     *
     * @param stack   手持物品
     * @param tooltip 提示列表
     * @param flag    提示标志
     */
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_sculk.sculk_bone_sickle")
                .withStyle(ChatFormatting.GRAY));
    }
}
