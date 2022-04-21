package team.cqr.cqrepoured.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.EntitySlimePart;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.entity.boss.EntityCQRBoarmage;
import team.cqr.cqrepoured.entity.boss.EntityCQRGiantSpider;
import team.cqr.cqrepoured.entity.boss.EntityCQRLich;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderKing;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamityCrystal;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamitySpawner;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaserTargeting;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityExterminatorHandLaser;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityRotatingLaser;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntitySpectreLordCurse;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntitySpectreLordIllusion;
import team.cqr.cqrepoured.entity.misc.EntityBubble;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.entity.misc.EntityElectricField;
import team.cqr.cqrepoured.entity.misc.EntityFlyingSkullMinion;
import team.cqr.cqrepoured.entity.misc.EntityIceSpike;
import team.cqr.cqrepoured.entity.misc.EntitySpiderEgg;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.entity.misc.EntityTNTPrimedCQR;
import team.cqr.cqrepoured.entity.misc.EntityWalkerKingIllusion;
import team.cqr.cqrepoured.entity.misc.EntityWalkerTornado;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEarthQuake;
import team.cqr.cqrepoured.entity.projectiles.ProjectileFireWallPart;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHomingEnderEye;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHookShotHook;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHotFireball;
import team.cqr.cqrepoured.entity.projectiles.ProjectilePoisonSpell;
import team.cqr.cqrepoured.entity.projectiles.ProjectileSpiderBall;
import team.cqr.cqrepoured.entity.projectiles.ProjectileThrownBlock;
import team.cqr.cqrepoured.entity.projectiles.ProjectileVampiricSpell;
import team.cqr.cqrepoured.entity.projectiles.ProjectileWeb;

