package team.cqr.cqrepoured.capability.faction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.IFactionRelated;

public interface IFactionRelationCapability extends IFactionRelated, INBTSerializable<CompoundTag> {
	
	public static final Capability<IFactionRelationCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
		
	});
	
	boolean hasInformationFor(Faction faction);

	public int getRelationFor(Faction faction);
	
	public void setReputationTowards(final ResourceLocation id, int value);
	public default void setReputationTowards(final Faction faction, int value) {
		this.setReputationTowards(faction, value);
	}
	
	@Override
	default EReputationState getRelationTowards(Faction faction) {
		if (this.hasInformationFor(faction)) {
			return EReputationState.getByInt(this.getRelationFor(faction));
		}
		return EReputationState.NEUTRAL;
	}

}
