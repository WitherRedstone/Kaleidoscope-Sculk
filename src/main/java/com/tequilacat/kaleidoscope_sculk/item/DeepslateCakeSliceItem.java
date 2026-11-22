package com.tequilacat.kaleidoscope_sculk.item;

import com.tequilacat.kaleidoscope_sculk.block.cake.DeepslateCakeBlock;
import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import com.tequilacat.kaleidoscope_sculk.init.KSDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 深板岩蛋糕片。
 * <p>
 * 始终可食用的食物物品：进食后获得生命恢复与抗性提升效果，
 * 但每次食用都会扣除 2 点生命，可能致死。对可支撑的空位使用时，
 * 可放置一块已咬过一口的深板岩蛋糕。
 */
public class DeepslateCakeSliceItem extends Item {

    /** 每次食用造成的伤害。 */
    private static final float BITE_DAMAGE = 2.0F;
    /** 进食耗时（刻）。 */
    private static final int USE_DURATION = 32;

    /**
     * 构造深板岩蛋糕片物品。
     * <p>
     * 食物属性为始终可食用、恢复 4 点饥饿值与 1.4 饱和度，
     * 并附带生命恢复与抗性提升效果。
     *
     * @param properties 物品属性
     */
    public DeepslateCakeSliceItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(4)
                .saturationMod(1.4F)
                .alwaysEat()
                .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 1.0F)
                .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 2), 1.0F)
                .build()));
    }



    /**
     * 获取进食音效。
     *
     * @return 深板岩破坏音效
     */
    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.DEEPSLATE_BREAK;
    }

    /**
     * 对方块使用物品。
     * <p>
     * 目标已是深板岩蛋糕时不处理；否则在点击面相邻的可替换且下方可支撑的位置
     * 放置一块已咬过一口的深板岩蛋糕，服务端消耗一个物品并播放放置音效。
     *
     * @param context 使用上下文
     * @return 交互结果
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        if (level.getBlockState(clickedPos).getBlock() instanceof DeepslateCakeBlock) {
            return InteractionResult.PASS;
        }

        BlockPos placePos = clickedPos.relative(context.getClickedFace());
        BlockState placeState = level.getBlockState(placePos);
        if (!placeState.canBeReplaced()
                || !level.getBlockState(placePos.below()).isFaceSturdy(level, placePos.below(), Direction.UP)) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        if (!level.isClientSide) {
            BlockState cakeState = KSBlocks.DEEPSLATE_CAKE.get()
                    .defaultBlockState()
                    .setValue(DeepslateCakeBlock.BITES, 3);
            level.setBlock(placePos, cakeState, Block.UPDATE_ALL);

            if (player != null && !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
            level.playSound(null, placePos, SoundEvents.DEEPSLATE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 使用物品进食。
     * <p>
     * 玩家无法进食时失败；否则开始进食。
     *
     * @param level  世界
     * @param player 玩家
     * @param hand   交互手
     * @return 交互结果
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.canEat(true)) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    /**
     * 完成进食。
     * <p>
     * 先执行默认进食逻辑；服务端对玩家额外造成 2 点伤害（会致死时使用
     * 自定义伤害类型），否则直接扣减生命并播放受伤音效，最后播放深板岩破碎音效。
     *
     * @param stack  物品堆叠
     * @param level  世界
     * @param living 使用实体
     * @return 使用后的物品堆叠
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        ItemStack result = super.finishUsingItem(stack, level, living);

        if (!level.isClientSide && living instanceof Player player) {
            float newHealth = player.getHealth() - BITE_DAMAGE;
            if (newHealth <= 0.0F) {
                Holder<DamageType> holder = level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(KSDamageTypes.DEEPSLATE_CAKE_SLICE);
                player.hurt(new DamageSource(holder, player), BITE_DAMAGE);
            } else {
                player.setHealth(newHealth);
                player.playSound(SoundEvents.PLAYER_HURT, 1.0F, 1.0F);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.DEEPSLATE_BREAK, SoundSource.PLAYERS, 1.0F, 1.5F);
        }
        return result;
    }

    /**
     * 获取进食耗时。
     *
     * @param stack 物品堆叠
     * @return 进食耗时（刻）
     */
    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    /**
     * 追加物品提示文本。
     * <p>
     * 添加用法说明（灰色）与食用伤害提示（红色）。
     *
     * @param stack   物品堆叠
     * @param level   世界，可能为 null
     * @param tooltip 提示组件列表
     * @param flag    提示标志
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.kaleidoscope_sculk.deepslate_cake_slice.tooltip")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.kaleidoscope_sculk.deepslate_cake_slice.damage")
                .withStyle(ChatFormatting.RED));
    }
}
