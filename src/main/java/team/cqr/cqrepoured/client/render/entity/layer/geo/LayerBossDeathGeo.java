package team.cqr.cqrepoured.client.render.entity.layer.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

public class LayerBossDeathGeo<T extends AbstractEntityCQRBoss & GeoEntity> extends GeoRenderLayer<T> {

	protected final BossDeathRayHelper rayHelper;
	
	public LayerBossDeathGeo(GeoEntityRenderer<T> renderer, int red, int green, int blue) {
		this(renderer, red, green, blue, 20F);
	}
	
	public LayerBossDeathGeo(GeoEntityRenderer<T> renderer, int red, int green, int blue, float raySize) {
		super(renderer);
		
		this.rayHelper = new BossDeathRayHelper(red, green, blue, raySize);
	}
	
	protected int getAnimationTick(T entity) {
		return entity.deathTime;
	}
	
	@Override
	public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
		
		int ticks = this.getAnimationTick(animatable);
		if(ticks > 0) {
			poseStack.translate(0, 1 + animatable.getBbHeight() / 2, 1.75F + animatable.getBbWidth() / 2);
			this.rayHelper.renderRays(poseStack, bufferSource.getBuffer(RenderType.lightning()), ticks, partialTick);
		}
	}
	
}
