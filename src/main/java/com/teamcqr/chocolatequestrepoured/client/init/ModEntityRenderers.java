package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonBodyPart;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRNetherDragonSegment;
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
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRHuman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRInquisitor;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROgre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROrc;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPigman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPirate;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSpectre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTestificate;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRWalker;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRZombie;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
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

		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDummy.class,
				renderManager -> new RenderCQREntity<EntityCQRDummy>(renderManager, "entity_mob_cqrdummy"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDwarf.class,
				renderManager -> new RenderCQREntity<EntityCQRDwarf>(renderManager, "entity_mob_cqrdwarf", 0.9D, 0.65D));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQREnderman.class,
				renderManager -> new RenderCQREntity<EntityCQREnderman>(renderManager, "entity_mob_cqrenderman"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGoblin.class,
				renderManager -> new RenderCQREntity<EntityCQRGoblin>(renderManager, "entity_mob_cqrgoblin"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGolem.class,
				renderManager -> new RenderCQREntity<EntityCQRGolem>(renderManager, "entity_mob_cqrgolem"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRHuman.class,
				renderManager -> new RenderCQREntity<EntityCQRHuman>(renderManager, "entity_mob_cqrhuman"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRIllager.class,
				renderManager -> new RenderCQREntity<EntityCQRIllager>(renderManager, "entity_mob_cqrillager"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRInquisitor.class,
				renderManager -> new RenderCQREntity<EntityCQRInquisitor>(renderManager, "entity_mob_cqrinquisitor"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMinotaur.class,
				renderManager -> new RenderCQREntity<EntityCQRMinotaur>(renderManager, "entity_mob_cqrminotaur"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMummy.class,
				renderManager -> new RenderCQREntity<EntityCQRMummy>(renderManager, "entity_mob_cqrmummy"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROgre.class,
				renderManager -> new RenderCQREntity<EntityCQROgre>(renderManager, "entity_mob_cqrogre"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROrc.class,
				renderManager -> new RenderCQREntity<EntityCQROrc>(renderManager, "entity_mob_cqrorc"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPigman.class,
				renderManager -> new RenderCQREntity<EntityCQRPigman>(renderManager, "entity_mob_cqrpigman", 1.2F, 1.1F));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirate.class,
				renderManager -> new RenderCQREntity<EntityCQRPirate>(renderManager, "entity_mob_cqrpirate"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSkeleton.class,
				renderManager -> new RenderCQREntity<EntityCQRSkeleton>(renderManager, "entity_mob_cqrskeleton"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSpectre.class,
				renderManager -> new RenderCQREntity<EntityCQRSpectre>(renderManager, "entity_mob_cqrspectre"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRTestificate.class,
				renderManager -> new RenderCQREntity<EntityCQRTestificate>(renderManager, "entity_mob_cqrtestificate"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRTriton.class,
				renderManager -> new RenderCQREntity<EntityCQRTriton>(renderManager, "entity_mob_cqrtriton"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWalker.class,
				renderManager -> new RenderCQREntity<EntityCQRWalker>(renderManager, "entity_mob_cqrwalker"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRZombie.class,
				renderManager -> new RenderCQREntity<EntityCQRZombie>(renderManager, "entity_mob_cqrzombie"));

		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragon.class,
				renderManager -> new RenderCQRNetherDragon(renderManager));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragonSegment.class,
				renderManager -> new RenderCQRNetherDragonSegment(renderManager, new ModelNetherDragonBodyPart()));
	}

}
