package com.teamcqr.chocolatequestrepoured.client.render.projectile;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileFireWallPart;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderProjectileFirewallPart extends Render<ProjectileFireWallPart>{

	public RenderProjectileFirewallPart(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRender(ProjectileFireWallPart entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		GlStateManager.disableLighting();
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        float f = entity.width * 1.4F;
        GlStateManager.scale(f, f, f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f1 = 0.5F;
        float f3 = entity.height / f;
        float f4 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        float rotateAngle = new Float(Math.atan2(entity.motionX, entity.motionZ)) - new Float(Math.toRadians(90));
       draw(rotateAngle, bufferbuilder, f1, f3, f4, textureatlassprite, textureatlassprite1);
       draw(rotateAngle + new Float(Math.toRadians(180)), bufferbuilder, f1, f3, f4, textureatlassprite, textureatlassprite1);

        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
	}

	private void draw(float rotateAngle, BufferBuilder bufferbuilder, float f1, float f3, float f4, TextureAtlasSprite textureatlassprite, TextureAtlasSprite textureatlassprite1) {
		GlStateManager.rotate(rotateAngle, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, -0.3F + (float)((int)f3) * 0.02F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f5 = 0.0F;
        int i = 0;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        while (f3 > 0.0F)
        {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f6 = textureatlassprite2.getMinU();
            float f7 = textureatlassprite2.getMinV();
            float f8 = textureatlassprite2.getMaxU();
            float f9 = textureatlassprite2.getMaxV();

            if (i / 2 % 2 == 0)
            {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            bufferbuilder.pos((double)(f1 - 0.0F), (double)(0.0F - f4), (double)f5).tex((double)f8, (double)f9).endVertex();
            bufferbuilder.pos((double)(-f1 - 0.0F), (double)(0.0F - f4), (double)f5).tex((double)f6, (double)f9).endVertex();
            bufferbuilder.pos((double)(-f1 - 0.0F), (double)(1.4F - f4), (double)f5).tex((double)f6, (double)f7).endVertex();
            bufferbuilder.pos((double)(f1 - 0.0F), (double)(1.4F - f4), (double)f5).tex((double)f8, (double)f7).endVertex();
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 += 0.03F;
            ++i;
        }
        bufferbuilder.finishDrawing();
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileFireWallPart entity) {
		return null;
	}

}
