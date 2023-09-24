package team.cqr.cqrepoured.entity.multipart;

import de.dertoaster.multihitboxlib.entity.hitbox.SubPartConfig;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AnimatablePartEntity<T extends Entity> extends CQRPartEntity<T> implements GeoEntity {

	protected final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
	
	public AnimatablePartEntity(T parent, SubPartConfig properties) {
		super(parent, properties);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.instanceCache;
	}

	@Override
	public double getTick(Object entity) {
		if (this.getParent() != null) {
			return this.getParent().tickCount;
		}
		return GeoEntity.super.getTick(entity);
	}
	
}
