package com.tequilacat.kaleidoscope_sculk.event;

import com.mojang.datafixers.util.Pair;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.util.DropHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * 灵魂帆旗帜事件类。
 * <p>
 * 玩家手持灵魂右键带骷髅图案的黑色旗帜时，将其转化为灵魂帆：
 * 移除原旗帜、在旗帜位置生成灵魂帆掉落物、播放音效并消耗灵魂，
 * 同时取消原交互。
 */
@Mod.EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class SoulSailBannerEvents {

    private SoulSailBannerEvents() {
    }

    /**
     * 处理右键方块事件。
     * <p>
     * 仅处理手持灵魂且目标为含骷髅图案的黑色旗帜的情况；
     * 服务端移除旗帜、生成灵魂帆掉落物、播放音效并消耗灵魂，
     * 最后取消交互并返回成功。
     *
     * @param event 右键方块事件
     */
    @SubscribeEvent
    public static void onBannerInteract(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(KSItems.SOUL.get())) {
            return;
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BannerBlock) || !isValidBanner(level, pos)) {
            return;
        }

        if (!level.isClientSide) {
            Player player = event.getEntity();
            level.removeBlock(pos, false);
            level.addFreshEntity(DropHelper.createDrop(level,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    new ItemStack(KSItems.SOUL_SAIL.get())));
            level.playSound(null, pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    /**
     * 判断指定位置的旗帜是否有效。
     * <p>
     * 要求为黑色旗帜，且方块实体图案或物品图案中含有骷髅图案。
     *
     * @param level 世界
     * @param pos   方块位置
     * @return 有效返回 true
     */
    private static boolean isValidBanner(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).is(Blocks.BLACK_BANNER)) {
            return false;
        }

        return level.getBlockEntity(pos) instanceof BannerBlockEntity banner
                && hasSkullPattern(banner.getPatterns());
    }

    /**
     * 判断旗帜图案层中是否含有骷髅图案。
     * <p>
     * 图案注册名路径中包含「skull」即视为骷髅图案。
     *
     * @param patterns 图案层
     * @return 含有骷髅图案返回 true
     */
    private static boolean hasSkullPattern(List<Pair<Holder<BannerPattern>, DyeColor>> patterns) {
        if (patterns == null) {
            return false;
        }

        for (Pair<Holder<BannerPattern>, DyeColor> layer : patterns) {
            ResourceLocation pattern = layer.getFirst().unwrapKey()
                    .map(ResourceKey::location)
                    .orElse(null);
            if (pattern != null && pattern.getPath().contains("skull")) {
                return true;
            }
        }
        return false;
    }
}
