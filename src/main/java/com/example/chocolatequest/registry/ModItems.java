package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import com.example.chocolatequest.item.AlchemyBagItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChocolateQuestReDone.MODID);

    public static final DeferredItem<AlchemyBagItem> ALCHEMY_BAG = ITEMS.register("alchemy_bag", () -> new AlchemyBagItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.SuperToolItem> SUPER_TOOL = ITEMS.register("super_tool", () -> new com.example.chocolatequest.item.SuperToolItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.StructureSelectorItem> STRUCTURE_SELECTOR = ITEMS.register("structure_selector", () -> new com.example.chocolatequest.item.StructureSelectorItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.MobToSpawnerItem> MOB_TO_SPAWNER_TOOL = ITEMS.register("mob_to_spawner_tool", () -> new com.example.chocolatequest.item.MobToSpawnerItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.SpawnerConverterItem> SPAWNER_CONVERTER = ITEMS.register("spawner_converter", () -> new com.example.chocolatequest.item.SpawnerConverterItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.BadgeItem> BADGE = ITEMS.register("badge", () -> new com.example.chocolatequest.item.BadgeItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.PathToolItem> PATH_TOOL = ITEMS.register("path_tool", () -> new com.example.chocolatequest.item.PathToolItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.TeleportStoneItem> TELEPORT_STONE = ITEMS.register("teleport_stone", () -> new com.example.chocolatequest.item.TeleportStoneItem(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.GuideBookItem> GUIDE_BOOK = ITEMS.register("guide_book", () -> new com.example.chocolatequest.item.GuideBookItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> EXPORTER = ITEMS.register("exporter", () -> new net.minecraft.world.item.BlockItem(ModBlocks.EXPORTER.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> SPECTER_LORD_SPAWN_EGG = ITEMS.register("specter_lord_spawn_egg",
            () -> new net.minecraft.world.item.SpawnEggItem(ModEntities.SPECTER_LORD.get(), 0x000000, 0x888888, new net.minecraft.world.item.Item.Properties()));

    // Items
    public static final DeferredItem<com.example.chocolatequest.item.gun.ItemFlamethrower> FLAMETHROWER = ITEMS.register("flamethrower", () -> new com.example.chocolatequest.item.gun.ItemFlamethrower(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.gun.ItemBubblePistol> BUBBLE_PISTOL = ITEMS.register("bubble_pistol", () -> new com.example.chocolatequest.item.gun.ItemBubblePistol(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.gun.ItemBubbleRifle> BUBBLE_RIFLE = ITEMS.register("bubble_rifle", () -> new com.example.chocolatequest.item.gun.ItemBubbleRifle(new Item.Properties()));
    public static final DeferredItem<com.example.chocolatequest.item.gun.ItemCannonBall> CANNON_BALL = ITEMS.register("cannon_ball", () -> new com.example.chocolatequest.item.gun.ItemCannonBall(new Item.Properties()));
    public static final DeferredItem<Item> REVOLVER = ITEMS.registerItem("revolver", com.example.chocolatequest.item.ItemRevolver::new, new Item.Properties());
    public static final DeferredItem<Item> MUSKET = ITEMS.registerItem("musket", com.example.chocolatequest.item.ItemMusket::new, new Item.Properties());
    public static final DeferredItem<Item> MUSKET_DAGGER_IRON = ITEMS.registerItem("musket_dagger_iron", p -> new com.example.chocolatequest.item.ItemMusketKnife(net.minecraft.world.item.Tiers.IRON, p), new Item.Properties());
    public static final DeferredItem<Item> MUSKET_DAGGER_DIAMOND = ITEMS.registerItem("musket_dagger_diamond", p -> new com.example.chocolatequest.item.ItemMusketKnife(net.minecraft.world.item.Tiers.DIAMOND, p), new Item.Properties());
    public static final DeferredItem<Item> MUSKET_DAGGER_PIRATE = ITEMS.registerItem("musket_dagger_pirate", p -> new com.example.chocolatequest.item.ItemMusketKnife(net.minecraft.world.item.Tiers.IRON, p), new Item.Properties());
    public static final DeferredItem<Item> MUSKET_DAGGER_SHADOW = ITEMS.registerItem("musket_dagger_shadow", p -> new com.example.chocolatequest.item.ItemMusketKnife(net.minecraft.world.item.Tiers.IRON, p), new Item.Properties());
    public static final DeferredItem<Item> MUSKET_DAGGER_MONKING = ITEMS.registerItem("musket_dagger_monking", p -> new com.example.chocolatequest.item.ItemMusketKnife(net.minecraft.world.item.Tiers.IRON, p), new Item.Properties());
    public static final DeferredItem<Item> CAPTAIN_REVOLVER = ITEMS.registerItem("captain_revolver", com.example.chocolatequest.item.ItemRevolver::new, new Item.Properties());
    public static final DeferredItem<Item> BULLET_IRON = ITEMS.registerItem("bullet_iron", p -> new com.example.chocolatequest.item.ItemBullet(p, com.example.chocolatequest.item.EBulletType.IRON), new Item.Properties());
    public static final DeferredItem<Item> BULLET_GOLD = ITEMS.registerItem("bullet_gold", p -> new com.example.chocolatequest.item.ItemBullet(p, com.example.chocolatequest.item.EBulletType.GOLD), new Item.Properties());
    public static final DeferredItem<Item> BULLET_DIAMOND = ITEMS.registerItem("bullet_diamond", p -> new com.example.chocolatequest.item.ItemBullet(p, com.example.chocolatequest.item.EBulletType.DIAMOND), new Item.Properties());
    public static final DeferredItem<Item> BULLET_FIRE = ITEMS.registerItem("bullet_fire", p -> new com.example.chocolatequest.item.ItemBullet(p, com.example.chocolatequest.item.EBulletType.FIRE), new Item.Properties());
    public static final DeferredItem<Item> GOLDEN_FEATHER = ITEMS.registerSimpleItem("golden_feather", new Item.Properties());

    public static final DeferredItem<Item> SCALE_TURTLE = ITEMS.registerItem("scale_turtle", Item::new, new Item.Properties());
    public static final DeferredItem<Item> CURSED_BONE = ITEMS.register("cursed_bone", () -> new com.example.chocolatequest.item.CursedBoneItem(new Item.Properties().durability(20)));
    public static final DeferredItem<Item> SLIME_BALL_CQR = ITEMS.registerSimpleItem("ball_slime", new Item.Properties());
    public static final DeferredItem<Item> BULL_LEATHER = ITEMS.registerSimpleItem("leather_bull", new Item.Properties());
    public static final DeferredItem<Item> SPIDER_LEATHER = ITEMS.registerSimpleItem("leather_spider", new Item.Properties());
    public static final DeferredItem<Item> MINI_HEALING_POTION = ITEMS.registerItem("mini_healing_potion", com.example.chocolatequest.item.MiniHealingPotionItem::new, new Item.Properties());
    public static final DeferredItem<Item> BULL_HORN = ITEMS.registerSimpleItem("horn_bull", new Item.Properties());

    public static final DeferredItem<Item> SPIDER_HOOK = ITEMS.register("spider_hook",
            () -> new com.example.chocolatequest.item.ItemSpiderHook(new Item.Properties().durability(250)));

    public static final DeferredItem<Item> HOOKSHOT = ITEMS.register("hookshot",
            () -> new com.example.chocolatequest.item.ItemHookshot(new Item.Properties().durability(250)));

    public static final DeferredItem<Item> LONGSHOT = ITEMS.register("longshot",
            () -> new com.example.chocolatequest.item.ItemHookshot(new Item.Properties().durability(250)));

    public static final DeferredItem<Item> STAFF = ITEMS.registerSimpleItem("staff", new Item.Properties());
    public static final DeferredItem<Item> SWORD_HANDLE = ITEMS.registerSimpleItem("sword_handle", new Item.Properties());
    
    // Splash Potions
    public static final DeferredItem<Item> WATER_POTION = ITEMS.register("water_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("water", new Item.Properties()));
    public static final DeferredItem<Item> POISON_POTION = ITEMS.register("poison_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("poison", new Item.Properties()));
    public static final DeferredItem<Item> ICE_POTION = ITEMS.register("ice_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("ice", new Item.Properties()));
    public static final DeferredItem<Item> FIRE_POTION = ITEMS.register("fire_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("fire", new Item.Properties()));
    public static final DeferredItem<Item> DARK_POTION = ITEMS.register("dark_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("dark", new Item.Properties()));
    public static final DeferredItem<Item> ELECTRIC_POTION = ITEMS.register("electric_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("electric", new Item.Properties()));
    public static final DeferredItem<Item> WIND_POTION = ITEMS.register("wind_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("wind", new Item.Properties()));
    public static final DeferredItem<Item> HEAL_POTION = ITEMS.register("heal_potion", () -> new com.example.chocolatequest.item.CQRPotionItem("heal", new Item.Properties()));

    // Custom Arrows
    public static final DeferredItem<Item> WATER_ARROW = ITEMS.register("water_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("water", new Item.Properties()));
    public static final DeferredItem<Item> POISON_ARROW = ITEMS.register("poison_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("poison", new Item.Properties()));
    public static final DeferredItem<Item> ICE_ARROW = ITEMS.register("ice_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("ice", new Item.Properties()));
    public static final DeferredItem<Item> FIRE_ARROW = ITEMS.register("fire_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("fire", new Item.Properties()));
    public static final DeferredItem<Item> DARK_ARROW = ITEMS.register("dark_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("dark", new Item.Properties()));
    public static final DeferredItem<Item> ELECTRIC_ARROW = ITEMS.register("electric_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("electric", new Item.Properties()));
    public static final DeferredItem<Item> WIND_ARROW = ITEMS.register("wind_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("wind", new Item.Properties()));
    public static final DeferredItem<Item> HEAL_ARROW = ITEMS.register("heal_arrow", () -> new com.example.chocolatequest.item.CQRArrowItem("heal", new Item.Properties()));

    public static final DeferredItem<Item> CQ_ZOMBIE_SPAWN_EGG = ITEMS.register("cq_zombie_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_ZOMBIE, 0x00AFAF, 0x799C65, new Item.Properties()));

    public static final DeferredItem<Item> CQ_SKELETON_SPAWN_EGG = ITEMS.register("cq_skeleton_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_SKELETON, 0xC1C1C1, 0x494949, new Item.Properties()));

    public static final DeferredItem<Item> CQ_SPECTER_SPAWN_EGG = ITEMS.register("cq_specter_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_SPECTER, 0xFFFFFF, 0x888888, new Item.Properties()));

    public static final DeferredItem<Item> CQ_ENDERMAN_SPAWN_EGG = ITEMS.register("cq_enderman_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_ENDERMAN, 0x161616, 0x000000, new Item.Properties()));

    public static final DeferredItem<Item> CQ_PIRATE_SPAWN_EGG = ITEMS.register("cq_pirate_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_PIRATE, 0xAA0000, 0x000000, new Item.Properties()));

    public static final DeferredItem<Item> SHELOB_SPAWN_EGG = ITEMS.register("shelob_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SHELOB, 0x1a1a1a, 0x7b1c1c, new Item.Properties()));

    public static final DeferredItem<Item> CQ_GREMLIN_SPAWN_EGG = ITEMS.register("cq_gremlin_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_GREMLIN, 0x55AA55, 0x000000, new Item.Properties()));

    public static final DeferredItem<Item> EXTERMINATOR_SPAWN_EGG = ITEMS.register("exterminator_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.EXTERMINATOR, 0x555555, 0xff0000, new Item.Properties()));

    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_BOARMAN_SPAWN_EGG = ITEMS.register("cq_boarman_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_BOARMAN, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_DUMMY_SPAWN_EGG = ITEMS.register("cq_dummy_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_DUMMY, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_DWARF_SPAWN_EGG = ITEMS.register("cq_dwarf_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_DWARF, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_GOBLIN_SPAWN_EGG = ITEMS.register("cq_goblin_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_GOBLIN, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_GOLEM_SPAWN_EGG = ITEMS.register("cq_golem_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_GOLEM, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_HUMAN_SPAWN_EGG = ITEMS.register("cq_human_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_HUMAN, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_ILLAGER_SPAWN_EGG = ITEMS.register("cq_illager_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_ILLAGER, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_MANDRIL_SPAWN_EGG = ITEMS.register("cq_mandril_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_MANDRIL, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_MINOTAUR_SPAWN_EGG = ITEMS.register("cq_minotaur_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_MINOTAUR, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_MUMMY_SPAWN_EGG = ITEMS.register("cq_mummy_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_MUMMY, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_NPC_SPAWN_EGG = ITEMS.register("cq_npc_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_NPC, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_OGRE_SPAWN_EGG = ITEMS.register("cq_ogre_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_OGRE, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_ORC_SPAWN_EGG = ITEMS.register("cq_orc_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_ORC, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> CQ_TRITON_SPAWN_EGG = ITEMS.register("cq_triton_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_TRITON, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> CQ_WALKER_SPAWN_EGG = ITEMS.register("cq_walker_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_WALKER, 0x888888, 0x444444, new net.minecraft.world.item.Item.Properties()));

    public static final DeferredItem<Item> CQ_BULL_SPAWN_EGG = ITEMS.register("cq_bull_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_BULL, 0x5c4033, 0x2e1a0f, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> CQ_ICE_BULL_SPAWN_EGG = ITEMS.register("cq_ice_bull_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_ICE_BULL, 0xadd8e6, 0xffffff, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> CQ_PIRATE_CAPTAIN_SPAWN_EGG = ITEMS.register("cq_pirate_captain_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_PIRATE_CAPTAIN, 0x5e1717, 0xe6d578, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> CQ_PIRATE_PARROT_SPAWN_EGG = ITEMS.register("cq_pirate_parrot_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQ_PIRATE_PARROT, 0x253192, 0xe8bc17, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> BOARMAGE_SPAWN_EGG = ITEMS.register("boarmage_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BOARMAGE, 0x5A3B28, 0x5BC0BE, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> LICH_SPAWN_EGG = ITEMS.register("lich_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.LICH, 0xD8D2B4, 0x294C4C, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> NECROMANCER_SPAWN_EGG = ITEMS.register("necromancer_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.NECROMANCER, 0x21162E, 0x79A857, new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> GREMLIN_SHAMAN_SPAWN_EGG = ITEMS.register("cq_gremlin_shaman_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.GREMLIN_SHAMAN, 0x55AA55, 0x00FF00, new net.minecraft.world.item.Item.Properties()));

    public static final DeferredItem<Item> CQR_DRAGON_SPAWN_EGG = ITEMS.register("cqr_dragon_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CQR_DRAGON, 0x246B36, 0x8FBC55, new Item.Properties()));

    public static final DeferredItem<Item> BULL_BATTLE_AXE = ITEMS.register("battle_axe_bull",
            () -> new com.example.chocolatequest.item.BullBattleAxeItem(ModTiers.BULL, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModTiers.BULL, 6, -3.1F))));

    public static final DeferredItem<Item> BULL_GREAT_SWORD = ITEMS.register("great_sword_bull",
            () -> new com.example.chocolatequest.item.GreatswordItem(ModTiers.BULL, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModTiers.BULL, 7, -3.2F)
                            .withModifierAdded(net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE, 
                                    new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "great_sword_reach"), 
                                            1.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), 
                                    net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND))));

    public static final DeferredItem<Item> SCOUTER_HELMET = ITEMS.register("scouter_helmet",
            () -> new com.example.chocolatequest.item.ScouterHelmetItem(ModArmorMaterials.SCOUTER, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> SCROLL_OF_ESCAPE = ITEMS.register("scroll_of_escape",
            () -> new com.example.chocolatequest.item.ScrollOfEscapeItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> HEAVY_IRON_HELMET = ITEMS.register("heavy_iron_helmet", 
        () -> new com.example.chocolatequest.item.HeavyIronArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_IRON, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> HEAVY_IRON_CHESTPLATE = ITEMS.register("heavy_iron_chestplate", 
        () -> new com.example.chocolatequest.item.HeavyIronArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_IRON, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredItem<Item> HEAVY_IRON_LEGGINGS = ITEMS.register("heavy_iron_leggings", 
        () -> new com.example.chocolatequest.item.HeavyIronArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_IRON, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredItem<Item> HEAVY_IRON_BOOTS = ITEMS.register("heavy_iron_boots", 
        () -> new com.example.chocolatequest.item.HeavyIronArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_IRON, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    public static final DeferredItem<Item> HEAVY_DIAMOND_HELMET = ITEMS.register("heavy_diamond_helmet", 
        () -> new com.example.chocolatequest.item.HeavyDiamondArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_DIAMOND, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(33))));
    public static final DeferredItem<Item> HEAVY_DIAMOND_CHESTPLATE = ITEMS.register("heavy_diamond_chestplate", 
        () -> new com.example.chocolatequest.item.HeavyDiamondArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_DIAMOND, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(33))));
    public static final DeferredItem<Item> HEAVY_DIAMOND_LEGGINGS = ITEMS.register("heavy_diamond_leggings", 
        () -> new com.example.chocolatequest.item.HeavyDiamondArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_DIAMOND, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(33))));
    public static final DeferredItem<Item> HEAVY_DIAMOND_BOOTS = ITEMS.register("heavy_diamond_boots", 
        () -> new com.example.chocolatequest.item.HeavyDiamondArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_DIAMOND, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(33))));

    public static final DeferredItem<Item> BULL_HELMET = ITEMS.register("bull_helmet", 
        () -> new com.example.chocolatequest.item.BullArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.BULL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> BULL_CHESTPLATE = ITEMS.register("bull_chestplate",
            () -> new com.example.chocolatequest.item.BullArmorItem(ModArmorMaterials.BULL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredItem<Item> BULL_LEGGINGS = ITEMS.register("bull_leggings",
            () -> new com.example.chocolatequest.item.BullArmorItem(ModArmorMaterials.BULL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredItem<Item> BULL_BOOTS = ITEMS.register("bull_boots",
            () -> new com.example.chocolatequest.item.BullArmorItem(ModArmorMaterials.BULL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    public static final DeferredItem<Item> IRON_GREAT_SWORD = ITEMS.register("great_sword_iron",
            () -> new com.example.chocolatequest.item.GreatswordItem(Tiers.IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 6, -3.1F)
                            .withModifierAdded(net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE, 
                                    new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "great_sword_reach"), 
                                            1.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), 
                                    net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND))));

    public static final DeferredItem<Item> DIAMOND_GREAT_SWORD = ITEMS.register("great_sword_diamond",
            () -> new com.example.chocolatequest.item.GreatswordItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 7, -3.1F)
                            .withModifierAdded(net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE, 
                                    new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "great_sword_reach"), 
                                            1.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), 
                                    net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND))));

    public static final DeferredItem<Item> MONKING_GREAT_SWORD = ITEMS.register("great_sword_monking",
            () -> new com.example.chocolatequest.item.GreatswordItem(ModTiers.MONKING, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModTiers.MONKING, 9, -3.0F)
                            .withModifierAdded(net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE, 
                                    new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "great_sword_reach"), 
                                            1.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), 
                                    net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND))));

    public static final DeferredItem<Item> MOONLIGHT_SWORD = ITEMS.register("sword_moonlight",
            () -> new com.example.chocolatequest.item.sword.SwordMoonlight(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 5, -2.4F))));

    public static final DeferredItem<Item> EARTH_SWORD = ITEMS.register("earth_sword",
            () -> new com.example.chocolatequest.item.sword.SwordEarth(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 6, -2.8F))));

    public static final DeferredItem<Item> SUNSHINE_SWORD = ITEMS.register("sword_sunshine",
            () -> new com.example.chocolatequest.item.sword.SwordSunshine(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 5, -2.4F))));

    public static final DeferredItem<Item> SPIDER_SWORD = ITEMS.register("sword_spider",
            () -> new com.example.chocolatequest.item.sword.SwordSpider(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4, -2.4F))));

    public static final DeferredItem<Item> TURTLE_SWORD = ITEMS.register("sword_turtle",
            () -> new com.example.chocolatequest.item.sword.SwordTurtle(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4, -2.4F))));

    public static final DeferredItem<Item> WALKER_SWORD = ITEMS.register("sword_walker",
            () -> new com.example.chocolatequest.item.sword.SwordWalker(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 5, -2.4F))));

    public static final DeferredItem<Item> WIND_SWORD = ITEMS.register("sword_wind",
            () -> new com.example.chocolatequest.item.sword.SwordWind(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4, -2.4F))));

    // Daggers
    public static final DeferredItem<Item> DIAMOND_DAGGER = ITEMS.register("dagger_diamond",
            () -> new com.example.chocolatequest.item.DaggerItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 1, -1.2F))));

    public static final DeferredItem<Item> IRON_DAGGER = ITEMS.register("dagger_iron",
            () -> new com.example.chocolatequest.item.DaggerItem(Tiers.IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 1, -1.2F))));

    public static final DeferredItem<Item> MONKING_DAGGER = ITEMS.register("dagger_monking",
            () -> new com.example.chocolatequest.item.DaggerItem(ModTiers.MONKING, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModTiers.MONKING, 2, -1.2F))));

    public static final DeferredItem<Item> PIRATE_DAGGER = ITEMS.register("dagger_pirate",
            () -> new com.example.chocolatequest.item.DaggerItem(Tiers.IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 1, -1.2F))));

    public static final DeferredItem<Item> SHADOW_DAGGER = ITEMS.register("dagger_shadow",
            () -> new com.example.chocolatequest.item.DaggerItem(Tiers.IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 1, -1.2F))));

    // Spears
    public static final DeferredItem<Item> IRON_SPEAR = ITEMS.register("spear_iron",
            () -> new com.example.chocolatequest.item.SpearItem(Tiers.IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 2, -2.2F)
                            .withModifierAdded(net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE, 
                                    new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "spear_reach"), 
                                            1.5, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), 
                                    net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND))));

    public static final DeferredItem<Item> DIAMOND_SPEAR = ITEMS.register("spear_diamond",
            () -> new com.example.chocolatequest.item.SpearItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.2F)
                            .withModifierAdded(net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE, 
                                    new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "spear_reach"), 
                                            1.5, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), 
                                    net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND))));
    public static final DeferredItem<com.example.chocolatequest.item.TurtleArmorItem> TURTLE_HELMET = ITEMS.register("turtle_helmet",
            () -> new com.example.chocolatequest.item.TurtleArmorItem(ModArmorMaterials.TURTLE, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredItem<com.example.chocolatequest.item.TurtleArmorItem> TURTLE_CHESTPLATE = ITEMS.register("turtle_chestplate",
            () -> new com.example.chocolatequest.item.TurtleArmorItem(ModArmorMaterials.TURTLE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredItem<com.example.chocolatequest.item.TurtleArmorItem> TURTLE_LEGGINGS = ITEMS.register("turtle_leggings",
            () -> new com.example.chocolatequest.item.TurtleArmorItem(ModArmorMaterials.TURTLE, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredItem<com.example.chocolatequest.item.TurtleArmorItem> TURTLE_BOOTS = ITEMS.register("turtle_boots",
            () -> new com.example.chocolatequest.item.TurtleArmorItem(ModArmorMaterials.TURTLE, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    public static final DeferredItem<Item> INQUISITION_HELMET = ITEMS.register("inquisition_helmet",
            () -> new com.example.chocolatequest.item.InquisitionArmorItem(ModArmorMaterials.INQUISITION, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredItem<Item> INQUISITION_CHESTPLATE = ITEMS.register("inquisition_chestplate",
            () -> new com.example.chocolatequest.item.InquisitionArmorItem(ModArmorMaterials.INQUISITION, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredItem<Item> INQUISITION_LEGGINGS = ITEMS.register("inquisition_leggings",
            () -> new com.example.chocolatequest.item.InquisitionArmorItem(ModArmorMaterials.INQUISITION, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredItem<Item> INQUISITION_BOOTS = ITEMS.register("inquisition_boots",
            () -> new com.example.chocolatequest.item.InquisitionArmorItem(ModArmorMaterials.INQUISITION, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    public static final DeferredItem<Item> SLIME_HELMET = ITEMS.register("slime_helmet",
            () -> new com.example.chocolatequest.item.SlimeArmorItem(ModArmorMaterials.SLIME, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> SLIME_CHESTPLATE = ITEMS.register("slime_chestplate",
            () -> new com.example.chocolatequest.item.SlimeArmorItem(ModArmorMaterials.SLIME, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredItem<Item> SLIME_LEGGINGS = ITEMS.register("slime_leggings",
            () -> new com.example.chocolatequest.item.SlimeArmorItem(ModArmorMaterials.SLIME, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredItem<Item> SLIME_BOOTS = ITEMS.register("slime_boots",
            () -> new com.example.chocolatequest.item.SlimeArmorItem(ModArmorMaterials.SLIME, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    public static final DeferredItem<ArmorItem> SPIDER_HELMET = ITEMS.register("helmet_spider", 
        () -> new com.example.chocolatequest.item.SpiderArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.SPIDER, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<ArmorItem> SPIDER_CHESTPLATE = ITEMS.register("chestplate_spider", 
        () -> new com.example.chocolatequest.item.SpiderArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.SPIDER, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredItem<ArmorItem> SPIDER_LEGGINGS = ITEMS.register("leggings_spider", 
        () -> new com.example.chocolatequest.item.SpiderArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.SPIDER, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredItem<ArmorItem> SPIDER_BOOTS = ITEMS.register("boots_spider", 
        () -> new com.example.chocolatequest.item.SpiderArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.SPIDER, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    public static final DeferredItem<ArmorItem> IRON_DYEABLE_HELMET = ITEMS.register("helmet_iron_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.IRON_DYEABLE, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<ArmorItem> IRON_DYEABLE_CHESTPLATE = ITEMS.register("chestplate_iron_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.IRON_DYEABLE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredItem<ArmorItem> IRON_DYEABLE_LEGGINGS = ITEMS.register("leggings_iron_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.IRON_DYEABLE, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredItem<ArmorItem> IRON_DYEABLE_BOOTS = ITEMS.register("boots_iron_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.IRON_DYEABLE, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    public static final DeferredItem<ArmorItem> DIAMOND_DYEABLE_HELMET = ITEMS.register("helmet_diamond_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.DIAMOND_DYEABLE, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(33))));
    public static final DeferredItem<ArmorItem> DIAMOND_DYEABLE_CHESTPLATE = ITEMS.register("chestplate_diamond_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.DIAMOND_DYEABLE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(33))));
    public static final DeferredItem<ArmorItem> DIAMOND_DYEABLE_LEGGINGS = ITEMS.register("leggings_diamond_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.DIAMOND_DYEABLE, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(33))));
    public static final DeferredItem<ArmorItem> DIAMOND_DYEABLE_BOOTS = ITEMS.register("boots_diamond_dyable", 
        () -> new ArmorItem(com.example.chocolatequest.registry.ModArmorMaterials.DIAMOND_DYEABLE, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(33))));

    public static final DeferredItem<Item> BACKPACK = ITEMS.register("backpack", 
        () -> new com.example.chocolatequest.item.BackpackItem(com.example.chocolatequest.registry.ModArmorMaterials.HEAVY_IRON, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FIRE_STAFF = ITEMS.register("fire_staff", 
        () -> new com.example.chocolatequest.item.staff.FireStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DARK_STAFF = ITEMS.register("dark_staff", 
        () -> new com.example.chocolatequest.item.staff.DarkStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> POISON_STAFF = ITEMS.register("poison_staff", 
        () -> new com.example.chocolatequest.item.staff.PoisonStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> WIND_STAFF = ITEMS.register("wind_staff", 
        () -> new com.example.chocolatequest.item.staff.WindStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> HEAL_STAFF = ITEMS.register("heal_staff", 
        () -> new com.example.chocolatequest.item.staff.HealStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> ELECTRIC_STAFF = ITEMS.register("electric_staff", 
        () -> new com.example.chocolatequest.item.staff.ElectricStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> ICE_STAFF = ITEMS.register("ice_staff", 
        () -> new com.example.chocolatequest.item.staff.IceStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> WATER_STAFF = ITEMS.register("water_staff", 
        () -> new com.example.chocolatequest.item.staff.WaterStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> GUN_STAFF = ITEMS.register("gun_staff", 
        () -> new com.example.chocolatequest.item.staff.GunStaffItem(new Item.Properties()));

    public static final DeferredItem<Item> SPIDER_STAFF = ITEMS.register("staff_spider", 
        () -> new com.example.chocolatequest.item.staff.PoisonStaffItem(new Item.Properties().stacksTo(1))); // mapped to PoisonStaff behavior

    // Invisible/Orb items just for projectile textures
    public static final DeferredItem<Item> PROJECTILE_DARK = ITEMS.register("projectile_dark", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PROJECTILE_POISON = ITEMS.register("projectile_poison", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PROJECTILE_WIND = ITEMS.register("projectile_wind", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PROJECTILE_HEART = ITEMS.register("projectile_heart", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PROJECTILE_BUBBLE = ITEMS.register("projectile_bubble", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HOOKSHOOT = ITEMS.register("hookshoot", () -> new com.example.chocolatequest.item.ItemHookshot(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MAGIC_BELL = ITEMS.register("magic_bell", () -> new com.example.chocolatequest.item.MagicBellItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> UNLIT_TORCH = ITEMS.register("unlit_torch",
            () -> new net.minecraft.world.item.StandingAndWallBlockItem(
                    ModBlocks.UNLIT_TORCH.get(),
                    ModBlocks.UNLIT_TORCH_WALL.get(),
                    new Item.Properties(),
                    net.minecraft.core.Direction.DOWN
            ));

    // Banners
    public static final DeferredItem<Item> BANNER_STAND = ITEMS.register("banner_stand", () -> new net.minecraft.world.item.BlockItem(ModBlocks.BANNER_STAND.get(), new Item.Properties()));
    public static final DeferredItem<Item> BANNER_END = ITEMS.register("banner_end", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_PIGMEN = ITEMS.register("banner_pigmen", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_DWARF = ITEMS.register("banner_dwarf", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_ZOMBIE = ITEMS.register("banner_zombie", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_SKELETON = ITEMS.register("banner_skeleton", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_PIRATE = ITEMS.register("banner_pirate", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_SHADOWS = ITEMS.register("banner_shadows", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_GOBLIN = ITEMS.register("banner_goblin", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_SPECTER = ITEMS.register("banner_specter", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_COLORFUL = ITEMS.register("banner_colorful", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_SQUID = ITEMS.register("banner_squid", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_MINOTAUR = ITEMS.register("banner_minotaur", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BANNER_BULL = ITEMS.register("banner_bull", () -> new com.example.chocolatequest.item.CQBannerItem(new Item.Properties().stacksTo(1)));

    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> MONKING_SPAWN_EGG = ITEMS.register("monking_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.MONKING, 0x4B3D34, 0x91796A, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> WALKER_KING_SPAWN_EGG = ITEMS.register("walker_king_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.WALKER_KING, 0x485259, 0x7E8A94, new net.minecraft.world.item.Item.Properties()));

    // Capes
    public static final DeferredItem<Item> CAPE_GOLEM = ITEMS.register("cape_golem", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_ILLAGER = ITEMS.register("cape_illager", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_PIRATE = ITEMS.register("cape_pirate", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_SKELETON = ITEMS.register("cape_skeleton", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_WALKER = ITEMS.register("cape_walker", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_ICEBULL = ITEMS.register("cape_icebull", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_ENDERMAN = ITEMS.register("cape_enderman", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_GOBLINSHAMAN = ITEMS.register("cape_goblinshaman", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPE_SPECTERLORD = ITEMS.register("cape_specterlord", () -> new com.example.chocolatequest.item.CapeItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CLOUD_BOOTS = ITEMS.register("cloud_boots", 
        () -> new com.example.chocolatequest.item.CloudBootsItem(com.example.chocolatequest.registry.ModArmorMaterials.CLOUD, ArmorItem.Type.BOOTS, 
            new Item.Properties().stacksTo(1).attributes(
                net.minecraft.world.item.component.ItemAttributeModifiers.builder()
                    .add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 
                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                            net.minecraft.resources.ResourceLocation.withDefaultNamespace("armor.boots"),
                            3.0, // Defense of cloud boots
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), 
                        net.minecraft.world.entity.EquipmentSlotGroup.FEET)
                    .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 
                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "cloud_boots_speed"),
                            0.025, // Noticeable, but no longer an excessive +60% of player base speed
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.FEET)
                    .add(net.minecraft.world.entity.ai.attributes.Attributes.JUMP_STRENGTH,
                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "cloud_boots_jump"),
                            0.50, // Powerful cloud-assisted jump (roughly Jump Boost V)
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.FEET)
                    .build())));

    public static final DeferredItem<Item> POISONOUS_WEB = ITEMS.register("poisonous_web", 
        () -> new BlockItem(ModBlocks.POISONOUS_WEB.get(), new Item.Properties()));
    public static final DeferredItem<Item> PHYLACTERY = ITEMS.register("phylactery", 
        () -> new BlockItem(ModBlocks.PHYLACTERY.get(), new Item.Properties()));
    public static final DeferredItem<Item> SPAWNER = ITEMS.register("spawner", 
        () -> new BlockItem(ModBlocks.SPAWNER.get(), new Item.Properties()));
    public static final DeferredItem<Item> BOSS_BLOCK = ITEMS.register("boss_block", 
        () -> new BlockItem(ModBlocks.BOSS_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> EXPORTER_CHEST = ITEMS.register("exporter_chest", 
        () -> new BlockItem(ModBlocks.EXPORTER_CHEST.get(), new Item.Properties()));
    public static final DeferredItem<Item> FORCE_FIELD_NEXUS = ITEMS.register("force_field_nexus",
            () -> new com.example.chocolatequest.item.ForceFieldNexusItem(ModBlocks.FORCE_FIELD_NEXUS.get(), new Item.Properties()));
    public static final DeferredItem<Item> NEXUS_CORE = ITEMS.register("nexus_core",
            () -> new com.example.chocolatequest.item.NexusCoreItem(ModBlocks.NEXUS_CORE.get(), new Item.Properties()));
    public static final DeferredItem<Item> MAP_PLACEHOLDER = ITEMS.register("map_placeholder", 
        () -> new BlockItem(ModBlocks.MAP_PLACEHOLDER.get(), new Item.Properties()));
    public static final DeferredItem<Item> TNT_CQR = ITEMS.register("tnt_cqr", 
        () -> new BlockItem(ModBlocks.TNT_CQR.get(), new Item.Properties()));
    public static final DeferredItem<Item> NULL_BLOCK = ITEMS.register("null_block", 
        () -> new BlockItem(ModBlocks.NULL_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> SOUL_BOTTLE = ITEMS.register("soul_bottle", 
        () -> new com.example.chocolatequest.item.SoulBottleItem(new Item.Properties().stacksTo(64)));

    public static final DeferredItem<Item> SPAWNER_CASTLE_PLAIN = ITEMS.register("castleplain", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/castle_arena-v2.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/castle_giant_tree.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/castle_lake_alpha.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/castle_neaekkleasia-v2.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/castle_ravine_v2.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/kanos_wizardscape-v3.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/redstone_castle-v2.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/normal/riverland_castle_v2.nbt")
            ),
            java.util.List.of(
                "cqrepoured:cq_zombie",
                "cqrepoured:cq_skeleton",
                "cqrepoured:cq_gremlin",
                "cqrepoured:cq_golem",
                "cqrepoured:cq_illager"
            ),
            new Item.Properties()
        )
    );
    public static final DeferredItem<Item> SPAWNER_CASTLE_SNOW = ITEMS.register("castlesnow", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/castle_snow_cathedral.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/castle_snow_cube_b.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/castle_snow_mountain_v2.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/castle_snow_redstone.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/castle_snow_spire_v2.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/mountain-village-v1.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/unnamedsnowcastle.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/snow/walker-citadel-v10.nbt")
            ),
            java.util.List.of("cqrepoured:cq_walker"),
            new Item.Properties()
        )
    );
    public static final DeferredItem<Item> SPAWNER_CASTLE_FLOATING = ITEMS.register("castlefloating", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/castles/floating/castle_floating_island.nbt")
            ),
            java.util.List.of("cqrepoured:cq_zombie"),
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_OUTPOST = ITEMS.register("spawner_outpost", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/outposts/plains/camp-v1.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/outposts/plains/outpost-tower-v2.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/outposts/plains/ruined-tower-v5.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/outposts/snow/walker_outpost_camp_v3.nbt")
            ),
            java.util.List.of("cqrepoured:cq_goblin", "cqrepoured:cq_walker", "cqrepoured:cq_pirate"),
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_ILLAGER_TOWER = ITEMS.register("spawner_illager_tower", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/outposts/plains/illager-tower-v3.nbt")
            ),
            java.util.List.of("cqrepoured:cq_illager"),
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_SHIP = ITEMS.register("spawner_ship", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/ships/ships_large/galleon.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/ships/ships_small/creeper_ship.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/ships/ships_small/enchanted_ship.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/ships/ships_small/skeleton_ship.nbt")
            ),
            java.util.List.of("cqrepoured:cq_pirate"),
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_STRONGHOLD = ITEMS.register("spawner_stronghold", 
        () -> new com.example.chocolatequest.item.StrongholdSpawnerItem(
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_TAVERN = ITEMS.register("spawner_tavern", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/taverns/campsite-v4.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/taverns/medium-tavern-2-v6.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/taverns/tavern-small-1-v7.nbt")
            ),
            java.util.List.of("cqrepoured:cq_human"),
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_CAVE = ITEMS.register("spawner_cave", 
        () -> new com.example.chocolatequest.item.CaveSpawnerItem(
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_VOLCANO = ITEMS.register("spawner_volcano", 
        () -> new com.example.chocolatequest.item.VolcanoSpawnerItem(
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_NETHERCITY = ITEMS.register("spawner_nethercity", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/nethercity/buildings/netheralchemicalmuseum.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/nethercity/buildings/netherbarracks.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/nethercity/buildings/nethercityportalroom.nbt")
            ),
            java.util.List.of("cqrepoured:cq_boarman"),
            new Item.Properties()
        )
    );

    public static final DeferredItem<Item> SPAWNER_FLOATING_CITY = ITEMS.register("spawner_floating_city", 
        () -> new com.example.chocolatequest.item.StructureSpawnerItem(
            java.util.List.of(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/floatingcity/islands/nethercitydiamondpyramid.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/nether_island/nether_dragon_island.nbt"),
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cqrepoured", "structure/islands/pirate_island.nbt")
            ),
            java.util.List.of("cqrepoured:cq_specter", "cqrepoured:cq_boarman", "cqrepoured:cq_pirate"),
            new Item.Properties()
        )
    );

    public static void applySharedCooldown(net.minecraft.world.entity.player.Player player, Item sourceItem, int ticks) {
        if (sourceItem instanceof com.example.chocolatequest.item.DaggerItem) {
            for (var item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                if (item instanceof com.example.chocolatequest.item.DaggerItem) {
                    player.getCooldowns().addCooldown(item, ticks);
                }
            }
        } else if (sourceItem instanceof com.example.chocolatequest.item.ItemRevolver) {
            for (var item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                if (item instanceof com.example.chocolatequest.item.ItemRevolver) {
                    player.getCooldowns().addCooldown(item, ticks);
                }
            }
        } else if (sourceItem instanceof com.example.chocolatequest.item.ItemHookshotBase) {
            for (var item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                if (item instanceof com.example.chocolatequest.item.ItemHookshotBase) {
                    player.getCooldowns().addCooldown(item, ticks);
                }
            }
        } else if (sourceItem.getClass().getPackageName().equals("com.example.chocolatequest.item.staff")) {
            for (var item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                if (item.getClass().getPackageName().equals("com.example.chocolatequest.item.staff")) {
                    player.getCooldowns().addCooldown(item, ticks);
                }
            }
        } else {
            player.getCooldowns().addCooldown(sourceItem, ticks);
        }
    }

    
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> GIANT_TORTOISE_SPAWN_EGG = ITEMS.register("giant_tortoise_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.GIANT_TORTOISE, 0x113311, 0x338833, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> ENDERMENACE_SPAWN_EGG = ITEMS.register("endermenace_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.ENDERMENACE, 0x110022, 0xAA00DD, new net.minecraft.world.item.Item.Properties()));
    public static final net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> SHULKER_GOLEM_SPAWN_EGG = ITEMS.register("shulker_golem_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SHULKER_GOLEM, 0x2B203D, 0xB56BE8, new net.minecraft.world.item.Item.Properties()));

    // --- SHIELDS ---
    public static final DeferredItem<Item> SHIELD_BULL = ITEMS.register("shield_bull", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_CARL = ITEMS.register("shield_carl", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_DRAGONSLAYER = ITEMS.register("shield_dragonslayer", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_FIRE = ITEMS.register("shield_fire", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_GOBLIN = ITEMS.register("shield_goblin", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_MONKING = ITEMS.register("shield_monking", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_MOON = ITEMS.register("shield_moon", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_MUMMY = ITEMS.register("shield_mummy", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_PIGMAN = ITEMS.register("shield_pigman", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_PIRATE = ITEMS.register("shield_pirate", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_PIRATE2 = ITEMS.register("shield_pirate2", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_RAINBOW = ITEMS.register("shield_rainbow", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_REFLECTIVE = ITEMS.register("shield_reflective", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_RUSTED = ITEMS.register("shield_rusted", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_SKELETON_FRIENDS = ITEMS.register("shield_skeleton_friends", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_SPECTER = ITEMS.register("shield_specter", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_SPIDER = ITEMS.register("shield_spider", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_SUN = ITEMS.register("shield_sun", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_TOMB = ITEMS.register("shield_tomb", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_TRITON = ITEMS.register("shield_triton", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_TURTLE = ITEMS.register("shield_turtle", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_WALKER = ITEMS.register("shield_walker", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_WARPED = ITEMS.register("shield_warped", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_ZOMBIE = ITEMS.register("shield_zombie", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));
    public static final DeferredItem<Item> SHIELD_WALKER_KING = ITEMS.register("shield_walker_king", () -> new net.minecraft.world.item.ShieldItem(new Item.Properties().durability(336)));

        public static final DeferredItem<Item> EXPORTER_CHEST_CUSTOM = ITEMS.register("exporter_chest_custom", () -> new net.minecraft.world.item.BlockItem(ModBlocks.EXPORTER_CHEST_CUSTOM.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> TABLE = ITEMS.register("table", () -> new net.minecraft.world.item.BlockItem(ModBlocks.TABLE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ANDESITE_CARVED = ITEMS.register("andesite_carved", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ANDESITE_CARVED.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ANDESITE_CUBE = ITEMS.register("andesite_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ANDESITE_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ANDESITE_SCALE = ITEMS.register("andesite_scale", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ANDESITE_SCALE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ANDESITE_SQUARE = ITEMS.register("andesite_square", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ANDESITE_SQUARE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ANDESITE_SMALL = ITEMS.register("andesite_small", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ANDESITE_SMALL.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ANDESITE_LARGE = ITEMS.register("andesite_large", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ANDESITE_LARGE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> DIORITE_CARVED = ITEMS.register("diorite_carved", () -> new net.minecraft.world.item.BlockItem(ModBlocks.DIORITE_CARVED.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> DIORITE_CUBE = ITEMS.register("diorite_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.DIORITE_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> DIORITE_SCALE = ITEMS.register("diorite_scale", () -> new net.minecraft.world.item.BlockItem(ModBlocks.DIORITE_SCALE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> DIORITE_SQUARE = ITEMS.register("diorite_square", () -> new net.minecraft.world.item.BlockItem(ModBlocks.DIORITE_SQUARE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> DIORITE_SMALL = ITEMS.register("diorite_small", () -> new net.minecraft.world.item.BlockItem(ModBlocks.DIORITE_SMALL.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> DIORITE_LARGE = ITEMS.register("diorite_large", () -> new net.minecraft.world.item.BlockItem(ModBlocks.DIORITE_LARGE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> GRANITE_CARVED = ITEMS.register("granite_carved", () -> new net.minecraft.world.item.BlockItem(ModBlocks.GRANITE_CARVED.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> GRANITE_CUBE = ITEMS.register("granite_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.GRANITE_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> GRANITE_SCALE = ITEMS.register("granite_scale", () -> new net.minecraft.world.item.BlockItem(ModBlocks.GRANITE_SCALE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> GRANITE_SQUARE = ITEMS.register("granite_square", () -> new net.minecraft.world.item.BlockItem(ModBlocks.GRANITE_SQUARE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> GRANITE_SMALL = ITEMS.register("granite_small", () -> new net.minecraft.world.item.BlockItem(ModBlocks.GRANITE_SMALL.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> GRANITE_LARGE = ITEMS.register("granite_large", () -> new net.minecraft.world.item.BlockItem(ModBlocks.GRANITE_LARGE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PRISMARINE_CARVED = ITEMS.register("prismarine_carved", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PRISMARINE_CARVED.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PRISMARINE_CUBE = ITEMS.register("prismarine_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PRISMARINE_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PRISMARINE_SQUARE = ITEMS.register("prismarine_square", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PRISMARINE_SQUARE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PRISMARINE_SMALL = ITEMS.register("prismarine_small", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PRISMARINE_SMALL.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PRISMARINE_LARGE = ITEMS.register("prismarine_large", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PRISMARINE_LARGE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ENDSTONE_CARVED = ITEMS.register("endstone_carved", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ENDSTONE_CARVED.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ENDSTONE_CUBE = ITEMS.register("endstone_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ENDSTONE_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ENDSTONE_SCALE = ITEMS.register("endstone_scale", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ENDSTONE_SCALE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ENDSTONE_SQUARE = ITEMS.register("endstone_square", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ENDSTONE_SQUARE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> ENDSTONE_SMALL = ITEMS.register("endstone_small", () -> new net.minecraft.world.item.BlockItem(ModBlocks.ENDSTONE_SMALL.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PURPUR_CARVED = ITEMS.register("purpur_carved", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PURPUR_CARVED.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PURPUR_CUBE = ITEMS.register("purpur_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PURPUR_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PURPUR_SCALE = ITEMS.register("purpur_scale", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PURPUR_SCALE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PURPUR_SMALL = ITEMS.register("purpur_small", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PURPUR_SMALL.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> PURPUR_LARGE = ITEMS.register("purpur_large", () -> new net.minecraft.world.item.BlockItem(ModBlocks.PURPUR_LARGE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> RED_NETHERBRICK_CARVED = ITEMS.register("red_netherbrick_carved", () -> new net.minecraft.world.item.BlockItem(ModBlocks.RED_NETHERBRICK_CARVED.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> RED_NETHERBRICK_CUBE = ITEMS.register("red_netherbrick_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.RED_NETHERBRICK_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> RED_NETHERBRICK_SCALE = ITEMS.register("red_netherbrick_scale", () -> new net.minecraft.world.item.BlockItem(ModBlocks.RED_NETHERBRICK_SCALE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> RED_NETHERBRICK_SQUARE = ITEMS.register("red_netherbrick_square", () -> new net.minecraft.world.item.BlockItem(ModBlocks.RED_NETHERBRICK_SQUARE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> RED_NETHERBRICK_LARGE = ITEMS.register("red_netherbrick_large", () -> new net.minecraft.world.item.BlockItem(ModBlocks.RED_NETHERBRICK_LARGE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> STONE_CUBE = ITEMS.register("stone_cube", () -> new net.minecraft.world.item.BlockItem(ModBlocks.STONE_CUBE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> STONE_SCALE = ITEMS.register("stone_scale", () -> new net.minecraft.world.item.BlockItem(ModBlocks.STONE_SCALE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> STONE_SQUARE = ITEMS.register("stone_square", () -> new net.minecraft.world.item.BlockItem(ModBlocks.STONE_SQUARE.get(), new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<Item> STONE_SMALL = ITEMS.register("stone_small", () -> new net.minecraft.world.item.BlockItem(ModBlocks.STONE_SMALL.get(), new net.minecraft.world.item.Item.Properties()));

    public static void register(net.neoforged.bus.api.IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

