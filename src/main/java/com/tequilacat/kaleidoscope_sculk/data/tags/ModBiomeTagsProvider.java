package com.tequilacat.kaleidoscope_sculk.data.tags;

import com.tequilacat.kaleidoscope_sculk.init.KSBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(KSBiomeTags.DEEP_DARK).add(Biomes.DEEP_DARK);
        tag(KSBiomeTags.SCULK_BIOMES).addTag(KSBiomeTags.DEEP_DARK);
    }
}
