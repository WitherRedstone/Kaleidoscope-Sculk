package com.tequilacat.kaleidoscope_sculk.item;

import com.tequilacat.kaleidoscope_sculk.config.KSServerConfig;
import com.tequilacat.kaleidoscope_sculk.effect.SoulPowerEffect;
import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.Optional;
import java.util.UUID;

/**
 * 灵魂帆。
 * <p>
 * 头戴装备，收割生命值低于阈值的非玩家实体：消耗经验获取吸收与灵魂力量加成，
 * 目标立即死亡，冷却记录在物品的数据组件中。
 */
public class SoulSailItem extends Item implements Equipable {

    /** 收割后增益效果的持续时间（刻）。 */
    public static final int BUFF_DURATION_TICKS = 12000;
    /** 锁定目标的最大视线距离（格）。 */
    public static final double TARGET_RANGE = 12.0D;
    /** 目标生命值低于最大值的该比例时才可收割。 */
    public static final double TARGET_HEALTH_THRESHOLD = 0.30D;
    /** 提示消息的颜色。 */
    private static final int SCULK_PINAPPLE_COLOR = 0x79F3FB;
    /** 收割的基础经验消耗。 */
    private static final double XP_COST_BASE = 50.0D;
    /** 收割按目标生命值线性增加的每点经验消耗。 */
    private static final double XP_COST_PER_HEALTH = 2.0D;
    /** 吸收效果等级按目标最大生命值的该比例换算。 */
    private static final double ABSORPTION_RATIO = 0.40D;
    /** 攻击力加成按目标最大生命值的该比例换算。 */
    private static final double ATTACK_DAMAGE_BONUS_RATIO = 0.10D;

    /**
     * 构造灵魂帆。
     *
     * @param properties 物品属性
     */
    public SoulSailItem(Properties properties) {
        super(properties);
    }

    /**
     * 获取装备槽位。
     *
     * @return 头部槽位
     */
    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    /**
     * 右键时穿戴到头部。
     *
     * @param level  所在世界
     * @param player 玩家
     * @param hand   使用手
     * @return 交互结果
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }

    /**
     * 获取收割冷却时长。
     *
     * @return 冷却刻数，由服务端配置的秒数换算
     */
    public static long cooldownTicks() {
        return KSServerConfig.REAP_COOLDOWN_SECONDS.get() * 20L;
    }

    /**
     * 判断实体是否为有效收割目标。
     * <p>
     * 目标必须存活且生命值低于最大值的阈值比例。
     *
     * @param target 待判断的实体
     * @return 是否为有效目标
     */
    public static boolean isValidTarget(LivingEntity target) {
        return target.isAlive()
                && target.getHealth() > 0.0F
                && target.getHealth() < target.getMaxHealth() * (float) TARGET_HEALTH_THRESHOLD;
    }

