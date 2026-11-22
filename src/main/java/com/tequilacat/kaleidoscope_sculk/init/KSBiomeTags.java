package com.tequilacat.kaleidoscope_sculk.init;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface KSBiomeTags {

    /** 幽匿效果生效的生物群系。 */
    TagKey<Biome> SCULK_BIOMES = TagKey.create(Registries.BIOME, KaleidoscopeSculk.id("sculk_biomes"));

    /** 深暗生物群系。 */
    TagKey<Biome> DEEP_DARK = TagKey.create(Registries.BIOME, KaleidoscopeSculk.id("deep_dark"));
}
