package com.tequilacat.kaleidoscope_sculk.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 食物方块碰撞箱工具类。
 * <p>
 * 本模组的菜肴方块都沿用一餐多口的设定，这里集中定义盘子、垫布等通用形状。
 */
public class FoodShapes {

    /** 浅盘：放在方块底部的一层薄板。 */
    public static final VoxelShape PLATE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);

    /** 垫布：比盘子更薄的一层。 */
    public static final VoxelShape MAT = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);

    /** 中等高度的菜肴主体。 */
    public static final VoxelShape MEDIUM = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D);

    /** 中等高度的菜肴主体加垫布。 */
    public static final VoxelShape MEDIUM_WITH_MAT = Shapes.or(MEDIUM, MAT);
}
