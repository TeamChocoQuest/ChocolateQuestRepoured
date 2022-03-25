package team.cqr.cqrepoured.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.EntitySlimePart;
import team.cqr.cqrepoured.entity.boss.*;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderKing;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamityCrystal;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamitySpawner;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.entity.misc.*;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEarthQuake;

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

	//Standard mobs
	public static final RegistryObject<EntityType<EntityCQREnderman>> ENDERMAN = registerSized(EntityCQREnderman::new, "enderman", 0.6F, 2.9F, 1);

	//Ender calamity
	public static final RegistryObject<EntityType<EntityCalamityCrystal>> CALAMITY_CRYSTAL = registerSized(EntityCalamityCrystal::new, "calamity_crystal", 2, 2, 1);
	public static final RegistryObject<EntityType<EntityCalamitySpawner>> CALAMITY_SPAWNER = registerSized(EntityCalamitySpawner::new, "calamity_spawner", 1, 1, 1);
	public static final RegistryObject<EntityType<EntityCQREnderCalamity>> ENDER_CALAMITY = registerSized(EntityCQREnderCalamity::new, "ender_calamity", 2, 2, 1);
	public static final RegistryObject<EntityType<EntityCQREnderKing>> ENDER_KING = registerSized(EntityCQREnderKing::new, "ender_king", 0.6F, 2.9F, 1);
	
	//Exterminator
	public static final RegistryObject<EntityType<EntityCQRExterminator>> EXTERMINATOR = registerSized(EntityCQRExterminator::new, "exterminator", 2, 2.75F, 1);
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
		//TODO
	}

	public static void registerEntityTypes()
	{
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}