package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;

import net.minecraft.entity.MultiPartEntityPart;
import team.cqr.cqrepoured.objects.entity.IDontRenderFire;
import team.cqr.cqrepoured.objects.entity.ISizable;

public class SubEntityExterminatorBackpack extends MultiPartEntityPart implements IBlacklistedFromStatues, IDontRenderFire, ISizable {

	public SubEntityExterminatorBackpack(EntityCQRExterminator parent, String partName) {
		super(parent, partName, 1.0F, 1.0F);
		this.initializeSize();
	}

	@Override
	public boolean canbeTurnedToStone() {
		return false;
	}

	//ISizable methods
	@Override
	public float getDefaultWidth() {
		return 1.0F;
	}

	@Override
	public float getDefaultHeight() {
		return 1.0F;
	}

	@Override
	public float getSizeVariation() {
		return ((ISizable) this.parent).getSizeVariation();
	}

	@Override
	public void applySizeVariation(float value) {
		//No, this should not be done in this entity, it happens in the parent...
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

}
