package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelLich;
import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNecromancer;
import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelPigMage;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderBubble;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRWasp;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderFlyingSkull;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderIceSpike;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderPirateParrot;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderSummoningCircle;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderWalkerTornado;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRGiantSpider;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRMage;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNecromancer;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRPirateCaptain;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRBoarman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRDummy;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRDwarf;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQREnderman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRGoblin;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRGolem;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRGremlin;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRIllager;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRMandril;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRMummy;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRNPC;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQROgre;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQROrc;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRPirate;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRSpectre;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRTriton;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRWalker;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mobs.RenderCQRZombie;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantEndermite;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfish;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfishGreen;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfishRed;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderPollo;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileBubble;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileBullet;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileFirewallPart;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileHotFireball;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileWeb;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityExporterChestRenderer;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityExporterRenderer;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityForceFieldNexusRenderer;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityTableRenderer;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRBoarmage;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantSpider;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRLich;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNecromancer;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateParrot;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityBubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityCQRWasp;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityIceSpike;
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
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporterChest;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityTable;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModEntityRenderers {

	private ModEntityRenderers() {

	}

	public static void registerRenderers() {
		registerTileRenderers();
		registerProjectileAndMiscRenderers();
		registerEntityRenderers();
		registerBossRenderers();
	}

	protected static void registerTileRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporter.class, new TileEntityExporterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForceFieldNexus.class, new TileEntityForceFieldNexusRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporterChest.class, new TileEntityExporterChestRenderer());
	}

	protected static void registerProjectileAndMiscRenderers() {
		// Projectiles
		RenderingRegistry.registerEntityRenderingHandler(ProjectileBullet.class, RenderProjectileBullet::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileCannonBall.class, RenderProjectileCannonBall::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileEarthQuake.class, RenderProjectileEarthQuake::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectilePoisonSpell.class, RenderProjectilePoisonSpell::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderBall.class, RenderProjectileSpiderBall::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileVampiricSpell.class, RenderProjectileVampiricSpell::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileFireWallPart.class, RenderProjectileFirewallPart::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileHookShotHook.class, RenderProjectileHookShotHook::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileBubble.class, RenderProjectileBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileHotFireball.class, RenderProjectileHotFireball::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileWeb.class, RenderProjectileWeb::new);

		// Miscs
		RenderingRegistry.registerEntityRenderingHandler(EntitySummoningCircle.class, RenderSummoningCircle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingSkullMinion.class, RenderFlyingSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBubble.class, RenderBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWalkerKingIllusion.class, renderManager -> new RenderCQREntity<>(renderManager, "boss/walker_king", true));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWasp.class, RenderCQRWasp::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWalkerTornado.class, RenderWalkerTornado::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirateParrot.class, RenderPirateParrot::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityIceSpike.class, RenderIceSpike::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityColoredLightningBolt.class, RenderColoredLightningBolt::new);
	}

	protected static void registerEntityRenderers() {
		// Mobs
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRBoarman.class, RenderCQRBoarman::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDummy.class, RenderCQRDummy::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDwarf.class, RenderCQRDwarf::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQREnderman.class, RenderCQREnderman::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGolem.class, RenderCQRGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGremlin.class, RenderCQRGremlin::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRIllager.class, RenderCQRIllager::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMandril.class, RenderCQRMandril::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMinotaur.class, RenderCQRMinotaur::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMummy.class, RenderCQRMummy::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNPC.class, RenderCQRNPC::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROgre.class, RenderCQROgre::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROrc.class, RenderCQROrc::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirate.class, RenderCQRPirate::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSkeleton.class, RenderCQRSkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSpectre.class, RenderCQRSpectre::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRTriton.class, RenderCQRTriton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWalker.class, RenderCQRWalker::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRZombie.class, RenderCQRZombie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGoblin.class, RenderCQRGoblin::new);

		// Mounts
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantEndermite.class, RenderGiantEndermite::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishNormal.class, RenderGiantSilverfish::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishGreen.class, RenderGiantSilverfishGreen::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishRed.class, RenderGiantSilverfishRed::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPollo.class, RenderPollo::new);
	}

	protected static void registerBossRenderers() {
		// Nether Dragon
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragon.class, RenderCQRNetherDragon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragonSegment.class, RenderCQRNetherDragonSegment::new);

		// Giant Tortoise
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantTortoise.class, RenderCQRGiantTortoise::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantTortoisePart.class, RenderCQRGiantTortoisePart::new);

		// Lich
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRLich.class, renderManager -> new RenderCQRMage<>(renderManager, new ModelLich(), "boss/lich"));

		// Boar Mage
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRBoarmage.class, renderManager -> new RenderCQRMage<>(renderManager, new ModelPigMage(), "boss/pig_mage"));

		// Necromancer
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNecromancer.class, renderManager -> new RenderCQRNecromancer(renderManager, new ModelNecromancer(), "boss/necromancer"));

		// Walker King
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWalkerKing.class, RenderCQRWalkerKing::new);

		// Pirate Captain
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirateCaptain.class, RenderCQRPirateCaptain::new);

		// Shelob
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantSpider.class, RenderCQRGiantSpider::new);
	}

}
