package team.cqr.cqrepoured.client.init;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import team.cqr.cqrepoured.client.render.entity.RenderBubble;
import team.cqr.cqrepoured.client.render.entity.RenderCQRFlyingSkull;
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
import team.cqr.cqrepoured.client.render.entity.boss.exterminator.RenderExterminatorBackpackPart;
import team.cqr.cqrepoured.client.render.entity.boss.netherdragon.RenderNetherDragonBodyPart;
import team.cqr.cqrepoured.client.render.entity.boss.netherdragon.RenderNetherDragonHead;
import team.cqr.cqrepoured.client.render.entity.boss.spectrelord.RenderCQRSpectreLord;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRBoarman;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRDummy;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRDwarf;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQREnderman;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRGoblin;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRGolem;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRGremlin;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRHuman;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRIllager;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRMandril;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRMinotaur;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRMummy;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRNPC;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQROgre;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQROrc;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRPirate;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRSkeleton;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRSpectre;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRTriton;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRWalker;
import team.cqr.cqrepoured.client.render.entity.mobs.RenderCQRZombie;
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
import team.cqr.cqrepoured.client.render.tileentity.TileEntityExporterRenderer;
import team.cqr.cqrepoured.client.render.tileentity.TileEntityTableRenderer;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaserTargeting;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityExterminatorHandLaser;
import team.cqr.cqrepoured.entity.boss.exterminator.SubEntityExterminatorFieldEmitter;
import team.cqr.cqrepoured.entity.boss.netherdragon.SubEntityNetherDragonSegment;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

public class CQREntityRenderers {
	
	protected static final Map<Class<? extends CQRPartEntity<?>>, Function<EntityRendererManager, ? extends EntityRenderer<? extends CQRPartEntity<?>>>> ENTITY_PART_RENDERER_PRODUCERS = new ConcurrentHashMap<>();
	protected static final Map<Class<? extends CQRPartEntity<?>>, EntityRenderer<? extends CQRPartEntity<?>>> ENTITY_PART_RENDERERS = new ConcurrentHashMap<>();

	protected static void registerEntityPartRenderer(final Class<? extends CQRPartEntity<?>> partClass, Function<EntityRendererManager, ? extends EntityRenderer<? extends CQRPartEntity<?>>> rendererFactory) {
		ENTITY_PART_RENDERER_PRODUCERS.put(partClass, rendererFactory);
	}
	
	public static <R extends EntityRenderer<? extends CQRPartEntity<?>>, P extends CQRPartEntity<?>> EntityRenderer<? extends CQRPartEntity<?>> getRendererFor(P partEntity, EntityRendererManager renderManager) {
		return ENTITY_PART_RENDERERS.computeIfAbsent((Class<? extends CQRPartEntity<?>>) partEntity.getClass(), partClass -> {
			Function<EntityRendererManager, ? extends EntityRenderer<? extends CQRPartEntity<?>>> constructor = null;
			for(Entry<Class<? extends CQRPartEntity<?>>, Function<EntityRendererManager, ? extends EntityRenderer<? extends CQRPartEntity<?>>>> entry : ENTITY_PART_RENDERER_PRODUCERS.entrySet()) {
				if(entry.getKey().equals(partClass)) {
					constructor = entry.getValue();
					break;
				} else if(partClass.isAssignableFrom(entry.getKey())) {
					constructor = entry.getValue();
				}
			}
			if(constructor != null) {
				return constructor.apply(renderManager);
			}
			return null;
		});
	}
	
	private CQREntityRenderers() {
	}

	public static void registerRenderers() {
		registerProjectileAndMiscRenderers();
		registerTileRenderers();
		/*
		 * if (CQRConfig.isAprilFoolsEnabled()) { registerAprilFoolsRenderer(); } else {
		 */
		registerEntityRenderers();
		/* } */
		registerBossRenderers();
		registerMountRenderers();
		
		registerPartRenderers();
	}

	public static void registerPartRenderers() {
		registerEntityPartRenderer(SubEntityNetherDragonSegment.class, RenderNetherDragonBodyPart::new);
		registerEntityPartRenderer(SubEntityExterminatorFieldEmitter.class, RenderExterminatorBackpackPart::new);
	}

	// Registers a big chungus renderer that renders on april the first
	protected static void registerAprilFoolsRenderer() {
		// RenderingRegistry.registerEntityRenderingHandler(AbstractEntityCQR.class, RenderChungus::new);
	}

	public static void registerTileRenderers() {
		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.TABLE.get(), TileEntityTableRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.EXPORTER.get(), TileEntityExporterRenderer::new);
		// ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForceFieldNexus.class, new TileEntityForceFieldNexusRenderer());
		// ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporterChest.class, new TileEntityExporterChestRenderer());
		// ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMap.class, new TileEntityMapPlaceHolderRenderer());
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
//		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_HOOKSHOT_HOOK.get(), RenderProjectileHookShotHook::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_BUBBLE.get(), RenderProjectileBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_HOT_FIREBALL.get(), RenderProjectileHotFireball::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_WEB.get(), RenderProjectileWeb::new);
		// RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderHook.class, RenderProjectileSpiderHook::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_THROWN_BLOCK.get(), RenderProjectileThrownBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PROJECTILE_HOMING_ENDER_EYE.get(), RenderProjectileHomingEnderEye::new);

