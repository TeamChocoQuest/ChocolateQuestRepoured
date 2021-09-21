package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;

import net.minecraft.entity.MultiPartEntityPart;
import team.cqr.cqrepoured.objects.entity.IDontRenderFire;
import team.cqr.cqrepoured.objects.entity.ISizable;
import team.cqr.cqrepoured.objects.entity.misc.EntityElectricFieldSizable;

public class SubEntityExterminatorFieldEmitter extends MultiPartEntityPart implements IBlacklistedFromStatues, IDontRenderFire, ISizable {

	private EntityElectricFieldSizable electricField = null;
	
	public SubEntityExterminatorFieldEmitter(EntityCQRExterminator parent, String partName) {
		super(parent, partName, 0.5F, 0.5F);
		this.initializeSize();
	}

	@Override
	public boolean canbeTurnedToStone() {
		return false;
	}
	
	//ISizable stuff
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
		//No, this should not be done in this entity, it happens in the parent...
	}

}
