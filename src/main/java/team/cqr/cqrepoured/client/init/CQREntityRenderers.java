package team.cqr.cqrepoured.client.init;

import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import team.cqr.cqrepoured.client.model.entity.boss.ModelLich;
import team.cqr.cqrepoured.client.model.entity.boss.ModelNecromancer;
import team.cqr.cqrepoured.client.model.entity.boss.ModelPigMage;
import team.cqr.cqrepoured.client.render.entity.*;
import team.cqr.cqrepoured.client.render.entity.boss.*;
import team.cqr.cqrepoured.client.render.entity.boss.endercalamity.RenderCQREnderCalamity;
import team.cqr.cqrepoured.client.render.entity.boss.endercalamity.RenderCQREnderKing;
import team.cqr.cqrepoured.client.render.entity.boss.endercalamity.RenderEndLaser;
import team.cqr.cqrepoured.client.render.entity.boss.exterminator.RenderCQRExterminator;
import team.cqr.cqrepoured.client.render.entity.boss.exterminator.RenderExterminatorBackpackPart;
import team.cqr.cqrepoured.client.render.entity.boss.spectrelord.RenderCQRSpectreLord;
import team.cqr.cqrepoured.client.render.entity.mobs.*;
import team.cqr.cqrepoured.client.render.entity.mounts.*;
import team.cqr.cqrepoured.client.render.projectile.*;
import team.cqr.cqrepoured.client.render.tileentity.*;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.*;
import team.cqr.cqrepoured.entity.boss.endercalamity.*;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.entity.boss.exterminator.SubEntityExterminatorFieldEmitter;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.entity.boss.netherdragon.SubEntityNetherDragonSegment;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.entity.misc.*;
import team.cqr.cqrepoured.entity.mobs.*;
import team.cqr.cqrepoured.entity.mount.*;
import team.cqr.cqrepoured.entity.projectiles.*;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.tileentity.*;

public class CQREntityRenderers
{
	private CQREntityRenderers() {}

	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_BULLET.get(), RenderProjectileBullet::new);
	/*	registerTileRenderers();
		registerProjectileAndMiscRenderers();
		if (CQRConfig.isAprilFoolsEnabled()) {
			registerAprilFoolsRenderer();
		} else {
			registerEntityRenderers();
		}
		registerBossRenderers();
		registerMountRenderers(); */
	}

	// Registers a big chungus renderer that renders on april the first
	protected static void registerAprilFoolsRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(AbstractEntityCQR.class, RenderChungus::new);
	}

	protected static void registerTileRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporter.class, new TileEntityExporterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForceFieldNexus.class, new TileEntityForceFieldNexusRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporterChest.class, new TileEntityExporterChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMap.class, new TileEntityMapPlaceHolderRenderer());
	}

	protected static void registerProjectileAndMiscRenderers() {
		// Projectiles
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_BULLET.get(), RenderProjectileBullet::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileCannonBall.class, RenderProjectileCannonBall::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileEarthQuake.class, RenderProjectileEarthQuake::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectilePoisonSpell.class, RenderProjectilePoisonSpell::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderBall.class, RenderProjectileSpiderBall::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileVampiricSpell.class, RenderProjectileVampiricSpell::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileFireWallPart.class, RenderProjectileFirewallPart::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileHookShotHook.class, RenderProjectileHookShotHook::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_BUBBLE.get(), RenderProjectileBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileHotFireball.class, RenderProjectileHotFireball::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileWeb.class, RenderProjectileWeb::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderHook.class, RenderProjectileSpiderHook::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileThrownBlock.class, RenderProjectileThrownBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileHomingEnderEye.class, RenderProjectileHomingEnderEye::new);

		// Miscs
		RenderingRegistry.registerEntityRenderingHandler(EntitySummoningCircle.class, RenderSummoningCircle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingSkullMinion.class, RenderFlyingSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBubble.class, RenderBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWalkerKingIllusion.class, RenderCQRWalkerKingIllusion::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWasp.class, RenderCQRWasp::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWalkerTornado.class, RenderWalkerTornado::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirateParrot.class, RenderPirateParrot::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityIceSpike.class, RenderIceSpike::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpiderEgg.class, RenderSpiderEgg::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCalamityCrystal.class, RenderCalamityCrystal::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileEnergyOrb.class, RenderEnergyOrb::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityElectricField.class, RenderElectricFieldEntity::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityColoredLightningBolt.class, RenderColoredLightningBolt::new);

		// Multipart parts
		RenderingRegistry.registerEntityRenderingHandler(PartEntity.class, RenderMultiPartPart::new);
	}

	protected static void registerEntityRenderers() {
		// Mobs
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRBoarman.class, RenderCQRBoarman::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDummy.class, RenderCQRDummy::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDwarf.class, RenderCQRDwarf::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQREnderman.class, RenderCQREnderman::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGolem.class, RenderCQRGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGremlin.class, RenderCQRGremlin::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRHuman.class, RenderCQRHuman::new);
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
	}

	protected static void registerMountRenderers() {
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
		RenderingRegistry.registerEntityRenderingHandler(SubEntityNetherDragonSegment.class, RenderCQRNetherDragonSegment::new);

		// Giant Tortoise
		// RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantTortoise.class, RenderCQRGiantTortoise::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantTortoise.class, RenderCQRGiantTortoiseGecko::new);

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

		// Spectre Lord
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSpectreLord.class, RenderCQRSpectreLord::new);
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordIllusion.class, RenderSpectreLordIllusion::new);
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordCurse.class, RenderSpectreLordCurse::new);
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordExplosion.class, RenderSpectreLordExplosion::new);
		RenderingRegistry.registerEntityRenderingHandler(AbstractEntityLaser.class, RenderLaser<AbstractEntityLaser>::new);

		// Ender King
		RenderingRegistry.registerEntityRenderingHandler(EntityCQREnderKing.class, RenderCQREnderKing::new);

		// Geckolib
		// Ender Calamity
		RenderingRegistry.registerEntityRenderingHandler(EntityCQREnderCalamity.class, RenderCQREnderCalamity::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEndLaserTargeting.class, RenderEndLaser<AbstractEntityLaser>::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEndLaser.class, RenderEndLaser<AbstractEntityLaser>::new);

		// GeckoLib
		// Exterminator
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRExterminator.class, RenderCQRExterminator::new);
		RenderingRegistry.registerEntityRenderingHandler(SubEntityExterminatorFieldEmitter.class, RenderExterminatorBackpackPart::new);
	}

}
