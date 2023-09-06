package team.cqr.cqrepoured.client.model.geo.block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

public class ModelNexusCore extends GeoModel<TileEntityForceFieldNexus>{
	
	protected static final ResourceLocation MODEL_RESLOC = CQRMain.prefix("geo/block/force_field_nexus.geo.json");
	protected static final ResourceLocation TEXTURE_RESLOC = CQRMain.prefix("textures/entity/force_field_nexus/force_field_nexus.png");

	@Override
	public ResourceLocation getAnimationResource(TileEntityForceFieldNexus animatable) {
		return CQRAnimations.Block.NEXUS_CORE;
	}

	@Override
	public ResourceLocation getModelResource(TileEntityForceFieldNexus object) {
		return MODEL_RESLOC;
	}

	@Override
	public ResourceLocation getTextureResource(TileEntityForceFieldNexus object) {
		return TEXTURE_RESLOC;
	}

}
