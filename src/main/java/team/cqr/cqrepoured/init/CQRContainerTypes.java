package team.cqr.cqrepoured.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.inventory.ContainerAlchemyBag;
import team.cqr.cqrepoured.inventory.ContainerBackpack;
import team.cqr.cqrepoured.inventory.ContainerBadge;
import team.cqr.cqrepoured.inventory.ContainerBossBlock;
import team.cqr.cqrepoured.inventory.ContainerCQREntity;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.inventory.ContainerMerchantEditTrade;
import team.cqr.cqrepoured.inventory.ContainerSpawner;

public class CQRContainerTypes {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CQRMain.MODID);

	public static final RegistryObject<MenuType<ContainerMerchant>> MERCHANT = CONTAINERS.register("merchant", () -> IForgeContainerType.create(ContainerMerchant::new));
	public static final RegistryObject<MenuType<ContainerMerchantEditTrade>> MERCHANT_EDIT_TRADE = CONTAINERS.register("merchant_edit_trade", () -> IForgeContainerType.create(ContainerMerchantEditTrade::new));
	public static final RegistryObject<MenuType<ContainerAlchemyBag>> ALCHEMY_BAG = CONTAINERS.register("alchemy_bag", () -> IForgeContainerType.create(ContainerAlchemyBag::new));
	public static final RegistryObject<MenuType<ContainerBackpack>> BACKPACK = CONTAINERS.register("backpack", () -> IForgeContainerType.create(ContainerBackpack::new));
	public static final RegistryObject<MenuType<ContainerBadge>> BADGE = CONTAINERS.register("badge", () -> IForgeContainerType.create(ContainerBadge::new));
	public static final RegistryObject<MenuType<ContainerBossBlock>> BOSS_BLOCK = CONTAINERS.register("boss_block", () -> IForgeContainerType.create(ContainerBossBlock::new));
	public static final RegistryObject<MenuType<ContainerCQREntity>> CQR_ENTITY_EDITOR = CONTAINERS.register("entity_editor", () -> IForgeContainerType.create(ContainerCQREntity::new));
	public static final RegistryObject<MenuType<ContainerSpawner>> SPAWNER = CONTAINERS.register("spawner", () -> IForgeContainerType.create(ContainerSpawner::new));

	public static void registerContainerTypes() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
