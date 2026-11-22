package com.tequilacat.kaleidoscope_sculk.data;

import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider registries) {
        Builder<Compostable, Item> compostables = builder(NeoForgeDataMaps.COMPOSTABLES);

        compostables.add(KSItems.ECHO_SEED, new Compostable(0.3F, true), false);
        compostables.add(KSItems.SCULK_PINAPPLE, new Compostable(0.65F, true), false);
    }
}
