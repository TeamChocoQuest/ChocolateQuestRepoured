package team.cqr.cqrepoured.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
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
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;

public class CQREntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CQRMain.MODID);
	
	//Default ticking rate is 3 => every 3 ticks it updates
	
	//Standard mobs
	public static final RegistryObject<EntityType<EntityCQREnderman>> ENDERMAN = registerSized(EntityCQREnderman::new, "enderman", 0.6F, 2.9F, 1);

	//Ender calamity
	public static final RegistryObject<EntityType<EntityCalamityCrystal>> CALAMITY_CRYSTAL = registerSized(EntityCalamityCrystal::new, "calamity_crystal", 2, 2, 1);
	public static final RegistryObject<EntityType<EntityCalamitySpawner>> CALAMITY_SPAWNER = registerSized(EntityCalamitySpawner::new, "calamity_spawner", 1, 1, 1);
	public static final RegistryObject<EntityType<EntityCQREnderCalamity>> ENDER_CALAMITY = registerSized(EntityCQREnderCalamity::new, "ender_calamity", 2, 2, 1);
	public static final RegistryObject<EntityType<EntityCQREnderKing>> ENDER_KING = registerSized(EntityCQREnderKing::new, "ender_king", 0.6F, 2.9F, 1);
	
	//Exterminator
	public static final RegistryObject<EntityType<EntityCQRExterminator>> EXTERMINATOR = registerSized(EntityCQRExterminator::new, "exterminator", 2, 2.75F, 1);

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
	
}
