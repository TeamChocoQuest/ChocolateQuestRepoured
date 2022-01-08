package team.cqr.cqrepoured.entity;

import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.entity.PartEntity;

public class MultiPartEntityPartSizable<T extends Entity & IEntityMultiPart<?> & ISizable> extends PartEntity<T> implements ISizable, IBlacklistedFromStatues, IDontRenderFire {

	private final float dw, dh;
	private final EntitySize size;

	public MultiPartEntityPartSizable(T parent, String partName, float width, float height) {
		super(parent);

		this.dw = width;
		this.dh = height;

		this.size = new EntitySize(width, height, false);
		
		this.initializeSize();
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public float getDefaultWidth() {
		return this.dw;
	}

	@Override
	public float getDefaultHeight() {
		return this.dh;
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
	public EntitySize getDimensions(Pose pPose) {
		return callOnGetDimensions(this.size);
	}
	
	public EntitySize getSize() {
		return this.size;
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT pCompound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT pCompound) {

	}

	public boolean is(Entity pEntity) {
		return this == pEntity || this.getParent() == pEntity;
	}

}
