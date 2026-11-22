package com.tequilacat.kaleidoscope_sculk.item;

import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenKnifeItem;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/**
 * 无声厨刀。
 * <p>
 * 继承外部模组的厨刀物品，材质取钻石等级，属性按剑的公式计算。
 */
public class SilentKnifeItem extends KitchenKnifeItem {

    /** 基础伤害加成。 */
    private static final float ATTACK_DAMAGE = 5.0F;
    /** 攻击速度修正。 */
    private static final float ATTACK_SPEED = -2.2F;

    /**
     * 构造无声厨刀。
     * <p>
     * 材质为钻石，耐久 1800。
     */
    public SilentKnifeItem() {
        super(Tiers.DIAMOND, new Properties().durability(1800));
    }

    /**
     * 获取默认属性修饰符。
     * <p>
     * 按剑的公式以钻石材质计算，伤害与速度取自上方常量。
     *
     * @return 物品属性修饰符
     */
    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return SwordItem.createAttributes(Tiers.DIAMOND, ATTACK_DAMAGE, ATTACK_SPEED);
    }
}
