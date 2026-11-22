package com.tequilacat.kaleidoscope_sculk.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import org.lwjgl.glfw.GLFW;

/**
 * 模组按键映射类。
 * <p>
 * 定义音波与灵魂帆两个游戏内按键及其分类，
 * 并在按键映射注册事件中向客户端注册。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID, value = Dist.CLIENT)
public final class KSKeys {

    /** 按键分类的翻译键。 */
    public static final String KEY_CATEGORY = "key.category.kaleidoscope_sculk";

    /** 音波按键（默认 V 键）。 */
    public static final KeyMapping SONIC_BOOM_KEY = new KeyMapping(
            "key.kaleidoscope_sculk.sonic_boom",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            KEY_CATEGORY
    );

    /** 灵魂帆按键（默认 I 键）。 */
    public static final KeyMapping SOUL_SAIL_KEY = new KeyMapping(
            "key.kaleidoscope_sculk.soul_sail",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            KEY_CATEGORY
    );

    /**
     * 注册按键映射。
     *
     * @param event 按键映射注册事件
     */
    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(SONIC_BOOM_KEY);
        event.register(SOUL_SAIL_KEY);
    }
}