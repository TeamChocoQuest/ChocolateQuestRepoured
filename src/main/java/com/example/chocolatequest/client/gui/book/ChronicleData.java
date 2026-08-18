package com.example.chocolatequest.client.gui.book;

import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ChronicleData {

    public enum Category {
        INDEX("Index", "I", 0xD4AF37, 0),
        DUNGEONS("Dungeons", "D", 0x2E7D32, 1),
        TRADES("Trades", "T", 0xD35400, 2),
        FACTIONS("Factions", "F", 0x1976D2, 3),
        ARSENAL("Arsenal", "A", 0xC0392B, 4),
        MAGIC("Magic", "M", 0x8E44AD, 5),
        BOSSES("Bosses", "B", 0xE67E22, 6),
        CREDITS("Credits", "C", 0x16A085, 14);

        public final String label;
        public final String letter;
        public final int color;
        public final int targetChapter;

        Category(String label, String letter, int color, int targetChapter) {
            this.label = label;
            this.letter = letter;
            this.color = color;
            this.targetChapter = targetChapter;
        }
    }

    public static class ShowcaseItem {
        public final ItemStack stack;
        public final String title;
        public final String description;

        public ShowcaseItem(ItemStack stack, String title, String description) {
            this.stack = stack;
            this.title = title;
            this.description = description;
        }
    }

    public static class TradeRecipe {
        public final ItemStack input1;
        public final ItemStack input2;
        public final ItemStack output;
        public final String title;

        public TradeRecipe(ItemStack input1, ItemStack input2, ItemStack output, String title) {
            this.input1 = input1;
            this.input2 = input2;
            this.output = output;
            this.title = title;
        }
    }

    public static class BossEntry {
        public final EntityType<?> entityType;
        public final float scale;
        public final String bossTitle;
        public final String subtitle;
        public final String description;
        public final String health;
        public final String difficulty;
        public final List<ShowcaseItem> signatureDrops;

        public BossEntry(EntityType<?> entityType, float scale, String bossTitle, String subtitle, String description, String health, String difficulty, List<ShowcaseItem> signatureDrops) {
            this.entityType = entityType;
            this.scale = scale;
            this.bossTitle = bossTitle;
            this.subtitle = subtitle;
            this.description = description;
            this.health = health;
            this.difficulty = difficulty;
            this.signatureDrops = signatureDrops;
        }
    }

    public static class PageContent {
        public enum PageType {
            STANDARD,
            INDEX,
            BOSS_SHOWCASE,
            ITEM_GRID,
            TRADE_RECIPES
        }

        public final PageType type;
        public final String header;
        public final String subHeader;
        public final List<String> textLines = new ArrayList<>();
        public final List<ShowcaseItem> items = new ArrayList<>();
        public final List<TradeRecipe> trades = new ArrayList<>();
        public BossEntry bossData = null;

        public PageContent(PageType type, String header, String subHeader) {
            this.type = type;
            this.header = header;
            this.subHeader = subHeader;
        }

        public PageContent addLine(String line) {
            this.textLines.add(line);
            return this;
        }

        public PageContent addItem(ItemStack stack, String name, String desc) {
            this.items.add(new ShowcaseItem(stack, name, desc));
            return this;
        }

        public PageContent addTrade(ItemStack in1, ItemStack in2, ItemStack out, String title) {
            this.trades.add(new TradeRecipe(in1, in2, out, title));
            return this;
        }

        public PageContent setBoss(BossEntry boss) {
            this.bossData = boss;
            return this;
        }
    }

    public static class Chapter {
        public final String title;
        public final Category category;
        public final PageContent leftPage;
        public final PageContent rightPage;

        public Chapter(String title, Category category, PageContent leftPage, PageContent rightPage) {
            this.title = title;
            this.category = category;
            this.leftPage = leftPage;
            this.rightPage = rightPage;
        }
    }

    public static List<Chapter> buildChapters() {
        List<Chapter> chapters = new ArrayList<>();

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        PageContent ch0Left = new PageContent(PageContent.PageType.INDEX, "CHRONICLE", "Table of Contents")
                .addLine("§lChocolate Quest Repoured§r")
                .addLine("§8§oThe Adventurer's Codex§r")
                .addLine("")
                .addLine("§lSelect a Chapter:§r")
                .addLine("1. Dungeons & Castles")
                .addLine("2. Tavern Trades & Armory")
                .addLine("3. Factions & Bastions")
                .addLine("4. Weapons & Firearms")
                .addLine("5. Magic & Relics")
                .addLine("6. Boss Showcases (8 Bosses)")
                .addLine("7. Credits & Dedication");

        PageContent ch0Right = new PageContent(PageContent.PageType.STANDARD, "WELCOME", "Introduction")
                .addLine("§lWelcome Adventurer!§r")
                .addLine("")
                .addLine("Welcome to Chocolate Quest Repoured! This mod brings back giant castles, flying islands, pirate fleets, tavern trades, and dangerous dungeons.")
                .addLine("")
                .addLine("Click the bookmarks on the right or any chapter title on the index page to explore mechanics, trade recipes, and 3D boss showcases.")
                .addLine("")
                .addLine("§8Good luck on your adventures!");

        chapters.add(new Chapter("Index & Overview", Category.INDEX, ch0Left, ch0Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        PageContent ch1Left = new PageContent(PageContent.PageType.STANDARD, "CASTLES", "Overworld Fortresses")
                .addLine("§lGrand Castles§r")
                .addLine("")
                .addLine("• §lPlains Castles§r: Giant stone fortresses with throne rooms, arenas, and libraries.")
                .addLine("")
                .addLine("• §lSnow Castles§r: Multi-story mountain fortresses found in snowy biomes.")
                .addLine("")
                .addLine("• §lSky Castles§r: Massive floating island castles up in the clouds.");

        PageContent ch1Right = new PageContent(PageContent.PageType.ITEM_GRID, "SEAS & RUINS", "Structures & Forts")
                .addLine("§lFleets & Keeps§r")
                .addLine("• §lPirate Ships§r: Galleons and warships sailing the oceans.")
                .addLine("• §lVolcano Keeps§r: Magma caves deep below ground.")
                .addLine("• §lNether Cities§r: Sprawling fortress cities across the Nether.")
                .addItem(new ItemStack(ModItems.SPAWNER_CASTLE_PLAIN.get()), "Plains Castle", "Overworld castle keep")
                .addItem(new ItemStack(ModItems.SPAWNER_SHIP.get()), "Pirate Galleon", "Ocean warship")
                .addItem(new ItemStack(ModItems.SPAWNER_VOLCANO.get()), "Volcano Keep", "Subterranean magma lair");

        chapters.add(new Chapter("Dungeons & Castles", Category.DUNGEONS, ch1Left, ch1Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        PageContent ch2Left = new PageContent(PageContent.PageType.TRADE_RECIPES, "TAVERN TRADES", "Outpost Blacksmithing")
                .addTrade(
                        new ItemStack(Items.IRON_HELMET),
                        new ItemStack(Items.SPIDER_EYE, 3),
                        new ItemStack(ModItems.SPIDER_HELMET.get()),
                        "Spider Helmet"
                )
                .addTrade(
                        new ItemStack(Items.IRON_CHESTPLATE),
                        new ItemStack(ModItems.BULL_HORN.get(), 2),
                        new ItemStack(ModItems.BULL_CHESTPLATE.get()),
                        "Bull Chestplate"
                )
                .addTrade(
                        new ItemStack(Items.DIAMOND_HELMET),
                        new ItemStack(ModItems.SCALE_TURTLE.get(), 2),
                        new ItemStack(ModItems.TURTLE_HELMET.get()),
                        "Turtle Helmet"
                );

        PageContent ch2Right = new PageContent(PageContent.PageType.TRADE_RECIPES, "SUPPLIES", "Ammo & Utilities")
                .addTrade(
                        new ItemStack(Items.IRON_INGOT, 2),
                        new ItemStack(Items.GUNPOWDER, 2),
                        new ItemStack(ModItems.BULLET_IRON.get(), 16),
                        "Iron Bullets (x16)"
                )
                .addTrade(
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.GUNPOWDER, 2),
                        new ItemStack(ModItems.BULLET_DIAMOND.get(), 8),
                        "Diamond Bullets (x8)"
                )
                .addTrade(
                        new ItemStack(Items.LEATHER_BOOTS),
                        new ItemStack(ModItems.SLIME_BALL_CQR.get(), 4),
                        new ItemStack(ModItems.SLIME_BOOTS.get()),
                        "Slime Boots"
                );

        chapters.add(new Chapter("Tavern Trades & Armory", Category.TRADES, ch2Left, ch2Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        PageContent ch3Left = new PageContent(PageContent.PageType.STANDARD, "FACTIONS", "Inhabitants of the Realm")
                .addLine("§lFactions§r")
                .addLine("")
                .addLine("• §lGremlins & Goblins§r: Forest tribes with fast spear fighters and shamans.")
                .addLine("")
                .addLine("• §lHumans & Dwarves§r: Tavern keepers and heavy armored knights.")
                .addLine("")
                .addLine("• §lPirates§r: Swashbucklers with flintlock pistols and cutlasses.");

        PageContent ch3Right = new PageContent(PageContent.PageType.ITEM_GRID, "REPUTATION", "Allegiances & Banners")
                .addLine("§lReputation§r")
                .addLine("• Helping and trading with NPCs earns faction reputation.")
                .addLine("• Attacking them turns the whole faction hostile on sight!")
                .addLine("• Walkers and Undead are always hostile.")
                .addItem(new ItemStack(ModItems.BANNER_GOBLIN.get()), "Goblin Banner", "Emblem of forest clans")
                .addItem(new ItemStack(ModItems.BANNER_DWARF.get()), "Dwarf Banner", "Crest of mountain halls")
                .addItem(new ItemStack(ModItems.BANNER_PIRATE.get()), "Pirate Jolly Roger", "Mark of pirate fleets");

        chapters.add(new Chapter("Factions & Bastions", Category.FACTIONS, ch3Left, ch3Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        PageContent ch4Left = new PageContent(PageContent.PageType.STANDARD, "FIREARMS", "Guns & Ammo")
                .addLine("§lFirearms§r")
                .addLine("• §lRevolver§r: Holds 6 shots. Sneak + Right Click with bullets in inventory to reload.")
                .addLine("• §lMusket§r: Powerful sniper shot. Combine with a knife in crafting for a melee bayonet!")
                .addLine("")
                .addLine("§lBullets (Damage modifiers)§r:")
                .addLine("  - Iron: +2.5 DMG")
                .addLine("  - Gold: +2.0 DMG (Fire)")
                .addLine("  - Diamond: +4.0 DMG")
                .addLine("  - Fire: +2.5 DMG (Fire)");

        PageContent ch4Right = new PageContent(PageContent.PageType.ITEM_GRID, "MELEE ARMS", "Blades & Daggers")
                .addLine("§lMelee Arsenal§r")
                .addLine("• §lGreatswords§r: Sweeping hits with +1 extra block reach bonus.")
                .addLine("• §lDaggers§r: Fast attacks and 3x damage on backstabs!")
                .addLine("• §lSpears§r: Long range thrusts with +1 reach.")
                .addItem(new ItemStack(ModItems.REVOLVER.get()), "Revolver", "Six-shot firearm")
                .addItem(new ItemStack(ModItems.MONKING_GREAT_SWORD.get()), "Greatsword", "+1 Reach bonus")
                .addItem(new ItemStack(ModItems.SHADOW_DAGGER.get()), "Shadow Dagger", "3x Backstab damage");

        chapters.add(new Chapter("Weapons & Firearms", Category.ARSENAL, ch4Left, ch4Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        PageContent ch5Left = new PageContent(PageContent.PageType.STANDARD, "MAGIC", "Elemental Staves")
                .addLine("§lMagic Staves§r")
                .addLine("§8Staves consume hunger instead of mana!§r")
                .addLine("")
                .addLine("• §lFire Staff§r: Channels a continuous flamethrower cone.")
                .addLine("• §lIce Staff§r: Freezes and slows targets.")
                .addLine("• §lWind Staff§r: Blasts a wind orb that knocks mobs away.")
                .addLine("• §lPoison Staff§r: Shoots a poison bolt that inflicts poison.")
                .addLine("• §lDark Staff§r: Vampiric staff that steals health on hit!");

        PageContent ch5Right = new PageContent(PageContent.PageType.ITEM_GRID, "RELICS", "Mobility & Utilities")
                .addLine("§lRelics & Tools§r")
                .addLine("• §lSpider Hook & Hookshot§r: Grapple to blocks or pull mobs.")
                .addLine("• §lCloud Boots§r: Huge jump boost & soft landing.")
                .addLine("• §lTeleport Stone§r: Sneak+Right Click to save a spot, use to warp back.")
                .addItem(new ItemStack(ModItems.FIRE_STAFF.get()), "Fire Staff", "Flamethrower staff")
                .addItem(new ItemStack(ModItems.SPIDER_HOOK.get()), "Spider Hook", "Grappling hook")
                .addItem(new ItemStack(ModItems.CLOUD_BOOTS.get()), "Cloud Boots", "High jump boots");

        chapters.add(new Chapter("Magic & Relics", Category.MAGIC, ch5Left, ch5Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> dragonDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.SHIELD_DRAGONSLAYER.get()), "Dragonslayer Shield", "Fireproof shield"),
                new ShowcaseItem(new ItemStack(ModItems.FIRE_STAFF.get()), "Fire Staff", "Flamethrower staff"),
                new ShowcaseItem(new ItemStack(Items.BLAZE_ROD), "Blaze Rod", "Fiery catalyst")
        );
        BossEntry dragonBoss = new BossEntry(
                ModEntities.CQR_DRAGON.get(),
                11.0F,
                "Green Dragon",
                "Terror of the Skies",
                "Flies around castle arenas and shoots fire breath.",
                "400 HP",
                "High",
                dragonDrops
        );

        PageContent ch6Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: DRAGON", "Boss Overview")
                .addLine("§lThe Green Dragon§r")
                .addLine("§8Flying Apex Boss§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 400 HP (200 Hearts)")
                .addLine("• §lType§r: Aerial Dragon")
                .addLine("• §lArmor§r: Heavy Scales")
                .addLine("• §lElement§r: Fire Breath");

        PageContent ch6Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(dragonBoss);

        chapters.add(new Chapter("Boss: Green Dragon", Category.BOSSES, ch6Left, ch6Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> walkerDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.WALKER_SWORD.get()), "Walker Sword", "Frost blade"),
                new ShowcaseItem(new ItemStack(ModItems.SHIELD_WALKER_KING.get()), "King's Shield", "Royal crest"),
                new ShowcaseItem(new ItemStack(ModItems.CAPE_WALKER.get()), "Walker Cape", "Monarch mantle")
        );
        BossEntry walkerBoss = new BossEntry(
                ModEntities.WALKER_KING.get(),
                26.0F,
                "The Walker King",
                "Frozen Citadel Monarch",
                "Commands blizzard tornadoes and heavy frost slashes.",
                "600 HP",
                "High",
                walkerDrops
        );

        PageContent ch7Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: WALKER KING", "Boss Overview")
                .addLine("§lThe Walker King§r")
                .addLine("§8Frozen Monarch Boss§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 600 HP (300 Hearts)")
                .addLine("• §lType§r: Walker Sovereign")
                .addLine("• §lArmor§r: Royal Plate")
                .addLine("• §lElement§r: Frost & Wind");

        PageContent ch7Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(walkerBoss);

        chapters.add(new Chapter("Boss: Walker King", Category.BOSSES, ch7Left, ch7Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> exterminatorDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.FLAMETHROWER.get()), "Flamethrower", "Continuous fire gun"),
                new ShowcaseItem(new ItemStack(ModItems.BACKPACK.get()), "Chest Backpack", "Extra storage rig"),
                new ShowcaseItem(new ItemStack(ModItems.CANNON_BALL.get()), "Cannonballs", "Demolition ordnance")
        );
        BossEntry exterminatorBoss = new BossEntry(
                ModEntities.EXTERMINATOR.get(),
                22.0F,
                "The Exterminator",
                "Mechanical Siege Titan",
                "Armored automaton with cannonballs, flamethrowers, and heavy artillery.",
                "250 HP",
                "Extreme",
                exterminatorDrops
        );

        PageContent ch8Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: EXTERMINATOR", "Boss Overview")
                .addLine("§lThe Exterminator§r")
                .addLine("§8Mechanical Siege Boss§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 250 HP (125 Hearts)")
                .addLine("• §lType§r: Steampunk Automaton")
                .addLine("• §lArmor§r: Reinforced Iron")
                .addLine("• §lArsenal§r: Cannons & Flamethrower");

        PageContent ch8Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(exterminatorBoss);

        chapters.add(new Chapter("Boss: Exterminator", Category.BOSSES, ch8Left, ch8Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> lichDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.PHYLACTERY.get()), "Phylactery", "Immortal vessel"),
                new ShowcaseItem(new ItemStack(ModItems.DARK_STAFF.get()), "Dark Staff", "Vampiric health-drain staff"),
                new ShowcaseItem(new ItemStack(ModItems.CURSED_BONE.get()), "Cursed Bone", "Summoning catalyst")
        );
        BossEntry lichBoss = new BossEntry(
                ModEntities.LICH.get(),
                28.0F,
                "The Lich",
                "Undying Sorcerer",
                "Siphons health, summons wraiths, and regenerates through phylacteries.",
                "180 HP",
                "Very High",
                lichDrops
        );

        PageContent ch9Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: THE LICH", "Boss Overview")
                .addLine("§lThe Lich§r")
                .addLine("§8Undead Sorcerer Boss§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 180 HP (90 Hearts)")
                .addLine("• §lType§r: Undead Necromancer")
                .addLine("• §lSpecial§r: Phylactery Immortality")
                .addLine("• §lElement§r: Dark Wither Magic");

        PageContent ch9Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(lichBoss);

        chapters.add(new Chapter("Boss: The Lich", Category.BOSSES, ch9Left, ch9Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> shelobDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.SPIDER_HOOK.get()), "Spider Hook", "Arachnid hookshot"),
                new ShowcaseItem(new ItemStack(ModItems.SPIDER_LEATHER.get()), "Spider Leather", "Chitinous hide"),
                new ShowcaseItem(new ItemStack(ModItems.SPIDER_SWORD.get()), "Spider Sword", "Poison-infused blade")
        );
        BossEntry shelobBoss = new BossEntry(
                ModEntities.SHELOB.get(),
                18.0F,
                "Shelob",
                "Queen of the Webbed Abyss",
                "Shoots sticky webs, leaps across chambers, and summons spider minions.",
                "200 HP",
                "High",
                shelobDrops
        );

        PageContent ch10Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: SHELOB", "Boss Overview")
                .addLine("§lShelob§r")
                .addLine("§8Giant Arachnid Sovereign§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 200 HP (100 Hearts)")
                .addLine("• §lType§r: Giant Arachnid")
                .addLine("• §lAbilities§r: Web Shot & Leaping")
                .addLine("• §lMinions§r: Spider Swarms");

        PageContent ch10Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(shelobBoss);

        chapters.add(new Chapter("Boss: Shelob", Category.BOSSES, ch10Left, ch10Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> necromancerDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.CURSED_BONE.get()), "Cursed Bone", "Summoning catalyst"),
                new ShowcaseItem(new ItemStack(ModItems.DARK_STAFF.get()), "Dark Staff", "Vampiric staff"),
                new ShowcaseItem(new ItemStack(Items.ECHO_SHARD), "Echo Shard", "Necrotic resonance core")
        );
        BossEntry necromancerBoss = new BossEntry(
                ModEntities.NECROMANCER.get(),
                26.0F,
                "The Necromancer",
                "Master of Dark Rites",
                "Summons legions of skeletons, zombies, and casts shadow bolts.",
                "150 HP",
                "High",
                necromancerDrops
        );

        PageContent ch11Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: NECROMANCER", "Boss Overview")
                .addLine("§lThe Necromancer§r")
                .addLine("§8Dark Summoner Boss§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 150 HP (75 Hearts)")
                .addLine("• §lType§r: Necromancer Summoner")
                .addLine("• §lArsenal§r: Undead Minions")
                .addLine("• §lMagic§r: Shadow Hexes");

        PageContent ch11Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(necromancerBoss);

        chapters.add(new Chapter("Boss: Necromancer", Category.BOSSES, ch11Left, ch11Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> pirateDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.CAPTAIN_REVOLVER.get()), "Captain Revolver", "Custom flintlock firearm"),
                new ShowcaseItem(new ItemStack(ModItems.BANNER_PIRATE.get()), "Pirate Jolly Roger", "Faction flag"),
                new ShowcaseItem(new ItemStack(ModItems.PIRATE_DAGGER.get()), "Pirate Cutlass", "Boarding blade")
        );
        BossEntry pirateBoss = new BossEntry(
                ModEntities.CQ_PIRATE_CAPTAIN.get(),
                26.0F,
                "Pirate Captain",
                "Dread Sovereign of the Seas",
                "Commands galleons, fires explosive pistols, and leads pirate crews.",
                "250 HP",
                "High",
                pirateDrops
        );

        PageContent ch12Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: PIRATE CAPTAIN", "Boss Overview")
                .addLine("§lPirate Captain§r")
                .addLine("§8Ocean Galleon Sovereign§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 250 HP (125 Hearts)")
                .addLine("• §lType§r: Pirate Dreadlord")
                .addLine("• §lWeapons§r: Revolver & Cutlass")
                .addLine("• §lAllies§r: Pirate Crew & Parrots");

        PageContent ch12Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(pirateBoss);

        chapters.add(new Chapter("Boss: Pirate Captain", Category.BOSSES, ch12Left, ch12Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        List<ShowcaseItem> boarmageDrops = List.of(
                new ShowcaseItem(new ItemStack(ModItems.FIRE_STAFF.get()), "Fire Staff", "Flamethrower staff"),
                new ShowcaseItem(new ItemStack(Items.MAGMA_CREAM), "Magma Cream", "Fiery potion reagent"),
                new ShowcaseItem(new ItemStack(ModItems.HEAL_POTION.get()), "Healing Elixir", "Vitality brew")
        );
        BossEntry boarmageBoss = new BossEntry(
                ModEntities.BOARMAGE.get(),
                26.0F,
                "The Boar Mage",
                "Pyromancer of the Wilds",
                "Casts devastating fiery projectiles and shields allied tribes.",
                "220 HP",
                "High",
                boarmageDrops
        );

        PageContent ch13Left = new PageContent(PageContent.PageType.STANDARD, "BOSS: BOARMAGE", "Boss Overview")
                .addLine("§lThe Boar Mage§r")
                .addLine("§8Beast Sorcerer Boss§r")
                .addLine("")
                .addLine("One of the powerful bosses of Chocolate Quest.")
                .addLine("")
                .addLine("• §lHealth§r: 220 HP (110 Hearts)")
                .addLine("• §lType§r: Pyromancer Shaman")
                .addLine("• §lMagic§r: Wild Fire Magic")
                .addLine("• §lElement§r: Flame & Lava");

        PageContent ch13Right = new PageContent(PageContent.PageType.BOSS_SHOWCASE, "SHOWCASE", "Live Boss Preview")
                .setBoss(boarmageBoss);

        chapters.add(new Chapter("Boss: Boarmage", Category.BOSSES, ch13Left, ch13Right));

        // -------------------------------------------------------------
        // -------------------------------------------------------------
        PageContent ch14Left = new PageContent(PageContent.PageType.STANDARD, "DEDICATION", "Inspiration & Legacy")
                .addLine("§lClassic Inspiration§r")
                .addLine("Inspired by the rich adventure and dungeon modding history of classic Minecraft.")
                .addLine("")
                .addLine("§lA Modern Reimagining§r")
                .addLine("This project is an independent modern reconstruction built from the ground up for modern NeoForge.")
                .addLine("")
                .addLine("§8Thank you for joining our adventures.");

        PageContent ch14Right = new PageContent(PageContent.PageType.STANDARD, "AUTHOR'S NOTE", "Development & AI")
                .addLine("§lModern Engineering§r")
                .addLine("Developed with custom modern code, clean assets, and AI assistance to bring the spirit of dungeon crawling to 1.21.1.")
                .addLine("")
                .addLine("§oWith deepest gratitude,§r")
                .addLine("§l~ Daniel§r")
                .addLine("")
                .addLine("§lFeedback & Testing§r")
                .addLine("Found bugs or balance issues?")
                .addLine("")
                .addLine("Please report issues to help us continue polishing the adventure experience for all players!");

        chapters.add(new Chapter("Credits & Dedication", Category.CREDITS, ch14Left, ch14Right));

        return chapters;
    }
}
