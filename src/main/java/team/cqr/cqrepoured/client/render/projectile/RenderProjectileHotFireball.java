package team.cqr.cqrepoured.client.render.projectile;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.data.ModelData;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHotFireball;

public class RenderProjectileHotFireball extends EntityRenderer<ProjectileHotFireball> {

	public RenderProjectileHotFireball(EntityRendererProvider.Context renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileHotFireball pEntity) {
		return InventoryMenu.BLOCK_ATLAS;
	}

	/*@Override
	protected ResourceLocation getEntityTexture(ProjectileHotFireball entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	} */

	@Override
	public void render(ProjectileHotFireball entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
	{
		BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
		matrixStack.pushPose();
		//GL11.glPushMatrix();
		//GL11.glTranslated(x, y, z);
		//this.bindEntityTexture(entity);
		//GlStateManager.translate(-0.25F, 0F, 0.25F);
		matrixStack.translate(-0.25F, 0F, 0.25F);
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		// GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		//GlStateManager.scale(0.5F, 0.5F, 0.5F);
		
		blockrendererdispatcher.renderSingleBlock(Blocks.OBSIDIAN.defaultBlockState(), matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
		// GlStateManager.translate(0.25F, 0.0F, 0.55F);
		GL11.glPopMatrix();
	}

}
