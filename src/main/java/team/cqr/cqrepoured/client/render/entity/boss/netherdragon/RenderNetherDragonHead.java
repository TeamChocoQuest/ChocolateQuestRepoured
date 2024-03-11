package team.cqr.cqrepoured.client.render.entity.boss.netherdragon;

import java.util.Optional;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelNetherDragonHeadGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;

public class RenderNetherDragonHead extends RenderCQREntityGeo<EntityCQRNetherDragon> {

	public RenderNetherDragonHead(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelNetherDragonHeadGeo(ModelNetherDragonHeadGeo.TEXTURE_NORMAL, ModelNetherDragonHeadGeo.MODEL_IDENT_NORMAL, "boss/nether_dragon_head"));

		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

	@Override
	protected Optional<ItemArmorGeoLayer<EntityCQRNetherDragon>> createArmorLayer(RenderCQREntityGeo<EntityCQRNetherDragon> renderCQREntityGeo) {
		return Optional.empty();
	}
	
	@Override
	protected Optional<BlockAndItemGeoLayer<EntityCQRNetherDragon>> createBlockAndItemLayer(RenderCQREntityGeo<EntityCQRNetherDragon> renderCQREntityGeo) {
		return Optional.empty();
	}
	
}
