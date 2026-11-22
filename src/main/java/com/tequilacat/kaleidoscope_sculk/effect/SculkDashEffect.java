package com.tequilacat.kaleidoscope_sculk.effect;

import com.tequilacat.kaleidoscope_sculk.init.KSBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

import java.util.UUID;

/**
 * 幽匿冲刺：在幽匿群系中提升移动速度与攻击速度。
 * <p>
 * 效果生效期间按固定间隔检查实体所处群系：位于幽匿群系（含深暗之域）时
 * 附加移动速度与攻击速度修饰符，离开后移除；客户端不处理逻辑。
 */
public class SculkDashEffect extends MobEffect {

    /**
     * 移动速度修饰符的唯一标识。
     * <p>
     * 使用固定 UUID 而非随机值，保证重复附加时能被识别并去重。
     */
    private static final UUID SPEED_MODIFIER = UUID.fromString("5d1f7c62-9a3b-4e08-b7d4-2c6e8f0a1b93");

    /** 移动速度修饰符的名称，仅用于调试与日志识别。 */
    private static final String SPEED_MODIFIER_NAME = "sculk_dash_speed";

    /**
     * 攻击速度修饰符的唯一标识。
     * <p>
     * 与移动速度修饰符分开管理，便于单独附加与移除。
     */
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("a84b2d17-6c9e-4f35-8b0a-7e2d5c3f9a61");

    /** 攻击速度修饰符的名称，仅用于调试与日志识别。 */
    private static final String ATTACK_SPEED_MODIFIER_NAME = "sculk_dash_attack_speed";

    /** 移动速度加成比例，按基础值乘算。 */
    public static final double SPEED_BONUS = 0.4D;

    /** 攻击速度加成比例，按基础值乘算。 */
    public static final double ATTACK_SPEED_BONUS = 0.5D;

    /**
     * 群系检查间隔（单位：tick）。
     * <p>
     * 取值 20，即每 20 tick（约 1 秒）执行一次群系判定与修饰符同步。
     */
    private static final int CHECK_INTERVAL = 20;

    /**
     * 构造幽匿冲刺效果。
     *
     * @param color 效果粒子的颜色值
     */
    public SculkDashEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    /**
     * 判断当前 tick 是否需要执行效果逻辑。
     * <p>
     * 仅在剩余持续时间能被检查间隔整除时返回 true，
     * 以此降低群系检测频率，避免每 tick 查询群系。
     *
     * @param duration  效果剩余持续时间
     * @param amplifier 效果等级（从 0 开始）
     * @return 需要执行效果逻辑时返回 true
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % CHECK_INTERVAL == 0;
    }

    /**
     * 执行效果逻辑：按实体所处群系同步属性修饰符。
     * <p>
     * 客户端直接跳过，不做任何处理；服务端判定实体位于幽匿群系时附加
     * 移动速度与攻击速度修饰符，否则移除已有修饰符。
     *
     * @param entity    受效果影响的实体
     * @param amplifier 效果等级（从 0 开始）
     */
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return;
        }
        if (isInSculkBiome(entity.level(), entity.blockPosition())) {
            applyModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER, SPEED_MODIFIER_NAME, SPEED_BONUS);
            applyModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, ATTACK_SPEED_MODIFIER_NAME, ATTACK_SPEED_BONUS);
        } else {
            clearModifiers(entity);
        }
        return;
    }

    /**
     * 为实体附加一个按基础值乘算的临时属性修饰符。
     * <p>
     * 属性不存在或同一标识的修饰符已附加时跳过，保证操作幂等。
     *
     * @param entity    目标实体
     * @param attribute 目标属性
     * @param id        修饰符唯一标识
     * @param name      修饰符名称
     * @param bonus     加成比例
     */
    private static void applyModifier(LivingEntity entity, Attribute attribute, UUID id, String name, double bonus) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null || instance.getModifier(id) != null) {
            return;
        }
        instance.addTransientModifier(new AttributeModifier(id, name, bonus,
                AttributeModifier.Operation.MULTIPLY_BASE));
    }

    /**
     * 移除实体身上的全部冲刺修饰符。
     * <p>
     * 供效果结束或离开幽匿群系时调用，实体为空时直接返回。
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
     * 移除指定属性上由给定标识标记的修饰符。
     * <p>
     * 属性不存在或修饰符未附加时不执行任何操作。
     *
     * @param entity    目标实体
     * @param attribute 目标属性
     * @param id        修饰符唯一标识
     */
    private static void removeModifier(LivingEntity entity, Attribute attribute, UUID id) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null && instance.getModifier(id) != null) {
            instance.removeModifier(id);
        }
    }

    /**
     * 判断指定坐标是否位于幽匿群系。
     * <p>
     * 同时匹配自定义群系标签与原版深暗之域生物群系。
     *
     * @param level 实体所在世界
     * @param pos   待检测的方块坐标
     * @return 处于幽匿群系时返回 true
     */
    private static boolean isInSculkBiome(Level level, BlockPos pos) {
        var biome = level.getBiome(pos);
        return biome.is(KSBiomeTags.SCULK_BIOMES) || biome.is(Biomes.DEEP_DARK);
    }
}
