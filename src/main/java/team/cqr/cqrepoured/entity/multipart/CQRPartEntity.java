package team.cqr.cqrepoured.entity.multipart;

import de.dertoaster.multihitboxlib.entity.MHLibPartEntity;
import de.dertoaster.multihitboxlib.entity.hitbox.SubPartConfig;
import net.minecraft.world.entity.Entity;

public abstract class CQRPartEntity<T extends Entity> extends MHLibPartEntity<T> {
	
	public CQRPartEntity(T parent, SubPartConfig properties) {
		super(parent, properties);
	}
	
}
