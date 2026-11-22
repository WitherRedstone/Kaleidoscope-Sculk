package com.tequilacat.kaleidoscope_sculk.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface KSBlockTags {

    TagKey<Block> FARMERS_DELIGHT_HEAT_SOURCES = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("farmersdelight", "heat_sources"));
}
