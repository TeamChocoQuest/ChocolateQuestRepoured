package team.cqr.cqrepoured.common.capability;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class SerializableCapabilityProvider<C extends INBTSerializable<T>, T extends Tag> extends BasicCapabilityProvider<C> implements INBTSerializable<T> {

	public SerializableCapabilityProvider(Capability<C> capability, C instance) {
		super(capability, instance);
	}

	public SerializableCapabilityProvider(Capability<C> capability, NonNullSupplier<C> instanceSupplier) {
		super(capability, instanceSupplier);
	}

	@Override
	public T serializeNBT() {
		return instance.orElseThrow(NullPointerException::new)
				.serializeNBT();
	}

	@Override
	public void deserializeNBT(T nbt) {
		instance.orElseThrow(NullPointerException::new)
				.deserializeNBT(nbt);
	}

}
