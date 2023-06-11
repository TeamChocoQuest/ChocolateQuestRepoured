package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.netherdragon.SubEntityNetherDragonSegment;

public class ModelNetherDragonBodyGeo extends AbstractModelGeoCQRBase<SubEntityNetherDragonSegment> {

	public ModelNetherDragonBodyGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	public ResourceLocation getAnimationResource(SubEntityNetherDragonSegment animatable) {
		return null;
	}
	
	protected final ResourceLocation TEXTURE_SKELETAL_DEFAULT = CQRMain.prefix("textures/entity/boss/nether_dragon_skeletal.png");
	
	protected final ResourceLocation MODEL_SKELETAL_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body/nether_dragon_body_skeletal.geo.json");
	
	protected final ResourceLocation MODEL_TAIL_START_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body/nether_dragon_tail_start.geo.json");
	protected final ResourceLocation MODEL_TAIL_CENTER_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body/nether_dragon_tail_middle.geo.json");
	protected final ResourceLocation MODEL_TAIL_END_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body/nether_dragon_tail_end.geo.json");
	
	@Override
	public ResourceLocation getModelResource(SubEntityNetherDragonSegment object) {
		if(object.isSkeletal()) {
			return MODEL_SKELETAL_RESLOC;
		} else {
			if(object.getPartIndex() <= 2) {
				if(object.getPartIndex() <= 1) {
					if(object.getPartIndex() <= 0) {
						return MODEL_TAIL_END_RESLOC;
					}
					return MODEL_TAIL_CENTER_RESLOC;
				}
				return MODEL_TAIL_START_RESLOC;
			}
			return MODEL_RESLOC;
		}
	}
	
	@Override
	public ResourceLocation getTextureResource(SubEntityNetherDragonSegment entity) {
		ResourceLocation result = super.getTextureResource(entity);
		if(result == this.TEXTURE_DEFAULT) {
			if(entity.isSkeletal()) {
				return this.TEXTURE_SKELETAL_DEFAULT;
			}
		}
		return result;
	}
	
}
