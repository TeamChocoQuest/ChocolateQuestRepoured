package com.tiviacz.chocolatequestrepoured.init;

import java.util.ArrayList;
import java.util.List;

import com.tiviacz.chocolatequestrepoured.init.base.ItemBase;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemCloudBoots;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class ModItems 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item GOLDEN_FEATHER = new ItemBase("golden_feather");
	
	public static final Item CLOUD_BOOTS = new ItemCloudBoots("cloud_boots", ItemCloudBoots.CLOUD, 1, EntityEquipmentSlot.FEET);

}
