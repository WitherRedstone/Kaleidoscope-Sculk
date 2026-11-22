package com.tequilacat.kaleidoscope_sculk.mixin;

import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 监守者振动感知的回声隐身注入。
 * <p>
 * 携带回声效果（ECHO）的实体不会触发监守者的振动感知，
 * 走动、跑跳、破坏方块都不会引来监守者。
 */
@Mixin(targets = "net.minecraft.world.entity.monster.warden.Warden$VibrationUser")
public class WardenVibrationUserMixin {

    /**
     * 在监守者判断能否接收振动之前过滤回声携带者。
     * <p>
     * 振动来源携带回声效果时直接返回 false 并取消原方法。
     * 监守者的振动用户是私有内部接口，无法直接 @Mixin 类名，
     * 因此通过 targets 以全限定名指向它。
     *
     * @param level     所在世界
     * @param pos       振动位置
     * @param gameEvent 振动事件
     * @param context   振动上下文
     * @param cir       回调
     */
    @Inject(method = "canReceiveVibration", at = @At("HEAD"), cancellable = true)
    private void kaleidoscope_sculk$ignoreEchoVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context,
                                                        CallbackInfoReturnable<Boolean> cir) {
        if (context.sourceEntity() instanceof LivingEntity living && living.hasEffect(KSEffects.ECHO)) {
            cir.setReturnValue(false);
        }
    }
}
