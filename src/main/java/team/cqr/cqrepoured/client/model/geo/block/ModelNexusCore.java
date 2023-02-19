package team.cqr.cqrepoured.client.model.geo.block;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

public class ModelNexusCore extends AnimatedGeoModel<TileEntityForceFieldNexus>{
	
	protected static final ResourceLocation ANIM_RESLOC = CQRMain.prefix("animations/nexus_block.animation.json");
	protected static final ResourceLocation MODEL_RESLOC = CQRMain.prefix("geo/block/nexus_block.geo.json");
	protected static final ResourceLocation TEXTURE_RESLOC = CQRMain.prefix("textures/block/nexus_core.png");

	@Override
	public ResourceLocation getAnimationFileLocation(TileEntityForceFieldNexus animatable) {
		return ANIM_RESLOC;
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
