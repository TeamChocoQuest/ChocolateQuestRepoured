package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.CQRMain;
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
	public float getBob(ParrotEntity pLivingBase, float pPartialTicks) {
		float f = MathHelper.lerp(pPartialTicks, pLivingBase.oFlap, pLivingBase.flap);
		float f1 = MathHelper.lerp(pPartialTicks, pLivingBase.oFlapSpeed, pLivingBase.flapSpeed);
		return (MathHelper.sin(f) + 1.0F) * f1;
	}

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/pirate_parrot.png");

	public RenderPirateParrot(EntityRendererManager renderManager) {
		super(renderManager, new ModelCQRPirateParrot(), 0.3F);

		this.addLayer(new LayerCQRPirateParrotHeldItem(this));
	}

}
