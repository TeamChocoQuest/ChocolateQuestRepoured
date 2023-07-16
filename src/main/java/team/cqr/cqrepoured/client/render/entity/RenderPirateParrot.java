package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Parrot;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRModelLayers;
import team.cqr.cqrepoured.client.model.entity.ModelCQRPirateParrot;
import team.cqr.cqrepoured.client.render.entity.layer.special.LayerCQRPirateParrotHeldItem;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

public class RenderPirateParrot extends MobRenderer<EntityCQRPirateParrot, ModelCQRPirateParrot> {

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(EntityCQRPirateParrot pEntity) {
		return TEXTURE;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	public float getBob(Parrot pLivingBase, float pPartialTicks) {
		float f = Mth.lerp(pPartialTicks, pLivingBase.oFlap, pLivingBase.flap);
		float f1 = Mth.lerp(pPartialTicks, pLivingBase.oFlapSpeed, pLivingBase.flapSpeed);
		return (Mth.sin(f) + 1.0F) * f1;
	}

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/pirate_parrot.png");

	public RenderPirateParrot(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelCQRPirateParrot(renderManager.getModelSet().bakeLayer(CQRModelLayers.PIRATE_PARROT)), 0.3F);

		this.addLayer(new LayerCQRPirateParrotHeldItem(this, renderManager.getItemInHandRenderer()));
	}

}
