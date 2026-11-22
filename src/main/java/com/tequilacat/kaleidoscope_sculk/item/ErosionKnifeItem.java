package com.tequilacat.kaleidoscope_sculk.item;

import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenKnifeItem;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/**
 * 侵蚀厨刀。
 * <p>
 * 继承外部模组的厨刀物品，材质取石质等级，属性按剑的公式计算。
 */
public class ErosionKnifeItem extends KitchenKnifeItem {

    /**
     * 构造侵蚀厨刀。
     * <p>
     * 材质为石质，耐久 500。
     */
    public ErosionKnifeItem() {
        super(Tiers.STONE, new Properties().durability(500));
    }

    /**
     * 获取默认属性修饰符。
     * <p>
     * 按剑的公式以石质计算：基础伤害加成 3、攻击速度修正 -2.0。
     *
     * @return 物品属性修饰符
     */
    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return SwordItem.createAttributes(Tiers.STONE, 3, -2.0F);
    }
}
