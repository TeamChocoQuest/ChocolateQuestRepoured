package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonHead;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRDwarf;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRPigman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragonSegment;
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
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPigman;
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
				renderManager -> new RenderCQRDwarf(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPigman.class,
				renderManager -> new RenderCQRPigman(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRZombie.class,
				renderManager -> new RenderCQREntity<EntityCQRZombie>(renderManager, "entity_mob_cqrzombie"));

		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragon.class,
				renderManager -> new RenderCQRNetherDragon(renderManager, new ModelNetherDragonHead()));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragonSegment.class,
				renderManager -> new RenderCQRNetherDragonSegment(renderManager/*, new ModelNetherDragonBodyPart()*/));
	}

}
