package team.cqr.cqrepoured.client.render.entity.boss;

import java.util.Optional;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelGiantTortoiseGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class RenderCQRGiantTortoise extends RenderCQREntityGeo<EntityCQRGiantTortoise> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRConstants.MODID, "textures/entity/boss/giant_tortoise.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRConstants.MODID, "geo/entity/boss/giant_tortoise.geo.json");

	public RenderCQRGiantTortoise(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelGiantTortoiseGeo(MODEL_RESLOC, TEXTURE, "boss/giant_tortoise"));
	}

	@Override
	protected Optional<ItemArmorGeoLayer<EntityCQRGiantTortoise>> createArmorLayer(RenderCQREntityGeo<EntityCQRGiantTortoise> renderCQREntityGeo) {
		// TODO: Create layer instance that can display armor on the limbs
		return Optional.empty();
	}
	
	@Override
	protected Optional<BlockAndItemGeoLayer<EntityCQRGiantTortoise>> createBlockAndItemLayer(RenderCQREntityGeo<EntityCQRGiantTortoise> renderCQREntityGeo) {
		return Optional.empty();
	}
	

}
