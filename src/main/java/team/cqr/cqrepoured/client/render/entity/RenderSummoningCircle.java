package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.util.PentagramUtil;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle;

public class RenderSummoningCircle extends EntityRenderer<EntitySummoningCircle> {

	// TODO: Move corner count, color and radius to the entity
	// TODO: Render the letters on the corners

	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(CQRConstants.MODID, "textures/entity/summoning_circles/zombie.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/summoning_circles/skeleton.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/summoning_circles/flying_skull.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/summoning_circles/flying_sword.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/summoning_circles/meteor.png") };

	public RenderSummoningCircle(Context renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntitySummoningCircle pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer,
                       int pPackedLight) {
		float ageInTicks = pEntity.tickCount + pPartialTicks;
		pMatrixStack.pushPose();
		pMatrixStack.mulPose(Axis.YP.rotation(ageInTicks * 4.0F));
		PentagramUtil.renderPentagram(pMatrixStack, CQRRenderTypes.emissiveSolid(), ageInTicks);
		pMatrixStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntitySummoningCircle entity) {
		if (entity.getTextureID() >= TEXTURES.length) {
			return TEXTURES[0];
		} else {
			return TEXTURES[entity.getTextureID()];
		}
	}

}
