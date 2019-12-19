package crafting.smelting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.init.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Copyright (c) 30.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class SmeltingHandler {
    public static void init() {

        ArrayList<Item> toRemove = new ArrayList<>();
        toRemove.add(Items.IRON_BOOTS);
        toRemove.add(Items.IRON_LEGGINGS);
        toRemove.add(Items.IRON_CHESTPLATE);
        toRemove.add(Items.IRON_HELMET);

        toRemove.add(Items.GOLDEN_BOOTS);
        toRemove.add(Items.GOLDEN_LEGGINGS);
        toRemove.add(Items.GOLDEN_CHESTPLATE);
        toRemove.add(Items.GOLDEN_HELMET);

        Map<ItemStack,ItemStack> map = FurnaceRecipes.instance().getSmeltingList();

        Iterator<Map.Entry<ItemStack,ItemStack>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<ItemStack,ItemStack> entry = iterator.next();
            if(toRemove.contains(entry.getKey().getItem())) {
                iterator.remove();
            }
        }


        //IRON ARMOR
        GameRegistry.addSmelting(Items.IRON_BOOTS,new ItemStack(Items.IRON_INGOT,4),1);
        GameRegistry.addSmelting(Items.IRON_LEGGINGS,new ItemStack(Items.IRON_INGOT,7),1);
        GameRegistry.addSmelting(Items.IRON_CHESTPLATE,new ItemStack(Items.IRON_INGOT,8),1);
        GameRegistry.addSmelting(Items.IRON_HELMET,new ItemStack(Items.IRON_INGOT,5),1);

        //GOLD ARMOR
        GameRegistry.addSmelting(Items.GOLDEN_BOOTS,new ItemStack(Items.GOLD_INGOT,4),1);
        GameRegistry.addSmelting(Items.GOLDEN_LEGGINGS,new ItemStack(Items.GOLD_INGOT,7),1);
        GameRegistry.addSmelting(Items.GOLDEN_CHESTPLATE,new ItemStack(Items.GOLD_INGOT,8),1);
        GameRegistry.addSmelting(Items.GOLDEN_HELMET,new ItemStack(Items.GOLD_INGOT,5),1);

        //HEAVY IRON ARMOR
        GameRegistry.addSmelting(ModItems.BOOTS_HEAVY_IRON,new ItemStack(Items.IRON_INGOT,4),1);
        GameRegistry.addSmelting(ModItems.LEGGINGS_HEAVY_IRON,new ItemStack(Items.IRON_INGOT,7),1);
        GameRegistry.addSmelting(ModItems.CHESTPLATE_HEAVY_IRON,new ItemStack(Items.IRON_INGOT,8),1);
        GameRegistry.addSmelting(ModItems.HELMET_HEAVY_IRON,new ItemStack(Items.IRON_INGOT,5),1);
    }
}
