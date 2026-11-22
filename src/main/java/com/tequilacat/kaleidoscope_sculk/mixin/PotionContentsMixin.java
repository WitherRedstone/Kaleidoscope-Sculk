package com.tequilacat.kaleidoscope_sculk.mixin;

import com.tequilacat.kaleidoscope_sculk.init.KSPotions;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * 药水的颜色注入。
 * <p>
 * 让深渊药水（ABYSS_POTION）在物品贴图与粒子中显示自定义的深紫色，
 * 其余药水仍走原版按效果混合的取色逻辑。
 */
@Mixin(PotionContents.class)
public class PotionContentsMixin {

    /**
     * 在取色前覆盖深渊药水的颜色。
     * <p>
     * 药水类型为深渊药水时直接返回深紫色并取消原方法；
     * 通过双重转换访问宿主对象自身的 potion 字段。
     *
     * @param cir 回调
     */
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void kaleidoscope_sculk$abyssPotionColor(CallbackInfoReturnable<Integer> cir) {
        PotionContents contents = (PotionContents) (Object) this;
        Optional<Holder<Potion>> potion = contents.potion();
        if (potion.isPresent() && potion.get().is(KSPotions.ABYSS_POTION.getKey())) {
            cir.setReturnValue(0x1B0C36);
        }
    }
}
