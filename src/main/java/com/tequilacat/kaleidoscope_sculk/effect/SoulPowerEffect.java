package com.tequilacat.kaleidoscope_sculk.effect;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * 收割之力：攻击伤害提高，数值取决于被收割目标的最大生命值。
 * <p>
 * 加成值存储在实体持久化数据中，效果生效期间按固定间隔同步为攻击伤害修饰符：
 * 加成大于 0 时附加或更新修饰符，否则移除；效果结束时移除修饰符。
 */
public class SoulPowerEffect extends MobEffect {

    /** 攻击伤害修饰符标识。 */
    private static final ResourceLocation ATTACK_DAMAGE_MODIFIER = KaleidoscopeSculk.id("soul_power_attack_damage");

    /** 持久化数据中记录加成值的键。 */
    private static final String BONUS_KEY = "kaleidoscope_sculk:soul_power_bonus";

    /** 同步间隔（刻）。 */
    private static final int SYNC_INTERVAL = 20;

    /**
     * 构造收割之力效果。
     *
     * @param color 效果颜色
     */
    public SoulPowerEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    /**
     * 判断本刻是否应执行效果逻辑。
     *
     * @param duration  剩余持续时间
     * @param amplifier 效果等级
     * @return 本刻应执行返回 true
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % SYNC_INTERVAL == 0;
    }

    /**
     * 执行效果逻辑。
     * <p>
     * 服务端下同步攻击伤害修饰符。
     *
     * @param entity    目标实体
     * @param amplifier 效果等级
     * @return 恒为 true
     */
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            ensureModifier(entity);
        }
        return true;
    }

    /**
     * 移除效果属性修饰符时调用。
     * <p>
     * 在父类处理基础上额外移除攻击伤害修饰符。
     *
     * @param attributeMap 属性映射
     */
    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        super.removeAttributeModifiers(attributeMap);
        AttributeInstance instance = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        if (instance != null) {
            instance.removeModifier(ATTACK_DAMAGE_MODIFIER);
        }
    }

    /**
     * 设置实体的攻击伤害加成值。
     *
     * @param entity 目标实体
     * @param bonus  加成值
     */
    public static void setBonus(LivingEntity entity, double bonus) {
        entity.getPersistentData().putDouble(BONUS_KEY, bonus);
    }

    /**
     * 同步实体的攻击伤害修饰符。
     * <p>
     * 属性不可用时跳过；加成不大于 0 时移除已有修饰符；
     * 已有修饰符与加成一致时保持不变，否则移除旧修饰符并附加新的。
     *
     * @param entity 目标实体
     */
    public static void ensureModifier(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (instance == null) {
            return;
        }

        double bonus = entity.getPersistentData().getDouble(BONUS_KEY);
        AttributeModifier existing = instance.getModifier(ATTACK_DAMAGE_MODIFIER);

        if (bonus <= 0.0D) {
            if (existing != null) {
                instance.removeModifier(ATTACK_DAMAGE_MODIFIER);
            }
            return;
        }

        if (existing != null && existing.amount() == bonus) {
            return;
        }
        if (existing != null) {
            instance.removeModifier(ATTACK_DAMAGE_MODIFIER);
        }
        instance.addTransientModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER, bonus, AttributeModifier.Operation.ADD_VALUE));
    }
}
