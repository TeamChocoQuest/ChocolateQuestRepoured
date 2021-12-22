package team.cqr.cqrepoured.client.render.entity.layers.geo;

import java.awt.Color;
import java.util.function.Function;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

public class LayerBossDeathGeo<T extends AbstractEntityCQRBoss & IAnimatable> extends AbstractCQRLayerGeo<T> {

	protected final BossDeathRayHelper rayHelper;
	
	public LayerBossDeathGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel, int red, int green, int blue) {
		this(renderer, funcGetCurrentTexture, funcGetCurrentModel, red, green, blue, 20F);
	}
	
	public LayerBossDeathGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel, int red, int green, int blue, float raySize) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
		
		this.rayHelper = new BossDeathRayHelper(red, green, blue, raySize);
	}
	
	protected int getAnimationTick(T entity) {
		return entity.deathTime;
	}

	@Override
	public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
		int ticks = this.getAnimationTick(entitylivingbaseIn);
		if(ticks > 0) {
			this.rayHelper.renderRays(ticks, partialTicks);
		}
	}

}
