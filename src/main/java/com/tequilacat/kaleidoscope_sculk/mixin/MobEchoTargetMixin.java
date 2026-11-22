package com.tequilacat.kaleidoscope_sculk.mixin;

import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 怪物索敌的回声隐身注入。
 * <p>
 * 携带回声效果（ECHO）的实体对怪物不可见，怪物不会将其选为攻击目标。
 */
@Mixin(Mob.class)
public class MobEchoTargetMixin {

    /**
     * 在怪物确定目标后过滤回声携带者。
     * <p>
     * 目标携带回声效果时把返回值改为 null；效果消失后怪物恢复正常索敌。
     *
     * @param cir 回调
     */
    @Inject(method = "getTarget", at = @At("RETURN"), cancellable = true)
    private void kaleidoscope_sculk$hideEchoHolderFromTargeting(CallbackInfoReturnable<LivingEntity> cir) {
        LivingEntity target = cir.getReturnValue();
        if (target != null && target.hasEffect(KSEffects.ECHO)) {
            cir.setReturnValue(null);
        }
    }
}
