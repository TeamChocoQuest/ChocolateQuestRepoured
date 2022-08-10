package team.cqr.cqrepoured.client.init;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import team.cqr.cqrepoured.client.render.entity.RenderBubble;
import team.cqr.cqrepoured.client.render.entity.RenderColoredLightningBolt;
import team.cqr.cqrepoured.client.render.entity.RenderElectricFieldEntity;
import team.cqr.cqrepoured.client.render.entity.RenderLaser;
import team.cqr.cqrepoured.client.render.entity.RenderSummoningCircle;
import team.cqr.cqrepoured.client.render.entity.RenderWalkerTornado;
import team.cqr.cqrepoured.client.render.entity.boss.RenderCQRBoarmage;
import team.cqr.cqrepoured.client.render.entity.boss.RenderCQRGiantTortoiseGecko;
import team.cqr.cqrepoured.client.render.entity.boss.RenderCQRLich;
import team.cqr.cqrepoured.client.render.entity.boss.RenderCQRNecromancer;
import team.cqr.cqrepoured.client.render.entity.boss.RenderCQRPirateCaptain;
import team.cqr.cqrepoured.client.render.entity.boss.RenderCQRWalkerKing;
import team.cqr.cqrepoured.client.render.entity.boss.RenderCQRWalkerKingIllusion;
import team.cqr.cqrepoured.client.render.entity.boss.endercalamity.RenderCQREnderCalamity;
import team.cqr.cqrepoured.client.render.entity.boss.endercalamity.RenderCQREnderKing;
import team.cqr.cqrepoured.client.render.entity.boss.endercalamity.RenderEndLaser;
import team.cqr.cqrepoured.client.render.entity.boss.exterminator.RenderCQRExterminator;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQREnderman;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRGremlin;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRIllager;
import team.cqr.cqrepoured.client.render.entity.mounts.RenderGiantEndermite;
import team.cqr.cqrepoured.client.render.entity.mounts.RenderGiantSilverfish;
import team.cqr.cqrepoured.client.render.entity.mounts.RenderGiantSilverfishGreen;
import team.cqr.cqrepoured.client.render.entity.mounts.RenderGiantSilverfishRed;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileBubble;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileBullet;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileCannonBall;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileEarthQuake;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileFirewallPart;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileHomingEnderEye;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileHotFireball;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectilePoisonSpell;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileSpiderBall;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileThrownBlock;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileVampiricSpell;
import team.cqr.cqrepoured.client.render.projectile.RenderProjectileWeb;
import team.cqr.cqrepoured.client.render.tileentity.TileEntityTableRenderer;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaserTargeting;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityExterminatorHandLaser;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class CQREntityRenderers
{
	private CQREntityRenderers() {}

	public static void registerRenderers() {
		registerProjectileAndMiscRenderers();
		registerTileRenderers();
		/*
		if (CQRConfig.isAprilFoolsEnabled()) {
			registerAprilFoolsRenderer();
		} else {*/
			registerEntityRenderers();
		/*}*/
		registerBossRenderers();
		registerMountRenderers(); 
	}

	// Registers a big chungus renderer that renders on april the first
	protected static void registerAprilFoolsRenderer() {
		//RenderingRegistry.registerEntityRenderingHandler(AbstractEntityCQR.class, RenderChungus::new);
	}

	public static void registerTileRenderers() {
		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.TABLE.get(), TileEntityTableRenderer::new);
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporter.class, new TileEntityExporterRenderer());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForceFieldNexus.class, new TileEntityForceFieldNexusRenderer());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporterChest.class, new TileEntityExporterChestRenderer());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMap.class, new TileEntityMapPlaceHolderRenderer());
	}

	public static void registerProjectileAndMiscRenderers() {
		// Projectiles
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_BULLET.get(), RenderProjectileBullet::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_CANNON_BALL.get(), RenderProjectileCannonBall::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_EARTH_QUAKE.get(), RenderProjectileEarthQuake::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_POISON_SPELL.get(), RenderProjectilePoisonSpell::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_SPIDER_BALL.get(), RenderProjectileSpiderBall::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_VAMPIRIC_SPELL.get(), RenderProjectileVampiricSpell::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_FIRE_WALL_PART.get(), RenderProjectileFirewallPart::new);
		//RenderingRegistry.registerEntityRenderingHandler(ProjectileHookShotHook.class, RenderProjectileHookShotHook::new);*/
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_BUBBLE.get(), RenderProjectileBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_HOT_FIREBALL.get(), RenderProjectileHotFireball::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_WEB.get(), RenderProjectileWeb::new);
		//RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderHook.class, RenderProjectileSpiderHook::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_THROWN_BLOCK.get(), RenderProjectileThrownBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_HOMING_ENDER_EYE.get(), RenderProjectileHomingEnderEye::new);

		// Miscs
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.BUBBLE.get(), RenderBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.SUMMONING_CIRCLE.get(), RenderSummoningCircle::new);
		/*RenderingRegistry.registerEntityRenderingHandler(EntityFlyingSkullMinion.class, RenderFlyingSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBubble.class, RenderBubble::new);*/
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.WALKER_KING_ILLUSION.get(), RenderCQRWalkerKingIllusion::new);
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRWasp.class, RenderCQRWasp::new);*/
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.WALKER_TORNADO.get(), RenderWalkerTornado::new);
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirateParrot.class, RenderPirateParrot::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityIceSpike.class, RenderIceSpike::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpiderEgg.class, RenderSpiderEgg::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCalamityCrystal.class, RenderCalamityCrystal::new);
		RenderingRegistry.registerEntityRenderingHandler(ProjectileEnergyOrb.class, RenderEnergyOrb::new);*/
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ELECTRIC_FIELD.get(), RenderElectricFieldEntity::new);

		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.COLORED_LIGHTNING.get(), RenderColoredLightningBolt::new);

		// Multipart parts
		/*RenderingRegistry.registerEntityRenderingHandler(PartEntity.class, RenderMultiPartPart::new);*/
	}

	protected static void registerEntityRenderers() {
		// Mobs
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRBoarman.class, RenderCQRBoarman::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDummy.class, RenderCQRDummy::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDwarf.class, RenderCQRDwarf::new);*/
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ENDERMAN.get(), RenderCQREnderman::new);
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRGolem.class, RenderCQRGolem::new);*/
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GREMLIN.get(), RenderCQRGremlin::new);
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRHuman.class, RenderCQRHuman::new);*/
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ILLAGER.get(), RenderCQRIllager::new);
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRMandril.class, RenderCQRMandril::new);
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
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGoblin.class, RenderCQRGoblin::new);*/
	}

	protected static void registerMountRenderers() {
		// Mounts
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_ENDERMITE.get(), RenderGiantEndermite::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_SILVERFISH.get(), RenderGiantSilverfish::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_SILVERFISH_GREEN.get(), RenderGiantSilverfishGreen::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_SILVERFISH_RED.get(), RenderGiantSilverfishRed::new);
		//RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.POLLO.get(), RenderPollo::new);
	}

	protected static void registerBossRenderers() {
		// Nether Dragon
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragon.class, RenderCQRNetherDragon::new);
		RenderingRegistry.registerEntityRenderingHandler(SubEntityNetherDragonSegment.class, RenderCQRNetherDragonSegment::new);*/

		// Giant Tortoise
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_TORTOISE.get(), RenderCQRGiantTortoiseGecko::new);

		// Shelob
		/*RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantSpider.class, RenderCQRGiantSpider::new);

		// Spectre Lord
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSpectreLord.class, RenderCQRSpectreLord::new);
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordIllusion.class, RenderSpectreLordIllusion::new);
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordCurse.class, RenderSpectreLordCurse::new);
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordExplosion.class, RenderSpectreLordExplosion::new);
		RenderingRegistry.registerEntityRenderingHandler(AbstractEntityLaser.class, RenderLaser<AbstractEntityLaser>::new);*/

		// Ender King
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ENDER_KING.get(), RenderCQREnderKing::new);

		// Geckolib
		// Ender Calamity
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ENDER_CALAMITY.get(), RenderCQREnderCalamity::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.END_LASER_TARGETING.get(), RenderEndLaser<EntityEndLaserTargeting>::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.END_LASER.get(), RenderEndLaser<EntityEndLaser>::new);
		
		// GeckoLib
		// Exterminator
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.EXTERMINATOR.get(), RenderCQRExterminator::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.LASER_EXTERMINATOR.get(), RenderLaser<EntityExterminatorHandLaser>::new);
		
		// Mages
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.LICH.get(), RenderCQRLich::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.NECROMANCER.get(), RenderCQRNecromancer::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.BOARMAGE.get(), RenderCQRBoarmage::new);
		// Walker King
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.WALKER_KING.get(), RenderCQRWalkerKing::new);

		// Pirate Captain
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PIRATE_CAPTAIN.get(), RenderCQRPirateCaptain::new);
	}

}
