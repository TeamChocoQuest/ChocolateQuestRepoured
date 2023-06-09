package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;

public class ModelNetherDragonHeadGeo extends AbstractModelGeoCQRBase<EntityCQRNetherDragon> {
	
	static final ResourceLocation TEXTURE_SKELETAL = CQRMain.prefix("textures/entity/boss/nether_dragon_skeletal_head.png");
	public static final ResourceLocation TEXTURE_NORMAL = CQRMain.prefix("textures/entity/boss/nether_dragon_head.png");

	public static final ResourceLocation MODEL_IDENT_NORMAL = CQRMain.prefix("geo/entity/boss/nether_dragon/nether_dragon_head_normal.geo.json");
	static final ResourceLocation MODEL_IDENT_SKELETAL = CQRMain.prefix("geo/entity/boss/nether_dragon/nether_dragon_head_skeletal.geo.json");

	public ModelNetherDragonHeadGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRNetherDragon animatable) {
		return CQRAnimations.Entity.NETHER_DRAGON;
	}
	
	@Override
	public ResourceLocation getModelLocation(EntityCQRNetherDragon object) {
		if(object.getSkeleProgress() >= 0) {
			return MODEL_IDENT_SKELETAL;
		}
		return super.getModelLocation(object);
	}
	
	@Override
	public ResourceLocation getTextureLocation(EntityCQRNetherDragon entity) {
		if(entity.getSkeleProgress() >= 0) {
			return TEXTURE_SKELETAL;
		}
		return super.getTextureLocation(entity);
	}

}
