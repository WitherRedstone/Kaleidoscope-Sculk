package com.tequilacat.kaleidoscope_sculk.data.tags;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSBlockTags;
import com.tequilacat.kaleidoscope_sculk.init.KSBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, KaleidoscopeSculk.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(KSBlocks.DEEPSLATE_STOVE.get());
        tag(Tags.Blocks.NEEDS_WOOD_TOOL)
                .add(KSBlocks.DEEPSLATE_STOVE.get());

        // ==================== 农夫乐事 ====================
        tag(KSBlockTags.FARMERS_DELIGHT_HEAT_SOURCES)
                .add(KSBlocks.DEEPSLATE_STOVE.get());
    }
}
