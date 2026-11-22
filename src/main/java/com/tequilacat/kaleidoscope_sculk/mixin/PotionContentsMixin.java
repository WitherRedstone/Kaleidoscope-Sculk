package com.tequilacat.kaleidoscope_sculk.mixin;

import com.tequilacat.kaleidoscope_sculk.init.KSPotions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 药水的颜色注入。
 * <p>
 * 让深渊药水（ABYSS_POTION）在物品贴图与粒子中显示自定义的深紫色，
 * 其余药水仍走原版按效果混合的取色逻辑。
 */
@Mixin(PotionUtils.class)
public class PotionContentsMixin {

    /**
     * 在取色前覆盖深渊药水的颜色。
     * <p>
     * 药水类型为深渊药水时直接返回深紫色并取消原方法。
     *
     * @param cir 回调
     */
    @Inject(method = "getColor(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private static void kaleidoscope_sculk$abyssPotionColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (PotionUtils.getPotion(stack) == KSPotions.ABYSS_POTION.get()) {
            cir.setReturnValue(0x1B0C36);
        }
    }
}
