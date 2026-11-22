package com.tequilacat.kaleidoscope_sculk.effect;

import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 声波：声波降临，固定 15 伤害附带目标当前生命的 8%。
 * <p>
 * 由声波效果或手持寂静厨刀触发：沿视线发射声波束，
 * 命中路径上的活体实体（排除驯养宠物）并造成伤害；
 * 手持厨刀连续射击超过一定次数后触发八方向环形爆发，
 * 每次射击消耗耐久与短冷却。非手持状态下每次消耗一层效果等级。
 */
public class SonicWaveEffect extends MobEffect {

    /** 冷却时间戳键。 */
    private static final String COOLDOWN_KEY = "kaleidoscope_sculk:sonic_boom_cooldown";
    /** 连射计数键。 */
    private static final String BURST_COUNTER_KEY = "kaleidoscope_sculk:sonic_boom_counter";
    /** 连射时间戳键。 */
    private static final String BURST_TIMESTAMP_KEY = "kaleidoscope_sculk:sonic_boom_timestamp";

    /** 射击冷却（刻）。 */
    private static final long COOLDOWN_TICKS = 20L;
    /** 连射计数过期时长（刻）。 */
    private static final long COUNTER_EXPIRE_TICKS = 200L;
    /** 触发环形爆发前允许的普通射击次数。 */
    private static final int NORMAL_SHOTS_BEFORE_BURST = 4;
    /** 环形爆发的方向数。 */
    private static final int BURST_DIRECTIONS = 8;
    /** 手持厨刀每次射击消耗的耐久。 */
    private static final int DURABILITY_COST = 50;
    /** 基础伤害。 */
    private static final double BASE_DAMAGE = 15.0D;
    /** 目标当前生命值转为伤害的比例。 */
    private static final double CURRENT_HEALTH_DAMAGE_RATIO = 0.08D;
    /** 声波射程（格）。 */
    private static final double RANGE = 35.0D;
    /** 声波判定半径（格）。 */
    private static final double RADIUS = 2.0D;

