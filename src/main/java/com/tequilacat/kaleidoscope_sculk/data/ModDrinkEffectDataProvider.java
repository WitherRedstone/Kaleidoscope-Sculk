package com.tequilacat.kaleidoscope_sculk.data;

import com.github.ysbbbbbb.kaleidoscopetavern.datamap.data.DrinkEffectData;
import com.github.ysbbbbbb.kaleidoscopetavern.init.ModEffects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSEffects;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.ToIntFunction;

public class ModDrinkEffectDataProvider implements DataProvider {
    private static final int[] SLIGHTLY_TIPSY_DURATIONS = {45, 30, 30, 20, 10};

    private static final ToIntFunction<String> ORDER_FIELDS = Util.make(new Object2IntOpenHashMap<>(), map -> {
        map.put("item", 0);
        map.defaultReturnValue(1);
    });

    private final Map<String, DrinkEffectData> data = Maps.newLinkedHashMap();
    private final PackOutput output;

    public ModDrinkEffectDataProvider(PackOutput output) {
        this.output = output;
    }

    private void addEntry() {
        add(KSItems.SCULK_BREW,
                List.of(effect(KSEffects.ECHO.get(), 60, 0)),
                List.of(effect(KSEffects.ECHO.get(), 180, 0)),
                List.of(effect(KSEffects.ECHO.get(), 300, 0), effect(MobEffects.DAMAGE_RESISTANCE, 60, 1)),
                List.of(effect(KSEffects.ECHO.get(), 600, 0), effect(MobEffects.DAMAGE_RESISTANCE, 360, 1))
        );
        add(KSItems.HONGLAN_WINE,
                List.of(effect(MobEffects.DIG_SPEED, 80, 0), effect(MobEffects.DAMAGE_RESISTANCE, 80, 0)),
                List.of(effect(MobEffects.DIG_SPEED, 240, 0), effect(MobEffects.DAMAGE_RESISTANCE, 240, 0)),
                List.of(effect(MobEffects.DIG_SPEED, 240, 1), effect(MobEffects.DAMAGE_RESISTANCE, 240, 1)),
                List.of(effect(MobEffects.DIG_SPEED, 540, 1), effect(MobEffects.DAMAGE_RESISTANCE, 540, 1))
        );
        addAllLevels(KSItems.HUADIAO_WINE,
                List.of(effect(KSEffects.SONIC_WAVE.get(), 20, 4)),
                List.of(effect(KSEffects.SONIC_WAVE.get(), 40, 5)),
                List.of(effect(KSEffects.SONIC_WAVE.get(), 60, 6)),
                List.of(effect(KSEffects.SONIC_WAVE.get(), 80, 7)),
                List.of(effect(KSEffects.SONIC_WAVE.get(), 100, 8)),
                List.of(effect(KSEffects.SONIC_WAVE.get(), 120, 9))
        );
    }

    @SafeVarargs
    public final void add(RegistryObject<Item> key, List<DrinkEffectData.Entry>... levelAbove2) {
        var itemKey = ForgeRegistries.ITEMS.getKey(key.get());
        if (itemKey == null) {
            throw new IllegalArgumentException("Item not registered: " + key.getId());
        }
        this.add(itemKey.getPath(), key, levelAbove2);
    }

    private DrinkEffectData.Entry effect(MobEffect effect, int duration, int amplifier) {
        return new DrinkEffectData.Entry(effect, duration, amplifier, 1f);
    }

    private void addCocktail(RegistryObject<Item> key, DrinkEffectData.Entry... entries) {
        var itemKey = ForgeRegistries.ITEMS.getKey(key.get());
        if (itemKey == null) {
            throw new IllegalArgumentException("Item not registered: " + key.getId());
        }
        this.add(itemKey.getPath(), new DrinkEffectData(key.get(), List.of(List.of(entries))));
    }

    @SafeVarargs
    public final void add(String fileName, RegistryObject<Item> key, List<DrinkEffectData.Entry>... levelAbove2) {
        int length = levelAbove2.length;
        if (length == 0) {
            throw new IllegalArgumentException("At least one level above 2 must be provided");
        }
        this.addWithLevel2Effects(fileName, key,
                List.of(),
                levelAbove2[0],
                levelAbove2[Math.min(1, length - 1)],
                levelAbove2[Math.min(2, length - 1)],
                levelAbove2[Math.min(3, length - 1)]
        );
    }

