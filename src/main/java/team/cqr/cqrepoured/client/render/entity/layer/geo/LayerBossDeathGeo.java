package team.cqr.cqrepoured.client.render.entity.layer.geo;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractCQRLayerGeo;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

import java.util.function.Function;

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
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		int ticks = this.getAnimationTick(entityLivingBaseIn);
		if(ticks > 0) {
			this.rayHelper.renderRays(matrixStackIn, bufferIn.getBuffer(RenderType.lightning()), ticks, partialTicks);
		}
	}
	
}
