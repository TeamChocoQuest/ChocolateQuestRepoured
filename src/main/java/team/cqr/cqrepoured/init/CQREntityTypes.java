package team.cqr.cqrepoured.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.passive.AnimalEntity;
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
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;
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
import team.cqr.cqrepoured.entity.mobs.EntityCQRBoarman;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDummy;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDwarf;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGolem;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGremlin;
import team.cqr.cqrepoured.entity.mobs.EntityCQRHuman;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMandril;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMinotaur;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMummy;
import team.cqr.cqrepoured.entity.mobs.EntityCQRNPC;
import team.cqr.cqrepoured.entity.mobs.EntityCQROgre;
import team.cqr.cqrepoured.entity.mobs.EntityCQROrc;
import team.cqr.cqrepoured.entity.mobs.EntityCQRPirate;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSkeleton;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSpectre;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;
import team.cqr.cqrepoured.entity.mobs.EntityCQRWalker;
import team.cqr.cqrepoured.entity.mobs.EntityCQRZombie;
import team.cqr.cqrepoured.entity.mount.EntityGiantEndermite;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishGreen;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishNormal;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishRed;
import team.cqr.cqrepoured.entity.mount.EntityPollo;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEarthQuake;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEnergyOrb;
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
			() -> EntityType.Builder.<ProjectileEarthQuake>of(ProjectileEarthQuake::new, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).fireImmune()
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

	public static final RegistryObject<EntityType<ProjectileEnergyOrb>> PROJECTILE_ENERGY_ORB = ENTITY_TYPES.register("projectile_energy_orb",
			() -> EntityType.Builder.<ProjectileEnergyOrb>of(ProjectileEnergyOrb::new, EntityClassification.MISC).sized(1.5F, 1.5F).clientTrackingRange(4).updateInterval(10)
					.build(CQRMain.prefix("projectile_energy_orb").toString())); 

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
	public static final RegistryObject<EntityType<EntityCQRDummy>> DUMMY = registerSizedHumanoid(EntityCQRDummy::new, "dummy", 1);
	public static final RegistryObject<EntityType<EntityCQRDwarf>> DWARF = registerSized(EntityCQRDwarf::new, "dwarf", 0.54F, 1.235F, 1);
	public static final RegistryObject<EntityType<EntityCQREnderman>> ENDERMAN = registerSized(EntityCQREnderman::new, "enderman", 0.6F, 2.9F, 1);
	public static final RegistryObject<EntityType<EntityCQRGremlin>> GREMLIN = registerSized(EntityCQRGremlin::new, "gremlin", 0.6F, 1.2F, 1);
	public static final RegistryObject<EntityType<EntityCQRGoblin>> GOBLIN = registerSized(EntityCQRGoblin::new, "goblin", 0.6F, 1.4F, 1);
	public static final RegistryObject<EntityType<EntityCQRGolem>> GOLEM = registerSizedHumanoid(EntityCQRGolem::new, "golem", 1);
	public static final RegistryObject<EntityType<EntityCQRHuman>> HUMAN = registerSizedHumanoid(EntityCQRHuman::new, "human", 1);
	public static final RegistryObject<EntityType<EntityCQRIllager>> ILLAGER = registerSizedHumanoid(EntityCQRIllager::new, "illager", 1);
	public static final RegistryObject<EntityType<EntityCQRNPC>> NPC = registerSizedHumanoid(EntityCQRNPC::new, "npc", 1);
	public static final RegistryObject<EntityType<EntityCQRMinotaur>> MINOTAUR = registerSizedHumanoid(EntityCQRMinotaur::new, "minotaur", 1);
	public static final RegistryObject<EntityType<EntityCQRMandril>> MANDRIL = registerSizedHumanoid(EntityCQRMandril::new, "mandril", 1);
	public static final RegistryObject<EntityType<EntityCQRMummy>> MUMMY = registerSizedHumanoid(EntityCQRMummy::new, "mummy", 1);
	public static final RegistryObject<EntityType<EntityCQROgre>> OGRE = registerSizedHumanoid(EntityCQROgre::new, "ogre", 1);
	public static final RegistryObject<EntityType<EntityCQROrc>> ORC = registerSizedHumanoid(EntityCQROrc::new, "orc", 1);
	public static final RegistryObject<EntityType<EntityCQRBoarman>> BOARMAN = registerSizedHumanoid(EntityCQRBoarman::new, "boarman", 1);
	public static final RegistryObject<EntityType<EntityCQRPirate>> PIRATE = registerSizedHumanoid(EntityCQRPirate::new, "pirate", 1);
	public static final RegistryObject<EntityType<EntityCQRSkeleton>> SKELETON = registerSizedHumanoid(EntityCQRSkeleton::new, "skeleton", 1);
	public static final RegistryObject<EntityType<EntityCQRSpectre>> SPECTRE = registerSizedHumanoid(EntityCQRSpectre::new, "spectre", 1);
	public static final RegistryObject<EntityType<EntityCQRTriton>> TRITON = registerSizedHumanoid(EntityCQRTriton::new, "triton", 1);
	public static final RegistryObject<EntityType<EntityCQRWalker>> WALKER = registerSizedHumanoid(EntityCQRWalker::new, "walker", 1);
	public static final RegistryObject<EntityType<EntityCQRZombie>> ZOMBIE = registerSizedHumanoid(EntityCQRZombie::new, "zombie", 1);

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
	
	//Mounts
	public static final RegistryObject<EntityType<EntityPollo>> POLLO = registerSized(EntityPollo::new, "pollo", 0.7F, 1.5F, 3);
	public static final RegistryObject<EntityType<EntityGiantEndermite>> GIANT_ENDERMITE = registerSized(EntityGiantEndermite::new, "giant_endermite", 1.5F, 1.5F, 3);
	public static final RegistryObject<EntityType<EntityGiantSilverfishNormal>> GIANT_SILVERFISH = registerSized(EntityGiantSilverfishNormal::new, "giant_silverfish", 2.0F, 1.0F, 3);
	public static final RegistryObject<EntityType<EntityGiantSilverfishRed>> GIANT_SILVERFISH_RED = registerSized(EntityGiantSilverfishRed::new, "giant_silverfish1", 2.0F, 1.0F, 3);
	public static final RegistryObject<EntityType<EntityGiantSilverfishGreen>> GIANT_SILVERFISH_GREEN = registerSized(EntityGiantSilverfishGreen::new, "giant_silverfish2", 2.0F, 1.0F, 3);
	
	protected static <T extends Entity>  RegistryObject<EntityType<T>> registerSizedHumanoid(IFactory<T> factory, final String entityName, int updateInterval) {
		return registerSized(factory, entityName, 0.6F, 1.875F, updateInterval);
	}
	
	protected static <T extends AbstractEntityLaser>  RegistryObject<EntityType<T>> registerLaser(IFactory<T> factory, String entityName) {
		return registerSized(factory, entityName, 0.1F, 0.1F, 1);
	}

	protected static <T extends Entity>  RegistryObject<EntityType<T>> registerSized(IFactory<T> factory, final String entityName, float width, float height, int updateInterval) {
		RegistryObject<EntityType<T>> result = ENTITY_TYPES.register(entityName, () -> EntityType.Builder
				.<T>of(factory, EntityClassification.MISC)
				.sized(width, height)
				.setTrackingRange(128)
				.clientTrackingRange(64)
				.updateInterval(updateInterval)
				.setShouldReceiveVelocityUpdates(true)
				.build(CQRMain.prefix(entityName).toString()));
		
		return result;
	}
	
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(BOARMAN.get(), EntityCQRBoarman.createCQRAttributes().build());
		
		event.put(DUMMY.get(), EntityCQRDummy.createCQRAttributes().build());
		
		event.put(DWARF.get(), EntityCQRDwarf.createCQRAttributes().build());
		
		event.put(GOBLIN.get(), EntityCQRGoblin.createCQRAttributes().build());
		
		event.put(GOLEM.get(), EntityCQRGolem.createCQRAttributes().build());
		
		event.put(HUMAN.get(), EntityCQRHuman.createCQRAttributes().build());
		
		event.put(MANDRIL.get(), EntityCQRMandril.createCQRAttributes().build());
		
		event.put(MINOTAUR.get(), EntityCQRMinotaur.createCQRAttributes().build());
		
		event.put(MUMMY.get(), EntityCQRMummy.createCQRAttributes().build());
		
		event.put(NPC.get(), EntityCQRNPC.createCQRAttributes().build());
		
		event.put(OGRE.get(), EntityCQROgre.createCQRAttributes().build());
		
		event.put(ORC.get(), EntityCQROrc.createCQRAttributes().build());
		
		event.put(PIRATE.get(), EntityCQRPirate.createCQRAttributes().build());
		
		event.put(SKELETON.get(), EntityCQRSkeleton.createCQRAttributes().build());
		
		event.put(SPECTRE.get(), EntityCQRSpectre.createCQRAttributes().build());
		
		event.put(TRITON.get(), EntityCQRTriton.createCQRAttributes().build());
		
		event.put(WALKER.get(), EntityCQRWalker.createCQRAttributes().build());
		
		event.put(ZOMBIE.get(), EntityCQRZombie.createCQRAttributes().build());
		
		event.put(ILLAGER.get(), EntityCQRIllager.createCQRAttributes().build());
		
		event.put(GREMLIN.get(), EntityCQRGremlin.createCQRAttributes().build());
		
		event.put(SMALL_SLIME.get(), EntitySlimePart.createMobAttributes().build());

		event.put(BOARMAGE.get(), EntityCQRBoarmage.createCQRAttributes().build());
		
		event.put(ENDER_CALAMITY.get(), EntityCQREnderCalamity.createCQRAttributes().build());
		
		event.put(ENDER_KING.get(), EntityCQREnderKing.createCQRAttributes().build());
		
		event.put(ENDERMAN.get(), EntityCQREnderman.createCQRAttributes().build());
		
		event.put(EXTERMINATOR.get(), EntityCQRExterminator.createCQRAttributes().build());
		
		event.put(GIANT_SPIDER.get(), EntityCQRGiantSpider.createCQRAttributes().build());
		
		event.put(GIANT_TORTOISE.get(), EntityCQRGiantTortoise.createCQRAttributes().build());
		
		event.put(LICH.get(), EntityCQRLich.createCQRAttributes().build());
		
		event.put(NECROMANCER.get(), EntityCQRNecromancer.createCQRAttributes().build());
		
		event.put(NETHER_DRAGON.get(), EntityCQRNetherDragon.createCQRAttributes().build());
		
		event.put(PIRATE_CAPTAIN.get(), EntityCQRPirateCaptain.createCQRAttributes().build());
		
		event.put(PIRATE_PARROT.get(), EntityCQRPirateParrot.createAttributes().build());
		
		event.put(SPECTRE_LORD_ILLUSION.get(), EntitySpectreLordIllusion.createCQRAttributes().build());
		
		event.put(WALKER_KING.get(), EntityCQRWalkerKing.createCQRAttributes().build());
		
		event.put(WALKER_KING_ILLUSION.get(), EntityWalkerKingIllusion.createCQRAttributes().build());
		
		event.put(GIANT_ENDERMITE.get(), AnimalEntity.createMobAttributes().build());
		event.put(GIANT_SILVERFISH.get(), AnimalEntity.createMobAttributes().build());
		event.put(GIANT_SILVERFISH_RED.get(), AnimalEntity.createMobAttributes().build());
		event.put(GIANT_SILVERFISH_GREEN.get(), AnimalEntity.createMobAttributes().build());
		event.put(POLLO.get(), AnimalEntity.createMobAttributes().build());
	}

	public static void registerEntityTypes()
	{
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}