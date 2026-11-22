package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.init.KSPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

/**
 * 酿造事件类。
 * <p>
 * 注册自定义酿造配方：以力量药水（强效）为基础，
 * 加入幽匿菌菇后酿造为深渊药水。
 */
@EventBusSubscriber(modid = KaleidoscopeSculk.MODID)
public final class BrewingEvents {

    /**
     * 注册酿造配方。
     * <p>
     * 向酿造构建器添加「强效力量药水 + 幽匿菌菇 → 深渊药水」的配方。
     *
     * @param event 酿造配方注册事件
     */
    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(
                Ingredient.of(potionStack(Potions.STRONG_STRENGTH)),
                Ingredient.of(KSItems.SCULK_FUNGUS.get()),
                potionStack(KSPotions.ABYSS_POTION)
        );
    }

    /**
     * 构造带指定药水内容的药水物品堆叠。
     *
     * @param potion 药水持有者
     * @return 药水物品堆叠
     */
    private static ItemStack potionStack(Holder<Potion> potion) {
        ItemStack stack = new ItemStack(Items.POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return stack;
    }
}
