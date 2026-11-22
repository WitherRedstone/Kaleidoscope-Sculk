package com.tequilacat.kaleidoscope_sculk.data;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackRegistriesProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, ModDatapackRegistriesProvider::bootstrapDamageTypes);

    public ModDatapackRegistriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(KaleidoscopeSculk.MODID));
    }

    private static void bootstrapDamageTypes(BootstrapContext<DamageType> context) {
        context.register(
                ResourceKey.create(Registries.DAMAGE_TYPE, KaleidoscopeSculk.id("deepslate_cake_slice")),
                new DamageType("deepslate_cake_slice", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    }
}
