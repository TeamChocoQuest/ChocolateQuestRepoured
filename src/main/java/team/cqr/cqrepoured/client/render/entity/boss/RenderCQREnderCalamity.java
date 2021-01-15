package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelEnderCalamity;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.util.SphereHelper;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQREnderCalamity extends RenderCQREntityGeo<EntityCQREnderCalamity> {
	
	private final SphereHelper sphereHelper;
	
	private static final ResourceLocation SPHERE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity_shield.png");
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity.png");

	public RenderCQREnderCalamity(RenderManager renderManager) {
		super(renderManager, new ModelEnderCalamity(TEXTURE), "boss/ender_calamity");
		this.sphereHelper = new SphereHelper(1.5F, SPHERE_TEXTURE, CQRConfig.bosses.enderCalamityShieldRoundness);
	}
	
	@Override
	public void renderEarly(EntityCQREnderCalamity animatable, float ticks, float red, float green, float blue, float partialTicks) {
		// Render the sphere
		if (animatable.isShieldActive()) {
			GlStateManager.pushMatrix();
			
			this.bindTexture(SPHERE_TEXTURE);
			
			GlStateManager.rotate(partialTicks, 0.0F, 1.0F, 0.0F);
			float color = new Float(0.25 + 0.5 * (0.5 * Math.cos(0.0625 * animatable.ticksExisted) + 0.5));
			//GlStateManager.color(color,color,color);
			
			//"Animation"
			GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)animatable.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            
			sphereHelper.render(animatable, 0,0,0, TEXTURE, color, color, color, 0.5F, this.getWidthScale(animatable), this.getHeightScale(animatable));
			
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
			
			this.bindTexture(getEntityTexture(animatable));
			
			GlStateManager.popMatrix();
		}
		
		super.renderEarly(animatable, ticks, red, green, blue, partialTicks);
	}

	// we do not hold items, so we can ignore this
	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQREnderCalamity currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQREnderCalamity currentEntity) {

	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQREnderCalamity currentEntity) {

	}
	
}
