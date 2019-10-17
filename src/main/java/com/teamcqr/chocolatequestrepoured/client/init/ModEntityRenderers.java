package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonHead;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRBoarman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREnderman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRGoblin;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRGolem;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRIllager;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRMandril;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQROgre;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQROrc;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRTriton;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantEndermite;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfish;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfishGreen;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfishRed;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileBullet;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityExporterRenderer;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityTableRenderer;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGoblin;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGolem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMandril;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRNPC;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROgre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROrc;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRBoarman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPirate;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSpectre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRWalker;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRZombie;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantEndermite;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishGreen;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishNormal;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishRed;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityTable;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModEntityRenderers {

	public static void registerRenderers() {
		// TILE ENTITY RENDERERS
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporter.class, new TileEntityExporterRenderer());

		// ENTITY RENDERERS
		//Projectiles
		RenderingRegistry.registerEntityRenderingHandler(ProjectileBullet.class,
				renderManager -> new RenderProjectileBullet(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileCannonBall.class,
				renderManager -> new RenderProjectileCannonBall(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileEarthQuake.class,
				renderManager -> new RenderProjectileEarthQuake(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectilePoisonSpell.class,
				renderManager -> new RenderProjectilePoisonSpell(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderBall.class,
				renderManager -> new RenderProjectileSpiderBall(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileVampiricSpell.class,
				renderManager -> new RenderProjectileVampiricSpell(renderManager));
		
		//Dummy
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDummy.class,
				renderManager -> new RenderCQREntity<EntityCQRDummy>(renderManager, "entity_mob_cqrdummy"));
		//Dwarf
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDwarf.class,
				renderManager -> new RenderCQREntity<EntityCQRDwarf>(renderManager, "entity_mob_cqrdwarf", 0.9D, 0.65D));
		//Enderman
		RenderingRegistry.registerEntityRenderingHandler(EntityCQREnderman.class,
				renderManager -> new RenderCQREnderman(renderManager));
		//Goblin
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGoblin.class,
				renderManager -> new RenderCQRGoblin(renderManager));
		//Golem
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGolem.class,
				renderManager -> new RenderCQRGolem(renderManager));
		//Illager
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRIllager.class,
				renderManager -> new RenderCQRIllager(renderManager));
		//Inquisiton
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNPC.class,
				renderManager -> new RenderCQREntity<EntityCQRNPC>(renderManager, "entity_mob_cqrinquisitor"));
		//Minotaur
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMinotaur.class,
				renderManager -> new RenderCQRMinotaur(renderManager));
		//Mandril
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMandril.class,
				renderManager -> new RenderCQRMandril(renderManager));
		//Mummy
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMummy.class,
				renderManager -> new RenderCQREntity<EntityCQRMummy>(renderManager, "entity_mob_cqrmummy"));
		//Ogre
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROgre.class,
				renderManager -> new RenderCQROgre(renderManager));
		//Orc
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROrc.class,
				renderManager -> new RenderCQROrc(renderManager));
		//Boarman
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRBoarman.class,
				renderManager -> new RenderCQRBoarman(renderManager, "entity_mob_cqrboarman_zombie"));
		//Pirate
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirate.class,
				renderManager -> new RenderCQREntity<EntityCQRPirate>(renderManager, "entity_mob_cqrpirate"));
		//Skeleton
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSkeleton.class,	
				renderManager -> new RenderCQRSkeleton(renderManager));
		//Spectre
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSpectre.class,
				renderManager -> new RenderCQREntity<EntityCQRSpectre>(renderManager, "entity_mob_cqrspectre"));
		//Triton
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRTriton.class,
				renderManager -> new RenderCQRTriton(renderManager));
		//Walker
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWalker.class,
				renderManager -> new RenderCQREntity<EntityCQRWalker>(renderManager, "entity_mob_cqrwalker"));
		//Zombie
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRZombie.class,
				renderManager -> new RenderCQREntity<EntityCQRZombie>(renderManager, "entity_mob_cqrzombie"));
		
		//Mounts
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantEndermite.class,
				renderManager -> new RenderGiantEndermite(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishNormal.class,
				renderManager -> new RenderGiantSilverfish(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishGreen.class,
				renderManager -> new RenderGiantSilverfishGreen(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishRed.class,
				renderManager -> new RenderGiantSilverfishRed(renderManager));
		
		//Bosses
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragon.class,

				renderManager -> new RenderCQRNetherDragon(renderManager, new ModelNetherDragonHead()));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragonSegment.class,
				renderManager -> new RenderCQRNetherDragonSegment(renderManager/*, new ModelNetherDragonBodyPart()*/));
	}

}
