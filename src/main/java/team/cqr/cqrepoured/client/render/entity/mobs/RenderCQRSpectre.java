package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.util.Color;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRSpectreGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSpectre;

public class RenderCQRSpectre extends RenderCQRBipedBaseGeo<EntityCQRSpectre> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/spectre_0.png");
	
	public RenderCQRSpectre(Context renderManager) {
		super(renderManager, new ModelCQRSpectreGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/spectre"));
	}
	
	@Override
	public Color getRenderColor(EntityCQRSpectre animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
		Color sr = super.getRenderColor(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn);
		
		return Color.ofRGBA(sr.getRed(), sr.getGreen(), sr.getBlue(), 0.5F);
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRSpectre currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRSpectre currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRSpectre currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRSpectre currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRSpectre currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRSpectre currentEntity, IBone bone) {
		
	}

	/*@Override
	public void doRender(EntityCQRSpectre entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}*/

}
