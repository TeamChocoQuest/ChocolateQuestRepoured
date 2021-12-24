package team.cqr.cqrepoured.client.render.entity.layer.special;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelBoneShield;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractLayerCQR;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;

public class LayerCQRNecromancerBoneShield<T extends EntityCQRNecromancer> extends AbstractLayerCQR<T> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/bone_shield.png");

	protected final ModelBase ring1 = new ModelBoneShield();
	protected final ModelBase ring2 = new ModelBoneShield();

	public LayerCQRNecromancerBoneShield(RenderCQREntity<T> renderer) {
		super(renderer);
	}

	@Override
	public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			float scale) {
		if (!entity.isBoneShieldActive()) {
			return;
		}

		this.renderer.bindTexture(TEXTURE);

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.8, 0.8, 0.8);
		this.ring1.render(entity, 45, 0, 0, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.7, 0.7, 0.7);
		this.ring2.render(entity, -45, 180, 0, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
