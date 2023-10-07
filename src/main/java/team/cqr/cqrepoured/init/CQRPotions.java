package team.cqr.cqrepoured.init;

import java.util.function.Supplier;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.potion.ElectrocutionMobEffect;
import team.cqr.cqrepoured.potion.PotionTwohanded;

public class CQRPotions {
	
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CQRConstants.MODID);

	public static final RegistryObject<MobEffect> TWOHANDED = register("twohanded", PotionTwohanded::new);
	public static final RegistryObject<MobEffect> ELECTROCUTION = register("electrocution", ElectrocutionMobEffect::new); 
			
	public static <T extends MobEffect> RegistryObject<MobEffect> register(String name, Supplier<T> supplier) {
		return EFFECTS.register(name, supplier);
	}
	
	public static void registerEffects() {
		EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
