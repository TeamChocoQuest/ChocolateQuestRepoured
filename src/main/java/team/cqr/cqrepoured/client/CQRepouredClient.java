package team.cqr.cqrepoured.client;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import team.cqr.cqrepoured.client.gui.ScreenAlchemyBag;
import team.cqr.cqrepoured.client.gui.ScreenBackpack;
import team.cqr.cqrepoured.client.gui.ScreenBadge;
import team.cqr.cqrepoured.client.gui.ScreenBossBlock;
import team.cqr.cqrepoured.client.gui.ScreenSpawner;
import team.cqr.cqrepoured.client.gui.npceditor.GuiCQREntity;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchant;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchantEditTrade;
import team.cqr.cqrepoured.client.init.CQREntityRenderers;
import team.cqr.cqrepoured.client.init.CQRItemProperties;
import team.cqr.cqrepoured.client.render.armor.RenderArmorKingCrown;
import team.cqr.cqrepoured.client.render.tileentity.TileEntityExporterChestRenderer;
import team.cqr.cqrepoured.client.render.tileentity.TileEntityForceFieldNexusRenderer;
import team.cqr.cqrepoured.client.util.SphereRenderer;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class CQRepouredClient {

	private static ItemStack blockEntityItemStack;

	public static void setBlockEntityItemStack(ItemStack blockEntityItemStack) {
		CQRepouredClient.blockEntityItemStack = blockEntityItemStack;
	}

	public static BlockState getBlockEntityBlockState(BlockEntity blockEntity) {
		if (blockEntityItemStack != null) {
			Item item = blockEntityItemStack.getItem();
			return item instanceof BlockItem ? ((BlockItem) item).getBlock().defaultBlockState() : Blocks.AIR.defaultBlockState();
		}
		if (blockEntity.hasLevel()) {
			return blockEntity.getBlockState();
		}
		return Blocks.AIR.defaultBlockState();
	}

	public static void setupClient(FMLClientSetupEvent event) {
		CQREntityRenderers.registerRenderers();

		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.EXPORTER_CHEST_CQR.get(), TileEntityExporterChestRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.EXPORTER_CHEST_CUSTOM.get(), TileEntityExporterChestRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CQRBlockEntities.FORCE_FIELD_NEXUS.get(), TileEntityForceFieldNexusRenderer::new);
		
		GeoArmorRenderer.registerArmorRenderer(ItemArmorCrown.class, () -> new RenderArmorKingCrown());

		event.enqueueWork(() -> {
			SphereRenderer.init();

			ScreenManager.register(CQRContainerTypes.SPAWNER.get(), ScreenSpawner::new);
			ScreenManager.register(CQRContainerTypes.BOSS_BLOCK.get(), ScreenBossBlock::new);
			ScreenManager.register(CQRContainerTypes.BACKPACK.get(), ScreenBackpack::new);
			ScreenManager.register(CQRContainerTypes.ALCHEMY_BAG.get(), ScreenAlchemyBag::new);
			ScreenManager.register(CQRContainerTypes.BADGE.get(), ScreenBadge::new);

			ScreenManager.register(CQRContainerTypes.MERCHANT.get(), GuiMerchant::new);
			ScreenManager.register(CQRContainerTypes.MERCHANT_EDIT_TRADE.get(), GuiMerchantEditTrade::new);
			ScreenManager.register(CQRContainerTypes.CQR_ENTITY_EDITOR.get(), GuiCQREntity::new);
			
			// Has to happen later
			CQRItemProperties.register();
		});
	}
	
}
