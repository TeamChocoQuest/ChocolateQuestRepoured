package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelEnderCalamity;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQREnderCalamity;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQREnderCalamity extends RenderCQREntityGeo<EntityCQREnderCalamity> {
	
	private final ResourceLocation SPHERE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity_shield.png");

	public RenderCQREnderCalamity(RenderManager renderManager) {
		super(renderManager, new ModelEnderCalamity(new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity.png")), "boss/ender_calamity");
	}

	@Override
	public void renderEarly(EntityCQREnderCalamity animatable, float ticks, float red, float green, float blue, float partialTicks) {
		// First: Render the sphere
		if (animatable.isShieldActive()) {
			//For shield rendering => https://github.com/Angry-Pixel/The-Betweenlands/blob/1.12-dev/src/main/java/thebetweenlands/client/render/entity/RenderFortressBoss.java
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, animatable.height / 2, 0);
			
			this.renderDaSphere(animatable.posX, animatable.posY, animatable.posZ, partialTicks, true, true, 1F, 1F, 1F, true, animatable.ticksExisted);
			
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
	
	protected void renderDaSphere(double x, double y, double z, float partialTicks, boolean renderOutlines, boolean renderInside, float lineAlpha, float insideAlpha, float alpha, boolean depthMask, int entityTick) {
		
	}

}
