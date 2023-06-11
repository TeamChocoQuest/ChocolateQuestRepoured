package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import team.cqr.cqrepoured.entity.projectiles.ProjectileThrownBlock;

public class RenderProjectileThrownBlock extends EntityRenderer<ProjectileThrownBlock> {

	public RenderProjectileThrownBlock(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileThrownBlock entity)
	{
		return PlayerContainer.BLOCK_ATLAS;
	}

	@Override
	public void render(ProjectileThrownBlock entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
		//GL11.glPushMatrix();
		//GL11.glTranslated(x, y, z);
		matrixStack.pushPose();
		matrixStack.translate(-0.35F, 0.0F, -0.35F);
		matrixStack.scale(0.7F, 0.7F, 0.7F);
		//this.bindEntityTexture(entity);
		//GlStateManager.translate(-0.35F, 0F, 0.35F);
		// GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		//GlStateManager.scale(0.7F, 0.7F, 0.7F);
		blockrendererdispatcher.renderBlock(entity.getBlock(), matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
		// GlStateManager.translate(0.25F, 0.0F, 0.55F);
		//GL11.glPopMatrix();
		matrixStack.popPose();
	}

}
