package team.cqr.cqrepoured.client.render.tileentity;

import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.LazyValue;
import team.cqr.cqrepoured.client.CQRepouredClient;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

public class ItemStackExporterChestRenderer extends BlockEntityWithoutLevelRenderer {

	private final LazyValue<TileEntityExporterChest> tileEntity;

	public ItemStackExporterChestRenderer(Supplier<TileEntityExporterChest> supp) {
		this.tileEntity = new LazyValue<>(supp);
	}

	@Override
	public void renderByItem(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack,
                             MultiBufferSource pMultiBuffer, int pCombinedLight, int pCombinedOverlay) {
		CQRepouredClient.setBlockEntityItemStack(pStack);
		BlockEntityRenderDispatcher.instance.renderItem(tileEntity.get(), pPoseStack, pMultiBuffer, pCombinedLight,
				pCombinedOverlay);
		CQRepouredClient.setBlockEntityItemStack(null);
	}

}
