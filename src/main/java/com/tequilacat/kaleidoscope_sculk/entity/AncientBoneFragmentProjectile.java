package com.tequilacat.kaleidoscope_sculk.entity;

import com.tequilacat.kaleidoscope_sculk.init.KSEntities;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * 远古骨片弹射物。
 * <p>
 * 投掷后可命中实体造成伤害并击退，命中后或落地时以一定概率
 * 掉落一枚远古骨片。掉落只发生一次，避免重复生成。
 */
public class AncientBoneFragmentProjectile extends ThrowableItemProjectile {

    /** 命中伤害。 */
    private static final float DAMAGE = 4.0F;
    /** 命中或落地后返回骨片的概率。 */
    private static final double RETURN_CHANCE = 0.7D;

    /** 是否已经处理过掉落。 */
    private boolean hasDropped;

    /**
     * 构造远古骨片弹射物。
     *
     * @param entityType 实体类型
     * @param level      世界
     */
    public AncientBoneFragmentProjectile(EntityType<? extends AncientBoneFragmentProjectile> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 由投掷者构造远古骨片弹射物。
     *
     * @param level   世界
     * @param shooter 投掷者
     */
    public AncientBoneFragmentProjectile(Level level, LivingEntity shooter) {
        super(KSEntities.ANCIENT_BONE_FRAGMENT_PROJECTILE.get(), shooter, level);
    }

    /**
     * 获取默认物品。
     *
     * @return 远古骨片
     */
    @Override
    protected Item getDefaultItem() {
        return KSItems.ANCIENT_BONE_FRAGMENT.get();
    }

    /**
     * 命中实体时调用。
     * <p>
     * 服务端对目标造成投掷伤害，命中活体且投掷者为活体时按
     * 投掷者到目标的水平方向击退目标，随后处理掉落。
     *
     * @param result 实体命中结果
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        Entity owner = this.getOwner();

        if (this.level().isClientSide) {
            return;
        }

        DamageSource damageSource = this.damageSources().thrown(this, owner);
        target.hurt(damageSource, DAMAGE);

        if (target instanceof LivingEntity livingTarget && owner instanceof LivingEntity) {
            double dx = target.getX() - owner.getX();
            double dz = target.getZ() - owner.getZ();
            livingTarget.knockback(0.5F, dx, dz);
        }
        this.spawnDropItem();
    }

    /**
     * 命中时调用。
     * <p>
     * 在父类处理基础上，服务端若尚未处理掉落则处理掉落，随后移除实体。
     *
     * @param result 命中结果
     */
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            if (!this.hasDropped) {
                this.spawnDropItem();
            }
            this.discard();
        }
    }

    /**
     * 生成掉落物。
     * <p>
     * 仅处理一次；按概率在当前坐标生成一枚远古骨片，
     * 并设置默认拾取延迟。
     */
    private void spawnDropItem() {
        if (this.hasDropped) {
            return;
        }
        this.hasDropped = true;

        if (this.level().getRandom().nextDouble() >= RETURN_CHANCE) {
            return;
        }

        ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(),
                new ItemStack(KSItems.ANCIENT_BONE_FRAGMENT.get(), 1));
        itemEntity.setDefaultPickUpDelay();
        this.level().addFreshEntity(itemEntity);
    }
}