@EventBusSubscriber(modid = CQRMain.MODID, bus = Bus.MOD)
public class CQREntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CQRMain.MODID);
	
	//Default ticking rate is 3 => every 3 ticks it updates
	//Misc mobs
	public static final RegistryObject<EntityType<EntitySlimePart>> SMALL_SLIME = registerSized(EntitySlimePart::new, "slime_part", 0.25F, 0.25F, 1); 

	//Projectiles //#TODO probably will be changed to registerSized, tweak values
	public static final RegistryObject<EntityType<ProjectileBubble>> PROJECTILE_BUBBLE = ENTITY_TYPES.register("projectile_bubble",
			() -> EntityType.Builder.<ProjectileBubble>of(ProjectileBubble::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10) //.fireImmune() I guess it isn't though
					.build(CQRMain.prefix("projectile_bubble").toString()));

	public static final RegistryObject<EntityType<ProjectileBullet>> PROJECTILE_BULLET = ENTITY_TYPES.register("projectile_bullet",
			() -> EntityType.Builder.<ProjectileBullet>of(ProjectileBullet::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).fireImmune()
					.build(CQRMain.prefix("projectile_bullet").toString()));

	public static final RegistryObject<EntityType<ProjectileEarthQuake>> PROJECTILE_EARTH_QUAKE = ENTITY_TYPES.register("projectile_earth_quake",
			() -> EntityType.Builder.<ProjectileEarthQuake>of(ProjectileEarthQuake::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).fireImmune()
					.build(CQRMain.prefix("projectile_earth_quake").toString()));

	public static final RegistryObject<EntityType<ProjectileCannonBall>> PROJECTILE_CANNON_BALL = ENTITY_TYPES.register("projectile_cannon_ball",
			() -> EntityType.Builder.<ProjectileCannonBall>of(ProjectileCannonBall::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).fireImmune()
					.build(CQRMain.prefix("projectile_cannon_ball").toString()));

	public static final RegistryObject<EntityType<ProjectileWeb>> PROJECTILE_WEB = ENTITY_TYPES.register("projectile_web",
			() -> EntityType.Builder.<ProjectileWeb>of(ProjectileWeb::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_web").toString()));

	public static final RegistryObject<EntityType<ProjectileVampiricSpell>> PROJECTILE_VAMPIRIC_SPELL = ENTITY_TYPES.register("projectile_vampiric_spell",
			() -> EntityType.Builder.<ProjectileVampiricSpell>of(ProjectileVampiricSpell::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_vampiric_spell").toString()));

	public static final RegistryObject<EntityType<ProjectileSpiderBall>> PROJECTILE_SPIDER_BALL = ENTITY_TYPES.register("projectile_spider_ball",
			() -> EntityType.Builder.<ProjectileSpiderBall>of(ProjectileSpiderBall::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_spider_ball").toString()));

	public static final RegistryObject<EntityType<ProjectilePoisonSpell>> PROJECTILE_POISON_SPELL = ENTITY_TYPES.register("projectile_poison_spell",
			() -> EntityType.Builder.<ProjectilePoisonSpell>of(ProjectilePoisonSpell::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_poison_spell").toString()));

	public static final RegistryObject<EntityType<ProjectileHotFireball>> PROJECTILE_HOT_FIREBALL = ENTITY_TYPES.register("projectile_hot_fireball",
			() -> EntityType.Builder.<ProjectileHotFireball>of(ProjectileHotFireball::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_hot_fireball").toString()));

	public static final RegistryObject<EntityType<ProjectileFireWallPart>> PROJECTILE_FIRE_WALL_PART = ENTITY_TYPES.register("projectile_fire_wall_part",
			() -> EntityType.Builder.<ProjectileFireWallPart>of(ProjectileFireWallPart::new, EntityClassification.MISC).sized(1.0F, 2.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_fire_wall_part").toString()));

	public static final RegistryObject<EntityType<ProjectileHomingEnderEye>> PROJECTILE_HOMING_ENDER_EYE = ENTITY_TYPES.register("projectile_homing_ender_eye",
			() -> EntityType.Builder.<ProjectileHomingEnderEye>of(ProjectileHomingEnderEye::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_homing_ender_eye").toString()));

	public static final RegistryObject<EntityType<ProjectileThrownBlock>> PROJECTILE_THROWN_BLOCK = ENTITY_TYPES.register("projectile_thrown_block",
			() -> EntityType.Builder.<ProjectileThrownBlock>of(ProjectileThrownBlock::new, EntityClassification.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_thrown_block").toString()));

	/*public static final RegistryObject<EntityType<ProjectileEnergyOrb>> PROJECTILE_ENERGY_ORB = ENTITY_TYPES.register("projectile_energy_orb",
			() -> EntityType.Builder.<ProjectileEnergyOrb>of(ProjectileEnergyOrb::new, EntityClassification.MISC).sized(1.5F, 1.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_energy_orb").toString())); */

	public static final RegistryObject<EntityType<ProjectileHookShotHook>> PROJECTILE_HOOKSHOT_HOOK = ENTITY_TYPES.register("projectile_hookshot_hook",
			() -> EntityType.Builder.<ProjectileHookShotHook>of(ProjectileHookShotHook::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(12).updateInterval(5)
					.build(CQRMain.prefix("projectile_hookshot_hook").toString()));
	

	public static final RegistryObject<EntityType<EntityColoredLightningBolt>> COLORED_LIGHTNING = ENTITY_TYPES.register("colored_lightning_bolt",
			() -> EntityType.Builder.<EntityColoredLightningBolt>of(EntityColoredLightningBolt::new, EntityClassification.MISC)
			.noSave()
			.sized(0.0F, 0.0F)
			.clientTrackingRange(32)
			.updateInterval(Integer.MAX_VALUE)
			.build(CQRMain.prefix("colored_lightning_bolt").toString())
		);
	public static final RegistryObject<EntityType<EntityWalkerTornado>> WALKER_TORNADO = ENTITY_TYPES.register("walker_tornado",
			() -> EntityType.Builder.<EntityWalkerTornado>of(EntityWalkerTornado::new, EntityClassification.MISC)
			.sized(0.5F, 0.5F)
			.clientTrackingRange(64)
			.updateInterval(1)
			.build(CQRMain.prefix("walker_tornado").toString())
		);
	
	//Standard mobs
	public static final RegistryObject<EntityType<EntityCQREnderman>> ENDERMAN = registerSized(EntityCQREnderman::new, "enderman", 0.6F, 2.9F, 1);

	//Ender calamity
	public static final RegistryObject<EntityType<EntityCalamityCrystal>> CALAMITY_CRYSTAL = registerSized(EntityCalamityCrystal::new, "calamity_crystal", 2, 2, 1);
	public static final RegistryObject<EntityType<EntityCalamitySpawner>> CALAMITY_SPAWNER = registerSized(EntityCalamitySpawner::new, "calamity_spawner", 1, 1, 1);
	public static final RegistryObject<EntityType<EntityCQREnderCalamity>> ENDER_CALAMITY = registerSized(EntityCQREnderCalamity::new, "ender_calamity", 2, 2, 1);
	public static final RegistryObject<EntityType<EntityCQREnderKing>> ENDER_KING = registerSized(EntityCQREnderKing::new, "ender_king", 0.6F, 2.9F, 1);
	public static final RegistryObject<EntityType<EntityEndLaser>> END_LASER = registerLaser(EntityEndLaser::new, "end_laser");
	public static final RegistryObject<EntityType<EntityEndLaserTargeting>> END_LASER_TARGETING = registerLaser(EntityEndLaserTargeting::new, "end_targeting_laser");
	
	//Exterminator
	public static final RegistryObject<EntityType<EntityCQRExterminator>> EXTERMINATOR = registerSized(EntityCQRExterminator::new, "exterminator", 2, 2.75F, 1);
	public static final RegistryObject<EntityType<EntityExterminatorHandLaser>> LASER_EXTERMINATOR = registerLaser(EntityExterminatorHandLaser::new, "exterminator_laser");
	public static final RegistryObject<EntityType<EntityElectricField>> ELECTRIC_FIELD = registerSized(EntityElectricField::new, "electric_field", 1.125F, 1.125F, 3);
	
	
	//Giant Tortoise
	public static final RegistryObject<EntityType<EntityCQRGiantTortoise>> GIANT_TORTOISE = registerSized(EntityCQRGiantTortoise::new, "giant_tortoise", 2F, 1.7F,1);
	
	//nether dragon
	public static final RegistryObject<EntityType<EntityCQRNetherDragon>> NETHER_DRAGON = registerSized(EntityCQRNetherDragon::new, "nether_dragon", 2F, 2F, 1);

	//Shelob
	public static final RegistryObject<EntityType<EntityCQRGiantSpider>> GIANT_SPIDER = registerSized(EntityCQRGiantSpider::new, "giant_spider", 2.3F, 1F, 1);
	
	//Boarmage
	public static final RegistryObject<EntityType<EntityCQRBoarmage>> BOARMAGE = registerSizedHumanoid(EntityCQRBoarmage::new, "boar_mage", 1);
	
	//Lich
	public static final RegistryObject<EntityType<EntityCQRLich>> LICH = registerSizedHumanoid(EntityCQRLich::new, "lich", 1);
	
	//Necromancer
	public static final RegistryObject<EntityType<EntityCQRNecromancer>> NECROMANCER = registerSizedHumanoid(EntityCQRNecromancer::new, "necromancer", 1);
	
	//Pirate Captain
	public static final RegistryObject<EntityType<EntityCQRPirateCaptain>> PIRATE_CAPTAIN = registerSizedHumanoid(EntityCQRPirateCaptain::new, "pirate_captain", 1);
	public static final RegistryObject<EntityType<EntityCQRPirateParrot>> PIRATE_PARROT = registerSized(EntityCQRPirateParrot::new, "pirate_parrot", 0.5F, 0.9F, 3);
	
	//Walker King
	public static final RegistryObject<EntityType<EntityCQRWalkerKing>> WALKER_KING = registerSizedHumanoid(EntityCQRWalkerKing::new, "walker_king", 1);
	public static final RegistryObject<EntityType<EntityWalkerKingIllusion>> WALKER_KING_ILLUSION = registerSizedHumanoid(EntityWalkerKingIllusion::new, "walker_king_illusion", 1);
	
	//Drachenlord
	public static final RegistryObject<EntityType<EntitySpectreLordIllusion>> SPECTRE_LORD_ILLUSION = registerSizedHumanoid(EntitySpectreLordIllusion::new, "spectre_lord_illusion", 1);
	public static final RegistryObject<EntityType<EntitySpectreLordCurse>> SPECTRE_LORD_CURSE = registerSized(EntitySpectreLordCurse::new, "spectre_lord_curse", 1.0F, 2.0F, 1);
	public static final RegistryObject<EntityType<EntityRotatingLaser>> LASER_ROTATING = registerLaser(EntityRotatingLaser::new, "laser_rotating");
	
	//Musc
	public static final RegistryObject<EntityType<EntityBubble>> BUBBLE = registerSized(EntityBubble::new, "bubble", 1.0F, 1.0F, 3);
	public static final RegistryObject<EntityType<EntityTNTPrimedCQR>> TNT_CQR = registerSized(EntityTNTPrimedCQR::new, "tnt_cqr", 1.0F, 1.0F, 3);
	public static final RegistryObject<EntityType<EntitySummoningCircle>> SUMMONING_CIRCLE = registerSized(EntitySummoningCircle::new, "summoning_circle", 2.0F, 0.005F, 3);
	public static final RegistryObject<EntityType<EntitySpiderEgg>> SPIDER_EGG = registerSized(EntitySpiderEgg::new, "spider_egg", 1.0F, 1.0F, 3);
	public static final RegistryObject<EntityType<EntityIceSpike>> ICE_SPIKE = registerSized(EntityIceSpike::new, "ice_spike", 0.5F, 0.8F, 3);
	public static final RegistryObject<EntityType<EntityFlyingSkullMinion>> FLYING_SKULL = registerSized(EntityFlyingSkullMinion::new, "flying_skull", 0.5F, 0.5F, 3);
	
	
	protected static <T extends Entity>  RegistryObject<EntityType<T>> registerSizedHumanoid(IFactory<T> factory, final String entityName, int updateInterval) {
		return registerSized(factory, entityName, 0.6F, 1.875F, updateInterval);
	}
	
	protected static <T extends AbstractEntityLaser>  RegistryObject<EntityType<T>> registerLaser(IFactory<T> factory, String entityName) {
		return registerSized(factory, entityName, 0.1F, 0.1F, 1);
	}

	protected static <T extends Entity>  RegistryObject<EntityType<T>> registerSized(IFactory<T> factory, final String entityName, float width, float height, int updateInterval) {
		return ENTITY_TYPES.register(entityName, () -> EntityType.Builder
				.<T>of(factory, EntityClassification.MISC)
				.sized(width, height)
				.setTrackingRange(128)
				.clientTrackingRange(64)
				.updateInterval(updateInterval)
				.setShouldReceiveVelocityUpdates(true)
				.build(CQRMain.prefix(entityName).toString()));
	}
	
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(SMALL_SLIME.get(), EntitySlimePart.createMobAttributes().build());
		
		event.put(EXTERMINATOR.get(), EntityCQRExterminator.createCQRAttributes().build());
	}

	public static void registerEntityTypes()
	{
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}