package com.tequilacat.kaleidoscope_sculk.event;

import com.tequilacat.kaleidoscope_sculk.KaleidoscopeSculk;
import com.tequilacat.kaleidoscope_sculk.init.KSItems;
import com.tequilacat.kaleidoscope_sculk.init.KSPotions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * 酿造事件类。
 * <p>
 * 注册自定义酿造配方：以力量药水（强效）为基础，
 * 加入幽匿菌菇后酿造为深渊药水。
 */
@Mod.EventBusSubscriber(modid = KaleidoscopeSculk.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class BrewingEvents {

    /**
     * 注册酿造配方。
     * <p>
     * 向酿造注册表添加「强效力量药水 + 幽匿菌菇 → 深渊药水」的配方。
     *
     * @param event 通用初始化事件
     */
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(
                new PotionMixRecipe(
                        Potions.STRONG_STRENGTH,
                        KSItems.SCULK_FUNGUS.get(),
                        KSPotions.ABYSS_POTION.get())
        ));
    }

    /**
     * 基于药水种类的酿造配方。
     */
    private record PotionMixRecipe(Potion input, Item ingredient, Potion result) implements IBrewingRecipe {

        @Override
        public boolean isInput(ItemStack stack) {
            return stack.is(Items.POTION) && PotionUtils.getPotion(stack) == input;
        }

        @Override
        public boolean isIngredient(ItemStack stack) {
            return stack.is(ingredient);
        }

        @Override
        public ItemStack getOutput(ItemStack in, ItemStack ing) {
            if (!isInput(in) || !isIngredient(ing)) {
                return ItemStack.EMPTY;
            }
            return PotionUtils.setPotion(new ItemStack(Items.POTION), result);
        }
    }
}
