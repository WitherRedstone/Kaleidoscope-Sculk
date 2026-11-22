package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItemTags;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.util.DropHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 幽匿方块采集事件。
 * <p>
 * 手持厨刀右键幽匿方块或幽匿脉络时，掉落幽匿菌与幽匿枝，
 * 幽匿脉络还会概率掉落幽匿幼虫，随后消耗刀具耐久并移除方块。
 */
@Mod.EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class SculkInteractionEvents {

    /** 幽匿枝的掉落概率。 */
    private static final float SCULK_BRANCH_DROP_CHANCE = 0.1F;
    /** 幽匿脉络额外掉落幽匿幼虫的概率。 */
    private static final float SCULK_CATERPILLAR_DROP_CHANCE = 0.05F;

    /**
     * 工具类，禁止实例化。
     */
    private SculkInteractionEvents() {
    }

    /**
     * 处理玩家对幽匿方块的右键采集。
     * <p>
     * 仅厨刀标签内的物品可触发，潜行时不响应。
     * 幽匿方块掉落 1 至 3 株幽匿菌，幽匿脉络固定 1 株；
     * 两类方块均按概率掉落幽匿枝，幽匿脉络再按概率掉落幽匿幼虫。
     * 掉落与耐久消耗只在服务端执行，客户端仅标记交互成功并取消事件。
     *
     * @param event 右键方块事件
     */
    @SubscribeEvent
    public static void onSculkInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack handItem = event.getItemStack();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (!handItem.is(KSItemTags.KITCHEN_KNIVES) || player.isShiftKeyDown()) {
            return;
        }

        boolean isSculk = state.is(Blocks.SCULK);
        boolean isSculkVein = state.is(Blocks.SCULK_VEIN);
        if (!isSculk && !isSculkVein) {
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            int fungusCount = isSculkVein ? 1 : 1 + serverLevel.random.nextInt(3);
            for (int i = 0; i < fungusCount; i++) {
                serverLevel.addFreshEntity(DropHelper.createDrop(serverLevel,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        KSItems.SCULK_FUNGUS.get().getDefaultInstance()));
            }

            if (serverLevel.random.nextFloat() < SCULK_BRANCH_DROP_CHANCE) {
                serverLevel.addFreshEntity(DropHelper.createDrop(serverLevel,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        KSItems.SCULK_BRANCH.get().getDefaultInstance()));
            }

            if (isSculkVein && serverLevel.random.nextFloat() < SCULK_CATERPILLAR_DROP_CHANCE) {
                serverLevel.addFreshEntity(DropHelper.createDrop(serverLevel,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        KSItems.SCULK_CATERPILLAR.get().getDefaultInstance()));
            }

            serverLevel.playSound(null, pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                EquipmentSlot slot = event.getHand() == InteractionHand.MAIN_HAND
                        ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                handItem.hurtAndBreak(2, player, p -> p.broadcastBreakEvent(slot));
            }

            serverLevel.destroyBlock(pos, false);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
