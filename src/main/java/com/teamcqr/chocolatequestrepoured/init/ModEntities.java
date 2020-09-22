package com.teamcqr.chocolatequestrepoured.init;

import static com.teamcqr.chocolatequestrepoured.util.InjectionUtil.Null;

import javax.annotation.Nonnull;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRBoarmage;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantSpider;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRLich;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNecromancer;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateParrot;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityBubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityCQRWasp;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityIceSpike;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySpiderEgg;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerKingIllusion;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerTornado;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRBoarman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGoblin;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGolem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGremlin;
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
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileFireWallPart;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHotFireball;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileWeb;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantEndermite;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishGreen;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishNormal;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishRed;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityPollo;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Reference.MODID)
public class ModEntities {

	private static int entityID = 0;

	public static final EntityEntry SLIME_PART = Null();
	public static final EntityEntry PROJECTILE_BULLET = Null();
	public static final EntityEntry PROJECTILE_CANNON_BALL = Null();
	public static final EntityEntry PROJECTILE_EARTH_QUAKE = Null();
	public static final EntityEntry PROJECTILE_POISON_SPELL = Null();
	public static final EntityEntry PROJECTILE_SPIDER_BALL = Null();
	public static final EntityEntry PROJECTILE_VAMPIRIC_SPELL = Null();
	public static final EntityEntry PROJECTILE_FIREWALL_PART = Null();
	public static final EntityEntry PROJECTILE_BUBBLE = Null();
	public static final EntityEntry PROJECTILE_HOT_FIREBALL = Null();
	public static final EntityEntry PROJECTILE_WEB = Null();

	public static final EntityEntry DUMMY = Null();
	public static final EntityEntry DWARF = Null();
	public static final EntityEntry ENDERMAN = Null();
	public static final EntityEntry GREMLIN = Null();
	public static final EntityEntry GOBLIN = Null();
	public static final EntityEntry GOLEM = Null();
	public static final EntityEntry ILLAGER = Null();
	public static final EntityEntry NPC = Null();
	public static final EntityEntry MINOTAUR = Null();
	public static final EntityEntry MANDRIL = Null();
	public static final EntityEntry MUMMY = Null();
	public static final EntityEntry OGRE = Null();
	public static final EntityEntry ORC = Null();
	public static final EntityEntry BOARMAN = Null();
	public static final EntityEntry PIRATE = Null();
	public static final EntityEntry SKELETON = Null();
	public static final EntityEntry SPECTRE = Null();
	public static final EntityEntry TRITON = Null();
	public static final EntityEntry WALKER = Null();
	public static final EntityEntry ZOMBIE = Null();

	public static final EntityEntry GIANT_ENDERMITE = Null();
	public static final EntityEntry GIANT_SILVERFISH = Null();
	public static final EntityEntry GIANT_SILVERFISH1 = Null();
	public static final EntityEntry GIANT_SILVERFISH2 = Null();
	public static final EntityEntry POLLO = Null();

	public static final EntityEntry NETHER_DRAGON = Null();
	public static final EntityEntry GIANT_TORTOISE = Null();
	public static final EntityEntry LICH = Null();
	public static final EntityEntry BOAR_MAGE = Null();
	public static final EntityEntry NECROMANCER = Null();
	public static final EntityEntry WALKER_KING = Null();
	public static final EntityEntry PIRATE_CAPTAIN = Null();
	public static final EntityEntry GIANT_SPIDER = Null();

	// Misc Entities
	public static final EntityEntry SUMMONING_CIRCLE = Null();
	public static final EntityEntry FLYING_SKULL = Null();
	public static final EntityEntry BUBBLE_ENTITY = Null();
	public static final EntityEntry WASP = Null();
	public static final EntityEntry COLORED_LIGHTNING_BOLT = Null();
	public static final EntityEntry ILLUSION_WALKER_KING = Null();
	public static final EntityEntry WALKER_TORNADO = Null();
	public static final EntityEntry PIRATE_PARROT = Null();
	public static final EntityEntry ICE_SPIKE = Null();

	@EventBusSubscriber(modid = Reference.MODID)
	public static class EntityRegistrationHandler {

