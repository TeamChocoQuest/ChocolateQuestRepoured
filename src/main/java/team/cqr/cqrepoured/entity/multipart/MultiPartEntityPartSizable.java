package team.cqr.cqrepoured.entity.multipart;


import java.util.Optional;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;

import de.dertoaster.multihitboxlib.entity.hitbox.SubPartConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.IEntityMultiPart;
import team.cqr.cqrepoured.entity.ISizable;

public abstract class MultiPartEntityPartSizable<T extends Entity & IEntityMultiPart<?> & ISizable> extends CQRPartEntity<T> implements ISizable, IBlacklistedFromStatues, IDontRenderFire {

	private EntityDimensions baseSize;
	private Optional<Tuple<Float, Float>> currentSizeModifier = Optional.empty();

	public MultiPartEntityPartSizable(T parent, SubPartConfig properties, float width, float height) {
		super(parent, properties);

		this.baseSize = new EntityDimensions(width, height, false);
		
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
		EntityDimensions workingDims = this.baseSize;
		if (workingDims == null) {
			workingDims = FALLBACK_SIZE;
		}
		workingDims = this.currentSizeModifier != null && this.currentSizeModifier.isPresent() ?
				workingDims.scale((Float) ((Tuple<Float, Float>) this.currentSizeModifier.get()).getA(),
				(Float) ((Tuple<Float, Float>) this.currentSizeModifier.get()).getB())
				: workingDims;
		
		return callOnGetDimensions(workingDims);
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
	
	@Override
	public void setScaling(Vec3 scale) {
		super.setScaling(scale);
		this.currentSizeModifier = Optional.ofNullable(new Tuple<>((float) scale.x(), (float) scale.y()));
	}

	public boolean is(Entity pEntity) {
		return this == pEntity || this.getParent() == pEntity;
	}

}
