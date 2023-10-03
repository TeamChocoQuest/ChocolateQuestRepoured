package team.cqr.cqrepoured.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class SerializableCapabilityProvider<C extends INBTSerializable<CompoundTag>> extends BasicCapabilityProvider<C> implements INBTSerializable<CompoundTag> {

	public SerializableCapabilityProvider(Capability<C> capability, C defaultData) {
		super(capability, defaultData);
	}

	@Override
	public CompoundTag serializeNBT() {
		return this.backend.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.backend.deserializeNBT(nbt);
	}

}