		@SubscribeEvent
		public static void registerTileEntities(RegistryEvent.Register<EntityEntry> event) {
			final EntityEntry[] entityEntries = {
					createEntityEntryWithoutEgg(EntitySlimePart.class, "slime_part", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileBullet.class, "projectile_bullet", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileCannonBall.class, "projectile_cannon_ball", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileEarthQuake.class, "projectile_earth_quake", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectilePoisonSpell.class, "projectile_poison_spell", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileSpiderBall.class, "projectile_spider_ball", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileVampiricSpell.class, "projectile_vampiric_spell", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileFireWallPart.class, "projectile_firewall_part", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileHookShotHook.class, "projectile_hookshot_hook", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileBubble.class, "projectile_bubble", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileHotFireball.class, "projectile_hot_fireball", 64, 1, true),
					createEntityEntryWithoutEgg(ProjectileWeb.class, "projectile_web", 64, 1, true),

					createEntityEntry(EntityCQRDummy.class, "dummy", 64, 1, true, 0xC29D62, 0x67502C),
					createEntityEntry(EntityCQRDwarf.class, "dwarf", 64, 1, true, 0x333333, 0x582800),
					createEntityEntry(EntityCQREnderman.class, "enderman", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRGremlin.class, "gremlin", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRGoblin.class, "goblin", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRGolem.class, "golem", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRIllager.class, "illager", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRNPC.class, "npc", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRMinotaur.class, "minotaur", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRMandril.class, "mandril", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRMummy.class, "mummy", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQROgre.class, "ogre", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQROrc.class, "orc", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRBoarman.class, "boarman", 64, 1, true, 0x333333, 0xEA9393),
					createEntityEntry(EntityCQRPirate.class, "pirate", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRSkeleton.class, "skeleton", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRSpectre.class, "spectre", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRTriton.class, "triton", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRWalker.class, "walker", 64, 1, true, 0x333333, 0xC3BDBD),
					createEntityEntry(EntityCQRZombie.class, "zombie", 64, 1, true, 0x333333, 0xC3BDBD),

					// Mounts
					createEntityEntry(EntityGiantEndermite.class, "giant_endermite", 64, 1, true, 0xC29D62, 0xEA9393),
					createEntityEntry(EntityGiantSilverfishNormal.class, "giant_silverfish", 64, 1, true, 0xC29D62, 0xEA9393),
					createEntityEntry(EntityGiantSilverfishRed.class, "giant_silverfish1", 64, 1, true, 0xC29D62, 0xEA9393),
					createEntityEntry(EntityGiantSilverfishGreen.class, "giant_silverfish2", 64, 1, true, 0xC29D62, 0xEA9393),
					createEntityEntry(EntityPollo.class, "pollo", 64, 1, true, 0xC29D62, 0xEA9393),

					// Bosses
					createEntityEntry(EntityCQRNetherDragon.class, "nether_dragon", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRGiantTortoise.class, "giant_tortoise", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRLich.class, "lich", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRBoarmage.class, "boar_mage", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRNecromancer.class, "necromancer", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRWalkerKing.class, "walker_king", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRPirateCaptain.class, "pirate_captain", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRGiantSpider.class, "giant_spider", 64, 1, true, 3289650, 000000),

					// Misc Entities
					createEntityEntryWithoutEgg(EntitySummoningCircle.class, "summoning_circle", 64, 1, true),
					createEntityEntryWithoutEgg(EntityFlyingSkullMinion.class, "flying_skull", 64, 1, true),
					createEntityEntryWithoutEgg(EntityBubble.class, "bubble_entity", 64, 1, true),
					createEntityEntryWithoutEgg(EntityWalkerKingIllusion.class, "illusion_walker_king", 64, 1, true),
					createEntityEntry(EntityCQRWasp.class, "wasp", 64, 1, true, 0xC29D62, 0xEA9393),
					createEntityEntryWithoutEgg(EntityColoredLightningBolt.class, "colored_lightning_bolt", 512, 1, true),
					createEntityEntryWithoutEgg(EntityWalkerTornado.class, "walker_tornado", 64, 1, true),
					createEntityEntryWithoutEgg(EntityCQRPirateParrot.class, "pirate_parrot", 64, 1, true),
					createEntityEntryWithoutEgg(EntityIceSpike.class, "ice_spike", 64, 1, true),
					createEntityEntryWithoutEgg(EntitySpiderEgg.class, "spider_egg", 64, 1, true), };

			event.getRegistry().registerAll(entityEntries);

			// Spawns
			EntityRegistry.addSpawn(EntityCQRWasp.class, 24, 3, 9, EnumCreatureType.CREATURE, Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND, Biomes.JUNGLE, Biomes.MUTATED_JUNGLE);
		}

		private static EntityEntry createEntityEntry(@Nonnull Class<? extends Entity> entityClass, String name, int trackerRange, int trackerUpdateFrequency, boolean sendVelocityUpdates, int eggColor1, int eggColor2) {
			return EntityEntryBuilder.create().entity(entityClass).id(new ResourceLocation(Reference.MODID, name), entityID++).name("cqr_" + name).egg(eggColor1, eggColor2).tracker(trackerRange, trackerUpdateFrequency, sendVelocityUpdates).build();
		}

		private static EntityEntry createEntityEntryWithoutEgg(@Nonnull Class<? extends Entity> entityClass, String name, int trackerRange, int trackerUpdateFrequency, boolean sendVelocityUpdates) {
			return EntityEntryBuilder.create().entity(entityClass).id(new ResourceLocation(Reference.MODID, name), entityID++).name("cqr_" + name).tracker(trackerRange, trackerUpdateFrequency, sendVelocityUpdates).build();
		}

	}

}
