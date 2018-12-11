package com.tiviacz.chocolatequestrepoured.init;

import java.util.ArrayList;
import java.util.List;

import com.tiviacz.chocolatequestrepoured.init.base.ArmorBase;
import com.tiviacz.chocolatequestrepoured.init.base.ItemBase;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorBull;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorHeavy;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorSlime;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorSpider;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorTurtle;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemBootsCloud;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemHelmetDragon;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemBullBattleAxe;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemDaggerBase;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemDaggerNinja;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemGreatSwordBase;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemSwordSpider;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemSwordTurtle;
import com.tiviacz.chocolatequestrepoured.objects.items.ItemTeleportStone;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Items;
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
	public static final ArmorMaterial ARMOR_CLOUD = EnumHelper.addArmorMaterial("armor_cloud", Reference.MODID + ":cloud", 33, new int[]{4, 7, 9, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial ARMOR_DRAGON = EnumHelper.addArmorMaterial("armor_dragon", Reference.MODID + ":dragon", 2850, new int[] { 4, 7, 9, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial ARMOR_BULL = EnumHelper.addArmorMaterial("armor_bull", Reference.MODID + ":bull", 1580, new int[] { 2, 5, 7, 2 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial ARMOR_SPIDER = EnumHelper.addArmorMaterial("armor_spider", Reference.MODID + ":spider", 1580, new int[] { 2, 5, 7, 2 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial ARMOR_SLIME = EnumHelper.addArmorMaterial("armor_slime", Reference.MODID + ":slime", 1580, new int[] { 4, 7, 9, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial ARMOR_TURTLE = EnumHelper.addArmorMaterial("armor_turtle", Reference.MODID + ":turtle", 675, new int[] { 3, 6, 8, 3 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial ARMOR_HEAVY_DIAMOND = EnumHelper.addArmorMaterial("armor_heavy_diamond", Reference.MODID + ":heavy_diamond", 2850, new int[] { 4, 7, 9, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial ARMOR_HEAVY_IRON = EnumHelper.addArmorMaterial("armor_heavy_iron", Reference.MODID + ":heavy_iron", 2850, new int[] { 4, 7, 9, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
	public static final ArmorMaterial ARMOR_INQUISITION = EnumHelper.addArmorMaterial("armor_inquisition", Reference.MODID + ":inquisition", 2850, new int[] { 3, 8, 8, 3 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ToolMaterial TOOL_MONKING_GREAT_SWORD = EnumHelper.addToolMaterial("tool_monking_great_sword", 0, 1410, 7.0F, 6F, 10);
	public static final ToolMaterial TOOL_BULL_GREAT_SWORD = EnumHelper.addToolMaterial("tool_bull_great_sword", 0, 1410, 7.0F, 5F, 10);
	public static final ToolMaterial TOOL_DIAMOND_GREAT_SWORD = EnumHelper.addToolMaterial("tool_diamond_great_sword", 0, 1410, 7.0F, 5F, 10);
	public static final ToolMaterial TOOL_IRON_GREAT_SWORD = EnumHelper.addToolMaterial("tool_iron_great_sword", 0, 1410, 7.0F, 4F, 10);
	public static final ToolMaterial TOOL_IRON_DAGGER = EnumHelper.addToolMaterial("tool_iron_dagger", 0, 1410, 7.0F, 0F, 10);
	public static final ToolMaterial TOOL_DIAMOND_DAGGER = EnumHelper.addToolMaterial("tool_diamond_dagger", 0, 1410, 7.0F, 1F, 10);
	public static final ToolMaterial TOOL_MONKING_DAGGER = EnumHelper.addToolMaterial("tool_monking_dagger", 0, 1410, 7.0F, 3F, 10);
	public static final ToolMaterial TOOL_NINJA_DAGGER = EnumHelper.addToolMaterial("tool_ninja_dagger", 0, 1410, 7.0F, 2F, 10);
	public static final ToolMaterial TOOL_BULL_BATTLE_AXE = EnumHelper.addToolMaterial("tool_bull_battle_axe", 0, 2048, 7.0F, 3F, 10);
	public static final ToolMaterial TOOL_TURTLE_SWORD = EnumHelper.addToolMaterial("tool_turtle_sword", 0, 2048, 7.0F, 3F, 10);
	public static final ToolMaterial TOOL_SPIDER_SWORD = EnumHelper.addToolMaterial("tool_spider_sword", 0, 1410, 7.0F, 5F, 10);
	
	//Daggers
	public static final Item DAGGER_IRON = new ItemDaggerBase("dagger_iron", TOOL_IRON_DAGGER, 25);
	public static final Item DAGGER_DIAMOND = new ItemDaggerBase("dagger_diamond", TOOL_DIAMOND_DAGGER, 20);
	public static final Item DAGGER_NINJA = new ItemDaggerNinja("dagger_ninja", TOOL_NINJA_DAGGER, 0);
	public static final Item DAGGER_MONKING = new ItemDaggerBase("dagger_monking", TOOL_MONKING_DAGGER, 5);  //#TODO Particle Spawning
	
	//Swords
	public static final Item SWORD_TURTLE = new ItemSwordTurtle("sword_turtle", TOOL_TURTLE_SWORD);
	public static final Item SWORD_SPIDER = new ItemSwordSpider("sword_spider", TOOL_SPIDER_SWORD);
	
	//Battle Axes
	public static final Item BATTLE_AXE_BULL = new ItemBullBattleAxe("battle_axe_bull", TOOL_BULL_BATTLE_AXE);   //#TODO (repair earthquake *known bug it cant go up*)
	
	//Great Swords
	public static final Item GREAT_SWORD_IRON = new ItemGreatSwordBase("great_sword_iron", TOOL_IRON_GREAT_SWORD, 0.8F, 100, -0.8F);
	public static final Item GREAT_SWORD_DIAMOND = new ItemGreatSwordBase("great_sword_diamond", TOOL_DIAMOND_GREAT_SWORD, 0.9F, 75, -0.7F);
	public static final Item GREAT_SWORD_BULL = new ItemGreatSwordBase("great_sword_bull", TOOL_BULL_GREAT_SWORD, 1F, 50, -0.6F);
	public static final Item GREAT_SWORD_MONKING = new ItemGreatSwordBase("great_sword_monking", TOOL_MONKING_GREAT_SWORD, 2F, 25, -0.6F);
	
	//Single Armor Items
	public static final Item HELMET_DRAGON = new ItemHelmetDragon("helmet_dragon", ARMOR_DRAGON, 1, EntityEquipmentSlot.HEAD);      //#TODO Make model centered on head // Abandon for now
	public static final Item BOOTS_CLOUD = new ItemBootsCloud("boots_cloud", ARMOR_CLOUD, 1, EntityEquipmentSlot.FEET);
	
	//Slime Armor Items
	public static final Item HELMET_SLIME = new ItemArmorSlime("helmet_slime", ARMOR_SLIME, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_SLIME = new ItemArmorSlime("chestplate_slime", ARMOR_SLIME, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_SLIME = new ItemArmorSlime("leggings_slime", ARMOR_SLIME, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_SLIME = new ItemArmorSlime("boots_slime", ARMOR_SLIME, 1, EntityEquipmentSlot.FEET);
	
	//Turtle Armor Items
	public static final Item HELMET_TURTLE = new ItemArmorTurtle("helmet_turtle", ARMOR_TURTLE, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_TURTLE = new ItemArmorTurtle("chestplate_turtle", ARMOR_TURTLE, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_TURTLE = new ItemArmorTurtle("leggings_turtle", ARMOR_TURTLE, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_TURTLE = new ItemArmorTurtle("boots_turtle", ARMOR_TURTLE, 1, EntityEquipmentSlot.FEET);
	
	//Bull Armor Items
	public static final Item HELMET_BULL = new ItemArmorBull("helmet_bull", ARMOR_BULL, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_BULL = new ItemArmorBull("chestplate_bull", ARMOR_BULL, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_BULL = new ItemArmorBull("leggings_bull", ARMOR_BULL, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_BULL = new ItemArmorBull("boots_bull", ARMOR_BULL, 1, EntityEquipmentSlot.FEET);
	
	//Spider Armor Items
	public static final Item HELMET_SPIDER = new ItemArmorSpider("helmet_spider", ARMOR_SPIDER, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_SPIDER = new ItemArmorSpider("chestplate_spider", ARMOR_SPIDER, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_SPIDER = new ItemArmorSpider("leggings_spider", ARMOR_SPIDER, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_SPIDER = new ItemArmorSpider("boots_spider", ARMOR_SPIDER, 1, EntityEquipmentSlot.FEET);
	
	//Inquisition Armor Items
	public static final Item HELMET_INQUISITION = new ArmorBase("helmet_inquisition", ARMOR_INQUISITION, 1, EntityEquipmentSlot.HEAD);
	public static final Item CHESTPLATE_INQUISITION = new ArmorBase("chestplate_inquisition", ARMOR_INQUISITION, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEGGINGS_INQUISITION = new ArmorBase("leggings_inquisition", ARMOR_INQUISITION, 2, EntityEquipmentSlot.LEGS);
	public static final Item BOOTS_INQUISITION = new ArmorBase("boots_inquisition", ARMOR_INQUISITION, 1, EntityEquipmentSlot.FEET);
			
	//Heavy Diamond Armor Items
	public static final Item HELMET_HEAVY_DIAMOND = new ItemArmorHeavy("helmet_heavy_diamond", ARMOR_HEAVY_DIAMOND, 1, EntityEquipmentSlot.HEAD, Items.DIAMOND);
	public static final Item CHESTPLATE_HEAVY_DIAMOND = new ItemArmorHeavy("chestplate_heavy_diamond", ARMOR_HEAVY_DIAMOND, 1, EntityEquipmentSlot.CHEST, Items.DIAMOND);
	public static final Item LEGGINGS_HEAVY_DIAMOND = new ItemArmorHeavy("leggings_heavy_diamond", ARMOR_HEAVY_DIAMOND, 2, EntityEquipmentSlot.LEGS, Items.DIAMOND);
	public static final Item BOOTS_HEAVY_DIAMOND = new ItemArmorHeavy("boots_heavy_diamond", ARMOR_HEAVY_DIAMOND, 1, EntityEquipmentSlot.FEET, Items.DIAMOND);
	
	//Heavy Iron Armor Items
	public static final Item HELMET_HEAVY_IRON = new ItemArmorHeavy("helmet_heavy_iron", ARMOR_HEAVY_IRON, 1, EntityEquipmentSlot.HEAD, Items.IRON_INGOT);
	public static final Item CHESTPLATE_HEAVY_IRON = new ItemArmorHeavy("chestplate_heavy_iron", ARMOR_HEAVY_IRON, 1, EntityEquipmentSlot.CHEST, Items.IRON_INGOT);
	public static final Item LEGGINGS_HEAVY_IRON = new ItemArmorHeavy("leggings_heavy_iron", ARMOR_HEAVY_IRON, 2, EntityEquipmentSlot.LEGS, Items.IRON_INGOT);
	public static final Item BOOTS_HEAVY_IRON = new ItemArmorHeavy("boots_heavy_iron", ARMOR_HEAVY_IRON, 1, EntityEquipmentSlot.FEET, Items.IRON_INGOT);
	
	//Ingridients
	public static final Item SCALE_TURTLE = new ItemBase("scale_turtle");
	public static final Item LEATHER_BULL = new ItemBase("leather_bull");
	public static final Item HORN_BULL = new ItemBase("horn_bull");
	public static final Item SCALE_SLIME = new ItemBase("scale_slime");
	public static final Item LEATHER_SPIDER = new ItemBase("leather_spider");
	public static final Item BONE_MONKING = new ItemBase("bone_monking");
	public static final Item FEATHER_GOLDEN = new ItemBase("feather_golden");
	
	//Other
	public static final Item POTION_HEALING = new ItemPotionHealing("potion_healing");
	public static final Item TELEPORT_STONE = new ItemTeleportStone("teleport_stone");    //#TODO Cooldown (Optional)   
}