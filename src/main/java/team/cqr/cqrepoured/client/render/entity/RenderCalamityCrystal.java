package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.models.entities.ModelCalamityCrystal;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCalamityCrystal;

public class RenderCalamityCrystal extends Render<EntityCalamityCrystal> {

	private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
	private final ModelBase modelEnderCrystal = new ModelCalamityCrystal(0.0F);

	public RenderCalamityCrystal(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCalamityCrystal entity) {
		return RenderCalamityCrystal.ENDER_CRYSTAL_TEXTURES;
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

	@Override
	public void doRender(EntityCalamityCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
		float f = (float) entity.innerRotation + partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		this.bindTexture(ENDER_CRYSTAL_TEXTURES);
		float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
		f1 = f1 * f1 + f1;

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.modelEnderCrystal.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		BlockPos blockpos = entity.getBeamTarget();

		if (blockpos != null) {
			this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
			float f2 = (float) blockpos.getX() + 0.5F;
			float f3 = (float) blockpos.getY() + 0.5F;
			float f4 = (float) blockpos.getZ() + 0.5F;
			double d0 = (double) f2 - entity.posX;
			double d1 = (double) f3 - entity.posY;
			double d2 = (double) f4 - entity.posZ;
			GlStateManager.pushAttrib();
			if (entity instanceof EntityCalamityCrystal) {
				if (((EntityCalamityCrystal) entity).isAbsorbing()) {
					GlStateManager.color(1.0F, 0.0F, 0.0F);
				} else {
					GlStateManager.color(0.0F, 1.0F, 0.0F);
				}
			}
			this.renderCrystalBeams(x + d0, y - 0.3D + (double) (f1 * 0.4F) + d1, z + d2, partialTicks, (double) f2, (double) f3, (double) f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
			GlStateManager.popAttrib();
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	// Copied from RenderDragon.java
	private void renderCrystalBeams(double p_188325_0_, double p_188325_2_, double p_188325_4_, float p_188325_6_, double p_188325_7_, double p_188325_9_, double p_188325_11_, int p_188325_13_, double p_188325_14_, double p_188325_16_, double p_188325_18_) {
		float f = (float) (p_188325_14_ - p_188325_7_);
		float f1 = (float) (p_188325_16_ - 1.0D - p_188325_9_);
		float f2 = (float) (p_188325_18_ - p_188325_11_);
		float f3 = MathHelper.sqrt(f * f + f2 * f2);
		float f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
		GlStateManager.translate((float) p_188325_0_, (float) p_188325_2_ + 2.0F, (float) p_188325_4_);
		GlStateManager.rotate((float) (-Math.atan2((double) f2, (double) f)) * (180F / (float) Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) (-Math.atan2((double) f3, (double) f1)) * (180F / (float) Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.shadeModel(7425);
		float f5 = 0.0F - ((float) p_188325_13_ + p_188325_6_) * 0.01F;
		float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0F - ((float) p_188325_13_ + p_188325_6_) * 0.01F;
		bufferbuilder.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
		int i = 8;

		for (int j = 0; j <= 8; ++j) {
			float f7 = MathHelper.sin((float) (j % 8) * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
			float f8 = MathHelper.cos((float) (j % 8) * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
			float f9 = (float) (j % 8) / 8.0F;
			bufferbuilder.pos((double) (f7 * 0.2F), (double) (f8 * 0.2F), 0.0D).tex((double) f9, (double) f5).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos((double) f7, (double) f8, (double) f4).tex((double) f9, (double) f6).color(255, 255, 255, 255).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(7424);
		RenderHelper.enableStandardItemLighting();
	}

}
