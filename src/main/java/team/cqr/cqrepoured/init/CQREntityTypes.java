package team.cqr.cqrepoured.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderKing;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamityCrystal;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamitySpawner;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class CQREntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CQRMain.MODID);
	
	//Default ticking rate is 3 => every 3 ticks it updates

	//Ender calamity
	public static final RegistryObject<EntityType<EntityCalamityCrystal>> CALAMITY_CRYSTAL = registerSized(EntityCalamityCrystal::new, "calamity_crystal", 2, 2, Integer.MAX_VALUE);
	public static final RegistryObject<EntityType<EntityCalamitySpawner>> CALAMITY_SPAWNER = registerSized(EntityCalamitySpawner::new, "calamity_spawner", 1, 1, 3);
	public static final RegistryObject<EntityType<EntityCQREnderCalamity>> ENDER_CALAMITY = registerSized(EntityCQREnderCalamity::new, "ender_calamity", 2, 2, 3);
	public static final RegistryObject<EntityType<EntityCQREnderKing>> ENDER_KING = registerSized(EntityCQREnderKing::new, "ender_king", 0.6F, 2.9F, 3);
	
	//Exterminator
	public static final RegistryObject<EntityType<EntityCQRExterminator>> EXTERMINATOR = registerSized(EntityCQRExterminator::new, "exterminator", 2, 2.75F, 3);

	//Giant Tortoise
	public static final RegistryObject<EntityType<EntityCQRGiantTortoise>> GIANT_TORTOISE = registerSized(EntityCQRGiantTortoise::new, "giant_tortoise", 2F, 1.7F,3);
	
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
