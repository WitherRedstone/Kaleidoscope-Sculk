package com.tequilacat.kaleidoscope_sculk.item;

import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenKnifeItem;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

/**
 * 侵蚀厨刀。
 * <p>
 * 继承外部模组的厨刀物品，材质取石质等级，属性按剑的公式计算。
 */
public class ErosionKnifeItem extends KitchenKnifeItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    /**
     * 构造侵蚀厨刀。
     * <p>
     * 材质为石质，耐久 500。
     */
    public ErosionKnifeItem() {
        super(Tiers.STONE, new Item.Properties().durability(500));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID,
                "Weapon modifier", 3.0D + Tiers.STONE.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID,
                "Weapon modifier", -2.0D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    /**
     * 获取默认属性修饰符。
     * <p>
     * 按剑的公式以石质计算：基础伤害加成 3、攻击速度修正 -2.0。
     *
     * @return 物品属性修饰符
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
