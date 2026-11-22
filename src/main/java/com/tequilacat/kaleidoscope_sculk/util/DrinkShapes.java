package com.tequilacat.kaleidoscope_sculk.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 饮品碰撞箱工具类。
 * <p>
 * 为酒馆的饮品方块提供按堆叠数量区分的碰撞箱形状数组。
 */
public class DrinkShapes {

    /**
     * 构建一个盒体形状。
     *
     * @param x1 起始 X
     * @param y1 起始 Y
     * @param z1 起始 Z
     * @param x2 结束 X
     * @param y2 结束 Y
     * @param z2 结束 Z
     * @return 盒体形状
     */
    private static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    /**
     * 幽匿酿造酒的碰撞箱形状（最多 4 瓶）。
     *
     * @return 按堆叠数量区分的形状数组
     */
    public static VoxelShape[] sculkBrew() {
        return new VoxelShape[]{
                box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D),
                box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D),
                box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D),
                box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D)
        };
    }

    /**
     * 花雕酒的碰撞箱形状（最多 3 瓶）。
     *
     * @return 按堆叠数量区分的形状数组
     */
    public static VoxelShape[] huadiaoWine() {
        return new VoxelShape[]{
                box(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D),
                box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
                box(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D)
        };
    }

    /**
     * 红蓝酒的碰撞箱形状（最多 3 瓶）。
     *
     * @return 按堆叠数量区分的形状数组
     */
    public static VoxelShape[] honglanWine() {
        return new VoxelShape[]{
                box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
                box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D),
                box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D)
        };
    }
}
