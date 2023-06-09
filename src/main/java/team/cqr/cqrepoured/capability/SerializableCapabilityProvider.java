package team.cqr.cqrepoured.capability;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.NonNullSupplier;

public class SerializableCapabilityProvider<C> extends BasicCapabilityProvider<C> implements ICapabilitySerializable<Tag> {

	public SerializableCapabilityProvider(Capability<C> capability, NonNullSupplier<C> instanceSupplier) {
		super(capability, instanceSupplier);
	}

	@Override
	public Tag serializeNBT() {
		return this.capability.writeNBT(this.instance.orElseThrow(NullPointerException::new), null);
	}

	@Override
	public void deserializeNBT(Tag nbt) {
		this.capability.readNBT(this.instance.orElseThrow(NullPointerException::new), null, nbt);
	}

}
