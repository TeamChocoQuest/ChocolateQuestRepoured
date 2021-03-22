package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.ModelBoneShield;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRNecromancer;
import team.cqr.cqrepoured.util.Reference;

public class LayerCQRNecromancerBoneShield extends AbstractLayerCQR {

	protected final ModelBase ring1;
	protected final ModelBase ring2;

	protected final RenderCQREntity<? extends EntityCQRNecromancer> RENDERER;
	protected final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/bone_shield.png");

	public LayerCQRNecromancerBoneShield(RenderCQREntity<? extends EntityCQRNecromancer> livingEntityRendererIn) {
		super(livingEntityRendererIn);
		this.RENDERER = livingEntityRendererIn;
		this.ring1 = new ModelBoneShield();
		this.ring2 = new ModelBoneShield();
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

		if (entity instanceof EntityCQRNecromancer && ((EntityCQRNecromancer) entity).isBoneShieldActive()) {
			this.RENDERER.bindTexture(this.TEXTURE);

			GlStateManager.pushMatrix();
			GlStateManager.scale(0.8, 0.8, 0.8);
			this.ring1.render(entity, 45, 0, 0, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.scale(0.7, 0.7, 0.7);
			this.ring2.render(entity, -45, 180, 0, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
