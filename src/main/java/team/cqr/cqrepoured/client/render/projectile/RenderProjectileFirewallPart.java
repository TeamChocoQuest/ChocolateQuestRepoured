package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.entity.projectiles.ProjectileFireWallPart;

public class RenderProjectileFirewallPart extends EntityRenderer<ProjectileFireWallPart> {

	public RenderProjectileFirewallPart(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileFireWallPart pEntity) {
		return PlayerContainer.BLOCK_ATLAS;
	}

	@Override
	public void render(ProjectileFireWallPart entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		/*//GlStateManager.disableLighting();
		RenderSystem.disableLighting();
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(new ResourceLocation("blocks/fire_layer_0"));
		//TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
		matrixStack.pushPose();
		//GlStateManager.pushMatrix();
		//GlStateManager.translate((float) x, (float) y, (float) z);

		float f = Math.min(entity.getBbWidth(), entity.getBbHeight()) * 1.8F;
		//GlStateManager.scale(f, f, f);
		matrixStack.scale(f, f, f);
		//Tessellator tessellator = Tessellator.getInstance();
		//BufferBuilder bufferbuilder = tessellator.getBuffer();

		float f1 = 0.5F;
		//float f4 = (float) (entity.getY() - entity.getEntityBoundingBox().minY); #TODO
		//GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f5 = 0.0F;
		float f6 = sprite.getU1();
		float f7 = sprite.getV0();
		float f8 = sprite.getU0();
		float f9 = sprite.getV1();

		//Minecraft.getMinecraft().renderEngine.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
 		//#TODO
		int iterations = 8;
		float rot = (360F / iterations);
		for (int i = 0; i < iterations; i++) {
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((f1 - 0.0F), (0.0F - f4), f5).tex(f8, f9).endVertex();
			bufferbuilder.pos((-f1 - 0.0F), (0.0F - f4), f5).tex(f6, f9).endVertex();
			bufferbuilder.pos((-f1 - 0.0F), (1.4F - f4), f5).tex(f6, f7).endVertex();
			bufferbuilder.pos((f1 - 0.0F), (1.4F - f4), f5).tex(f8, f7).endVertex();
			tessellator.draw();
			GlStateManager.rotate(rot, 0F, 1F, 0F);
		}

		//GlStateManager.popMatrix();
		//GlStateManager.enableLighting();
		matrixStack.popPose();
		RenderSystem.enableLighting();*/
	}
}