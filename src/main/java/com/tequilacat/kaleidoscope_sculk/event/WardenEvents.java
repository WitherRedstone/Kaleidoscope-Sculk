package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.util.DropHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监守者事件类。
 * <p>
 * 按监守者累计受到的伤害比例掉落远古骨片：每累计最大生命值的 10%，
 * 掉落 1~2 枚骨片；监守者死亡或离开世界时清除累计记录。
 * 监守者被击杀时额外掉落 1~2 根监守者触须。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class WardenEvents {

    /** 各监守者累计受到的伤害，键为实体 UUID。 */
    private static final Map<UUID, Float> ACCUMULATED_DAMAGE = new ConcurrentHashMap<>();

    /** 每累计最大生命值的该比例触发一次掉落。 */
    private static final float DROP_INTERVAL_RATIO = 0.10F;

    /** 每次触发的最少掉落数量。 */
    private static final int MIN_DROPS_PER_INTERVAL = 1;

    /** 每次触发的最多掉落数量。 */
    private static final int MAX_DROPS_PER_INTERVAL = 2;

    private WardenEvents() {
    }

    /**
     * 处理监守者受伤后事件。
     * <p>
     * 服务端累计其受到的伤害，按跨越的伤害区间次数掉落远古骨片，
     * 每次触发掉落 1~2 枚。
     *
     * @param event 受伤后事件
     */
    @SubscribeEvent
    public static void onWardenDamage(LivingDamageEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Warden warden) || warden.level().isClientSide) {
            return;
        }

        float damage = event.getNewDamage();
        if (damage <= 0.0F) {
            return;
        }

        UUID uuid = warden.getUUID();
        float maxHealth = warden.getMaxHealth();
        float intervalDamage = maxHealth * DROP_INTERVAL_RATIO;

        float currentDamage = ACCUMULATED_DAMAGE.getOrDefault(uuid, 0.0F);
        float newDamage = Math.min(currentDamage + damage, maxHealth);

        int intervals = (int) (newDamage / intervalDamage) - (int) (currentDamage / intervalDamage);
        for (int i = 0; i < intervals; i++) {
            int count = MIN_DROPS_PER_INTERVAL
                    + warden.getRandom().nextInt(MAX_DROPS_PER_INTERVAL - MIN_DROPS_PER_INTERVAL + 1);
            for (int j = 0; j < count; j++) {
                warden.level().addFreshEntity(DropHelper.createDrop(warden.level(),
                        warden.getX(), warden.getY(), warden.getZ(),
                        new ItemStack(KSItems.ANCIENT_BONE_FRAGMENT.get())));
            }
        }

        ACCUMULATED_DAMAGE.put(uuid, newDamage);
    }

    /**
     * 处理监守者死亡事件。
     * <p>
     * 清除其累计伤害记录。
     *
     * @param event 死亡事件
     */
    @SubscribeEvent
    public static void onWardenDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Warden warden) {
            ACCUMULATED_DAMAGE.remove(warden.getUUID());
        }
    }

    /**
     * 处理监守者离开世界事件。
     * <p>
     * 清除其累计伤害记录，避免残留数据。
     *
     * @param event 实体离开世界事件
     */
    @SubscribeEvent
    public static void onWardenLeaveLevel(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Warden warden) {
            ACCUMULATED_DAMAGE.remove(warden.getUUID());
        }
    }

    /**
     * 处理监守者掉落事件。
     * <p>
     * 在默认掉落基础上额外添加 1~2 根监守者触须。
     *
     * @param event 活体掉落事件
     */
    @SubscribeEvent
    public static void onWardenDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Warden warden)) {
            return;
        }

        int count = 1 + warden.getRandom().nextInt(2);
        for (int i = 0; i < count; i++) {
            ItemEntity drop = DropHelper.createDrop(warden.level(), warden.getX(), warden.getY(), warden.getZ(),
                    new ItemStack(KSItems.WARDEN_TENDRIL.get()));
            event.getDrops().add(drop);
        }
    }
}
