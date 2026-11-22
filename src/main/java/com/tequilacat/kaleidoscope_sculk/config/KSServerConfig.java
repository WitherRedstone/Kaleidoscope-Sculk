package com.tequilacat.kaleidoscope_sculk.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class KSServerConfig {

    public static final ModConfigSpec.IntValue REAP_COOLDOWN_SECONDS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> REAP_BLACKLIST;

    public static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        BUILDER.comment("通用配置").push("Common Config");
        REAP_COOLDOWN_SECONDS = BUILDER
                .comment("魂幡收割的冷却时间（秒）")
                .comment("Soul Sail cooldown.")
                .defineInRange("cooldownSeconds", 600, 60, 1200);
        REAP_BLACKLIST = BUILDER
                .comment("永远不能被收割的实体列表")
                .comment("Entity ids that can never be reaped.")
                .defineList("blacklist", List.of(), () -> "", entry -> entry instanceof String);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }


}
