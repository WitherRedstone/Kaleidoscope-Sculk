package com.tequilacat.kaleidoscope_sculk.item;

import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * 菜品物品。
 * <p>
 * 继承碗装食物物品（BowlFoodBlockItem），吃完后在创造模式下
 * 把容器物品返还给玩家，背包放不下时掉落在地。
 */
public class DishItem extends BowlFoodBlockItem {

    /** 容器物品，吃完后返还。 */
    private final ItemLike container;

    /**
     * 构造菜品物品。
     *
     * @param block          食物方块
     * @param foodProperties 食物属性
     * @param container      容器物品
     */
    public DishItem(Block block, FoodProperties foodProperties, ItemLike container) {
        super(block, foodProperties, container);
        this.container = container;
    }

    /**
     * 吃完食物后返还容器。
     * <p>
     * 客户端、非玩家或非创造模式直接走父类逻辑；
     * 创造模式下把容器塞进背包，塞不下则掉在地上。
     *
     * @param stack  食物物品
     * @param level  所在世界
     * @param entity 进食者
     * @return 进食后的物品
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (level.isClientSide || !(entity instanceof Player player) || !player.hasInfiniteMaterials()) {
            return result;
        }
        ItemStack containerStack = new ItemStack(container);
        if (!player.getInventory().add(containerStack)) {
            player.drop(containerStack, false);
        }
        return result;
    }
}
