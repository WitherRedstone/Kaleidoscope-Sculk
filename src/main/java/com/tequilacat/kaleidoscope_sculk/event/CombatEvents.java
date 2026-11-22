package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 战斗事件类。
 * <p>
 * 处理与深渊、回响效果相关的战斗逻辑：
 * 深渊效果使玩家所处深度越深、造成的伤害越高（随效果等级提高上限），
 * 回响效果在攻击时被消耗。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class CombatEvents {

    /** 深渊生效的起始高度。 */
    private static final int START_DEPTH = 0;
    /** 深渊计算深度上限对应的高度。 */
    private static final int MAX_DEPTH = -60;
    /** 深渊基础最大额外伤害。 */
    private static final float BASE_MAX_BONUS_DAMAGE = 5.0F;
    /** 深渊每级提高的最大额外伤害。 */
    private static final float BONUS_PER_LEVEL = 3.0F;
    /** 深度曲线的缓和尺度。 */
    private static final double DEPTH_SCALE = 15.0;
    /** 可计入的深度范围。 */
    private static final double MAX_DEPTH_RANGE = START_DEPTH - MAX_DEPTH;

    /**
     * 深渊：玩家越深伤害越高；回响会在攻击时消耗。
     * <p>
     * 客户端不处理；解析攻击者后，若攻击者为拥有深渊效果的玩家，
     * 按其所处高度与效果等级计算额外伤害并叠加；若攻击者拥有回响效果则移除。
     *
     * @param event 受到伤害事件
     */
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        LivingEntity attacker = resolveAttacker(event.getSource());
        if (attacker == null) {
            return;
        }

        if (attacker instanceof Player player) {
            MobEffectInstance abyss = player.getEffect(KSEffects.ABYSS);
            if (abyss != null) {
                float bonus = abyssBonus(player.getY(), abyss.getAmplifier());
                if (bonus > 0.0F) {
                    event.setAmount(event.getAmount() + bonus);
                }
            }
        }

        if (attacker.hasEffect(KSEffects.ECHO)) {
            attacker.removeEffect(KSEffects.ECHO);
        }
    }

    /**
     * 从伤害来源解析攻击者。
     * <p>
     * 优先取来源实体，其次取直接实体，均非活体时返回 null。
     *
     * @param source 伤害来源
     * @return 攻击者，无法解析时返回 null
     */
    private static LivingEntity resolveAttacker(DamageSource source) {
        if (source.getEntity() instanceof LivingEntity living) {
            return living;
        }
        return source.getDirectEntity() instanceof LivingEntity living ? living : null;
    }

    /**
     * 计算深渊额外伤害。
     * <p>
     * 未低于起始高度时无加成；否则按深度占比（以缓和曲线计算）
     * 乘以随等级提高的最大额外伤害，结果限制在有效区间内。
     *
     * @param y         攻击者高度
     * @param amplifier 深渊效果等级
     * @return 额外伤害
     */
    private static float abyssBonus(double y, int amplifier) {
        if (y >= START_DEPTH) {
            return 0.0F;
        }

        double depth = Math.min(START_DEPTH - y, MAX_DEPTH_RANGE);
        double ratio = depth / (depth + DEPTH_SCALE);
        float maxBonus = BASE_MAX_BONUS_DAMAGE + amplifier * BONUS_PER_LEVEL;
        return Math.clamp((float) (ratio * maxBonus), 0.0F, maxBonus);
    }
}