    @SafeVarargs
    private void addWithLevel2Effects(RegistryObject<Item> key, List<DrinkEffectData.Entry>... levelAbove1) {
        var itemKey = ForgeRegistries.ITEMS.getKey(key.get());
        if (itemKey == null) {
            throw new IllegalArgumentException("Item not registered: " + key.getId());
        }
        this.addWithLevel2Effects(itemKey.getPath(), key, levelAbove1);
    }

    @SafeVarargs
    private void addWithLevel2Effects(String fileName, RegistryObject<Item> key,
                                      List<DrinkEffectData.Entry>... levelAbove1) {
        int length = levelAbove1.length;
        if (length == 0) {
            throw new IllegalArgumentException("At least one level above 1 must be provided");
        }

        var effects = Lists.<List<DrinkEffectData.Entry>>newArrayListWithExpectedSize(SLIGHTLY_TIPSY_DURATIONS.length + 1);
        // 等级 1，固定为反胃 30s
        effects.add(List.of(new DrinkEffectData.Entry(MobEffects.CONFUSION, 30, 0, 1f)));
        // 等级 2-6，除难以下咽外，都会额外附带对应时长的微醺效果
        for (int i = 0; i < SLIGHTLY_TIPSY_DURATIONS.length; i++) {
            effects.add(withSlightlyTipsy(SLIGHTLY_TIPSY_DURATIONS[i], levelAbove1[Math.min(i, length - 1)]));
        }

        this.add(fileName, new DrinkEffectData(key.get(), effects));
    }

    public void addAllLevels(RegistryObject<? extends Item> key,
                             List<DrinkEffectData.Entry> level1,
                             List<DrinkEffectData.Entry> level2,
                             List<DrinkEffectData.Entry> level3,
                             List<DrinkEffectData.Entry> level4,
                             List<DrinkEffectData.Entry> level5,
                             List<DrinkEffectData.Entry> level6) {
        var itemKey = BuiltInRegistries.ITEM.getKey(key.get());
        var effects = Lists.newArrayList(level1, level2, level3, level4, level5, level6);
        this.add(itemKey.getPath(), new DrinkEffectData(key.get(), effects));
    }

    private List<DrinkEffectData.Entry> withSlightlyTipsy(int duration, List<DrinkEffectData.Entry> entries) {
        var result = Lists.<DrinkEffectData.Entry>newArrayListWithExpectedSize(entries.size() + 1);
        result.add(effect(ModEffects.SLIGHTLY_TIPSY.get(), duration, 0));
        result.addAll(entries);
        return result;
    }

    public void add(String fileName, DrinkEffectData value) {
        this.data.put(fileName, value);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addEntry();

        List<CompletableFuture<?>> futures = Lists.newArrayList();
        var pathProvider = this.output.createPathProvider(PackOutput.Target.DATA_PACK, "datamap/drink_effect");

        for (var entry : data.entrySet()) {
            DrinkEffectData.CODEC
                    .encodeStart(JsonOps.INSTANCE, entry.getValue())
                    .resultOrPartial(KaleidoscopeSculk.LOGGER::error)
                    .ifPresent(json -> {
                        var filePath = pathProvider.json(KaleidoscopeSculk.id(entry.getKey()));
                        var future = this.saveStable(cache, json, filePath);
                        futures.add(future);
                    });
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @SuppressWarnings("all")
    private CompletableFuture<?> saveStable(CachedOutput output, JsonElement json, Path path) {
        return CompletableFuture.runAsync(() -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                HashingOutputStream hashing = new HashingOutputStream(Hashing.sha1(), stream);

                try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(hashing, StandardCharsets.UTF_8))) {
                    writer.setSerializeNulls(false);
                    writer.setIndent("  ");
                    GsonHelper.writeValue(writer, json, Comparator.comparingInt(ORDER_FIELDS));
                }

                output.writeIfNeeded(path, stream.toByteArray(), hashing.hash());
            } catch (IOException ioexception) {
                KaleidoscopeSculk.LOGGER.error("Failed to save file to {}", path, ioexception);
            }
        }, Util.backgroundExecutor());
    }

    @Override
    public String getName() {
        return "Drink Effect Data";
    }
}
