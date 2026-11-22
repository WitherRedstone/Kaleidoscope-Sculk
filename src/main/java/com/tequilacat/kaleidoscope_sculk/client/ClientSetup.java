package com.tequilacat.kaleidoscope_sculk.client;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * 客户端初始化类。
 * <p>
 * 注册实体渲染器：为远古骨片弹射物绑定通用的投掷物品渲染器。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID, value = Dist.CLIENT)
public class ClientSetup {

    /**
     * 注册实体渲染器。
     * <p>
     * 为远古骨片弹射物注册投掷物品渲染器，使其以物品形式显示。
     *
     * @param event 实体渲染器注册事件
     */
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(KSEntities.ANCIENT_BONE_FRAGMENT_PROJECTILE.get(),
                ThrownItemRenderer::new);
    }
}
