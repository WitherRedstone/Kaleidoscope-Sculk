package com.tequilacat.kaleidoscope_sculk.config;


import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public final class KSServerConfig {

    public static final ForgeConfigSpec.IntValue REAP_COOLDOWN_SECONDS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REAP_BLACKLIST;

    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.comment("通用配置").push("Common Config");
        REAP_COOLDOWN_SECONDS = BUILDER
                .comment("魂幡收割的冷却时间（秒）")
                .comment("Soul Sail cooldown.")
                .defineInRange("cooldownSeconds", 600, 60, 1200);
        REAP_BLACKLIST = BUILDER
                .comment("永远不能被收割的实体列表")
                .comment("Entity ids that can never be reaped.")
                .defineList("blacklist", List.of(), obj -> obj instanceof String);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }


}
