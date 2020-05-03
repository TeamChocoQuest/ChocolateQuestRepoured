package com.teamcqr.chocolatequestrepoured.client.render.projectile;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileLargeFireball;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProjectileLargeFireball extends Render<ProjectileLargeFireball> {

	protected RenderProjectileLargeFireball(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileLargeFireball entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
	@Override
    public void doRender(ProjectileLargeFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        this.bindEntityTexture(entity);
        GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(-0.5F, 0F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(Blocks.LAVA.getDefaultState(), 8);
        GlStateManager.translate(-1.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
    }
	

}
