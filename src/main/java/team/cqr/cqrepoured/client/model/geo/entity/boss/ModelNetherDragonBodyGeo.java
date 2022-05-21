package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.netherdragon.SubEntityNetherDragonSegment;

public class ModelNetherDragonBodyGeo extends AbstractModelGeoCQRBase<SubEntityNetherDragonSegment> {

	public ModelNetherDragonBodyGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	public ResourceLocation getAnimationFileLocation(SubEntityNetherDragonSegment animatable) {
		return null;
	}
	
	protected final ResourceLocation TEXTURE_SKELETAL_DEFAULT = CQRMain.prefix("textures/entity/boss/nether_dragon_body_skeletal.png");
	
	protected final ResourceLocation MODEL_SKELETAL_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body_skeletal.geo.json");
	
	protected final ResourceLocation MODEL_TAIL_START_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body_tail_start.geo.json");
	protected final ResourceLocation MODEL_TAIL_CENTER_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body_tail_center.geo.json");
	protected final ResourceLocation MODEL_TAIL_END_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body_tail_end.geo.json");
	
	@Override
	public ResourceLocation getModelLocation(SubEntityNetherDragonSegment object) {
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
	public ResourceLocation getTextureLocation(SubEntityNetherDragonSegment entity) {
		ResourceLocation result = super.getTextureLocation(entity);
		if(result == this.TEXTURE_DEFAULT) {
			if(entity.isSkeletal()) {
				return this.TEXTURE_SKELETAL_DEFAULT;
			}
		}
		return result;
	}
	
}
