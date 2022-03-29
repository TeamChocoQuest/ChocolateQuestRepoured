package team.cqr.cqrepoured.client.render.tileentity;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import team.cqr.cqrepoured.client.CQRepouredClient;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

public class ItemStackExporterChestRenderer extends ItemStackTileEntityRenderer {

	private final LazyValue<TileEntityExporterChest> tileEntity;

	public ItemStackExporterChestRenderer(Supplier<TileEntityExporterChest> supp) {
		this.tileEntity = new LazyValue<>(supp);
	}

	@Override
	public void renderByItem(ItemStack pStack, TransformType pTransformType, MatrixStack pPoseStack,
			IRenderTypeBuffer pMultiBuffer, int pCombinedLight, int pCombinedOverlay) {
		CQRepouredClient.setBlockEntityItemStack(pStack);
		TileEntityRendererDispatcher.instance.renderItem(tileEntity.get(), pPoseStack, pMultiBuffer, pCombinedLight,
				pCombinedOverlay);
		CQRepouredClient.setBlockEntityItemStack(null);
	}

}
