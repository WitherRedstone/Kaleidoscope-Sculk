package com.tequilacat.kaleidoscope_sculk.item;

import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

/**
 * 菜品物品。
 * <p>
 * 继承碗装食物物品（BowlFoodBlockItem），容器由方块掉落表在吃完时返还；
 * 这里只把返还的容器换成本菜品自己的那一种。
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
        super(block, foodProperties);
        this.container = container;
    }

    /**
     * 获取吃完后返还的容器。
     *
     * @return 容器物品
     */
    @Override
    public Item getContainerItem() {
        return container.asItem();
    }
}
