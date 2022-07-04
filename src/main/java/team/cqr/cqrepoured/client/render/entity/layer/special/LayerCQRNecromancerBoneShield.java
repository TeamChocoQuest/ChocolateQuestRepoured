package team.cqr.cqrepoured.client.render.entity.layer.special;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.renderers.geo.layer.AbstractLayerGeo;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelBoneShield;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;

public class LayerCQRNecromancerBoneShield extends AbstractLayerGeo<EntityCQRNecromancer> {
	
	protected final ModelBoneShield shieldModel;

	protected final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/bone_shield.png");

	public LayerCQRNecromancerBoneShield(RenderCQREntityGeo<EntityCQRNecromancer> renderer, Function<EntityCQRNecromancer, ResourceLocation> funcGetCurrentTexture, Function<EntityCQRNecromancer, ResourceLocation> funcGetCurrentModel) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
		this.shieldModel = new ModelBoneShield(RenderType::entityTranslucentCull);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityCQRNecromancer entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
			float headPitch) {
		if (entity.isBoneShieldActive() || true) {
			/*this.RENDERER.bindTexture(this.TEXTURE);

			GlStateManager.pushMatrix();
			GlStateManager.scale(0.8, 0.8, 0.8);
			this.ring1.render(entity, 45, 0, 0, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.scale(0.7, 0.7, 0.7);
			this.ring2.render(entity, -45, 180, 0, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();*/
			matrixStackIn.pushPose();
			matrixStackIn.scale(0.8F, 0.8F, 0.8F);
			this.shieldModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.cutout()), packedLightIn, packedLightIn, partialTicks, ageInTicks, netHeadYaw, headPitch);
			matrixStackIn.popPose();
			
			matrixStackIn.pushPose();
			matrixStackIn.scale(0.7F, 0.7F, 0.7F);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
			this.shieldModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.cutout()), packedLightIn, packedLightIn, partialTicks, ageInTicks, netHeadYaw, headPitch);
			matrixStackIn.popPose();
		}
	}

}