    /**
     * 判断玩家是否可以收割目标。
     * <p>
     * 在有效目标的基础上排除 Boss、他人拥有的宠物与黑名单实体。
     *
     * @param reaper 收割者
     * @param target 目标实体
     * @return 是否可以收割
     */
    public static boolean canReap(Player reaper, LivingEntity target) {
        if (!isValidTarget(target)) {
            return false;
        }
        if (target.getType().is(Tags.EntityTypes.BOSSES)) {
            return false;
        }
        if (target instanceof OwnableEntity ownable) {
            UUID owner = ownable.getOwnerUUID();
            if (owner != null && !owner.equals(reaper.getUUID())) {
                return false;
            }
        }
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
        String idString = id.toString();
        for (String entry : KSServerConfig.REAP_BLACKLIST.get()) {
            if (idString.equals(entry)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算收割目标所需的经验点数。
     *
     * @param target 目标实体
     * @return 所需经验点数
     */
    public static int xpCost(LivingEntity target) {
        return (int) Math.round(XP_COST_BASE + XP_COST_PER_HEALTH * target.getHealth());
    }

    /**
     * 计算达到某等级所需的总经验点数。
     * <p>
     * 按原版经验曲线分段计算，16 级以下、17 到 31 级、32 级以上各用一段公式。
     *
     * @param level 等级
     * @return 总经验点数
     */
    public static int totalXpForLevel(int level) {
        if (level <= 0) {
            return 0;
        }
        if (level <= 16) {
            return level * level + 6 * level;
        }
        if (level <= 31) {
            return (int) (2.5D * level * level - 40.5D * level + 360.0D);
        }
        return (int) (4.5D * level * level - 162.5D * level + 2220.0D);
    }

    /**
     * 计算升到下一级所需的经验点数。
     * <p>
     * 与原版经验曲线一致，不同等级段的每级增量不同。
     *
     * @param level 当前等级
     * @return 升级所需经验点数
     */
    public static int xpNeededForNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        }
        return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
    }

    /**
     * 计算玩家的总经验点数。
     * <p>
     * 由已完成等级的总经验与当前等级的经验进度相加得到，用于与消耗比较。
     *
     * @param player 玩家
     * @return 总经验点数
     */
    public static int totalXpPoints(Player player) {
        int level = Math.max(0, player.experienceLevel);
        int partial = Math.round(player.experienceProgress * xpNeededForNextLevel(level));
        return totalXpForLevel(level) + Math.max(0, partial);
    }

    /**
     * 计算吸收效果的等级。
     * <p>
     * 按目标最大生命值换算成效果等级（每 4 点生命值一级），并限制在 0 到 254 之间。
     *
     * @param targetMaxHealth 目标最大生命值
     * @return 吸收效果等级
     */
    public static int absorptionAmplifier(float targetMaxHealth) {
        int levels = Math.round(targetMaxHealth * (float) ABSORPTION_RATIO / 4.0F);
        return Mth.clamp(levels - 1, 0, 254);
    }

    /**
     * 计算收割后的攻击力加成。
     *
     * @param targetMaxHealth 目标最大生命值
     * @return 攻击力加成
     */
    public static double attackDamageBonus(float targetMaxHealth) {
        return targetMaxHealth * ATTACK_DAMAGE_BONUS_RATIO;
    }

    /**
     * 执行灵魂收割。
     * <p>
     * 校验头部装备与冷却后寻找视线目标，扣除经验并调用收割流程；
     * 冷却时间写入物品的数据组件，各失败分支会向玩家提示原因。
     *
     * @param player 收割者
     */
    public static void absorb(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        ItemStack sail = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!sail.is(KSItems.SOUL_SAIL.get())) {
            return;
        }

        long now = level.getGameTime();
        Long readyAt = null;
        if (sail.getTag() != null && sail.getTag().contains("soul_sail_cooldown")) {
            readyAt = sail.getTag().getLong("soul_sail_cooldown");
        }
        if (readyAt != null && now < readyAt) {
            player.displayClientMessage(message(
                    "item.kaleidoscope_sculk.soul_sail.cooldown", (readyAt - now + 19L) / 20L), true);
            return;
        }

        LivingEntity target = findTarget(player);
        if (target == null) {
            player.displayClientMessage(message(
                    "item.kaleidoscope_sculk.soul_sail.no_target"), true);
            return;
        }

        boolean creative = player.isCreative();
        int cost = xpCost(target);
        if (!creative && totalXpPoints(player) < cost) {
            player.displayClientMessage(message(
                    "item.kaleidoscope_sculk.soul_sail.not_enough_xp", cost), true);
            return;
        }

        reap(player, level, target, cost, !creative);
        sail.getOrCreateTag().putLong("soul_sail_cooldown", now + cooldownTicks());
    }

    /**
     * 构造提示消息。
     * <p>
     * 统一使用幽匿色加粗样式。
     *
     * @param key  翻译键
     * @param args 翻译参数
     * @return 提示消息
     */
    private static MutableComponent message(String key, Object... args) {
        return Component.translatable(key, args)
                .withStyle(s -> s.withColor(SCULK_PINAPPLE_COLOR).withBold(true));
    }

    /**
     * 沿视线寻找收割目标。
     * <p>
     * 在搜索范围内取视线射线命中的最近实体，玩家自身与其它玩家不参与。
     *
     * @param player 收割者
     * @return 最近的合法目标，没有时为 null
     */
    private static LivingEntity findTarget(ServerPlayer player) {
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getLookAngle().scale(TARGET_RANGE));

        AABB searchBox = player.getBoundingBox().inflate(TARGET_RANGE + 1.0D);
        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;

        for (LivingEntity candidate : player.level().getEntitiesOfClass(LivingEntity.class, searchBox,
                e -> e != player && !(e instanceof Player) && canReap(player, e))) {
            Optional<Vec3> hit = candidate.getBoundingBox().inflate(0.3D).clip(start, end);
            if (hit.isEmpty()) {
                continue;
            }
            double distance = start.distanceToSqr(hit.get());
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        return best;
    }

    /**
     * 执行收割结算。
     * <p>
     * 扣除经验后直接击杀目标，并给玩家施加吸收与灵魂力量效果；
     * 随后播放幽匿音效并在目标位置生成粒子。
     *
     * @param player   收割者
     * @param level    所在世界
     * @param target   目标实体
     * @param xpCost   经验消耗
     * @param chargeXp 是否扣除经验，创造模式为 false
     */
    private static void reap(ServerPlayer player, ServerLevel level, LivingEntity target,
                             int xpCost, boolean chargeXp) {
        float targetMaxHealth = target.getMaxHealth();

        if (chargeXp) {
            player.giveExperiencePoints(-xpCost);
        }

        target.setLastHurtByPlayer(player);
        target.hurt(level.damageSources().playerAttack(player), Float.MAX_VALUE);
        if (target.isAlive()) {
            target.kill();
        }

        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,
                BUFF_DURATION_TICKS, absorptionAmplifier(targetMaxHealth)));
        player.addEffect(new MobEffectInstance(KSEffects.SOUL_POWER.get(),
                BUFF_DURATION_TICKS, 0));
        SoulPowerEffect.setBonus(player, attackDamageBonus(targetMaxHealth));
        SoulPowerEffect.ensureModifier(player);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 1.0F, 0.7F);
        level.sendParticles(ParticleTypes.SCULK_SOUL,
                target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ(),
                24, 0.3D, 0.5D, 0.3D, 0.05D);
    }
}