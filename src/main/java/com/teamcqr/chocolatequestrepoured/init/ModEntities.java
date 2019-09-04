package com.teamcqr.chocolatequestrepoured.init;

import static com.teamcqr.chocolatequestrepoured.util.InjectionUtil.Null;

import javax.annotation.Nonnull;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPigman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRZombie;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
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
	
	public static final EntityEntry DWARF = Null();
	public static final EntityEntry PIGMEN = Null();
	public static final EntityEntry ZOMBIE = Null();
	
	public static final EntityEntry NETHER_DRAGON = Null();
	
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
					
					createEntityEntry(EntityCQRDwarf.class, "dwarf", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRPigman.class, "pigmen", 64, 1, true, 3289650, 000000),
					createEntityEntry(EntityCQRZombie.class, "zombie", 64, 1, true, 3289650, 000000),
					
					createEntityEntry(EntityCQRNetherDragon.class, "nether_dragon", 64, 1, true, 3289650, 000000)
			};
			
			event.getRegistry().registerAll(entityEntries);
		}

		private static EntityEntry createEntityEntry(@Nonnull Class<? extends Entity> entityClass, String name, int trackerRange,
				int trackerUpdateFrequency, boolean sendVelocityUpdates, int eggColor1, int eggColor2) {
			return EntityEntryBuilder.create().entity(entityClass)
					.id(new ResourceLocation(Reference.MODID, name), entityID++).name(name).egg(eggColor1, eggColor2)
					.tracker(64, 1, false).build();
		}

		private static EntityEntry createEntityEntryWithoutEgg(@Nonnull Class<? extends Entity> entityClass, String name, int trackerRange,
				int trackerUpdateFrequency, boolean sendVelocityUpdates) {
			return EntityEntryBuilder.create().entity(entityClass)
					.id(new ResourceLocation(Reference.MODID, name), entityID++).name(name).tracker(64, 1, false)
					.build();
		}

	}

}
