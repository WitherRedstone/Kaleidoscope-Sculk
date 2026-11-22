package com.tequilacat.kaleidoscope_sculk.item;

import com.tequilacat.kaleidoscope_sculk.entity.AncientBoneFragmentProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 远古骨片。
 * <p>
 * 使用时向玩家视线方向投掷远古骨片弹射物，并播放投掷音效；
 * 非创造模式下消耗一个物品。
 */
public class AncientBoneFragmentItem extends Item {

    /**
     * 构造远古骨片物品。
     *
     * @param properties 物品属性
     */
    public AncientBoneFragmentItem(Properties properties) {
        super(properties);
    }

    /**
     * 使用物品投掷骨片。
     * <p>
     * 服务端生成骨片弹射物并沿视线方向发射；随后播放投掷音效，
     * 非创造模式消耗一个物品。
     *
     * @param level  世界
     * @param player 玩家
     * @param hand   交互手
     * @return 交互结果
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            AncientBoneFragmentProjectile projectile = new AncientBoneFragmentProjectile(level, player);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);
        }

        player.playSound(SoundEvents.SNOWBALL_THROW, 0.5F, 0.8F);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
