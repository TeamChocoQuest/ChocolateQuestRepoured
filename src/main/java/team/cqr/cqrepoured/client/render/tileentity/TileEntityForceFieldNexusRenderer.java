package team.cqr.cqrepoured.client.render.tileentity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import mod.azure.azurelib.renderer.layer.AutoGlowingGeoLayer;
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
		super(new ModelNexusCore());
		
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
			@Override
			protected RenderType getRenderType(TileEntityForceFieldNexus animatable) {
				return CQRRenderTypes.emissive(getTextureResource(animatable));
			}
		});
	}

	@Override
	public RenderType getRenderType(TileEntityForceFieldNexus animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(texture);
	}

}
