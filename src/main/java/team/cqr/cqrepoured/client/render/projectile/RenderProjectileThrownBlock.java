package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.model.data.ModelData;
import team.cqr.cqrepoured.entity.projectiles.ProjectileThrownBlock;

public class RenderProjectileThrownBlock extends EntityRenderer<ProjectileThrownBlock> {

	public RenderProjectileThrownBlock(Context renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileThrownBlock entity)
	{
		return InventoryMenu.BLOCK_ATLAS;
	}

	@Override
	public void render(ProjectileThrownBlock entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
		//GL11.glPushMatrix();
		//GL11.glTranslated(x, y, z);
		matrixStack.pushPose();
		matrixStack.translate(-0.35F, 0.0F, -0.35F);
		matrixStack.scale(0.7F, 0.7F, 0.7F);
		//this.bindEntityTexture(entity);
		//GlStateManager.translate(-0.35F, 0F, 0.35F);
		// GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		//GlStateManager.scale(0.7F, 0.7F, 0.7F);
		blockrendererdispatcher.renderSingleBlock(entity.getBlock(), matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
		// GlStateManager.translate(0.25F, 0.0F, 0.55F);
		//GL11.glPopMatrix();
		matrixStack.popPose();
	}

}
