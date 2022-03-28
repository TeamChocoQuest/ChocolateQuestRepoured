package team.cqr.cqrepoured.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.inventory.*;

public class CQRContainerTypes {
	
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CQRMain.MODID);

	//public static final RegistryObject<ContainerType<ContainerMerchant>> MERCHANT = CONTAINERS.register("merchant", () -> new ContainerType<>(ContainerMerchant::new));
	//public static final RegistryObject<ContainerType<ContainerMerchantEditTrade>> MERCHANT_EDIT_TRADE = CONTAINERS.register("merchant_edit_trade", () -> new ContainerType<>(ContainerMerchantEditTrade::new));
	public static final RegistryObject<ContainerType<AlchemyBagContainer>> ALCHEMY_BAG = CONTAINERS.register("alchemy_bag", () -> IForgeContainerType.create(AlchemyBagContainer::new));
	public static final RegistryObject<ContainerType<BackpackContainer>> BACKPACK = CONTAINERS.register("backpack", () -> IForgeContainerType.create(BackpackContainer::new));
	public static final RegistryObject<ContainerType<ContainerBadge>> BADGE = CONTAINERS.register("badge", () -> IForgeContainerType.create(ContainerBadge::new));
	public static final RegistryObject<ContainerType<ContainerBossBlock>> BOSS_BLOCK = CONTAINERS.register("boss_block", () -> IForgeContainerType.create(ContainerBossBlock::new));
	public static final RegistryObject<ContainerType<ContainerCQREntity>> CQR_ENTITY_EDITOR = CONTAINERS.register("entity_editor", () -> IForgeContainerType.create(ContainerCQREntity::new));
	public static final RegistryObject<ContainerType<ContainerSpawner>> SPAWNER = CONTAINERS.register("spawner", () -> IForgeContainerType.create(ContainerSpawner::new));

	public static void registerContainerTypes() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
