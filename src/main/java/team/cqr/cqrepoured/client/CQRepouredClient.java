package team.cqr.cqrepoured.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.cqr.cqrepoured.client.gui.ScreenSpawner;
import team.cqr.cqrepoured.client.gui.ScreenAlchemyBag;
import team.cqr.cqrepoured.client.gui.ScreenBackpack;
import team.cqr.cqrepoured.client.gui.ScreenBadge;
import team.cqr.cqrepoured.client.gui.ScreenBossBlock;
import team.cqr.cqrepoured.client.init.CQREntityRenderers;
import team.cqr.cqrepoured.client.render.tileentity.TileEntityExporterChestRenderer;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class CQRepouredClient {

	private static ItemStack blockEntityItemStack;

	public static void setBlockEntityItemStack(ItemStack blockEntityItemStack) {
		CQRepouredClient.blockEntityItemStack = blockEntityItemStack;
	}

	public static BlockState getBlockEntityBlockState(TileEntity blockEntity) {
		if (blockEntityItemStack != null) {
			Item item = blockEntityItemStack.getItem();
			return item instanceof BlockItem ? ((BlockItem) item).getBlock().defaultBlockState()
					: Blocks.AIR.defaultBlockState();
		}
		if (blockEntity.hasLevel()) {
			return blockEntity.getBlockState();
		}
		return Blocks.AIR.defaultBlockState();
	}

	public static void setupClient(FMLClientSetupEvent event)
	{
		ScreenManager.register(CQRContainerTypes.SPAWNER.get(), ScreenSpawner::new);
		ScreenManager.register(CQRContainerTypes.BOSS_BLOCK.get(), ScreenBossBlock::new);
		ScreenManager.register(CQRContainerTypes.BACKPACK.get(), ScreenBackpack::new);
		ScreenManager.register(CQRContainerTypes.ALCHEMY_BAG.get(), ScreenAlchemyBag::new);
		ScreenManager.register(CQRContainerTypes.BADGE.get(), ScreenBadge::new);

		CQREntityRenderers.registerRenderers();

		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.EXPORTER_CHEST_CQR.get(), TileEntityExporterChestRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.EXPORTER_CHEST_CUSTOM.get(), TileEntityExporterChestRenderer::new);
	}

}
