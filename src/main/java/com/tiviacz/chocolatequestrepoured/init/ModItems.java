package com.tiviacz.chocolatequestrepoured.init;

import java.util.ArrayList;
import java.util.List;

import com.tiviacz.chocolatequestrepoured.init.base.ItemBase;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorBull;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorSlime;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorSpider;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorTurtle;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemBootsCloud;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemHelmetDragon;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemSwordBull;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemSwordSpider;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemSwordTurtle;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ModItems 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//Materials #TODO Balance
	public static final ArmorMaterial CLOUD = EnumHelper.addArmorMaterial("cloud_boots", Reference.MODID + ":cloud", 33, new int[]{4, 9, 7, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial DRAGON = EnumHelper.addArmorMaterial("dragon", Reference.MODID + ":dragon", 2850, new int[] { 4, 9, 7, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial BULL_ARMOR = EnumHelper.addArmorMaterial("bull", Reference.MODID + ":bull", 1580, new int[] { 2, 7, 5, 2 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial SPIDER_ARMOR = EnumHelper.addArmorMaterial("spider", Reference.MODID + ":spider", 1580, new int[] { 2, 7, 5, 2 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial SLIME_ARMOR = EnumHelper.addArmorMaterial("slime", Reference.MODID + ":slime", 1580, new int[] { 4, 9, 7, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial TURTLE_ARMOR = EnumHelper.addArmorMaterial("turtle", Reference.MODID + ":turtle", 675, new int[] { 3, 6, 8, 3 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ToolMaterial BULL_SWORD = EnumHelper.addToolMaterial("bull_sword", 0, 1410, 7.0F, 5F, 10);
	public static final ToolMaterial TURTLE_SWORD = EnumHelper.addToolMaterial("turtle_sword", 0, 1410, 7.0F, 3F, 10);
	public static final ToolMaterial SPIDER_SWORD = EnumHelper.addToolMaterial("bull_sword", 0, 1410, 7.0F, 5F, 10);
	
	//Cloud Items
	public static final Item GOLDEN_FEATHER = new ItemBase("golden_feather");
	public static final Item CLOUD_BOOTS = new ItemBootsCloud("cloud_boots", CLOUD, 1, EntityEquipmentSlot.FEET);
	
	//Slime Items
	public static final Item HELMET_SLIME = new ItemArmorSlime("helmet_slime", SLIME_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_SLIME = new ItemArmorSlime("chestplate_slime", SLIME_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_SLIME = new ItemArmorSlime("leggings_slime", SLIME_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_SLIME = new ItemArmorSlime("boots_slime", SLIME_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item SCALE_SLIME = new ItemBase("scale_slime");
	
	//Turtle Items
	public static final Item HELMET_TURTLE = new ItemArmorTurtle("helmet_turtle", TURTLE_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_TURTLE = new ItemArmorTurtle("chestplate_turtle", TURTLE_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_TURTLE = new ItemArmorTurtle("leggings_turtle", TURTLE_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_TURTLE = new ItemArmorTurtle("boots_turtle", TURTLE_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item SWORD_TURTLE = new ItemSwordTurtle("sword_turtle", TURTLE_SWORD);
	public static final Item SCALE_TURTLE = new ItemBase("scale_turtle");
	
	//Bull Items
	public static final Item HELMET_BULL = new ItemArmorBull("helmet_bull", BULL_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_BULL = new ItemArmorBull("chestplate_bull", BULL_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_BULL = new ItemArmorBull("leggings_bull", BULL_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_BULL = new ItemArmorBull("boots_bull", BULL_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item SWORD_BULL = new ItemSwordBull("sword_bull", BULL_SWORD);              //#TODO Fix holding animation
	public static final Item LEATHER_BULL = new ItemBase("leather_bull");
	
	//Spider Items
	public static final Item HELMET_SPIDER = new ItemArmorSpider("helmet_spider", SPIDER_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_SPIDER = new ItemArmorSpider("chestplate_spider", SPIDER_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_SPIDER = new ItemArmorSpider("leggings_spider", SPIDER_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_SPIDER = new ItemArmorSpider("boots_spider", SPIDER_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item SWORD_SPIDER = new ItemSwordSpider("sword_spider", SPIDER_SWORD);
	public static final Item LEATHER_SPIDER = new ItemBase("leather_spider");
	
	//Other
	public static final Item HELMET_DRAGON = new ItemHelmetDragon("helmet_dragon", DRAGON, 1, EntityEquipmentSlot.HEAD);  //#TODO Make model centered on head // Abandon for now
	public static final Item POTION_HEALING = new ItemPotionHealing("potion_healing");
}