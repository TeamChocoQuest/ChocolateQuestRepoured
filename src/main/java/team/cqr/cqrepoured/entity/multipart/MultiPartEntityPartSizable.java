package team.cqr.cqrepoured.entity.multipart;


import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.IEntityMultiPart;
import team.cqr.cqrepoured.entity.ISizable;

public abstract class MultiPartEntityPartSizable<T extends Entity & IEntityMultiPart<?> & ISizable> extends CQRPartEntity<T> implements ISizable, IBlacklistedFromStatues, IDontRenderFire {

	public MultiPartEntityPartSizable(T parent, String partName, float width, float height) {
		super(parent);

		this.size = new EntityDimensions(width, height, false);
		
		this.initializeSize();
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public float getSizeVariation() {
		return ((ISizable) this.getParent()).getSizeVariation();
	}

	@Override
	public void applySizeVariation(float value) {
		// No, this should not be done in this entity, it happens in the parent...
	}

	@Override
	public boolean canBeTurnedToStone() {
		return false;
	}

	@Override
	public Pose getPose() {
		return this.getParent().getPose();
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		return callOnGetDimensions(this.size);
	}
	
	@Override
	protected void defineSynchedData() {

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {

	}

	public boolean is(Entity pEntity) {
		return this == pEntity || this.getParent() == pEntity;
	}

}
