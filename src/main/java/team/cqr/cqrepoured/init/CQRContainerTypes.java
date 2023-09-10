package team.cqr.cqrepoured.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.inventory.ContainerAlchemyBag;
import team.cqr.cqrepoured.inventory.ContainerBackpack;
import team.cqr.cqrepoured.inventory.ContainerBadge;
import team.cqr.cqrepoured.inventory.ContainerBossBlock;
import team.cqr.cqrepoured.inventory.ContainerCQREntity;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.inventory.ContainerMerchantEditTrade;
import team.cqr.cqrepoured.inventory.ContainerSpawner;

public class CQRContainerTypes {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CQRConstants.MODID);

	public static final RegistryObject<MenuType<ContainerMerchant>> MERCHANT = CONTAINERS.register("merchant", () -> IForgeMenuType.create(ContainerMerchant::new));
	public static final RegistryObject<MenuType<ContainerMerchantEditTrade>> MERCHANT_EDIT_TRADE = CONTAINERS.register("merchant_edit_trade", () -> IForgeMenuType.create(ContainerMerchantEditTrade::new));
	public static final RegistryObject<MenuType<ContainerAlchemyBag>> ALCHEMY_BAG = CONTAINERS.register("alchemy_bag", () -> IForgeMenuType.create(ContainerAlchemyBag::new));
	public static final RegistryObject<MenuType<ContainerBackpack>> BACKPACK = CONTAINERS.register("backpack", () -> IForgeMenuType.create(ContainerBackpack::new));
	public static final RegistryObject<MenuType<ContainerBadge>> BADGE = CONTAINERS.register("badge", () -> IForgeMenuType.create(ContainerBadge::new));
	public static final RegistryObject<MenuType<ContainerBossBlock>> BOSS_BLOCK = CONTAINERS.register("boss_block", () -> IForgeMenuType.create(ContainerBossBlock::new));
	public static final RegistryObject<MenuType<ContainerCQREntity>> CQR_ENTITY_EDITOR = CONTAINERS.register("entity_editor", () -> IForgeMenuType.create(ContainerCQREntity::new));
	public static final RegistryObject<MenuType<ContainerSpawner>> SPAWNER = CONTAINERS.register("spawner", () -> IForgeMenuType.create(ContainerSpawner::new));

	public static void registerContainerTypes() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
