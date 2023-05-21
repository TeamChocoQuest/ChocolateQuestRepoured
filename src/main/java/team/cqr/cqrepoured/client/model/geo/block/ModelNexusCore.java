package team.cqr.cqrepoured.client.model.geo.block;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

public class ModelNexusCore extends AnimatedGeoModel<TileEntityForceFieldNexus>{
	
	protected static final ResourceLocation MODEL_RESLOC = CQRMain.prefix("geo/block/force_field_nexus.geo.json");
	protected static final ResourceLocation TEXTURE_RESLOC = CQRMain.prefix("textures/entity/force_field_nexus/force_field_nexus.png");

	@Override
	public ResourceLocation getAnimationFileLocation(TileEntityForceFieldNexus animatable) {
		return CQRAnimations.Block.NEXUS_CORE;
	}

	@Override
	public ResourceLocation getModelLocation(TileEntityForceFieldNexus object) {
		return MODEL_RESLOC;
	}

	@Override
	public ResourceLocation getTextureLocation(TileEntityForceFieldNexus object) {
		return TEXTURE_RESLOC;
	}

}
