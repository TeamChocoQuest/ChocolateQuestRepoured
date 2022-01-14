package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.awt.Color;
import java.util.function.Function;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.client.render.entity.layer.IElectrocuteLayerRenderLogic;

public class LayerElectrocuteGeo<T extends EntityLivingBase & IAnimatable> extends AbstractCQRLayerGeo<T> implements IElectrocuteLayerRenderLogic<T> {

	public LayerElectrocuteGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
	}

	@Override
	public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.renderLayerLogic(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
		this.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, headPitch);
	}
	
	@Override
	public void performPreLineRenderPreparation() {
		//Fixes rotations on glib entities
	}

}
