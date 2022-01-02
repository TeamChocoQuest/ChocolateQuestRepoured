package team.cqr.cqrepoured.capability;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class SerializableCapabilityProvider<C> extends BasicCapabilityProvider<C> implements INBTSerializable<INBT> {

	public SerializableCapabilityProvider(Capability<C> capability, C instance) {
		super(capability, instance);
	}

	@Override
	public INBT serializeNBT() {
		return this.capability.getStorage().writeNBT(this.capability, this.instance, null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		this.capability.getStorage().readNBT(this.capability, this.instance, null, nbt);
	}

}
