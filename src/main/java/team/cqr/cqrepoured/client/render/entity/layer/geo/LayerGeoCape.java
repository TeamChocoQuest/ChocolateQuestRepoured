package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerGeoCape<T extends AbstractEntityCQR & IAnimatable> extends AbstractCQRLayerGeo<T>  {
	
	protected final  Function<T, RenderType> GET_RENDER_TYPE;

	public LayerGeoCape(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel, Function<T, RenderType> funcGetRenderTypeWithCapeTexture) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
		
		this.GET_RENDER_TYPE = funcGetRenderTypeWithCapeTexture;
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(!entity.hasCape()) {
			return;
		}
		
		matrixStackIn.pushPose();
		
		GeoModel model =this.getEntityModel().getModel(this.funcGetCurrentModel.apply(entity)); 

		if(model != null) {
			List<GeoBone> topBones = model.topLevelBones.stream().filter(Objects::nonNull).collect(Collectors.toList());
			Deque<Boolean> hidden = new LinkedList<>();
			topBones.forEach(bone -> {
				hidden.add(bone.isHidden());
				bone.setHidden(!(bone.getName().endsWith("Cape") || bone.getName().endsWith("cape")));
			});
			
			this.getRenderer().render(
					model,
					entity, 
					partialTicks, 
					this.GET_RENDER_TYPE.apply(entity), 
					matrixStackIn, 
					bufferIn, 
					null, 
					packedLightIn, 
					OverlayTexture.NO_OVERLAY, 
					1F, 1F, 1F, 1F
			);
			
			topBones.forEach(bone -> {
				Boolean isHidden = hidden.pollFirst();
				if(isHidden != null && bone.isHidden) {
					bone.setHidden(isHidden.booleanValue());
				} else {
					bone.setHidden(true);
				}
			});
		}
		
		matrixStackIn.popPose();
	}

}
