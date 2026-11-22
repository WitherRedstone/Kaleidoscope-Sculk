package com.tequilacat.kaleidoscope_sculk.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 掉落物工具。
 */
public final class DropHelper {

    /**
     * 生成一个带默认拾取延迟的掉落物。
     *
     * @param level 世界
     * @param x     x 坐标
     * @param y     y 坐标
     * @param z     z 坐标
     * @param stack 物品堆叠
     * @return 掉落物实体
     */
    public static ItemEntity createDrop(Level level, double x, double y, double z, ItemStack stack) {
        ItemEntity drop = new ItemEntity(level, x, y, z, stack);
        drop.setDefaultPickUpDelay();
        return drop;
    }
}
