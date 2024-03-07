package team.cqr.cqrepoured.faction.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.faction.init.FactionCapabilities;

public class FactionRelationCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static final ResourceLocation IDENTIFIER = CQRepoured.prefix("faction_relations");

	private final IFactionRelationCapability backend = new FactionRelationCapabilityImplementation();
	private final LazyOptional<IFactionRelationCapability> optionalData = LazyOptional.of(() -> backend);

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
		return FactionCapabilities.FACTION_RELATION.orEmpty(cap, optionalData);
	}

}
