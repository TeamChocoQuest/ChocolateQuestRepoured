package team.cqr.cqrepoured.capability.electric;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class ElectricShockCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static final ResourceLocation IDENTIFIER = CQRMain.prefix("electric-spread-effect");
	
	private final IElectricShockCapability backend = new ElectricShockCapabilityImplementation();
	private final LazyOptional<IElectricShockCapability> optionalData = LazyOptional.of(() -> backend);
	
	@Override
	public CompoundTag serializeNBT() {
		return this.backend.serializeNBT();
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.backend.deserializeNBT(nbt);
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return CQRCapabilities.ELECTRIC_SPREAD.orEmpty(cap, optionalData);
	}

}
