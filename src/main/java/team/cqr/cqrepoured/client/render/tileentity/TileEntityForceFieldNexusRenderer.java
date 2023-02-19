package team.cqr.cqrepoured.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import team.cqr.cqrepoured.client.model.geo.block.ModelNexusCore;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

/*
 * 06.01.2020
 * Author: DerToaster98
 * Github: https://github.com/DerToaster98
 */
@OnlyIn(Dist.CLIENT)
public class TileEntityForceFieldNexusRenderer extends GeoBlockRenderer<TileEntityForceFieldNexus> {

	public TileEntityForceFieldNexusRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new ModelNexusCore());
	}
	
	@Override
	public RenderType getRenderType(TileEntityForceFieldNexus animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

}
