package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.util.DropHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Iterator;

/**
 * 厨刀击杀动物时把肉转换成幽寂肉。
 * <p>
 * 玩家用侵蚀厨刀或寂静厨刀击杀猪、羊、牛、鸡时，播放幽匿转化粒子与音效，
 * 并把掉落中的各类肉类物品合并转换为等量的幽寂肉。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class KnifeKillEvents {

    private KnifeKillEvents() {
    }

    /**
     * 处理活体掉落事件。
     * <p>
     * 需为玩家用指定厨刀击杀可转化动物；随后播放转化粒子，
     * 移除掉落中的所有肉类并按总数量生成一份幽寂肉。
     *
     * @param event 活体掉落事件
     */
    @SubscribeEvent
    public static void onKnifeKillDrops(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        if (!(event.getSource().getEntity() instanceof Player killer)) {
            return;
        }

        ItemStack mainHand = killer.getMainHandItem();
        if (!mainHand.is(KSItems.EROSION_KITCHEN_KNIFE.get())
                && !mainHand.is(KSItems.SILENT_KITCHEN_KNIFE.get())) {
            return;
        }
        if (!isConvertibleAnimal(entity)) {
            return;
        }

        playConvertParticles(entity);

        int meatCount = 0;
        for (Iterator<ItemEntity> iterator = event.getDrops().iterator(); iterator.hasNext(); ) {
            ItemEntity drop = iterator.next();
            ItemStack stack = drop.getItem();
            if (isMeatItem(stack)) {
                meatCount += stack.getCount();
                iterator.remove();
                drop.discard();
            }
        }

        if (meatCount > 0) {
            event.getDrops().add(DropHelper.createDrop(entity.level(),
                    entity.getX(), entity.getY(), entity.getZ(),
                    new ItemStack(KSItems.EERIE_MEAT.get(), meatCount)));
        }
    }

    /**
     * 播放幽匿转化粒子与音效。
     * <p>
     * 仅服务端生效：围绕实体生成幽匿电荷、灵魂火焰、幽匿灵魂与灵魂粒子，
     * 并播放幽匿方块扩散音效。
     *
     * @param entity 被击杀的实体
     */
    private static void playConvertParticles(Entity entity) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 pos = entity.position();
        serverLevel.sendParticles(new SculkChargeParticleOptions(serverLevel.random.nextFloat()),
                pos.x, pos.y + 0.5, pos.z, 25, 1.5, 1.2, 1.5, 0.05);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                pos.x, pos.y + 0.2, pos.z, 20, 1.2, 0.15, 1.2, 0.02);
        serverLevel.sendParticles(ParticleTypes.SCULK_SOUL,
                pos.x, pos.y + 0.3, pos.z, 15, 1.0, 0.1, 1.0, 0.01);
        serverLevel.sendParticles(ParticleTypes.SOUL,
                pos.x, pos.y + 0.6, pos.z, 12, 1.2, 0.8, 1.2, 0.01);
        serverLevel.sendParticles(new SculkChargeParticleOptions(serverLevel.random.nextFloat()),
                pos.x, pos.y + 0.1, pos.z, 15, 1.6, 0.1, 1.6, 0.03);

        serverLevel.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.PLAYERS,
                0.6F, 0.8F + serverLevel.random.nextFloat() * 0.4F);
    }

    /**
     * 判断实体是否为可转化的动物。
     * <p>
     * 仅猪、羊、牛、鸡可转化。
     *
     * @param entity 目标实体
     * @return 可转化返回 true
     */
    private static boolean isConvertibleAnimal(Entity entity) {
        EntityType<?> type = entity.getType();
        return type == EntityType.PIG
                || type == EntityType.SHEEP
                || type == EntityType.COW
                || type == EntityType.CHICKEN;
    }

    /**
     * 判断物品是否为肉类。
     * <p>
     * 包括各类生猪/熟肉、兔肉与幽寂肉本身。
     *
     * @param stack 物品堆叠
     * @return 是肉类返回 true
     */
    private static boolean isMeatItem(ItemStack stack) {
        return stack.is(Items.PORKCHOP)
                || stack.is(Items.COOKED_PORKCHOP)
                || stack.is(Items.MUTTON)
                || stack.is(Items.COOKED_MUTTON)
                || stack.is(Items.BEEF)
                || stack.is(Items.COOKED_BEEF)
                || stack.is(Items.CHICKEN)
                || stack.is(Items.COOKED_CHICKEN)
                || stack.is(Items.RABBIT)
                || stack.is(Items.COOKED_RABBIT)
                || stack.is(KSItems.EERIE_MEAT.get());
    }
}
