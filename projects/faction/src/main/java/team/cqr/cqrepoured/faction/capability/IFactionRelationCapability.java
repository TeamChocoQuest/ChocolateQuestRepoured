package team.cqr.cqrepoured.faction.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.IFactionRelated;

@AutoRegisterCapability
public interface IFactionRelationCapability extends IFactionRelated, INBTSerializable<CompoundTag> {
	
	Entity getHolder();
	void setHolder(Entity value);
	
	boolean hasInformationFor(Faction faction);

	public int getRelationFor(Faction faction);
	
	public void setReputationTowards(final ResourceLocation id, int value);
	public default void setReputationTowards(final Faction faction, int value) {
		if (!faction.canRepuChange()) {
			return;
		}
		this.setReputationTowards(faction, value);
	}
	
	@Override
	default EReputationState getRelationTowards(Faction faction) {
		return EReputationState.getByInt(this.getExactRelationTowards(faction));
	}
	
	@Override
	default int getExactRelationTowards(Faction faction) {
		if (this.hasInformationFor(faction)) {
			return this.getRelationFor(faction);
		}
		return 0;
	}
	
}
