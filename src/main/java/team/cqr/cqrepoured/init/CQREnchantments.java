package team.cqr.cqrepoured.init;

import java.util.function.Supplier;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.enchantment.EnchantmentLightningProtection;
import team.cqr.cqrepoured.enchantment.EnchantmentSpectral;

public class CQREnchantments {
	
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CQRMain.MODID);

	public static final RegistryObject<Enchantment> LIGHTNING_PROTECTION = register("lightning_protection", EnchantmentLightningProtection::new);
	public static final RegistryObject<Enchantment> SPECTRAL = register("spectral", EnchantmentSpectral::new);

	protected static RegistryObject<Enchantment> register(final String name, Supplier<? extends Enchantment> supplier) {
		return ENCHANTMENTS.register(name, supplier);
	}
	
	public static void registerEnchantments() {
		ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