		// Miscs
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.BUBBLE.get(), RenderBubble::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.SUMMONING_CIRCLE.get(), RenderSummoningCircle::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.FLYING_SKULL.get(), RenderCQRFlyingSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.WALKER_KING_ILLUSION.get(), RenderCQRWalkerKingIllusion::new);
		/* RenderingRegistry.registerEntityRenderingHandler(EntityCQRWasp.class, RenderCQRWasp::new); */
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.WALKER_TORNADO.get(), RenderWalkerTornado::new);
		/*
		 * RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirateParrot.class, RenderPirateParrot::new); RenderingRegistry.registerEntityRenderingHandler(EntityIceSpike.class, RenderIceSpike::new);
		 * RenderingRegistry.registerEntityRenderingHandler(EntitySpiderEgg.class, RenderSpiderEgg::new); RenderingRegistry.registerEntityRenderingHandler(EntityCalamityCrystal.class, RenderCalamityCrystal::new);
		 * RenderingRegistry.registerEntityRenderingHandler(ProjectileEnergyOrb.class, RenderEnergyOrb::new);
		 */
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ELECTRIC_FIELD.get(), RenderElectricFieldEntity::new);

		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.COLORED_LIGHTNING.get(), RenderColoredLightningBolt::new);

		// Multipart parts
		/* RenderingRegistry.registerEntityRenderingHandler(PartEntity.class, RenderMultiPartPart::new); */
	}

	protected static void registerEntityRenderers() {
		// Mobs
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.BOARMAN.get(), RenderCQRBoarman::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.DUMMY.get(), RenderCQRDummy::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.DWARF.get(), RenderCQRDwarf::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ENDERMAN.get(), RenderCQREnderman::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GOLEM.get(), RenderCQRGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GREMLIN.get(), RenderCQRGremlin::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.HUMAN.get(), RenderCQRHuman::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ILLAGER.get(), RenderCQRIllager::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.MANDRIL.get(), RenderCQRMandril::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.MINOTAUR.get(), RenderCQRMinotaur::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.MUMMY.get(), RenderCQRMummy::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.NPC.get(), RenderCQRNPC::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.OGRE.get(), RenderCQROgre::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ORC.get(), RenderCQROrc::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.PIRATE.get(), RenderCQRPirate::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.SKELETON.get(), RenderCQRSkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.SPECTRE.get(), RenderCQRSpectre::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.TRITON.get(), RenderCQRTriton::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.WALKER.get(), RenderCQRWalker::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ZOMBIE.get(), RenderCQRZombie::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GOBLIN.get(), RenderCQRGoblin::new);
	}

	protected static void registerMountRenderers() {
		// Mounts
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_ENDERMITE.get(), RenderGiantEndermite::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_SILVERFISH.get(), RenderGiantSilverfish::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_SILVERFISH_GREEN.get(), RenderGiantSilverfishGreen::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_SILVERFISH_RED.get(), RenderGiantSilverfishRed::new);
		// RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.POLLO.get(), RenderPollo::new);
	}

	protected static void registerBossRenderers() {
		// Nether Dragon
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.NETHER_DRAGON.get(), RenderNetherDragonHead::new);
		/* RenderingRegistry.registerEntityRenderingHandler(SubEntityNetherDragonSegment.class, RenderCQRNetherDragonSegment::new); */

		// Giant Tortoise
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.GIANT_TORTOISE.get(), RenderCQRGiantTortoiseGecko::new);

		// Shelob
		/*
		 * RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantSpider.class, RenderCQRGiantSpider::new);
		 * 
		 * // Spectre Lord RenderingRegistry.registerEntityRenderingHandler(EntityCQRSpectreLord.class, RenderCQRSpectreLord::new); // RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordIllusion.class, RenderSpectreLordIllusion::new);
		 * // RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordCurse.class, RenderSpectreLordCurse::new); // RenderingRegistry.registerEntityRenderingHandler(EntitySpectreLordExplosion.class, RenderSpectreLordExplosion::new);
		 * RenderingRegistry.registerEntityRenderingHandler(AbstractEntityLaser.class, RenderLaser<AbstractEntityLaser>::new);
		 */

		// Ender King
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ENDER_KING.get(), RenderCQREnderKing::new);

		// Geckolib
		// Ender Calamity
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.ENDER_CALAMITY.get(), RenderCQREnderCalamity::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.END_LASER_TARGETING.get(), RenderEndLaser<EntityEndLaserTargeting>::new);
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.END_LASER.get(), RenderEndLaser<EntityEndLaser>::new);
		
		// Spectre Lord
		RenderingRegistry.registerEntityRenderingHandler(CQREntityTypes.SPECTRE_LORD.get(), RenderCQRSpectreLord::new);

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
