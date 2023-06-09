package team.cqr.cqrepoured.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.model.geo.block.ModelNexusCore;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

/*
 * 06.01.2020
 * Author: DerToaster98
 * Github: https://github.com/DerToaster98
 */
@OnlyIn(Dist.CLIENT)
public class TileEntityForceFieldNexusRenderer extends GeoBlockRenderer<TileEntityForceFieldNexus> {

	protected static final ResourceLocation TEXTURE_RESLOC = CQRMain.prefix("textures/entity/force_field_nexus/force_field_nexus_glow.png");

	public TileEntityForceFieldNexusRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new ModelNexusCore());
	}

	@Override
	public RenderType getRenderType(TileEntityForceFieldNexus animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void render(GeoModel model, TileEntityForceFieldNexus animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                       float red, float green, float blue, float alpha) {
		super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		if (this.getCurrentModelRenderCycle() == EModelRenderCycle.REPEATED) {
			RenderType rtGlowing = CQRRenderTypes.emissive(TEXTURE_RESLOC);
			VertexConsumer vertexBuilderTmp = renderTypeBuffer.getBuffer(rtGlowing);
			super.render(model, animatable, partialTicks, rtGlowing, matrixStackIn, renderTypeBuffer, vertexBuilderTmp, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
	}

}
