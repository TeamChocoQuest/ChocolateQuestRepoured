package team.cqr.cqrepoured.common.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public abstract class BasicCapabilityProvider<C> implements ICapabilityProvider {

	protected final Capability<C> capability;
	public final C backend;
	public final LazyOptional<C> optionalData;

	public BasicCapabilityProvider(Capability<C> capability, C defaultValue) {
		this.capability = capability;
		this.backend = defaultValue;
		this.optionalData = LazyOptional.of(() -> defaultValue);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return this.capability.orEmpty(cap, optionalData);
	}

}
