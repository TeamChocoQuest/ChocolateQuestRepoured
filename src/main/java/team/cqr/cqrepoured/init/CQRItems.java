package team.cqr.cqrepoured.init;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet.EBulletType;
import team.cqr.cqrepoured.item.CQRSpawnEggItem;
import team.cqr.cqrepoured.item.ItemAlchemyBag;
import team.cqr.cqrepoured.item.ItemBadge;
import team.cqr.cqrepoured.item.ItemBullBattleAxe;
import team.cqr.cqrepoured.item.ItemCursedBone;
import team.cqr.cqrepoured.item.ItemGoldenFeather;
import team.cqr.cqrepoured.item.ItemHookshot;
import team.cqr.cqrepoured.item.ItemLongshot;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.item.ItemMobToSpawner;
import team.cqr.cqrepoured.item.ItemPotionHealing;
import team.cqr.cqrepoured.item.ItemShieldDummy;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.item.ItemSpawnerConverter;
import team.cqr.cqrepoured.item.ItemSpiderHook;
import team.cqr.cqrepoured.item.ItemSpikedGlove;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.item.ItemSuperTool;
import team.cqr.cqrepoured.item.ItemTeleportStone;
import team.cqr.cqrepoured.item.ItemUnprotectedPositionTool;
import team.cqr.cqrepoured.item.armor.ItemArmorBull;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
import team.cqr.cqrepoured.item.armor.ItemArmorHeavy;
import team.cqr.cqrepoured.item.armor.ItemArmorInquisition;
import team.cqr.cqrepoured.item.armor.ItemArmorSlime;
import team.cqr.cqrepoured.item.armor.ItemArmorSpider;
import team.cqr.cqrepoured.item.armor.ItemArmorTurtle;
import team.cqr.cqrepoured.item.armor.ItemBackpack;
import team.cqr.cqrepoured.item.armor.ItemBootsCloud;
import team.cqr.cqrepoured.item.armor.ItemHelmetDragon;
import team.cqr.cqrepoured.item.gun.ItemBubblePistol;
import team.cqr.cqrepoured.item.gun.ItemBubbleRifle;
import team.cqr.cqrepoured.item.gun.ItemBullet;
import team.cqr.cqrepoured.item.gun.ItemCannonBall;
import team.cqr.cqrepoured.item.gun.ItemFlamethrower;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemMusketKnife;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.shield.ItemShieldCQR;
import team.cqr.cqrepoured.item.shield.ItemShieldWalkerKing;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.item.staff.ItemStaff;
import team.cqr.cqrepoured.item.staff.ItemStaffFire;
import team.cqr.cqrepoured.item.staff.ItemStaffGun;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;
import team.cqr.cqrepoured.item.staff.ItemStaffPoison;
import team.cqr.cqrepoured.item.staff.ItemStaffSpider;
import team.cqr.cqrepoured.item.staff.ItemStaffThunder;
import team.cqr.cqrepoured.item.staff.ItemStaffVampiric;
import team.cqr.cqrepoured.item.staff.ItemStaffWind;
import team.cqr.cqrepoured.item.sword.ItemDagger;
import team.cqr.cqrepoured.item.sword.ItemDaggerNinja;
import team.cqr.cqrepoured.item.sword.ItemFakeSwordHealingStaff;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;
import team.cqr.cqrepoured.item.sword.ItemSwordMoonlight;
import team.cqr.cqrepoured.item.sword.ItemSwordSpider;
import team.cqr.cqrepoured.item.sword.ItemSwordSunshine;
import team.cqr.cqrepoured.item.sword.ItemSwordTurtle;
import team.cqr.cqrepoured.item.sword.ItemSwordWalker;

