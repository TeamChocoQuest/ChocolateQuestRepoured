package team.cqr.cqrepoured.inventory;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;

public class CQRContainerTypes {
	
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CQRMain.MODID);

	public static final RegistryObject<ContainerType<ContainerMerchant>> MERCHANT = CONTAINERS.register(
			"cqr_merchant",
			() -> new ContainerType<>(ContainerMerchant::fromNetwork)
	);
	public static ContainerType<ContainerMerchantEditTrade> MERCHANT_EDIT_TRADE;
	public static ContainerType<ContainerAlchemyBag> ALCHEMY_BAG;
	public static ContainerType<ContainerBackpack> BACKPACK;
	public static ContainerType<ContainerBadge> BADGE;
	public static ContainerType<ContainerBossBlock> BOSS_BLOCK;
	public static ContainerType<ContainerCQREntity> CQR_ENTITY_EDITOR;
	public static ContainerType<ContainerSpawner> SPAWNER;

}
