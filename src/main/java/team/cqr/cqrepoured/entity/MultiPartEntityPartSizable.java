package team.cqr.cqrepoured.entity;

import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraftforge.entity.PartEntity;

public class MultiPartEntityPartSizable<T extends IEntityMultiPart & ISizable> extends PartEntity implements ISizable, IBlacklistedFromStatues, IDontRenderFire {

	private final float dw, dh;

	public MultiPartEntityPartSizable(T parent, String partName, float width, float height) {
		super(parent, partName, width, height);

		this.dw = width;
		this.dh = height;

		this.initializeSize();
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
		return ((ISizable) this.parent).getSizeVariation();
	}

	@Override
	public void applySizeVariation(float value) {
		// No, this should not be done in this entity, it happens in the parent...
	}

	@Override
	public boolean canBeTurnedToStone() {
		return false;
	}

}