public class CQRItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CQRMain.MODID);

	// Daggers
	public static final RegistryObject<ItemDagger> DAGGER_IRON = register("dagger_iron", prop -> new ItemDagger(prop, ItemTier.IRON, 25));
	public static final RegistryObject<ItemDagger> DAGGER_DIAMOND = register("dagger_diamond", prop -> new ItemDagger(prop, ItemTier.DIAMOND, 20));
	public static final RegistryObject<ItemDaggerNinja> DAGGER_NINJA = register("dagger_ninja", prop -> new ItemDaggerNinja(prop, CQRMaterials.CQRItemTiers.TOOL_NINJA, 15));
	public static final RegistryObject<ItemDagger> DAGGER_MONKING = register("dagger_monking", prop -> new ItemDagger(prop, CQRMaterials.CQRItemTiers.TOOL_MONKING, 10));

	// Swords
	public static final RegistryObject<ItemSwordTurtle> SWORD_TURTLE = register("sword_turtle", prop -> new ItemSwordTurtle(CQRMaterials.CQRItemTiers.TOOL_TURTLE, prop));
	public static final RegistryObject<ItemSwordSpider> SWORD_SPIDER = register("sword_spider", prop -> new ItemSwordSpider(CQRMaterials.CQRItemTiers.TOOL_SPIDER, prop));
	public static final RegistryObject<ItemSwordMoonlight> SWORD_MOONLIGHT = register("sword_moonlight", prop -> new ItemSwordMoonlight(CQRMaterials.CQRItemTiers.TOOL_MOONLIGHT, prop));
	public static final RegistryObject<ItemSwordSunshine> SWORD_SUNSHINE = register("sword_sunshine", prop -> new ItemSwordSunshine(CQRMaterials.CQRItemTiers.TOOL_SUNSHINE, prop));

	// Battle Axes
	public static final RegistryObject<ItemBullBattleAxe> BATTLE_AXE_BULL = register("battle_axe_bull", prop -> new ItemBullBattleAxe(CQRMaterials.CQRItemTiers.TOOL_BULL, prop));

	// Walker RegistryObject<Item>s
	public static final RegistryObject<ItemSwordWalker> SWORD_WALKER = register("sword_walker", prop -> new ItemSwordWalker(CQRMaterials.CQRItemTiers.TOOL_WALKER, prop));
	public static final RegistryObject<ItemShieldWalkerKing> SHIELD_WALKER_KING = register("shield_walker_king", ItemShieldWalkerKing::new);

	// Shields
	public static final RegistryObject<ItemShieldCQR> SHIELD_BULL = registerShield("bull");
	public static final RegistryObject<ItemShieldCQR> SHIELD_CARL = registerShield("carl");
	public static final RegistryObject<ItemShieldCQR> SHIELD_DRAGONSLAYER = registerShield("dragonslayer");
	public static final RegistryObject<ItemShieldCQR> SHIELD_FIRE = registerShield("fire");
	public static final RegistryObject<ItemShieldCQR> SHIELD_GOBLIN = registerShield("goblin");
	public static final RegistryObject<ItemShieldCQR> SHIELD_MONKING = registerShield("monking");
	public static final RegistryObject<ItemShieldCQR> SHIELD_MOON = registerShield("moon");
	public static final RegistryObject<ItemShieldCQR> SHIELD_MUMMY = registerShield("mummy");
	public static final RegistryObject<ItemShieldCQR> SHIELD_PIGMAN = registerShield("pigman");
	public static final RegistryObject<ItemShieldCQR> SHIELD_PIRATE = registerShield("pirate");
	public static final RegistryObject<ItemShieldCQR> SHIELD_PIRATE2 = registerShield("pirate_alt");
	public static final RegistryObject<ItemShieldCQR> SHIELD_RAINBOW = registerShield("rainbow");
	public static final RegistryObject<ItemShieldCQR> SHIELD_REFLECTIVE = registerShield("reflective");
	public static final RegistryObject<ItemShieldCQR> SHIELD_RUSTED = registerShield("rusted");
	public static final RegistryObject<ItemShieldCQR> SHIELD_SKELETON_FRIENDS = registerShield("skeleton_friends");
	public static final RegistryObject<ItemShieldCQR> SHIELD_SPECTER = registerShield("specter");
	public static final RegistryObject<ItemShieldCQR> SHIELD_SPIDER = registerShield("spider");
	public static final RegistryObject<ItemShieldCQR> SHIELD_SUN = registerShield("sun");
	public static final RegistryObject<ItemShieldCQR> SHIELD_TOMB = registerShield("tomb");
	public static final RegistryObject<ItemShieldCQR> SHIELD_TRITON = registerShield("triton");
	public static final RegistryObject<ItemShieldCQR> SHIELD_TURTLE = registerShield("turtle");
	public static final RegistryObject<ItemShieldCQR> SHIELD_WARPED = registerShield("warped");
	public static final RegistryObject<ItemShieldCQR> SHIELD_WALKER = registerShield("walker");
	public static final RegistryObject<ItemShieldCQR> SHIELD_ZOMBIE = registerShield("zombie");

	// Great Swords
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_IRON = register("great_sword_iron", prop -> new ItemGreatSword(prop, ItemTier.IRON, 30));
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_DIAMOND = register("great_sword_diamond", prop -> new ItemGreatSword(prop, ItemTier.DIAMOND, 25));
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_BULL = register("great_sword_bull", prop -> new ItemGreatSword(prop, CQRMaterials.CQRItemTiers.TOOL_BULL, 20));
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_MONKING = register("great_sword_monking", prop -> new ItemGreatSword(prop, CQRMaterials.CQRItemTiers.TOOL_MONKING, 20));

	// Spears
	public static final RegistryObject<ItemSpearBase> SPEAR_DIAMOND = register("spear_diamond", prop -> new ItemSpearBase(prop, ItemTier.DIAMOND));
	public static final RegistryObject<ItemSpearBase> SPEAR_IRON = register("spear_iron", prop -> new ItemSpearBase(prop, ItemTier.IRON));

	// Staves
	public static final RegistryObject<ItemStaff> STAFF = register("staff", ItemStaff::new);
	public static final RegistryObject<ItemStaffFire> STAFF_FIRE = register("staff_fire", ItemStaffFire::new);
	public static final RegistryObject<ItemStaffVampiric> STAFF_VAMPIRIC = register("staff_vampiric", ItemStaffVampiric::new); // #TODO DESCRIPTION
	public static final RegistryObject<ItemStaffWind> STAFF_WIND = register("staff_wind", ItemStaffWind::new); // #TODO DESCRIPTION
	public static final RegistryObject<ItemStaffPoison> STAFF_POISON = register("staff_poison", ItemStaffPoison::new); // #TODO DESCRIPTION
	public static final RegistryObject<ItemStaffHealing> STAFF_HEALING = register("staff_healing", ItemStaffHealing::new);
	public static final RegistryObject<ItemFakeSwordHealingStaff> DIAMOND_SWORD_FAKE_HEALING_STAFF = register("diamond_sword_fake_healing_staff", props -> new ItemFakeSwordHealingStaff(ItemTier.DIAMOND, props));
	public static final RegistryObject<ItemStaffThunder> STAFF_THUNDER = register("staff_thunder", ItemStaffThunder::new);
	public static final RegistryObject<ItemStaffSpider> STAFF_SPIDER = register("staff_spider", ItemStaffSpider::new);
	public static final RegistryObject<ItemStaffGun> STAFF_GUN = register("staff_gun", ItemStaffGun::new); // #TODO TEXTURES

	// Guns
	public static final RegistryObject<ItemRevolver> REVOLVER = register("revolver", ItemRevolver::new);
	public static final RegistryObject<ItemRevolver> CAPTAIN_REVOLVER = register("captain_revolver", ItemRevolver::new);
	public static final RegistryObject<ItemMusket> MUSKET = register("musket", ItemMusket::new);
	public static final RegistryObject<ItemMusketKnife> MUSKET_DAGGER_IRON = register("musket_dagger_iron", prop -> new ItemMusketKnife(ItemTier.IRON, prop)); // #TODO TEXTURES, tweak stats?
	public static final RegistryObject<ItemMusketKnife> MUSKET_DAGGER_DIAMOND = register("musket_dagger_diamond", prop -> new ItemMusketKnife(ItemTier.DIAMOND, prop)); // #TODO TEXTURES, tweak stats
	public static final RegistryObject<ItemMusketKnife> MUSKET_DAGGER_MONKING = register("musket_dagger_monking", prop -> new ItemMusketKnife(CQRMaterials.CQRItemTiers.TOOL_MONKING, prop)); // #TODO TEXTURES, tweak
	public static final RegistryObject<ItemBullet> BULLET_IRON = registerBullet("bullet_iron", ItemBullet::new, EBulletType.IRON);
	public static final RegistryObject<ItemBullet> BULLET_GOLD = registerBullet("bullet_gold", ItemBullet::new, EBulletType.GOLD);
	public static final RegistryObject<ItemBullet> BULLET_DIAMOND = registerBullet("bullet_diamond", ItemBullet::new, EBulletType.DIAMOND);
	public static final RegistryObject<ItemBullet> BULLET_FIRE = registerBullet("bullet_fire", ItemBullet::new, EBulletType.FIRE);
	public static final RegistryObject<ItemCannonBall> CANNON_BALL = register("cannon_ball", ItemCannonBall::new);
	public static final RegistryObject<ItemFlamethrower> FLAMETHROWER = register("flamethrower", ItemFlamethrower::new); // #TODO TEXTURES
	public static final RegistryObject<ItemBubblePistol> BUBBLE_PISTOL = register("bubble_pistol", ItemBubblePistol::new);
	public static final RegistryObject<ItemBubbleRifle> BUBBLE_RIFLE = register("bubble_rifle", ItemBubbleRifle::new);

	// Hookers
	public static final RegistryObject<ItemHookshot> HOOKSHOT = register("hookshot", ItemHookshot::new);
	public static final RegistryObject<ItemLongshot> LONGSHOT = register("longshot", ItemLongshot::new);
	public static final RegistryObject<ItemSpiderHook> SPIDERHOOK = register("spiderhook", ItemSpiderHook::new);

	// Single Armor RegistryObject<Item>s
	public static final RegistryObject<ItemHelmetDragon> HELMET_DRAGON = register("helmet_dragon", prop -> new ItemHelmetDragon(CQRMaterials.ArmorMaterials.ARMOR_DRAGON, EquipmentSlotType.HEAD, prop)); // #TODO Make model centered on head // Abandon for now
	public static final RegistryObject<ItemBootsCloud> BOOTS_CLOUD = register("boots_cloud", prop -> new ItemBootsCloud(CQRMaterials.ArmorMaterials.ARMOR_CLOUD, EquipmentSlotType.FEET, prop));
	public static final RegistryObject<ItemBackpack> BACKPACK = register("backpack", prop -> new ItemBackpack(CQRMaterials.ArmorMaterials.ARMOR_BACKPACK, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemSpikedGlove> SPIKED_GLOVE = register("spiked_glove", ItemSpikedGlove::new);
	public static final RegistryObject<ItemArmorCrown> KING_CROWN = register("king_crown", prop -> new ItemArmorCrown(CQRMaterials.ArmorMaterials.ARMOR_CROWN, prop));

	// Slime Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorSlime> HELMET_SLIME = register("helmet_slime", prop -> new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorSlime> CHESTPLATE_SLIME = register("chestplate_slime", prop -> new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorSlime> LEGGINGS_SLIME = register("leggings_slime", prop -> new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, EquipmentSlotType.LEGS, prop));;
	public static final RegistryObject<ItemArmorSlime> BOOTS_SLIME = register("boots_slime", prop -> new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, EquipmentSlotType.FEET, prop));

	// Turtle Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorTurtle> HELMET_TURTLE = register("helmet_turtle", prop -> new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorTurtle> CHESTPLATE_TURTLE = register("chestplate_turtle", prop -> new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorTurtle> LEGGINGS_TURTLE = register("leggings_turtle", prop -> new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorTurtle> BOOTS_TURTLE = register("boots_turtle", prop -> new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, EquipmentSlotType.FEET, prop));

	// Bull Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorBull> HELMET_BULL = register("helmet_bull", prop -> new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorBull> CHESTPLATE_BULL = register("chestplate_bull", prop -> new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorBull> LEGGINGS_BULL = register("leggings_bull", prop -> new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorBull> BOOTS_BULL = register("boots_bull", prop -> new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, EquipmentSlotType.FEET, prop));

	// Spider Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorSpider> HELMET_SPIDER = register("helmet_spider", prop -> new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorSpider> CHESTPLATE_SPIDER = register("chestplate_spider", prop -> new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorSpider> LEGGINGS_SPIDER = register("leggings_spider", prop -> new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorSpider> BOOTS_SPIDER = register("boots_spider", prop -> new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, EquipmentSlotType.FEET, prop));

	// Inquisition Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorInquisition> HELMET_INQUISITION = register("helmet_inquisition", prop -> new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorInquisition> CHESTPLATE_INQUISITION = register("chestplate_inquisition", prop -> new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorInquisition> LEGGINGS_INQUISITION = register("leggings_inquisition", prop -> new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorInquisition> BOOTS_INQUISITION = register("boots_inquisition", prop -> new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, EquipmentSlotType.FEET, prop));

	// Heavy Diamond Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorHeavy> HELMET_HEAVY_DIAMOND = register("helmet_heavy_diamond", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorHeavy> CHESTPLATE_HEAVY_DIAMOND = register("chestplate_heavy_diamond", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorHeavy> LEGGINGS_HEAVY_DIAMOND = register("leggings_heavy_diamond", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorHeavy> BOOTS_HEAVY_DIAMOND = register("boots_heavy_diamond", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, EquipmentSlotType.FEET, prop));

	// Heavy Iron Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorHeavy> HELMET_HEAVY_IRON = register("helmet_heavy_iron", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorHeavy> CHESTPLATE_HEAVY_IRON = register("chestplate_heavy_iron", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorHeavy> LEGGINGS_HEAVY_IRON = register("leggings_heavy_iron", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorHeavy> BOOTS_HEAVY_IRON = register("boots_heavy_iron", prop -> new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, EquipmentSlotType.FEET, prop));

	// Dyable Iron Armor
	public static final RegistryObject<ItemArmorDyable> HELMET_IRON_DYABLE = register("helmet_iron_dyable", prop -> new ItemArmorDyable(ArmorMaterial.IRON, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorDyable> CHESTPLATE_IRON_DYABLE = register("chestplate_iron_dyable", prop -> new ItemArmorDyable(ArmorMaterial.IRON, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorDyable> LEGGINGS_IRON_DYABLE = register("leggings_iron_dyable", prop -> new ItemArmorDyable(ArmorMaterial.IRON, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorDyable> BOOTS_IRON_DYABLE = register("boots_iron_dyable", prop -> new ItemArmorDyable(ArmorMaterial.IRON, EquipmentSlotType.FEET, prop));

	// Dyable Diamond Armor
	public static final RegistryObject<ItemArmorDyable> HELMET_DIAMOND_DYABLE = register("helmet_diamond_dyable", prop -> new ItemArmorDyable(ArmorMaterial.DIAMOND, EquipmentSlotType.HEAD, prop));
	public static final RegistryObject<ItemArmorDyable> CHESTPLATE_DIAMOND_DYABLE = register("chestplate_diamond_dyable", prop -> new ItemArmorDyable(ArmorMaterial.DIAMOND, EquipmentSlotType.CHEST, prop));
	public static final RegistryObject<ItemArmorDyable> LEGGINGS_DIAMOND_DYABLE = register("leggings_diamond_dyable", prop -> new ItemArmorDyable(ArmorMaterial.DIAMOND, EquipmentSlotType.LEGS, prop));
	public static final RegistryObject<ItemArmorDyable> BOOTS_DIAMOND_DYABLE = register("boots_diamond_dyable", prop -> new ItemArmorDyable(ArmorMaterial.DIAMOND, EquipmentSlotType.FEET, prop));

	// Ingridients
	public static final RegistryObject<ItemLore> SCALE_TURTLE = register("scale_turtle", ItemLore::new);
	public static final RegistryObject<ItemLore> LEATHER_BULL = register("leather_bull", ItemLore::new);
	public static final RegistryObject<ItemLore> HORN_BULL = register("horn_bull", ItemLore::new);
	public static final RegistryObject<ItemLore> BALL_SLIME = register("ball_slime", ItemLore::new);
	public static final RegistryObject<ItemLore> LEATHER_SPIDER = register("leather_spider", ItemLore::new);
	public static final RegistryObject<ItemLore> BONE_MONKING = register("bone_monking", ItemLore::new);
	public static final RegistryObject<ItemLore> GIANT_SPIDER_POISON = register("giant_spider_poison", ItemLore::new);
	public static final RegistryObject<ItemLore> FEATHER_GOLDEN = register("feather_golden", ItemGoldenFeather::new);

	// Other
	public static final RegistryObject<ItemPotionHealing> POTION_HEALING = register("potion_healing", ItemPotionHealing::new);
	public static final RegistryObject<ItemTeleportStone> TELEPORT_STONE = register("teleport_stone", ItemTeleportStone::new);
	public static final RegistryObject<ItemCursedBone> CURSED_BONE = register("cursed_bone", ItemCursedBone::new);

	// Creative
	public static final RegistryObject<ItemSuperTool> SUPER_TOOL = register("super_tool", ItemSuperTool::new);
	public static final RegistryObject<ItemStructureSelector> STRUCTURE_SELECTOR = register("structure_selector", ItemStructureSelector::new);
	public static final RegistryObject<ItemSoulBottle> SOUL_BOTTLE = register("soul_bottle", ItemSoulBottle::new);
	public static final RegistryObject<ItemMobToSpawner> MOB_TO_SPAWNER_TOOL = register("mob_to_spawner_tool", ItemMobToSpawner::new);
	public static final RegistryObject<ItemSpawnerConverter> SPAWNER_CONVERTER = register("spawner_converter", ItemSpawnerConverter::new);
	public static final RegistryObject<ItemBadge> BADGE = register("badge", ItemBadge::new);
	//public static final RegistryObject<ItemPathTool> PATH_TOOL = null;
	public static final RegistryObject<ItemShieldDummy> DUMMY_SHIELD = null;
	public static final RegistryObject<ItemAlchemyBag> ALCHEMY_BAG = register("alchemy_bag", ItemAlchemyBag::new);
	public static final RegistryObject<ItemUnprotectedPositionTool> UNPROTECTED_POSITIONS_TOOL = register("unprotected_positions_tool", ItemUnprotectedPositionTool::new);
	
	// Spawneggs
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_DUMMY = registerSpawnEgg(CQREntityTypes.DUMMY, 0xB8945F, 0x7E6237, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_DWARF = registerSpawnEgg(CQREntityTypes.DWARF, 0xB4846D, 0x5A5754, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_ENDERMAN = registerSpawnEgg(CQREntityTypes.ENDERMAN, 0x161616, 0xE079FA, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_GREMLIN = registerSpawnEgg(CQREntityTypes.GREMLIN, 0x606060, 0xE42E2E, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_GOBLIN = registerSpawnEgg(CQREntityTypes.GOBLIN, 0x6A8A3E, 0x513430, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_GOLEM = registerSpawnEgg(CQREntityTypes.GOLEM, 0xD2CECC, 0xC0A189, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_HUMAN = registerSpawnEgg(CQREntityTypes.HUMAN, 0xFFD4A8, 0xEDBE7D, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_ILLAGER = registerSpawnEgg(CQREntityTypes.ILLAGER, 0x959B9B, 0x505656, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_NPC = registerSpawnEgg(CQREntityTypes.NPC, 0xFFE5D2, 0x5DD345, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_MINOTAUR = registerSpawnEgg(CQREntityTypes.MINOTAUR, 0xC69680, 0x3F3024, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_MANDRIL = registerSpawnEgg(CQREntityTypes.MANDRIL, 0x505050, 0x8A552E, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_MUMMY = registerSpawnEgg(CQREntityTypes.MUMMY, 0x846737, 0x5E5546, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_OGRE = registerSpawnEgg(CQREntityTypes.OGRE, 0xA67B4E, 0x4B3318, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_ORC = registerSpawnEgg(CQREntityTypes.ORC, 0x637F41, 0x750732, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_BOARMAN = registerSpawnEgg(CQREntityTypes.BOARMAN, 0xE68583, 0x385226, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_PIRATE = registerSpawnEgg(CQREntityTypes.PIRATE, 0xAEAEAE, 0x5E1717, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_SKELETON = registerSpawnEgg(CQREntityTypes.SKELETON, 0xA1A1A1, 0x797979, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_SPECTRE = registerSpawnEgg(CQREntityTypes.SPECTRE, 0x848484, 0x226767, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_TRITON = registerSpawnEgg(CQREntityTypes.TRITON, 0x385265, 0xFFD57D, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_WALKER = registerSpawnEgg(CQREntityTypes.WALKER, 0x483255, 0x00C2E0, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_ZOMBIE = registerSpawnEgg(CQREntityTypes.ZOMBIE, 0x35226, 0x00A8A8, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_ENDER_KING = registerSpawnEgg(CQREntityTypes.ENDER_KING, 0x161616, 0xE079FA, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_ENDER_CALAMITY = registerSpawnEgg(CQREntityTypes.ENDER_CALAMITY, 0x161616, 0xE079FA, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_EXTERMINATOR = registerSpawnEgg(CQREntityTypes.EXTERMINATOR, 0xDBCDC1, 0x2F42E6, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_GIANT_TORTOISE = registerSpawnEgg(CQREntityTypes.GIANT_TORTOISE, 0x875B31, 0x388D3A, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_NETHER_DRAGON = registerSpawnEgg(CQREntityTypes.NETHER_DRAGON, 0xFFEDDD, 0x7F0000, CQRMain.CQR_SPAWN_EGG_TAB);
	
	//public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_GIANT_SPIDER = registerSpawnEgg(CQREntityTypes.GIANT_SPIDER, 0x751515, 0x44A595, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_BOARMAGE = registerSpawnEgg(CQREntityTypes.BOARMAGE, 0x2C2B2A, 0xE67973, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_LICH = registerSpawnEgg(CQREntityTypes.LICH, 0x751515, 0xA3A3A3, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_NECROMANCER = registerSpawnEgg(CQREntityTypes.NECROMANCER, 0x2C2B2A, 0x29512C, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_PIRATE_CAPTAIN = registerSpawnEgg(CQREntityTypes.PIRATE_CAPTAIN, 0x5E1717, 0xE6D578, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_PIRATE_PARROT = registerSpawnEgg(CQREntityTypes.PIRATE_PARROT, 0xB10039, 0x5C5CFF, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_WALKER_KING = registerSpawnEgg(CQREntityTypes.WALKER_KING, 0x483255, 0x00C2E0, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_WALKER_KING_ILLUSION = registerSpawnEgg(CQREntityTypes.WALKER_KING_ILLUSION, 0x483255, 0x00C2E0, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_SPECTRE_LORD = registerSpawnEgg(CQREntityTypes.SPECTRE_LORD, 0x848484, 0x226767, CQRMain.CQR_SPAWN_EGG_TAB);
	public static final RegistryObject<ForgeSpawnEggItem> SPAWN_EGG_SPECTRE_LORD_ILLUSION = registerSpawnEgg(CQREntityTypes.SPECTRE_LORD_ILLUSION, 0x848484, 0x226767, CQRMain.CQR_SPAWN_EGG_TAB);
	
	public static <T extends Item> RegistryObject<T> register(String name, Function<Item.Properties, T> itemSupplier) {
		return register(name, itemSupplier, CQRMain.CQR_ITEMS_TAB);
	}

	private static RegistryObject<ItemShieldCQR> registerShield(String string) {
		return register("shield_" + string, prop -> new ItemShieldCQR(prop, 1024, Items.IRON_INGOT));
	}

	public static <T extends Item> RegistryObject<T> register(String name, Function<Item.Properties, T> itemSupplier,
			ItemGroup tab) {
		return ITEMS.register(name, () -> itemSupplier.apply(new Item.Properties().tab(tab)));
	}
	
	public static <T extends Item> RegistryObject<T> registerBullet(String name, BiFunction<Item.Properties, EBulletType, T> itemSupplier, final EBulletType type) {
		return registerBullet(name, itemSupplier, CQRMain.CQR_ITEMS_TAB, type);
	}

	public static <T extends Item> RegistryObject<T> registerBullet(String name, BiFunction<Item.Properties, EBulletType, T> itemSupplier,
			ItemGroup tab, final EBulletType type) {
		return ITEMS.register(name, () -> itemSupplier.apply(new Item.Properties().tab(tab), type));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ForgeSpawnEggItem> RegistryObject<T> registerSpawnEgg(RegistryObject<? extends EntityType<? extends Entity>> entityType, int colorMain, int colorSecondary, ItemGroup tab) {
		return (RegistryObject<T>) ITEMS.register("cqr_" + entityType.getId().getPath() + "_spawnegg" , () -> new CQRSpawnEggItem(
				entityType, 
				colorMain, 
				colorSecondary, 
				new Item.Properties().tab(tab)
		));
	}

	public static void registerItems() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	/*
	public static class EventHandler {

		public static final List<Item> ITEMS = new ArrayList<>();
		public static final List<Item> SPAWN_EGGS = new ArrayList<>();

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final Item[] items = {
					setItemName(new ItemDagger(ToolMaterial.IRON, 25), "dagger_iron"),
					setItemName(new ItemDagger(ToolMaterial.DIAMOND, 20), "dagger_diamond"),
					setItemName(new ItemDaggerNinja(CQRMaterials.ToolMaterials.TOOL_NINJA, 15), "dagger_ninja"),
					setItemName(new ItemDagger(CQRMaterials.ToolMaterials.TOOL_MONKING, 10), "dagger_monking"),

					setItemName(new ItemSwordTurtle(CQRMaterials.ToolMaterials.TOOL_TURTLE), "sword_turtle"),
					setItemName(new ItemSwordSpider(CQRMaterials.ToolMaterials.TOOL_SPIDER), "sword_spider"),
					setItemName(new ItemSwordMoonlight(CQRMaterials.ToolMaterials.TOOL_MOONLIGHT), "sword_moonlight"),
					setItemName(new ItemSwordSunshine(CQRMaterials.ToolMaterials.TOOL_SUNSHINE), "sword_sunshine"),

					setItemName(new ItemBullBattleAxe(CQRMaterials.ToolMaterials.TOOL_BULL), "battle_axe_bull"),

					setItemName(new ItemSwordWalker(CQRMaterials.ToolMaterials.TOOL_WALKER), "sword_walker"),
					setItemName(new ItemShieldWalkerKing(), "shield_walker_king"),

					setItemName(new ItemGreatSword(ToolMaterial.IRON, 0.8F, 30), "great_sword_iron"),
					setItemName(new ItemGreatSword(ToolMaterial.DIAMOND, 0.9F, 25), "great_sword_diamond"),
					setItemName(new ItemGreatSword(CQRMaterials.ToolMaterials.TOOL_BULL, 1.0F, 20), "great_sword_bull"),
					setItemName(new ItemGreatSword(CQRMaterials.ToolMaterials.TOOL_MONKING, 2.0F, 20), "great_sword_monking"),

					setItemName(new ItemSpearBase(ToolMaterial.DIAMOND), "spear_diamond"),
					setItemName(new ItemSpearBase(ToolMaterial.IRON), "spear_iron"),

					setItemName(new ItemStaff(), "staff"),
					setItemName(new ItemStaffFire(), "staff_fire"),
					setItemName(new ItemStaffVampiric(), "staff_vampiric"),
					setItemName(new ItemStaffWind(), "staff_wind"),
					setItemName(new ItemStaffPoison(), "staff_poison"),
					setItemName(new ItemStaffHealing(), "staff_healing"),
					setItemNameAndTab(new ItemFakeSwordHealingStaff(ToolMaterial.DIAMOND), "diamond_sword_fake_healing_staff", null),
					setItemName(new ItemStaffThunder(), "staff_thunder"),
					setItemName(new ItemStaffSpider(), "staff_spider"),
					setItemName(new ItemStaffGun(), "staff_gun"),

					setItemName(new ItemRevolver(), "revolver"),
					setItemName(new ItemRevolver(), "captain_revolver"),
					setItemName(new ItemMusket(), "musket"),
					setItemName(new ItemMusketKnife(ToolMaterial.IRON), "musket_dagger_iron"),
					setItemName(new ItemMusketKnife(ToolMaterial.DIAMOND), "musket_dagger_diamond"),
					setItemName(new ItemMusketKnife(CQRMaterials.ToolMaterials.TOOL_MONKING), "musket_dagger_monking"),
					setItemName(new ItemBullet(), "bullet_iron"),
					setItemName(new ItemBullet(), "bullet_gold"),
					setItemName(new ItemBullet(), "bullet_diamond"),
					setItemName(new ItemBullet(), "bullet_fire"),
					setItemName(new ItemCannonBall(), "cannon_ball"),
					setItemName(new ItemFlamethrower(), "flamethrower"),
					setItemName(new ItemBubblePistol(), "bubble_pistol"),
					setItemName(new ItemBubbleRifle(), "bubble_rifle"),

					setItemName(new ItemHookshot(), "hookshot"),
					setItemName(new ItemLongshot(), "longshot"),
					setItemName(new ItemSpiderHook(), "spiderhook"),

					setItemName(new ItemHelmetDragon(CQRMaterials.ArmorMaterials.ARMOR_DRAGON, -1, EquipmentSlotType.HEAD), "helmet_dragon"),
					setItemName(new ItemBootsCloud(CQRMaterials.ArmorMaterials.ARMOR_CLOUD, -1, EquipmentSlotType.FEET), "boots_cloud"),
					setItemName(new ItemBackpack(CQRMaterials.ArmorMaterials.ARMOR_BACKPACK, -1, EquipmentSlotType.CHEST), "backpack"),
					setItemName(new ItemSpikedGlove(), "spiked_glove"),
					setItemName(new ItemCrown(CQRMaterials.ArmorMaterials.ARMOR_CROWN, -1), "king_crown"),

					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.HEAD), "helmet_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.CHEST), "chestplate_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.LEGS), "leggings_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.FEET), "boots_slime"),

					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.HEAD), "helmet_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.CHEST), "chestplate_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.LEGS), "leggings_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.FEET), "boots_turtle"),

					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.HEAD), "helmet_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.CHEST), "chestplate_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.LEGS), "leggings_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.FEET), "boots_bull"),

					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.HEAD), "helmet_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.CHEST), "chestplate_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.LEGS), "leggings_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.FEET), "boots_spider"),

					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.HEAD), "helmet_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.CHEST), "chestplate_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.LEGS), "leggings_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.FEET), "boots_inquisition"),

					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.HEAD), "helmet_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.CHEST), "chestplate_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.LEGS), "leggings_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.FEET), "boots_heavy_diamond"),

					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.HEAD), "helmet_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.CHEST), "chestplate_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.LEGS), "leggings_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.FEET), "boots_heavy_iron"),

					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.HEAD), "helmet_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.CHEST), "chestplate_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.LEGS), "leggings_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.FEET), "boots_diamond_dyable"),

					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.HEAD), "helmet_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.CHEST), "chestplate_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.LEGS), "leggings_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.FEET), "boots_iron_dyable"),

					setItemName(new ItemLore(), "scale_turtle"),
					setItemName(new ItemLore(), "leather_bull"),
					setItemName(new ItemLore(), "horn_bull"),
					setItemName(new ItemLore(), "ball_slime"),
					setItemName(new ItemLore(), "leather_spider"),
					setItemName(new ItemLore(), "bone_monking"),
					setItemName(new ItemLore(), "giant_spider_poison"),
					setItemName(new ItemGoldenFeather(), "feather_golden"),

					setItemName(new ItemPotionHealing(), "potion_healing"),
					setItemName(new ItemTeleportStone(), "teleport_stone"),
					setItemName(new ItemCursedBone(), "cursed_bone"),
					setItemName(new ItemMagicBell(), "magic_bell"),

					setItemNameAndTab(new ItemSuperTool(), "super_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemStructureSelector(), "structure_selector", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemSoulBottle(), "soul_bottle", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemMobToSpawner(), "mob_to_spawner_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemSpawnerConverter(), "spawner_converter", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemBadge(), "badge", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemPathTool(), "path_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemShieldDummy(), "dummy_shield", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemAlchemyBag(), "alchemy_bag", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemUnprotectedPositionTool(), "unprotected_positions_tool", CQRMain.CQR_CREATIVE_TOOL_TAB) };

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
				Item item = setItemNameAndTab(new ItemDungeonPlacer(i), "dungeon_placer_d" + i, CQRMain.CQR_DUNGEON_PLACER_TAB);
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
				Item item = setItemNameAndTab(spawnEggList.get(i), "cqr_" + entityName + "_spawn_egg_" + i, CQRMain.CQR_SPAWN_EGG_TAB);
				registry.register(item);
				ITEMS.add(item);
				SPAWN_EGGS.add(item);
			}
		}

		private static Item setItemName(Item item, String name) {
			return setItemNameAndTab(item, name, CQRMain.CQR_ITEMS_TAB);
		}

		private static Item setItemNameAndTab(Item item, String name, @Nullable ItemGroup tab) {
			return item.setRegistryName(CQRMain.MODID, name).setTranslationKey(name).setCreativeTab(tab);
		}

	}
	*/

}
