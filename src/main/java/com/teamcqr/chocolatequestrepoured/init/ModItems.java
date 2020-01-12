package com.teamcqr.chocolatequestrepoured.init;

import static com.teamcqr.chocolatequestrepoured.util.InjectionUtil.Null;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRBoarman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGremlin;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGolem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMandril;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRNPC;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROgre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROrc;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPirate;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSpectre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRWalker;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRZombie;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemHookshot;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemLongshot;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemBullBattleAxe;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemGoldenFeather;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemTeleportStone;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemSoulBottle;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemMobToSpawner;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemBadge;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemAlchemyBag;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemStructureSelector;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemSpawnerConverter;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemDungeonPlacer;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemSpawnEggCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorBull;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorDyable;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorHeavy;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorSlime;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorSpider;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorTurtle;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemBackpack;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemBootsCloud;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemHelmetDragon;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemBullet;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemCannonBall;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemFlamethrower;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusket;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusketKnife;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemRevolver;
import com.teamcqr.chocolatequestrepoured.objects.items.shields.ItemShieldCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.shields.ItemShieldWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.items.spears.ItemSpearBase;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaff;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffFire;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffGun;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffHealing;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffPoison;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffSpider;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffThunder;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffVampiric;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffWind;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemDagger;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemDaggerNinja;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemGreatSword;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemSwordMoonlight;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemSwordSpider;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemSwordSunshine;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemSwordTurtle;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemSwordWalker;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class ModItems {

	// Daggers
	public static final Item DAGGER_IRON = Null();
	public static final Item DAGGER_DIAMOND = Null();
	public static final Item DAGGER_NINJA = Null();
	public static final Item DAGGER_MONKING = Null();

	// Swords
	public static final Item SWORD_TURTLE = Null();
	public static final Item SWORD_SPIDER = Null();
	public static final Item SWORD_MOONLIGHT = Null();
	public static final Item SWORD_SUNSHINE = Null();

	// Battle Axes
	public static final Item BATTLE_AXE_BULL = Null();

	// Walker items
	public static final Item SWORD_WALKER = Null();
	public static final Item SHIELD_WALKER_KING = Null();

	// Shields
	public static final Item SHIELD_BULL = Null();
	public static final Item SHIELD_CARL = Null();
	public static final Item SHIELD_DRAGONSLAYER = Null();
	public static final Item SHIELD_FIRE = Null();
	public static final Item SHIELD_GOBLIN = Null();
	public static final Item SHIELD_MONKING = Null();
	public static final Item SHIELD_MOON = Null();
	public static final Item SHIELD_MUMMY = Null();
	public static final Item SHIELD_PIGMAN = Null();
	public static final Item SHIELD_PIRATE = Null();
	public static final Item SHIELD_PIRATE2 = Null();
	public static final Item SHIELD_RAINBOW = Null();
	public static final Item SHIELD_REFLECTIVE = Null();
	public static final Item SHIELD_RUSTED = Null();
	public static final Item SHIELD_SKELETON_FRIENDS = Null();
	public static final Item SHIELD_SPECTER = Null();
	public static final Item SHIELD_SPIDER = Null();
	public static final Item SHIELD_SUN = Null();
	public static final Item SHIELD_TOMB = Null();
	public static final Item SHIELD_TRITON = Null();
	public static final Item SHIELD_TURTLE = Null();
	public static final Item SHIELD_WARPED = Null();
	public static final Item SHIELD_WALKER = Null();
	public static final Item SHIELD_ZOMBIE = Null();

	// Great Swords
	public static final Item GREAT_SWORD_IRON = Null();
	public static final Item GREAT_SWORD_DIAMOND = Null();
	public static final Item GREAT_SWORD_BULL = Null();
	public static final Item GREAT_SWORD_MONKING = Null();

	// Spears
	public static final Item SPEAR_DIAMOND = Null();
	public static final Item SPEAR_IRON = Null();

	// Staves
	public static final Item STAFF = Null();
	public static final Item STAFF_FIRE = Null();
	public static final Item STAFF_VAMPIRIC = Null(); // #TODO DESCRIPTION
	public static final Item STAFF_WIND = Null(); // #TODO DESCRIPTION
	public static final Item STAFF_POISON = Null(); // #TODO DESCRIPTION
	public static final Item STAFF_HEALING = Null();
	public static final Item STAFF_THUNDER = Null();
	public static final Item STAFF_SPIDER = Null();
	public static final Item STAFF_GUN = Null(); // #TODO TEXTURES

	// Guns
	public static final Item REVOLVER = Null();
	public static final Item CAPTAIN_REVOLVER = Null();
	public static final Item MUSKET = Null();
	public static final Item MUSKET_DAGGER_IRON = Null(); // #TODO TEXTURES
	public static final Item MUSKET_DAGGER_DIAMOND = Null(); // #TODO TEXTURES
	public static final Item MUSKET_DAGGER_MONKING = Null(); // #TODO TEXTURES
	public static final Item BULLET_IRON = Null();
	public static final Item BULLET_GOLD = Null();
	public static final Item BULLET_DIAMOND = Null();
	public static final Item BULLET_FIRE = Null();
	public static final Item CANNON_BALL = Null();
	public static final Item FLAMETHROWER = Null(); // #TODO TEXTURES

	// Single Armor Items
	public static final Item HELMET_DRAGON = Null(); // #TODO Make model centered on head // Abandon for now
	public static final Item BOOTS_CLOUD = Null();
	public static final Item BACKPACK = Null();

	// Slime Armor Items
	public static final Item HELMET_SLIME = Null();
	public static final Item CHESTPLATE_SLIME = Null();
	public static final Item LEGGINGS_SLIME = Null();
	public static final Item BOOTS_SLIME = Null();

	// Turtle Armor Items
	public static final Item HELMET_TURTLE = Null();
	public static final Item CHESTPLATE_TURTLE = Null();
	public static final Item LEGGINGS_TURTLE = Null();
	public static final Item BOOTS_TURTLE = Null();

	// Bull Armor Items
	public static final Item HELMET_BULL = Null();
	public static final Item CHESTPLATE_BULL = Null();
	public static final Item LEGGINGS_BULL = Null();
	public static final Item BOOTS_BULL = Null();

	// Spider Armor Items
	public static final Item HELMET_SPIDER = Null();
	public static final Item CHESTPLATE_SPIDER = Null();
	public static final Item LEGGINGS_SPIDER = Null();
	public static final Item BOOTS_SPIDER = Null();

	// Inquisition Armor Items
	public static final Item HELMET_INQUISITION = Null();
	public static final Item CHESTPLATE_INQUISITION = Null();
	public static final Item LEGGINGS_INQUISITION = Null();
	public static final Item BOOTS_INQUISITION = Null();

	// Heavy Diamond Armor Items
	public static final Item HELMET_HEAVY_DIAMOND = Null();
	public static final Item CHESTPLATE_HEAVY_DIAMOND = Null();
	public static final Item LEGGINGS_HEAVY_DIAMOND = Null();
	public static final Item BOOTS_HEAVY_DIAMOND = Null();

	// Heavy Iron Armor Items
	public static final Item HELMET_HEAVY_IRON = Null();
	public static final Item CHESTPLATE_HEAVY_IRON = Null();
	public static final Item LEGGINGS_HEAVY_IRON = Null();
	public static final Item BOOTS_HEAVY_IRON = Null();

	// Dyable Iron Armor
	public static final Item HELMET_IRON_DYABLE = Null();
	public static final Item CHESTPLATE_IRON_DYABLE = Null();
	public static final Item LEGGINGS_IRON_DYABLE = Null();
	public static final Item BOOTS_IRON_DYABLE = Null();

	// Dyable Diamond Armor
	public static final Item HELMET_DIAMOND_DYABLE = Null();
	public static final Item CHESTPLATE_DIAMOND_DYABLE = Null();
	public static final Item LEGGINGS_DIAMOND_DYABLE = Null();
	public static final Item BOOTS_DIAMOND_DYABLE = Null();

	// Ingridients
	public static final Item SCALE_TURTLE = Null();
	public static final Item LEATHER_BULL = Null();
	public static final Item HORN_BULL = Null();
	public static final Item BALL_SLIME = Null();
	public static final Item LEATHER_SPIDER = Null();
	public static final Item BONE_MONKING = Null();
	public static final Item GIANT_SPIDER_POISON = Null();
	public static final Item FEATHER_GOLDEN = Null();

	// Other
	public static final Item POTION_HEALING = Null();
	public static final Item TELEPORT_STONE = Null();
	public static final Item SOUL_BOTTLE = Null();
	public static final Item BADGE = Null();
	public static final Item ALCHEMY_BAG = Null();

	// Dungeon tools
	public static final Item MOB_TO_SPAWNER_TOOL = Null();
	public static final Item SPAWNER_CONVERTER = Null();
	public static final Item STRUCTURE_SELECTOR = Null();

	@EventBusSubscriber(modid = Reference.MODID)
	public static class ItemRegistrationHandler {

		public static final List<Item> ITEMS = new ArrayList<Item>();
		public static final List<Item> SPAWN_EGGS = new ArrayList<Item>();

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final Item[] items = {
					setItemName(new ItemDagger(ModMaterials.ToolMaterials.TOOL_IRON_DAGGER, 25), "dagger_iron"),
					setItemName(new ItemDagger(ModMaterials.ToolMaterials.TOOL_DIAMOND_DAGGER, 20), "dagger_diamond"),
					setItemName(new ItemDaggerNinja(ModMaterials.ToolMaterials.TOOL_NINJA_DAGGER, 15), "dagger_ninja"),
					setItemName(new ItemDagger(ModMaterials.ToolMaterials.TOOL_MONKING_DAGGER, 10), "dagger_monking"),

					setItemName(new ItemSwordTurtle(ModMaterials.ToolMaterials.TOOL_TURTLE_SWORD), "sword_turtle"),
					setItemName(new ItemSwordSpider(ModMaterials.ToolMaterials.TOOL_SPIDER_SWORD), "sword_spider"),
					setItemName(new ItemSwordMoonlight(ModMaterials.ToolMaterials.TOOL_MOONLIGHT_SWORD), "sword_moonlight"),
					setItemName(new ItemSwordSunshine(ModMaterials.ToolMaterials.TOOL_SUNSHINE_SWORD), "sword_sunshine"),

					setItemName(new ItemBullBattleAxe(ModMaterials.ToolMaterials.TOOL_BULL_BATTLE_AXE), "battle_axe_bull"),

					setItemName(new ItemSwordWalker(ModMaterials.ToolMaterials.TOOL_WALKER_SWORD), "sword_walker"),
					setItemName(new ItemShieldWalkerKing(), "shield_walker_king"),

					setItemName(new ItemGreatSword(ModMaterials.ToolMaterials.TOOL_IRON_GREAT_SWORD, 0.8F, 30, -0.8F), "great_sword_iron"),
					setItemName(new ItemGreatSword(ModMaterials.ToolMaterials.TOOL_DIAMOND_GREAT_SWORD, 0.9F, 25, -0.7F), "great_sword_diamond"),
					setItemName(new ItemGreatSword(ModMaterials.ToolMaterials.TOOL_BULL_GREAT_SWORD, 1F, 20, -0.6F), "great_sword_bull"),
					setItemName(new ItemGreatSword(ModMaterials.ToolMaterials.TOOL_MONKING_GREAT_SWORD, 2F, 20, -0.6F), "great_sword_monking"),

					setItemName(new ItemSpearBase(ModMaterials.ToolMaterials.TOOL_DIAMOND_SPEAR, 10F, -0.5F), "spear_diamond"),
					setItemName(new ItemSpearBase(ModMaterials.ToolMaterials.TOOL_IRON_SPEAR, 10F, -0.5F), "spear_iron"),

					setItemName(new ItemStaff(), "staff"),
					setItemName(new ItemStaffFire(), "staff_fire"),
					setItemName(new ItemStaffVampiric(), "staff_vampiric"),
					setItemName(new ItemStaffWind(), "staff_wind"),
					setItemName(new ItemStaffPoison(), "staff_poison"),
					setItemName(new ItemStaffHealing(), "staff_healing"),
					setItemName(new ItemStaffThunder(), "staff_thunder"),
					setItemName(new ItemStaffSpider(), "staff_spider"),
					setItemName(new ItemStaffGun(), "staff_gun"),

					setItemName(new ItemRevolver(), "revolver"),
					setItemName(new ItemRevolver(), "captain_revolver"),
					setItemName(new ItemMusket(), "musket"),
					setItemName(new ItemMusketKnife(ModMaterials.ToolMaterials.TOOL_MUSKET_IRON), "musket_dagger_iron"),
					setItemName(new ItemMusketKnife(ModMaterials.ToolMaterials.TOOL_MUSKET_DIAMOND), "musket_dagger_diamond"),
					setItemName(new ItemMusketKnife(ModMaterials.ToolMaterials.TOOL_MUSKET_MONKING), "musket_dagger_monking"),
					setItemName(new ItemBullet(), "bullet_iron"),
					setItemName(new ItemBullet(), "bullet_gold"),
					setItemName(new ItemBullet(), "bullet_diamond"),
					setItemName(new ItemBullet(), "bullet_fire"),
					setItemName(new ItemCannonBall(), "cannon_ball"),
					setItemName(new ItemFlamethrower(), "flamethrower"),

					setItemName(new ItemHookshot(), "hookshot"),
					setItemName(new ItemLongshot(), "longshot"),

					setItemName(new ItemHelmetDragon(ModMaterials.ArmorMaterials.ARMOR_DRAGON, -1, EntityEquipmentSlot.HEAD), "helmet_dragon"),
					setItemName(new ItemBootsCloud(ModMaterials.ArmorMaterials.ARMOR_CLOUD, -1, EntityEquipmentSlot.FEET), "boots_cloud"),
					setItemName(new ItemBackpack(ModMaterials.ArmorMaterials.ARMOR_BACKPACK, -1, EntityEquipmentSlot.CHEST), "backpack"),

					setItemName(new ItemArmorSlime(ModMaterials.ArmorMaterials.ARMOR_SLIME, -1, EntityEquipmentSlot.HEAD), "helmet_slime"),
					setItemName(new ItemArmorSlime(ModMaterials.ArmorMaterials.ARMOR_SLIME, -1, EntityEquipmentSlot.CHEST), "chestplate_slime"),
					setItemName(new ItemArmorSlime(ModMaterials.ArmorMaterials.ARMOR_SLIME, -1, EntityEquipmentSlot.LEGS), "leggings_slime"),
					setItemName(new ItemArmorSlime(ModMaterials.ArmorMaterials.ARMOR_SLIME, -1, EntityEquipmentSlot.FEET), "boots_slime"),

					setItemName(new ItemArmorTurtle(ModMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EntityEquipmentSlot.HEAD), "helmet_turtle"),
					setItemName(new ItemArmorTurtle(ModMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EntityEquipmentSlot.CHEST), "chestplate_turtle"),
					setItemName(new ItemArmorTurtle(ModMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EntityEquipmentSlot.LEGS), "leggings_turtle"),
					setItemName(new ItemArmorTurtle(ModMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EntityEquipmentSlot.FEET), "boots_turtle"),

					setItemName(new ItemArmorBull(ModMaterials.ArmorMaterials.ARMOR_BULL, -1, EntityEquipmentSlot.HEAD), "helmet_bull"),
					setItemName(new ItemArmorBull(ModMaterials.ArmorMaterials.ARMOR_BULL, -1, EntityEquipmentSlot.CHEST), "chestplate_bull"),
					setItemName(new ItemArmorBull(ModMaterials.ArmorMaterials.ARMOR_BULL, -1, EntityEquipmentSlot.LEGS), "leggings_bull"),
					setItemName(new ItemArmorBull(ModMaterials.ArmorMaterials.ARMOR_BULL, -1, EntityEquipmentSlot.FEET), "boots_bull"),

					setItemName(new ItemArmorSpider(ModMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EntityEquipmentSlot.HEAD), "helmet_spider"),
					setItemName(new ItemArmorSpider(ModMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EntityEquipmentSlot.CHEST), "chestplate_spider"),
					setItemName(new ItemArmorSpider(ModMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EntityEquipmentSlot.LEGS), "leggings_spider"),
					setItemName(new ItemArmorSpider(ModMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EntityEquipmentSlot.FEET), "boots_spider"),

					setItemName(new ItemArmor(ModMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EntityEquipmentSlot.HEAD), "helmet_inquisition"),
					setItemName(new ItemArmor(ModMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EntityEquipmentSlot.CHEST), "chestplate_inquisition"),
					setItemName(new ItemArmor(ModMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EntityEquipmentSlot.LEGS), "leggings_inquisition"),
					setItemName(new ItemArmor(ModMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EntityEquipmentSlot.FEET), "boots_inquisition"),

					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EntityEquipmentSlot.HEAD), "helmet_heavy_diamond"),
					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EntityEquipmentSlot.CHEST), "chestplate_heavy_diamond"),
					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EntityEquipmentSlot.LEGS), "leggings_heavy_diamond"),
					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EntityEquipmentSlot.FEET), "boots_heavy_diamond"),

					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EntityEquipmentSlot.HEAD), "helmet_heavy_iron"),
					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EntityEquipmentSlot.CHEST), "chestplate_heavy_iron"),
					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EntityEquipmentSlot.LEGS), "leggings_heavy_iron"),
					setItemName(new ItemArmorHeavy(ModMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EntityEquipmentSlot.FEET), "boots_heavy_iron"),

					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.DIAMOND_DYABLE, -1, EntityEquipmentSlot.HEAD), "helmet_diamond_dyable"),
					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.DIAMOND_DYABLE, -1, EntityEquipmentSlot.CHEST), "chestplate_diamond_dyable"),
					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.DIAMOND_DYABLE, -1, EntityEquipmentSlot.LEGS), "leggings_diamond_dyable"),
					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.DIAMOND_DYABLE, -1, EntityEquipmentSlot.FEET), "boots_diamond_dyable"),

					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.IRON_DYABLE, -1, EntityEquipmentSlot.HEAD), "helmet_iron_dyable"),
					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.IRON_DYABLE, -1, EntityEquipmentSlot.CHEST), "chestplate_iron_dyable"),
					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.IRON_DYABLE, -1, EntityEquipmentSlot.LEGS), "leggings_iron_dyable"),
					setItemName(new ItemArmorDyable(ModMaterials.ArmorMaterials.IRON_DYABLE, -1, EntityEquipmentSlot.FEET), "boots_iron_dyable"),

					setItemName(new Item(), "scale_turtle"),
					setItemName(new Item(), "leather_bull"),
					setItemName(new Item(), "horn_bull"),
					setItemName(new Item(), "ball_slime"),
					setItemName(new Item(), "leather_spider"),
					setItemName(new Item(), "bone_monking"),
					setItemName(new Item(), "giant_spider_poison"),
					setItemName(new ItemGoldenFeather(), "feather_golden"),

					setItemName(new ItemPotionHealing(), "potion_healing"),
					setItemName(new ItemTeleportStone(), "teleport_stone"),
					setItemName(new ItemSoulBottle(), "soul_bottle"),
					setItemName(new ItemMobToSpawner(), "mob_to_spawner_tool"),
					setItemName(new ItemBadge(), "badge"),
					setItemName(new ItemAlchemyBag(), "alchemy_bag"),

					setItemName(new ItemStructureSelector(), "structure_selector"),
					setItemName(new ItemSpawnerConverter(), "spawner_converter") };

			IForgeRegistry<Item> registry = event.getRegistry();

			for (Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}

			for (int i = 0; i < ItemShieldCQR.SHIELD_NAMES.length; i++) {
				Item item = setItemName(new ItemShieldCQR(1024, Items.IRON_INGOT), "shield_" + ItemShieldCQR.SHIELD_NAMES[i]);
				registry.register(item);
				ITEMS.add(item);
			}

			for (int i = 0; i <= ItemDungeonPlacer.HIGHEST_ICON_NUMBER; i++) {
				Item item = setItemNameAndTab(new ItemDungeonPlacer(i), "dungeon_placer_d" + i, CQRMain.CQRDungeonPlacerTab);
				registry.register(item);
				ITEMS.add(item);
			}

			registerSpawnEggs(EntityCQRBoarman.class, "boarman", registry);
			registerSpawnEggs(EntityCQRDummy.class, "dummy", registry);
			registerSpawnEggs(EntityCQRDwarf.class, "dwarf", registry);
			registerSpawnEggs(EntityCQREnderman.class, "enderman", registry);
			registerSpawnEggs(EntityCQRGremlin.class, "gremlin", registry);
			registerSpawnEggs(EntityCQRGolem.class, "golem", registry);
			registerSpawnEggs(EntityCQRIllager.class, "illager", registry);
			registerSpawnEggs(EntityCQRMandril.class, "mandril", registry);
			registerSpawnEggs(EntityCQRMinotaur.class, "minotaur", registry);
			registerSpawnEggs(EntityCQRMummy.class, "mummy", registry);
			registerSpawnEggs(EntityCQRNPC.class, "npc", registry);
			registerSpawnEggs(EntityCQROgre.class, "ogre", registry);
			registerSpawnEggs(EntityCQROrc.class, "orc", registry);
			registerSpawnEggs(EntityCQRPirate.class, "pirate", registry);
			registerSpawnEggs(EntityCQRSkeleton.class, "skeleton", registry);
			registerSpawnEggs(EntityCQRSpectre.class, "spectre", registry);
			registerSpawnEggs(EntityCQRTriton.class, "triton", registry);
			registerSpawnEggs(EntityCQRWalker.class, "walker", registry);
			registerSpawnEggs(EntityCQRZombie.class, "zombie", registry);
		}

		private static void registerSpawnEggs(Class<? extends AbstractEntityCQR> entityClass, String entityName, IForgeRegistry<Item> registry) {
			List<Item> spawnEggList = ItemSpawnEggCQR.getItemList(entityClass, entityName);
			for (int i = 0; i < spawnEggList.size(); i++) {
				Item item = setItemNameAndTab(spawnEggList.get(i), "cqr_" + entityName + "_spawn_egg_" + i, CQRMain.CQRSpawnEggTab);
				registry.register(item);
				ITEMS.add(item);
				SPAWN_EGGS.add(item);
			}
		}

		private static Item setItemName(Item item, String name) {
			return setItemNameAndTab(item, name, CQRMain.CQRItemsTab);
		}

		private static Item setItemNameAndTab(Item item, String name, @Nullable CreativeTabs tab) {
			return item.setUnlocalizedName(name).setRegistryName(Reference.MODID, name).setCreativeTab(tab);
		}
	}

}