    /**
     * 构造声波效果。
     *
     * @param color 效果颜色
     */
    public SonicWaveEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    /**
     * 发射声波。
     * <p>
     * 需手持寂静厨刀或拥有声波效果；冷却内无法再次发射。
     * 手持厨刀时按连射计数决定是否触发环形爆发，消耗耐久并摆动手臂；
     * 随后发射声波并在非手持时消耗一层效果。
     *
     * @param player 玩家
     */
    public static void fire(ServerPlayer player) {
        boolean holdingKnife = player.getMainHandItem().is(KSItems.SILENT_KITCHEN_KNIFE.get());
        if (!holdingKnife && !player.hasEffect(KSEffects.SONIC_WAVE.get())) {
            return;
        }

        long now = player.level().getGameTime();
        long lastUse = player.getPersistentData().getLong(COOLDOWN_KEY);
        if (now - lastUse < COOLDOWN_TICKS) {
            return;
        }
        player.getPersistentData().putLong(COOLDOWN_KEY, now);

        boolean radialBurst = false;
        if (holdingKnife) {
            long lastShot = player.getPersistentData().getLong(BURST_TIMESTAMP_KEY);
            if (now - lastShot > COUNTER_EXPIRE_TICKS) {
                player.getPersistentData().putInt(BURST_COUNTER_KEY, 0);
            }

            int shotCount = player.getPersistentData().getInt(BURST_COUNTER_KEY) + 1;
            radialBurst = shotCount > NORMAL_SHOTS_BEFORE_BURST;
            player.getPersistentData().putInt(BURST_COUNTER_KEY, radialBurst ? 0 : shotCount);
            player.getPersistentData().putLong(BURST_TIMESTAMP_KEY, now);

            player.getMainHandItem().hurtAndBreak(DURABILITY_COST, player, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            player.swing(InteractionHand.MAIN_HAND, true);
        }

        shoot(player, holdingKnife, radialBurst);
    }

    /**
     * 重置连射计数与时间戳。
     *
     * @param player 玩家
     */
    public static void resetStreak(Player player) {
        player.getPersistentData().remove(BURST_COUNTER_KEY);
        player.getPersistentData().remove(BURST_TIMESTAMP_KEY);
    }

    /**
     * 执行发射。
     * <p>
     * 记录发射前的效果等级与时长，按是否环形爆发选择发射方式，
     * 播放监守者声波音效；非手持厨刀时消耗一层效果，剩余等级大于 0 时补回。
     *
     * @param player       玩家
     * @param holdingKnife 是否手持厨刀
     * @param radialBurst  是否环形爆发
     */
    private static void shoot(ServerPlayer player, boolean holdingKnife, boolean radialBurst) {
        ServerLevel level = player.serverLevel();
        MobEffectInstance currentEffect = player.getEffect(KSEffects.SONIC_WAVE.get());

        int currentAmplifier;
        int currentDuration;
        if (holdingKnife) {
            currentAmplifier = currentEffect != null ? currentEffect.getAmplifier() : 0;
            currentDuration = currentEffect != null ? currentEffect.getDuration() : 0;
        } else {
            if (currentEffect == null) {
                return;
            }
            currentAmplifier = currentEffect.getAmplifier();
            currentDuration = currentEffect.getDuration();
        }

        Vec3 startPos = player.getEyePosition();
        if (radialBurst) {
            fireRadialBurst(player, level, startPos, RANGE, RADIUS);
        } else {
            fireStraightBeam(player, level, startPos, player.getLookAngle(), RANGE, RADIUS);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 3.0F, 1.0F);

        if (!holdingKnife) {
            player.removeEffect(KSEffects.SONIC_WAVE.get());
            if (currentAmplifier > 0) {
                player.addEffect(new MobEffectInstance(KSEffects.SONIC_WAVE.get(),
                        currentDuration, currentAmplifier - 1));
            }
        }
    }

    /**
     * 发射直线声波束。
     * <p>
     * 沿视线检测命中实体，向每个命中目标绘制声波粒子并造成伤害；
     * 未命中任何实体时沿视线绘制粒子轨迹。
     *
     * @param player   玩家
     * @param level    服务端世界
     * @param startPos 起点
     * @param lookVec  视线方向
     * @param range    射程
     * @param radius   判定半径
     */
    private static void fireStraightBeam(ServerPlayer player, ServerLevel level, Vec3 startPos,
                                         Vec3 lookVec, double range, double radius) {
        List<EntityHit> hits = rayTraceEntities(player, startPos, lookVec, range, radius);
        int lastParticleStep = -1;

        for (EntityHit hit : hits) {
            Vec3 targetPos = hit.entity().getEyePosition();
            Vec3 direction = targetPos.subtract(startPos).normalize();
            int steps = Mth.floor(startPos.distanceTo(targetPos)) + 7;

            for (int j = 1; j < steps; ++j) {
                int stepKey = j >> 1;
                if (stepKey <= lastParticleStep) {
                    continue;
                }
                level.sendParticles(ParticleTypes.SONIC_BOOM,
                        startPos.x + direction.x * j, startPos.y + direction.y * j,
                        startPos.z + direction.z * j,
                        1, 0.0, 0.0, 0.0, 0.0);
                lastParticleStep = stepKey;
            }

            damageEntity(player, level, hit.entity());
        }

        if (hits.isEmpty()) {
            drawBeamParticles(level, startPos, lookVec, range);
        }
    }

    /**
     * 发射八方向环形声波。
     * <p>
     * 沿八个水平方向分别绘制粒子并检测命中实体，
     * 同一实体只受一次伤害。
     *
     * @param player   玩家
     * @param level    服务端世界
     * @param startPos 起点
     * @param range    射程
     * @param radius   判定半径
     */
    private static void fireRadialBurst(ServerPlayer player, ServerLevel level, Vec3 startPos,
                                        double range, double radius) {
        Set<LivingEntity> damaged = new HashSet<>();
        for (int i = 0; i < BURST_DIRECTIONS; i++) {
            double angle = Math.toRadians(i * (360.0 / BURST_DIRECTIONS));
            Vec3 direction = new Vec3(-Math.sin(angle), 0.0, Math.cos(angle));
            drawBeamParticles(level, startPos, direction, range);
            for (EntityHit hit : rayTraceEntities(player, startPos, direction, range, radius)) {
                if (damaged.add(hit.entity())) {
                    damageEntity(player, level, hit.entity());
                }
            }
        }
    }

    /**
     * 对目标造成声波伤害。
     * <p>
     * 伤害为基础值加目标当前生命值的比例，命中后触发攻击后附魔效果。
     *
     * @param player 玩家
     * @param level  服务端世界
     * @param target 目标实体
     */
    private static void damageEntity(ServerPlayer player, ServerLevel level, LivingEntity target) {
        float damage = (float) BASE_DAMAGE + target.getHealth() * (float) CURRENT_HEALTH_DAMAGE_RATIO;
        DamageSource source = level.damageSources().sonicBoom(player);
        if (target.hurt(source, damage)) {
            EnchantmentHelper.doPostDamageEffects(player, target);
        }
    }

    /**
     * 沿指定方向绘制声波粒子轨迹。
     *
     * @param level    服务端世界
     * @param startPos 起点
     * @param direction 方向
     * @param range    射程
     */
    private static void drawBeamParticles(ServerLevel level, Vec3 startPos, Vec3 direction, double range) {
        for (int j = 1; j <= (int) range; j++) {
            level.sendParticles(ParticleTypes.SONIC_BOOM,
                    startPos.x + direction.x * j, startPos.y + direction.y * j,
                    startPos.z + direction.z * j,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    /**
     * 沿射线检测命中的活体实体。
     * <p>
     * 在射线包围盒范围内收集候选实体（排除自身、死亡与驯养宠物），
     * 与射线做盒体相交检测并按距离排序。
     *
     * @param player   玩家
     * @param startPos 起点
     * @param direction 方向
     * @param range    射程
     * @param radius   判定半径
     * @return 按距离排序的命中列表
     */
    private static List<EntityHit> rayTraceEntities(Player player, Vec3 startPos, Vec3 direction,
                                                    double range, double radius) {
        List<EntityHit> hits = new ArrayList<>();
        AABB searchBox = player.getBoundingBox().inflate(range);
        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, searchBox,
                e -> e != player && e.isAlive() && !isTamedPet(e));

        Vec3 endPos = startPos.add(direction.scale(range));

        for (LivingEntity entity : entities) {
            AABB hitBox = entity.getBoundingBox().inflate(radius);
            if (!hitBox.intersects(startPos, endPos)) {
                continue;
            }

            Vec3 intersectPoint = getClosestIntersection(startPos, endPos, hitBox);
            if (intersectPoint == null) {
                continue;
            }

            double distance = startPos.distanceTo(intersectPoint);
            if (distance <= range) {
                hits.add(new EntityHit(entity, distance));
            }
        }

        hits.sort(Comparator.comparingDouble(EntityHit::distance));
        return hits;
    }

    /**
     * 计算线段与盒体的最近交点。
     * <p>
     * 使用逐轴裁剪求交，线段与盒体不相交时返回 null。
     *
     * @param start 线段起点
     * @param end   线段终点
     * @param aabb  盒体
     * @return 交点位置，无交点时返回 null
     */
    private static Vec3 getClosestIntersection(Vec3 start, Vec3 end, AABB aabb) {
        Vec3 dir = end.subtract(start);
        double tMin = 0.0;
        double tMax = 1.0;

        double[] min = {aabb.minX, aabb.minY, aabb.minZ};
        double[] max = {aabb.maxX, aabb.maxY, aabb.maxZ};
        double[] origin = {start.x, start.y, start.z};
        double[] delta = {dir.x, dir.y, dir.z};

        for (int axis = 0; axis < 3; axis++) {
            if (Math.abs(delta[axis]) < 1e-8) {
                if (origin[axis] < min[axis] || origin[axis] > max[axis]) {
                    return null;
                }
                continue;
            }

            double t1 = (min[axis] - origin[axis]) / delta[axis];
            double t2 = (max[axis] - origin[axis]) / delta[axis];
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }
            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);
            if (tMin > tMax) {
                return null;
            }
        }

        double t = Math.max(tMin, 0.0);
        return t > 1.0 ? null : start.add(dir.scale(t));
    }

    /**
     * 判断实体是否为驯养宠物。
     * <p>
     * 已驯服的可驯养动物或存在主人 UUID 的实体视为宠物。
     *
     * @param entity 目标实体
     * @return 是驯养宠物返回 true
     */
    private static boolean isTamedPet(LivingEntity entity) {
        if (entity instanceof TamableAnimal tamable && tamable.isTame()) {
            return true;
        }
        return entity instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null;
    }

    /**
     * 命中记录。
     *
     * @param entity   命中的实体
     * @param distance 沿射线的命中距离
     */
    private record EntityHit(LivingEntity entity, double distance) {
    }
}