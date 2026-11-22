package com.tequilacat.kaleidoscope_sculk.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.BaseCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.ChiliCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.TeaTreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SickleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 作物的镰刀兼容注入。
 * <p>
 * 手持镰刀对作物右键时，把返回值改为转交默认方块交互，
 * 使本模组的镰刀也能对这些作物执行收割逻辑。
 */
@Mixin({BaseCropBlock.class, ChiliCropBlock.class, TeaTreeBlock.class})
public abstract class CookeryCropSickleMixin {

    /**
     * 在作物处理右键之前放行镰刀。
     * <p>
     * 手持镰刀时以 PASS 作为返回值并取消原方法，
     * 任何镰刀子类都会命中；require = 0 表示目标方法不存在时不报错。
     * 方法名带模组 ID 前缀，避免与其它注入冲突。
     *
     * @param state     方块状态
     * @param level     所在世界
     * @param pos       方块位置
     * @param player    玩家
     * @param hand      使用手
     * @param hitResult 命中结果
     * @param cir       回调
     */
    @Inject(method = "use", at = @At("HEAD"), cancellable = true, require = 0)
    private void kaleidoscope_sculk$letAnySickleHarvest(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult,
                                                        CallbackInfoReturnable<InteractionResult> cir) {
        if (player.getItemInHand(hand).getItem() instanceof SickleItem) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
