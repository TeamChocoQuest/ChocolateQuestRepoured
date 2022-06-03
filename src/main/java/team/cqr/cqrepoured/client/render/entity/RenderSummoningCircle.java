package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.util.PentagramUtil;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle;

public class RenderSummoningCircle extends EntityRenderer<EntitySummoningCircle> {

	// TODO: Move corner count, color and radius to the entity
	// TODO: Render the letters on the corners

	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(CQRMain.MODID, "textures/entity/summoning_circles/zombie.png"),
			new ResourceLocation(CQRMain.MODID, "textures/entity/summoning_circles/skeleton.png"),
			new ResourceLocation(CQRMain.MODID, "textures/entity/summoning_circles/flying_skull.png"),
			new ResourceLocation(CQRMain.MODID, "textures/entity/summoning_circles/flying_sword.png"),
			new ResourceLocation(CQRMain.MODID, "textures/entity/summoning_circles/meteor.png") };

	public RenderSummoningCircle(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntitySummoningCircle pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer,
			int pPackedLight) {
		float ageInTicks = pEntity.tickCount + pPartialTicks;
		pMatrixStack.pushPose();
		pMatrixStack.mulPose(Vector3f.YP.rotation(ageInTicks * 4.0F));
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
