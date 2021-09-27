package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;

import net.minecraft.entity.MultiPartEntityPart;
import team.cqr.cqrepoured.objects.entity.IDontRenderFire;
import team.cqr.cqrepoured.objects.entity.ISizable;
import team.cqr.cqrepoured.objects.entity.misc.EntityElectricFieldSizable;

public class SubEntityExterminatorFieldEmitter extends MultiPartEntityPart implements IBlacklistedFromStatues, IDontRenderFire, ISizable {

	private EntityElectricFieldSizable electricField = null;
	private EntityCQRExterminator exterminator;

	public SubEntityExterminatorFieldEmitter(EntityCQRExterminator parent, String partName) {
		super(parent, partName, 0.5F, 0.5F);
		this.exterminator = parent;
		this.initializeSize();
	}

	@Override
	public boolean canbeTurnedToStone() {
		return false;
	}

	// ISizable stuff
	@Override
	public float getDefaultWidth() {
		return 0.5F;
	}

	@Override
	public float getDefaultHeight() {
		return 0.5F;
	}

	@Override
	public float getSizeVariation() {
		return ((ISizable) this.parent).getSizeVariation();
	}

	@Override
	public void applySizeVariation(float value) {
		// No, this should not be done in this entity, it happens in the parent...
		try {
			this.electricField.applySizeVariation(value);
		} catch (NullPointerException npe) {
			// Ignore
		}
	}

	@Override
	public void setSizeVariation(float size) {
		ISizable.super.setSizeVariation(size);
		try {
			this.electricField.setSizeVariation(size);
		} catch (NullPointerException npe) {
			// Ignore
		}
	}

	@Override
	public void resize(float widthScale, float heightSacle) {
		ISizable.super.resize(widthScale, heightSacle);
		try {
			this.electricField.resize(widthScale, heightSacle);
		} catch (NullPointerException npe) {
			// Ignore
		}
	}

	public void createElectricField(int charge) {
		if (this.electricField != null) {
			this.electricField.setCharge(charge);
		} else {
			this.electricField = new EntityElectricFieldSizable(this.exterminator.getWorld(), charge, this.exterminator.getPersistentID());
		}
	}

	public void destroyElectricField() {
		try {
			this.electricField.destroyField();
		} catch (NullPointerException npe) {
			// Ignore
		}
	}
	
}
