package team.cqr.cqrepoured.crafting.smelting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import team.cqr.cqrepoured.init.CQRItems;

/**
 * Copyright (c) 30.06.2019 Developed by MrMarnic GitHub: https://github.com/MrMarnic
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

		Map<ItemStack, ItemStack> map = FurnaceRecipes.instance().getSmeltingList();

		Iterator<Map.Entry<ItemStack, ItemStack>> iterator = map.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<ItemStack, ItemStack> entry = iterator.next();
			if (toRemove.contains(entry.getKey().getItem())) {
				iterator.remove();
			}
		}

		// IRON ARMOR
		GameRegistry.addSmelting(Items.IRON_BOOTS, new ItemStack(Items.IRON_NUGGET, 4), 1);
		GameRegistry.addSmelting(Items.IRON_LEGGINGS, new ItemStack(Items.IRON_NUGGET, 7), 1);
		GameRegistry.addSmelting(Items.IRON_CHESTPLATE, new ItemStack(Items.IRON_NUGGET, 8), 1);
		GameRegistry.addSmelting(Items.IRON_HELMET, new ItemStack(Items.IRON_NUGGET, 5), 1);

		// GOLD ARMOR
		GameRegistry.addSmelting(Items.GOLDEN_BOOTS, new ItemStack(Items.IRON_NUGGET, 4), 1);
		GameRegistry.addSmelting(Items.GOLDEN_LEGGINGS, new ItemStack(Items.IRON_NUGGET, 7), 1);
		GameRegistry.addSmelting(Items.GOLDEN_CHESTPLATE, new ItemStack(Items.IRON_NUGGET, 8), 1);
		GameRegistry.addSmelting(Items.GOLDEN_HELMET, new ItemStack(Items.IRON_NUGGET, 5), 1);

		// HEAVY IRON ARMOR
		GameRegistry.addSmelting(CQRItems.BOOTS_HEAVY_IRON, new ItemStack(Items.IRON_NUGGET, 4), 1);
		GameRegistry.addSmelting(CQRItems.LEGGINGS_HEAVY_IRON, new ItemStack(Items.IRON_NUGGET, 7), 1);
		GameRegistry.addSmelting(CQRItems.CHESTPLATE_HEAVY_IRON, new ItemStack(Items.IRON_NUGGET, 8), 1);
		GameRegistry.addSmelting(CQRItems.HELMET_HEAVY_IRON, new ItemStack(Items.IRON_NUGGET, 5), 1);
		
		// CROWN
		GameRegistry.addSmelting(CQRItems.KING_CROWN, new ItemStack(Items.IRON_NUGGET, 3), 1);
		
		//Dyable iron armor
		GameRegistry.addSmelting(CQRItems.HELMET_IRON_DYABLE, new ItemStack(Items.IRON_NUGGET, 5), 1);
		GameRegistry.addSmelting(CQRItems.CHESTPLATE_IRON_DYABLE, new ItemStack(Items.IRON_NUGGET, 8), 1);
		GameRegistry.addSmelting(CQRItems.LEGGINGS_IRON_DYABLE, new ItemStack(Items.IRON_NUGGET, 7), 1);
		GameRegistry.addSmelting(CQRItems.BOOTS_IRON_DYABLE, new ItemStack(Items.IRON_NUGGET, 4), 1);
		
		//Iron dagger
		GameRegistry.addSmelting(CQRItems.DAGGER_IRON, new ItemStack(Items.IRON_INGOT), 0.5F);
		
		//Iron spear
		GameRegistry.addSmelting(CQRItems.SPEAR_IRON, new ItemStack(Items.IRON_NUGGET, 3), 0.25F);
		
		//Iron great sword
		GameRegistry.addSmelting(CQRItems.GREAT_SWORD_IRON, new ItemStack(Items.IRON_INGOT, 3), 1);
	}
}
