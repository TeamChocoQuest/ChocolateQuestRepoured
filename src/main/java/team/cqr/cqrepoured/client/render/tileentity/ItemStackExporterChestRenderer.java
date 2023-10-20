package team.cqr.cqrepoured.client.render.tileentity;

import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.client.CQRepouredClient;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

public class ItemStackExporterChestRenderer extends BlockEntityWithoutLevelRenderer {

	private final LazyLoadedValue<TileEntityExporterChest> tileEntity;

	public ItemStackExporterChestRenderer(Supplier<TileEntityExporterChest> supp, BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
		super(pBlockEntityRenderDispatcher, pEntityModelSet);
		this.tileEntity = new LazyLoadedValue<>(supp);
	}
	
	@Override
	public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		super.renderByItem(pStack, pDisplayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		CQRepouredClient.setBlockEntityItemStack(pStack);
		Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.tileEntity.get(), pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		CQRepouredClient.setBlockEntityItemStack(null);
	}

}
