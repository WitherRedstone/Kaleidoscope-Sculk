package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.util.DropHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 灵魂火事件类。
 * <p>
 * 玩家破坏灵魂火时按小概率掉落一个灵魂物品，
 * 掉落物在方块中心位置生成。
 */
@Mod.EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class SoulFireEvents {

    /** 破坏灵魂火掉落灵魂的概率。 */
    private static final float SOUL_DROP_CHANCE = 0.03F;

    private SoulFireEvents() {
    }

    /**
     * 处理方块破坏事件。
     * <p>
     * 仅处理服务端破坏灵魂火的情况；按概率在方块中心生成灵魂掉落物。
     *
     * @param event 方块破坏事件
     */
    @SubscribeEvent
    public static void onSoulFireBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide() || !event.getState().is(Blocks.SOUL_FIRE)) {
            return;
        }

        Level level = (Level) event.getLevel();
        if (level.getRandom().nextFloat() < SOUL_DROP_CHANCE) {
            level.addFreshEntity(DropHelper.createDrop(level,
                    event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5,
                    new ItemStack(KSItems.SOUL.get())));
        }
    }
}
