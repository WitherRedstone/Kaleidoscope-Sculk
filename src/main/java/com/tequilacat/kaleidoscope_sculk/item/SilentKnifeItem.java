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
 * 无声厨刀。
 * <p>
 * 继承外部模组的厨刀物品，材质取钻石等级，属性按剑的公式计算。
 */
public class SilentKnifeItem extends KitchenKnifeItem {

    /** 基础伤害加成。 */
    private static final float ATTACK_DAMAGE = 5.0F;

    /** 攻击速度修正。 */
    private static final float ATTACK_SPEED = -2.2F;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    /**
     * 构造无声厨刀。
     * <p>
     * 材质为钻石，耐久 1800。
     */
    public SilentKnifeItem() {
        super(Tiers.DIAMOND, new Item.Properties().durability(1800));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID,
                "Weapon modifier", (double) (ATTACK_DAMAGE + Tiers.DIAMOND.getAttackDamageBonus()),
                AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID,
                "Weapon modifier", (double) ATTACK_SPEED, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    /**
     * 获取默认属性修饰符。
     * <p>
     * 按剑的公式以钻石材质计算，伤害与速度取自上方常量。
     *
     * @return 物品属性修饰符
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
