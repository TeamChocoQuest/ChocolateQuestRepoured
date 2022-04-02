package team.cqr.cqrepoured.client.render.projectile;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHotFireball;

public class RenderProjectileHotFireball extends EntityRenderer<ProjectileHotFireball> {

	public RenderProjectileHotFireball(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileHotFireball pEntity) {
		return PlayerContainer.BLOCK_ATLAS;
	}

	/*@Override
	protected ResourceLocation getEntityTexture(ProjectileHotFireball entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	} */

	@Override
	public void render(ProjectileHotFireball entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
	{
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
		matrixStack.pushPose();
		//GL11.glPushMatrix();
		//GL11.glTranslated(x, y, z);
		//this.bindEntityTexture(entity);
		//GlStateManager.translate(-0.25F, 0F, 0.25F);
		matrixStack.translate(-0.25F, 0F, 0.25F);
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		// GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		//GlStateManager.scale(0.5F, 0.5F, 0.5F);
		
		blockrendererdispatcher.renderBlock(Blocks.OBSIDIAN.defaultBlockState(), matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
		// GlStateManager.translate(0.25F, 0.0F, 0.55F);
		GL11.glPopMatrix();
	}

}
