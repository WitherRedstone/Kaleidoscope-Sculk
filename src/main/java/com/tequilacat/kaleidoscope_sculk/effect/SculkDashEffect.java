package com.tequilacat.kaleidoscope_sculk.effect;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

/**
 * 幽匿冲刺：在幽匿群系中提升移动速度与攻击速度。
 * <p>
 * 效果生效期间按固定间隔检查实体所处群系：位于幽匿群系（含深暗之域）时
 * 附加移动速度与攻击速度修饰符，离开后移除；客户端不处理逻辑。
 */
public class SculkDashEffect extends MobEffect {

    /** 移动速度修饰符标识。 */
    private static final ResourceLocation SPEED_MODIFIER = KaleidoscopeSculk.id("sculk_dash_speed");

    /** 攻击速度修饰符标识。 */
    private static final ResourceLocation ATTACK_SPEED_MODIFIER = KaleidoscopeSculk.id("sculk_dash_attack_speed");

    /** 移动速度加成。 */
    public static final double SPEED_BONUS = 0.4D;

    /** 攻击速度加成。 */
    public static final double ATTACK_SPEED_BONUS = 0.5D;

    /** 检查间隔（刻）。 */
    private static final int CHECK_INTERVAL = 20;

    /**
     * 构造幽匿冲刺效果。
     *
     * @param color 效果颜色
     */
    public SculkDashEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    /**
     * 判断本刻是否应执行效果逻辑。
     * <p>
     * 按固定间隔检查，避免每刻都做群系判定。
     *
     * @param duration  剩余持续时间
     * @param amplifier 效果等级
     * @return 本刻应执行返回 true
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % CHECK_INTERVAL == 0;
    }

    /**
     * 执行效果逻辑。
     * <p>
     * 客户端直接返回；服务端在幽匿群系中附加速度修饰符，
     * 否则移除已有修饰符。
     *
     * @param entity    目标实体
     * @param amplifier 效果等级
     * @return 恒为 true
     */
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return true;
        }

        if (isInSculkBiome(entity.level(), entity.blockPosition())) {
            applyModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER, SPEED_BONUS);
            applyModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, ATTACK_SPEED_BONUS);
        } else {
            clearModifiers(entity);
        }
        return true;
    }

    /**
     * 为实体的指定属性附加修饰符。
     * <p>
     * 属性不可用或已存在同名修饰符时跳过。
     *
     * @param entity     目标实体
     * @param attribute  属性
     * @param modifierId 修饰符标识
     * @param bonus      加成值
     */
    private static void applyModifier(LivingEntity entity, Holder<Attribute> attribute, ResourceLocation modifierId, double bonus) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null || instance.getModifier(modifierId) != null) {
            return;
        }
        instance.addTransientModifier(new AttributeModifier(
                modifierId, bonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    /**
     * 移除该效果附加的全部修饰符。
     *
     * @param entity 目标实体
     */
    public static void clearModifiers(LivingEntity entity) {
        if (entity == null) {
            return;
        }
        removeModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
        removeModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER);
    }

    /**
     * 移除实体的指定属性修饰符。
     *
     * @param entity     目标实体
     * @param attribute  属性
     * @param modifierId 修饰符标识
     */
    private static void removeModifier(LivingEntity entity, Holder<Attribute> attribute, ResourceLocation modifierId) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null && instance.getModifier(modifierId) != null) {
            instance.removeModifier(modifierId);
        }
    }

    /**
     * 判断位置是否处于幽匿群系。
     * <p>
     * 匹配幽匿群系标签或深暗之域群系。
     *
     * @param level 世界
     * @param pos   位置
     * @return 处于幽匿群系返回 true
     */
    private static boolean isInSculkBiome(Level level, BlockPos pos) {
        var biome = level.getBiome(pos);
        return biome.is(KSBiomeTags.SCULK_BIOMES) || biome.is(Biomes.DEEP_DARK);
    }
}
