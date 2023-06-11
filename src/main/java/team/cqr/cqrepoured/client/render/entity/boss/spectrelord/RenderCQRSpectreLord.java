package team.cqr.cqrepoured.client.render.entity.boss.spectrelord;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.ModelSpectrelordGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;

public class RenderCQRSpectreLord extends RenderCQRBipedBaseGeo<EntityCQRSpectreLord> {
	
	public static final ResourceLocation TEXTURE_SPECTRE_LORD_DEFAULT = CQRMain.prefix("textures/entity/boss/spectre_lord.png");

	public RenderCQRSpectreLord(EntityRendererManager renderManager) {
		super(renderManager,new ModelSpectrelordGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE_SPECTRE_LORD_DEFAULT, "boss/spectre_lord"));
	}

	//TODO: Update
	/*@Override
	protected void renderModel(EntityCQRSpectreLord entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		boolean flag = entitylivingbaseIn.getInvisibility() > 0.0F;
		if (flag) {
			GlStateManager.alphaFunc(GL11.GL_GREATER, entitylivingbaseIn.getInvisibility());
			this.bindTexture(InvisibilityTexture.get(this.getEntityTexture(entitylivingbaseIn)));
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.depthFunc(GL11.GL_EQUAL);
		}
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		if (flag) {
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
		}
	}*/
	
	@Override
	public Color getRenderColor(EntityCQRSpectreLord animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn) {
		Color sr = super.getRenderColor(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn);
		
		return Color.ofRGBA(sr.getRed(), sr.getGreen(), sr.getBlue(), 0.5F);
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRSpectreLord currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRSpectreLord currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRSpectreLord currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRSpectreLord currentEntity, IBone bone) {
		
	}

	@Override
	protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, EntityCQRSpectreLord currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRSpectreLord currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, EntityCQRSpectreLord currentEntity) {
		
	}

}
