package team.cqr.cqrepoured.client.render.entity.boss;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.ModelWalkerKingGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerBossDeathGeo;
import team.cqr.cqrepoured.client.render.texture.InvisibilityTexture;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;

public class RenderCQRWalkerKing extends RenderCQRBipedBaseGeo<EntityCQRWalkerKing> {
	
	public static final ResourceLocation TEXTURE_WALKER_KING_DEFAULT = CQRMain.prefix("textures/entity/boss/walker_king.png");

	public RenderCQRWalkerKing(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelWalkerKingGeo<>(STANDARD_BIPED_GEO_MODEL, TEXTURE_WALKER_KING_DEFAULT, "boss/walker_king"));

		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
		this.addRenderLayer(new LayerBossDeathGeo<>(this, 191, 0, 255));
	}
	
	private boolean renderingDeath = false;
	private boolean renderingDeathSecondRenderCycle = false;
	
	@Override
	public void render(EntityCQRWalkerKing entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		if(entity.deathTime > 0) {
			this.renderingDeath = true;
			
			super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
			
			this.renderingDeathSecondRenderCycle = true;
			super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
			this.renderingDeathSecondRenderCycle = false;
			
			
			this.renderingDeath = false;
		} else {
			super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		}
	}
	
	@Override
	public RenderType getRenderType(EntityCQRWalkerKing animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		if (this.renderingDeath) {
			//float f2 = (float)animatable.deathTime / 200.0F;
			if(this.renderingDeathSecondRenderCycle) {
				return RenderType.entityDecal(this.getTextureLocation(animatable));
			}
			return RenderType.dragonExplosionAlpha(InvisibilityTexture.get(this.getTextureLocation(animatable)));
		}
		return super.getRenderType(animatable, texture, bufferSource, partialTick);
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRWalkerKing entityLivingBaseIn) {
		return 0;
	}

}
