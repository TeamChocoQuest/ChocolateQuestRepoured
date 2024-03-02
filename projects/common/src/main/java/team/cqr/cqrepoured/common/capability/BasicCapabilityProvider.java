package team.cqr.cqrepoured.common.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class BasicCapabilityProvider<C> implements ICapabilityProvider {

	private final Capability<C> capability;
	protected final LazyOptional<C> instance;

	public BasicCapabilityProvider(Capability<C> capability, C instance) {
		this(capability, () -> instance);
	}

	public BasicCapabilityProvider(Capability<C> capability, NonNullSupplier<C> instanceSupplier) {
		this.capability = capability;
		this.instance = LazyOptional.of(instanceSupplier);
	}

	@Override
	@NotNull
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return capability.orEmpty(cap, instance);
	}

}
