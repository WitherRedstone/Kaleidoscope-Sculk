package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.entity.AncientBoneFragmentProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KSEntities {
    DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, KaleidoscopeSculk.MODID);

    /** 远古骨片投掷物 */
    DeferredHolder<EntityType<?>, EntityType<AncientBoneFragmentProjectile>> ANCIENT_BONE_FRAGMENT_PROJECTILE =
            ENTITY_TYPES.register("ancient_bone_fragment_projectile",
                    () -> EntityType.Builder.<AncientBoneFragmentProjectile>of(
                            AncientBoneFragmentProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(64)
                            .updateInterval(10)
                            .build("ancient_bone_fragment_projectile"));

    static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
